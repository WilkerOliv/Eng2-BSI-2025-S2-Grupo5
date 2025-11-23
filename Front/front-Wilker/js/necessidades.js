// =============================================================
// CONFIGURAÇÕES
// =============================================================
const API_PC   = "http://localhost:8080/api/pessoas-carentes";
const API_CAT  = "http://localhost:8080/api/categorias";
const API_PROD = "http://localhost:8080/api/produtos";
const API_NEC  = "http://localhost:8080/api/necessidades/produtos";

let itensPendentes = [];

// ITENS DO MODAL (Edição)
let itensEdicao = [];

let editNecId = null;
let editCpf = null;
let editData = null;

let modalEditarLista = null;


// =============================================================
// CARREGAMENTO INICIAL
// =============================================================
document.addEventListener("DOMContentLoaded", () => {

    modalEditarLista = new bootstrap.Modal(document.getElementById("modalEditarLista"));

    document.getElementById("data").valueAsDate = new Date();

    setupBuscaPessoa();
    carregarCategorias();
    carregarCategoriasEdicao();
    carregarNecessidades();
    setupBotoes();
});


// =============================================================
// AUTOCOMPLETE PESSOA
// =============================================================
function setupBuscaPessoa() {

    const pcSearch = document.getElementById("pcSearch");
    const pcAutocomplete = document.getElementById("pcAutocomplete");
    const pcCpf = document.getElementById("pcCpf");

    function limpa() { pcAutocomplete.innerHTML = ""; }

    pcSearch.addEventListener("input", async () => {

        const termo = pcSearch.value.trim().toLowerCase();
        limpa();

        if (termo.length < 2) return;

        const resp = await fetch(`${API_PC}?termo=${encodeURIComponent(termo)}`);
        if (!resp.ok) return;

        const lista = await resp.json();

        const filtrados = lista.filter(p =>
            p.pcCpf.toLowerCase().includes(termo) ||
            p.pcNome.toLowerCase().includes(termo)
        );

        filtrados.forEach(p => {
            const div = document.createElement("div");
            div.className = "autocomplete-item";
            div.textContent = `${p.pcNome} - ${p.pcCpf}`;
            div.onclick = () => {
                pcSearch.value = p.pcNome;
                pcCpf.value = p.pcCpf;
                limpa();
            };
            pcAutocomplete.appendChild(div);
        });
    });

    document.addEventListener("click", (e) => {
        if (!pcAutocomplete.contains(e.target) && e.target !== pcSearch) limpa();
    });
}



// =============================================================
// CARREGAR CATEGORIAS E PRODUTOS (TELA PRINCIPAL)
// =============================================================
async function carregarCategorias() {

    const categoria = document.getElementById("categoria");

    const [resCat, resProd] = await Promise.all([
        fetch(API_CAT),
        fetch(API_PROD)
    ]);

    const categorias = await resCat.json();
    const produtos = await resProd.json();

    const catComProd = new Set(produtos.map(p => p.categoriaProdCod));

    categoria.innerHTML = "";

    categorias.filter(c => catComProd.has(c.catCod))
        .forEach(c => {
            const op = document.createElement("option");
            op.value = c.catCod;
            op.textContent = c.catDescr;
            categoria.appendChild(op);
        });

    categoria.onchange = carregarProdutos;

    carregarProdutos();
}

async function carregarProdutos() {

    const categoria = document.getElementById("categoria");
    const produto   = document.getElementById("produto");

    const catId = categoria.value;

    if (!catId) {
        produto.innerHTML = "";
        return;
    }

    const res = await fetch(`${API_PROD}?categoria=${catId}`);
    const lista = await res.json();

    produto.innerHTML = "";

    lista.forEach(p => {
        produto.innerHTML += `<option value="${p.prodCod}">${p.prodDescr}</option>`;
    });
}



// =============================================================
// BOTÕES DA TELA DE NOVA LISTA
// =============================================================
function setupBotoes() {

    document.getElementById("btnAddItem").onclick = () => {

        const prodSel = document.getElementById("produto");
        const prodCod = Number(prodSel.value);
        const qtd = Number(document.getElementById("quantidade").value);

        if (!prodCod || qtd < 1) {
            alerta("Informe produto e quantidade válida!", "danger");
            return;
        }

        const prodNome = prodSel.options[prodSel.selectedIndex].textContent;

        let item = itensPendentes.find(i => i.produtoCod === prodCod);
        if (item) item.quantidade += qtd;
        else itensPendentes.push({ produtoCod: prodCod, produtoNome: prodNome, quantidade: qtd });

        document.getElementById("quantidade").value = "";
        atualizarTabelaPendentes();
    };

    document.getElementById("btnSalvarLista").onclick = salvarLista;

    document.getElementById("btnBuscar").onclick = () =>
        carregarNecessidades(document.getElementById("buscar").value);

    document.getElementById("btnResetBusca").onclick = () => {
        document.getElementById("buscar").value = "";
        carregarNecessidades();
    };
}



// =============================================================
// TABELA DE ITENS PENDENTES
// =============================================================
function atualizarTabelaPendentes() {

    const box = document.getElementById("pendingBox");
    const tbody = document.querySelector("#pendingTable tbody");

    tbody.innerHTML = "";

    if (itensPendentes.length === 0) {
        box.style.display = "none";
        return;
    }

    itensPendentes.forEach((it, i) => {
        tbody.innerHTML += `
            <tr>
                <td>${it.produtoNome}</td>
                <td>${it.quantidade}</td>
                <td><button class="btn btn-danger btn-sm" onclick="removerItemPendente(${i})">X</button></td>
            </tr>
        `;
    });

    box.style.display = "block";
}

function removerItemPendente(i) {
    itensPendentes.splice(i, 1);
    atualizarTabelaPendentes();
}



// =============================================================
// SALVAR NOVA LISTA DE NECESSIDADES
// =============================================================
async function salvarLista() {

    const cpf = document.getElementById("pcCpf").value;
    const data = document.getElementById("data").value;
    const obs = document.getElementById("observacaoGeral").value;

    if (!cpf || itensPendentes.length === 0) {
        alerta("Selecione pessoa e adicione ao menos 1 item.", "danger");
        return;
    }

    const body = {
        cpf: cpf,
        data: data,
        observacao: obs,
        itens: itensPendentes.map(i => ({
            produtoCod: i.produtoCod,
            quantidade: i.quantidade
        }))
    };

    const resp = await fetch(`${API_NEC}/lista`, {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(body)
    });

    if (!resp.ok) return alerta("Erro ao registrar lista!", "danger");

    alerta("Lista registrada!", "success");

    itensPendentes = [];
    atualizarTabelaPendentes();
    carregarNecessidades();
}



// =============================================================
// LISTAR NECESSIDADES
// =============================================================
async function carregarNecessidades(termo = "") {

    const resp = await fetch(API_NEC);
    let dados = await resp.json();

    const t = termo.toLowerCase();

    if (t !== "") {
        dados = dados.filter(n =>
            n.pessoaNome.toLowerCase().includes(t) ||
            n.pessoaCpf.toLowerCase().includes(t) ||
            (n.observacao && n.observacao.toLowerCase().includes(t))
        );
    }

    const tbody = document.querySelector("#tabelaNec tbody");
    tbody.innerHTML = "";

    dados.forEach(n => {
        tbody.innerHTML += `
            <tr>
                <td>${n.pessoaCpf}</td>
                <td>${n.pessoaNome}</td>
                <td>${formatarData(n.data)}</td>
                <td>${n.observacao || ""}</td>
                <td>
                    <button class="btn btn-primary btn-sm"
                            onclick="abrirEdicao(${n.necId}, '${n.pessoaCpf}', '${n.pessoaNome}', '${n.observacao || ""}', '${n.data}')">👁️</button>
                    <button class="btn btn-danger btn-sm" onclick="excluirLista(${n.necId})">🗑️</button>
                </td>
            </tr>
        `;
    });
}



// =============================================================
// ABRIR MODAL DE EDIÇÃO
// =============================================================
async function abrirEdicao(necId, cpf, nome, obs, data) {

    editNecId = necId;
    editCpf = cpf;
    editData = data;

    document.getElementById("ePessoa").textContent = nome;
    document.getElementById("eCpf").textContent = cpf;
    document.getElementById("eObs").value = obs;

    await carregarCategoriasEdicao();
    await carregarProdutosEdicao();

    await carregarItensEdicao(necId);   // Carrega UMA VEZ do backend
    desenharTabelaEdicao();            // Renderiza com os itens da memória

    modalEditarLista.show();
}



// =============================================================
// CATEGORIAS/PRODUTOS - MODAL
// =============================================================
async function carregarCategoriasEdicao() {

    const eCat = document.getElementById("eCat");

    const [c1, c2] = await Promise.all([fetch(API_CAT), fetch(API_PROD)]);

    const cats = await c1.json();
    const prods = await c2.json();

    const catSet = new Set(prods.map(p => p.categoriaProdCod));

    eCat.innerHTML = "";

    cats.filter(c => catSet.has(c.catCod))
        .forEach(c => {
            eCat.innerHTML += `<option value="${c.catCod}">${c.catDescr}</option>`;
        });

    eCat.onchange = carregarProdutosEdicao;
}


async function carregarProdutosEdicao() {

    const eCat = document.getElementById("eCat");
    const eProd = document.getElementById("eProd");

    const res = await fetch(`${API_PROD}?categoria=${eCat.value}`);
    const lista = await res.json();

    eProd.innerHTML = "";

    lista.forEach(p => {
        eProd.innerHTML += `<option value="${p.prodCod}">${p.prodDescr}</option>`;
    });
}



// =============================================================
// CARREGAR ITENS NO MODAL (APENAS UMA VEZ)
// =============================================================
async function carregarItensEdicao(necId) {

    const resp = await fetch(`${API_NEC}/${necId}`);
    itensEdicao = await resp.json();

    // Agora os itens ficam SOMENTE NA MEMÓRIA
}



// =============================================================
// DESENHAR TABELA DO MODAL — (sincroniza inputs com memória)
// =============================================================
function desenharTabelaEdicao() {

    const tbody = document.querySelector("#eTabelaItens tbody");
    tbody.innerHTML = "";

    itensEdicao.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>${item.produtoDescr}</td>
                <td>${item.categoriaNome}</td>
                <td>
                    <input type="number"
                           min="1"
                           value="${item.quantidade}"
                           class="form-control form-control-sm quant-edit"
                           data-prod="${item.produtoCod}">
                </td>
                <td>
                    <button class="btn btn-danger btn-sm"
                            onclick="removerItemEdicao(${item.produtoCod})">X</button>
                </td>
            </tr>
        `;
    });

    // 🔥 CORREÇÃO DEFINITIVA:
    // sincroniza cada input com o array itensEdicao
    document.querySelectorAll(".quant-edit").forEach(inp => {
        inp.addEventListener("input", () => {

            const prod = Number(inp.dataset.prod);
            const novoValor = Number(inp.value);

            const item = itensEdicao.find(i => i.produtoCod === prod);
            if (item) item.quantidade = novoValor;
        });
    });
}



// =============================================================
// REMOVER ITEM NO MODAL (LOCAL)
// =============================================================
function removerItemEdicao(prodCod) {
    itensEdicao = itensEdicao.filter(i => i.produtoCod !== prodCod);
    desenharTabelaEdicao();
}



// =============================================================
// ADICIONAR ITEM NO MODAL (LOCAL)
// =============================================================
document.getElementById("eBtnAdd").onclick = () => {

    const eProd = document.getElementById("eProd");
    const prodCod = Number(eProd.value);
    const qtd = Number(document.getElementById("eQtd").value);

    if (!prodCod || qtd < 1) {
        alerta("Quantidade inválida!", "danger");
        return;
    }

    let item = itensEdicao.find(i => i.produtoCod === prodCod);

    if (item) {
        item.quantidade += qtd;
    } else {

        const prodNome = eProd.options[eProd.selectedIndex].textContent;
        const catNome = document.getElementById("eCat")
                        .options[document.getElementById("eCat").selectedIndex].textContent;

        itensEdicao.push({
            produtoCod: prodCod,
            quantidade: qtd,
            produtoDescr: prodNome,
            categoriaNome: catNome
        });
    }

    document.getElementById("eQtd").value = "";
    desenharTabelaEdicao();
};



// =============================================================
// SALVAR EDIÇÃO
// =============================================================
document.getElementById("eBtnSalvar").onclick = salvarEdicao;

async function salvarEdicao() {

    const obs = document.getElementById("eObs").value;

    const body = {
        cpf: editCpf,
        data: editData,
        observacao: obs,
        itens: itensEdicao.map(i => ({
            produtoCod: i.produtoCod,
            quantidade: Number(i.quantidade)
        }))
    };

    const resp = await fetch(`${API_NEC}/lista/${editNecId}`, {
        method: "PUT",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(body)
    });

    if (resp.ok) {
        alerta("Lista atualizada!", "success");
        modalEditarLista.hide();
        carregarNecessidades();
    } else {
        alerta("Erro ao atualizar lista!", "danger");
    }
}



// =============================================================
// EXCLUIR LISTA COMPLETA
// =============================================================
async function excluirLista(necId) {
    if (!confirm("Excluir lista inteira?")) return;

    const resp = await fetch(`${API_NEC}/lista/${necId}`, { method:"DELETE" });

    if (resp.ok) {
        alerta("Lista excluída!", "success");
        carregarNecessidades();
    }
}



// =============================================================
// FUNÇÕES UTILITÁRIAS
// =============================================================
function alerta(msg, tipo="info") {
    const div = document.getElementById("alert-container");
    div.innerHTML = `<div class='alert alert-${tipo}'>${msg}</div>`;
    setTimeout(() => div.innerHTML = "", 3000);
}

function formatarData(dt) {
    if (!dt) return "";
    return dt.split("-").reverse().join("/");
}
