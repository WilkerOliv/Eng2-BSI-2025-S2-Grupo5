// ======================= CONFIG =======================
const API = "http://localhost:8081/api";
const el = (id) => document.getElementById(id);

let itensCarrinho = [];
let itemSelecionado = null;

// ======================= AUTOCOMPLETE =======================
el("buscarItem").addEventListener("input", async () => {
    const termo = el("buscarItem").value.trim();

    if (termo.length === 0) {
        el("autoItem").style.display = "none";
        return;
    }

    const res = await fetch(`${API}/estoque?buscar=${encodeURIComponent(termo)}`);
    const lista = await res.json();

    // FILTRA SOMENTE ALIMENTOS (categoria 1)
    const filtrado = lista.filter(e => e.categoriaCod === 1);

    const box = el("autoItem");
    box.innerHTML = "";
    box.style.display = "block";

    if (filtrado.length === 0) {
        box.innerHTML = `<div class="autocomplete-item text-muted">Nenhum alimento encontrado</div>`;
        return;
    }

    filtrado.forEach(l => {
        const div = document.createElement("div");
        div.className = "autocomplete-item";
        div.textContent = `${l.produtoDescr} (ID do produto: ${l.produtoProdCod})`;
        div.onclick = () => selecionarProduto(l.produtoProdCod);
        box.appendChild(div);
    });
});

// ======================= SELECIONAR PRODUTO =======================
async function selecionarProduto(produtoCod) {
    el("autoItem").style.display = "none";
    el("buscarItem").value = "";

    // Lista os LOTES disponíveis desse produto
    const res = await fetch(`${API}/estoque?buscar=${produtoCod}`);
    const lista = await res.json();

    const lotes = lista.filter(e => e.produtoProdCod === produtoCod);

    const sel = el("selectLote");
    sel.innerHTML = "";

    lotes.forEach(l => {
        const op = document.createElement("option");
        op.value = l.estCod;
        op.textContent = `${l.estCod} – ${l.produtoDescr} – ${l.estProdQuantidade} un – Val: ${formatarData(l.dataValidade)}`;
        op.dataset.nome = l.produtoDescr;
        op.dataset.validade = l.dataValidade;
        sel.appendChild(op);
    });

    itemSelecionado = lotes.length > 0 ? lotes[0] : null;
}

function formatarData(d) {
    const dt = new Date(d);
    return `${String(dt.getDate()).padStart(2,"0")}/${String(dt.getMonth()+1).padStart(2,"0")}/${dt.getFullYear()}`;
}

// ======================= ADICIONAR ITEM NO CARRINHO =======================
el("btnAddItem").onclick = () => {
    const loteId = el("selectLote").value;
    const qtd = Number(el("qtdItem").value);

    if (!loteId) return Utils.showAlert(alert-area, "warning", "Selecione um lote.");
    if (qtd <= 0) return Utils.showAlert(alert-area, "warning", "Quantidade inválida.");

    const opt = el("selectLote").selectedOptions[0];

    itensCarrinho.push({
        estCod: Number(loteId),
        quantidade: qtd,
        produtoNome: opt.dataset.nome,
        validade: opt.dataset.validade
    });

    atualizarTabela();
};

function atualizarTabela() {
    const tb = el("tbItens");
    tb.innerHTML = "";

    itensCarrinho.forEach((i, idx) => {
        tb.innerHTML += `
        <tr>
            <td>${i.estCod}</td>
            <td>${i.produtoNome}</td>
            <td>${i.quantidade}</td>
            <td>${formatarData(i.validade)}</td>
            <td><button class="btn btn-sm btn-danger" onclick="remover(${idx})">Excluir</button></td>
        </tr>
        `;
    });
}

function remover(i) {
    itensCarrinho.splice(i,1);
    atualizarTabela();
}

// ======================= SALVAR =======================
el("btnSalvar").onclick = async () => {

    const dto = {
        funcionarioCpf: el("cpfFunc").value.trim(),
        observacao: el("obsSaida").value.trim(),
        itens: itensCarrinho
    };

    if (!dto.funcionarioCpf)
        return Utils.showAlert(alert-area, "warning", "CPF obrigatório.");

    if (itensCarrinho.length === 0)
        return Utils.showAlert(alert-area, "warning", "Adicione pelo menos um item.");

    const res = await fetch(`${API}/saidas-refeicao`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(dto)
    });

    const body = await res.json();

    if (!res.ok) {
        return Utils.showAlert(alert-area, "danger", body.erro || "Erro ao registrar saída.");
    }

    Utils.showAlert(alert-area, "success", "Saída registrada com sucesso!");
    itensCarrinho = [];
    atualizarTabela();
};

