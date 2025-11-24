const API = "http://localhost:8081/api";
const el = (id) => document.getElementById(id);

let loteSelecionado = null;
let acertos = [];

// =============== AUTOCOMPLETE ===============
el("buscarItem").addEventListener("input", async () => {
    const termo = el("buscarItem").value.trim();

    if (termo === "") {
        el("autoItem").style.display = "none";
        return;
    }

    const res = await fetch(`${API}/estoque?buscar=${encodeURIComponent(termo)}`);
    const lista = await res.json();

    const box = el("autoItem");
    box.innerHTML = "";
    box.style.display = "block";

    if (lista.length === 0) {
        box.innerHTML = `<div class="autocomplete-item text-muted">Nenhum item encontrado</div>`;
        return;
    }

    lista.forEach(l => {
        const div = document.createElement("div");
        div.className = "autocomplete-item";
        div.textContent = `${l.produtoDescr} (ID lote: ${l.estCod})`;
        div.onclick = () => selecionarProduto(l.produtoProdCod);
        box.appendChild(div);
    });
});

// =============== LISTAR LOTES DE UM PRODUTO ===============
async function selecionarProduto(produtoCod) {
    el("autoItem").style.display = "none";
    el("buscarItem").value = "";

    const res = await fetch(`${API}/estoque?buscar=${produtoCod}`);
    const lista = await res.json();

    const lotes = lista.filter(l => l.produtoProdCod == produtoCod);

    const sel = el("selectLote");
    sel.innerHTML = "";

    lotes.forEach(l => {
        const op = document.createElement("option");
        op.value = l.estCod;
        op.textContent = `${l.estCod} – ${l.produtoDescr} – ${l.estProdQuantidade} un – Val: ${formatarData(l.dataValidade)}`;
        op.dataset.nome = l.produtoDescr;
        op.dataset.qtd = l.estProdQuantidade;
        op.dataset.validade = l.dataValidade;
        sel.appendChild(op);
    });

    atualizarQtd();
}

function atualizarQtd() {
    const opt = el("selectLote").selectedOptions[0];
    if (!opt) return;

    el("qtdAtual").value = Number(opt.dataset.qtd);
}

// Atualiza automaticamente quando muda lote
el("selectLote").addEventListener("change", atualizarQtd);

function formatarData(d) {
    const dt = new Date(d);
    return dt.toLocaleDateString("pt-BR");
}

// =============== ADICIONAR ACERTO ===============
el("btnAdd").onclick = () => {

    const opt = el("selectLote").selectedOptions[0];
    if (!opt) {
        return Utils.showAlert("alert-area", "warning", "Selecione um lote.");
    }

    const anterior = Number(opt.dataset.qtd);
    const novo = Number(el("novaQtd").value);
    const motivo = el("motivo").value.trim();

    if (novo < 0) return Utils.showAlert("alert-area", "warning", "Nova quantidade inválida.");
    if (!motivo) return Utils.showAlert("alert-area", "warning", "Informe o motivo.");

    acertos.push({
        estCod: Number(opt.value),
        quantidadeAnterior: anterior,
        novaQuantidade: novo,
        motivo: motivo,
        produtoNome: opt.dataset.nome,
        validade: opt.dataset.validade
    });

    atualizarTabela();
};

function atualizarTabela() {
    const tb = el("tbAcertos");
    tb.innerHTML = "";

    acertos.forEach((a, i) => {
        tb.innerHTML += `
        <tr>
            <td>${a.estCod}</td>
            <td>${a.produtoNome}</td>
            <td>${a.quantidadeAnterior}</td>
            <td>${a.novaQuantidade}</td>
            <td>${formatarData(a.validade)}</td>
            <td>${a.motivo}</td>
            <td><button class="btn btn-sm btn-danger" onclick="remover(${i})">X</button></td>
        </tr>
        `;
    });
}

function remover(i) {
    acertos.splice(i, 1);
    atualizarTabela();
}

// =============== SALVAR ACERTOS ===============
el("btnSalvar").onclick = async () => {

    const dto = {
        funcionarioCpf: el("cpf").value.trim(),
        itens: acertos
    };

    if (!dto.funcionarioCpf)
        return Utils.showAlert("alert-area", "warning", "CPF é obrigatório.");

    if (acertos.length === 0)
        return Utils.showAlert("alert-area", "warning", "Nenhum acerto adicionado.");

    const res = await fetch(`${API}/acertos-estoque`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(dto)
    });

    const body = await res.json();

    if (!res.ok)
        return Utils.showAlert("alert-area", "danger", body.erro || "Erro ao registrar acertos.");

    Utils.showAlert("alert-area", "success", "Acertos registrados com sucesso!");

    acertos = [];
    atualizarTabela();
};
