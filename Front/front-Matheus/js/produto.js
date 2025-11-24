const API_PROD = "http://localhost:8081/api/produtos";
const API_CAT = "http://localhost:8081/api/categorias";

const el = (id) => document.getElementById(id);

async function carregarCategorias() {
    const res = await fetch(API_CAT);
    const lista = await res.json();

    let sel = el("categoria");
    sel.innerHTML = `<option value="">Selecione...</option>`;

    lista.forEach(c => {
        sel.innerHTML += `<option value="${c.catCod}">${c.catDescr}</option>`;
    });
}

async function carregarProdutos() {
    const res = await fetch(API_PROD);
    const lista = await res.json();

    let tbody = el("tabelaProdutos");
    tbody.innerHTML = "";

    lista.forEach(p => {
        tbody.innerHTML += `
            <tr>
                <td>${p.prodCod}</td>
                <td>${p.prodDescr}</td>
                <td>${p.categoriaDescr}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="excluir(${p.prodCod})">Excluir</button>
                </td>
            </tr>`;
    });
}

async function salvar() {
    const descr = el("prodDescr").value.trim();
    const categoria = el("categoria").value;

    if (!descr || !categoria) {
        alert("Preencha todos os campos!");
        return;
    }

    const payload = { prodDescr: descr, categoriaCod: categoria };

    await fetch(API_PROD, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload)
    });

    await carregarProdutos();
}

async function excluir(id) {
    if (!confirm("Excluir produto?")) return;

    await fetch(`${API_PROD}/${id}`, { method: "DELETE" });
    await carregarProdutos();
}

document.addEventListener("DOMContentLoaded", () => {
    carregarCategorias();
    carregarProdutos();
    el("btnSalvar").addEventListener("click", salvar);
});
