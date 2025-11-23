// =========================
// CONFIGURAÇÕES
// =========================
const API_LISTAS = "http://localhost:8080/api/listas";
const API_FUNC = "http://localhost:8080/api/funcionarios";
const API_CATEG = "http://localhost:8080/api/categorias";
const API_PROD = "http://localhost:8080/api/produtos";

const alertBox = document.getElementById("alert-container");

// =========================
// CAMPOS DO FORM
// =========================
const funcSearch = document.getElementById("funcSearch");
const funcAutocomplete = document.getElementById("funcAutocomplete");
const funcionarioCpf = document.getElementById("funcionarioCpf");
const lcCod = document.getElementById("lcCod");
const dataCriacao = document.getElementById("dataCriacao");
const descricao = document.getElementById("descricao");
const statusAtendimento = document.getElementById("statusAtendimento");

const categoriaItem = document.getElementById("categoriaItem");
const produtoItem = document.getElementById("produtoItem");
const qtItem = document.getElementById("qtItem");

const tabelaItens = document.querySelector("#tabelaItens tbody");
const tabelaListas = document.querySelector("#tabelaListas tbody");

let itensPendentes = [];

// =========================
// DATA: máximo hoje
// =========================
const hoje = new Date().toISOString().split("T")[0];
if (!dataCriacao.value) dataCriacao.value = hoje;
dataCriacao.max = hoje;

// =========================
// AUTOCOMPLETE FUNCIONÁRIO
// =========================
funcSearch.addEventListener("input", async () => {
  const termo = funcSearch.value.trim();
  if (termo.length < 2) {
    funcAutocomplete.innerHTML = "";
    return;
  }

  const res = await fetch(API_FUNC + "/busca?termo=" + encodeURIComponent(termo));
  if (!res.ok) return;

  const lista = await res.json();
  funcAutocomplete.innerHTML = "";

  lista.forEach(f => {
    const item = document.createElement("div");
    item.className = "autocomplete-item";
    item.innerText = `${f.funcCpf} - ${f.funcNome}`;
    item.onclick = () => {
      funcSearch.value = f.funcNome;
      funcionarioCpf.value = f.funcCpf;
      funcAutocomplete.innerHTML = "";
    };
    funcAutocomplete.appendChild(item);
  });
});

function statusToText(st) {
  return st === 0 ? "ABERTA" :
         st === 1 ? "EM ANDAMENTO" : "CONCLUÍDA";
}

// =========================
// CARREGAR LISTAS
// =========================
async function carregarListas() {
  let url = API_LISTAS;
  const tipo = document.getElementById("tipoBusca").value;
  const termo = document.getElementById("buscar").value.trim();

  if (termo !== "") {
    url += `?tipo=${tipo}&termo=${encodeURIComponent(termo)}`;
  }

  const res = await fetch(url);
  if (!res.ok) return;

  const listas = await res.json();
  tabelaListas.innerHTML = "";

  listas.forEach(l => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${l.lcCod}</td>
      <td>${l.funcNome}</td>
      <td>${l.funcionarioFuncCpf}</td>
      <td>${l.dataCriacao}</td>
      <td>${l.descricao}</td>
      <td>${statusToText(l.statusAtendimento)}</td>
      <td>
        <button class="btn-visualizar" data-view="${l.lcCod}" title="Visualizar">&#128065;</button>
        <button class="btn btn-warning btn-sm" data-ed="${l.lcCod}">Editar</button>
        <button class="btn btn-danger btn-sm" data-del="${l.lcCod}">Excluir</button>
      </td>
    `;
    tabelaListas.appendChild(tr);
  });

  tabelaListas.querySelectorAll("[data-view]").forEach(b => {
    b.onclick = () => visualizarLista(b.dataset.view);
  });

  tabelaListas.querySelectorAll("[data-ed]").forEach(b => {
    b.onclick = () => editarLista(b.dataset.ed);
  });

  tabelaListas.querySelectorAll("[data-del]").forEach(b => {
    b.onclick = () => excluirLista(b.dataset.del);
  });
}

// =========================
// VISUALIZAÇÃO DA LISTA
// =========================
async function visualizarLista(id) {
  const [r1, r2] = await Promise.all([
    fetch(API_LISTAS + "/" + id),
    fetch(API_LISTAS + "/" + id + "/itens")
  ]);

  if (!r1.ok || !r2.ok) {
    Utils.toast("danger", "Erro ao visualizar.");
    return;
  }

  const lista = await r1.json();
  const itens = await r2.json();

  document.getElementById("vFunc").innerText = `${lista.funcNome} (${lista.funcionarioFuncCpf})`;
  document.getElementById("vDesc").innerText = lista.descricao;
  document.getElementById("vData").innerText = lista.dataCriacao;
  document.getElementById("vStatus").innerText = statusToText(lista.statusAtendimento);

  const corpo = document.getElementById("vItens");
  corpo.innerHTML = "";

  itens.forEach(i => {
    corpo.innerHTML += `
      <tr>
        <td>${i.produtoProdCod}</td>
        <td>${i.produtoDescr}</td>
        <td>${i.quantidade}</td>
      </tr>
    `;
  });

  new bootstrap.Modal("#modalViewLista").show();
}

// =========================
// EDITAR LISTA
// =========================
async function editarLista(id) {
  try {
    const res = await fetch(API_LISTAS + "/" + id);
    if (!res.ok) throw new Error("Erro ao carregar lista.");
    const l = await res.json();

    lcCod.value = l.lcCod;
    funcSearch.value = l.funcNome;
    funcionarioCpf.value = l.funcionarioFuncCpf;
    dataCriacao.value = l.dataCriacao;
    descricao.value = l.descricao;
    statusAtendimento.value = l.statusAtendimento;

    itensPendentes = [];

    await carregarCategorias();
    await carregarItensExistentes(id);

    bloquearCampos(l.statusAtendimento);

  } catch (err) {
    Utils.toast("danger", err.message);
  }
}


// =========================
// SALVAR LISTA
// =========================
document.getElementById("formLista").addEventListener("submit", async (e) => {
  e.preventDefault();

  const obj = {
    funcionarioFuncCpf: funcionarioCpf.value,
    dataCriacao: dataCriacao.value,
    descricao: descricao.value,
    statusAtendimento: Number(statusAtendimento.value)
  };

  if (!obj.funcionarioFuncCpf) return Utils.toast("warning", "Selecione um funcionário!");
  if (!obj.descricao.trim()) return Utils.toast("warning", "Descrição é obrigatória!");

  let nova = lcCod.value === "";
  let res;

  if (nova) {
    res = await fetch(API_LISTAS, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(obj)
    });
  } else {
    res = await fetch(API_LISTAS + "/" + lcCod.value, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(obj)
    });
  }

  if (!res.ok) {
    Utils.toast("danger", "Erro ao salvar lista.");
    return;
  }

  const listaSalva = await res.json();
  const idLista = listaSalva.lcCod;

  // GRAVAR ITENS PENDENTES
  if (nova && itensPendentes.length > 0) {
    for (const it of itensPendentes) {
      await fetch(`${API_LISTAS}/${idLista}/itens`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          produtoCod: it.produtoCod,
          quantidade: it.quantidade
        })
      });
    }
  }

  Utils.toast("success", "Lista salva!");
  limparForm();
  carregarListas();
});

// =========================
// EXCLUIR LISTA
// =========================
async function excluirLista(id) {
  if (!confirm("Excluir lista?")) return;

  const res = await fetch(API_LISTAS + "/" + id, { method: "DELETE" });
  if (!res.ok) return Utils.toast("danger", "Erro ao excluir.");

  Utils.toast("success", "Lista excluída!");
  carregarListas();
}

// =========================
// CARREGAR CATEGORIAS
// =========================
async function carregarCategorias() {
  const [r1, r2] = await Promise.all([fetch(API_CATEG), fetch(API_PROD)]);
  const categorias = await r1.json();
  const produtos = await r2.json();

  const catsComProd = new Set(produtos.map(p => p.categoriaProdCod));

  categoriaItem.innerHTML = "";
  categorias
    .filter(c => catsComProd.has(c.catCod))
    .forEach(c => {
      categoriaItem.innerHTML += `<option value="${c.catCod}">${c.catDescr}</option>`;
    });

  carregarProdutos();
}

categoriaItem.onchange = carregarProdutos;

// =========================
// CARREGAR PRODUTOS
// =========================
async function carregarProdutos() {
  const cat = categoriaItem.value;
  produtoItem.innerHTML = "";
  
  const res = await fetch(API_PROD + `?categoria=${cat}`);
  if (!res.ok) return;

  const lista = await res.json();
  lista.forEach(p => {
    produtoItem.innerHTML += `<option value="${p.prodCod}">${p.prodDescr}</option>`;
  });
}

// =========================
// CARREGAR ITENS (LISTA EXISTENTE)
// =========================
async function carregarItensExistentes(id) {
  const res = await fetch(API_LISTAS + `/${id}/itens`);
  if (!res.ok) return;

  const itens = await res.json();
  tabelaItens.innerHTML = "";

  itens.forEach(i => {
    tabelaItens.innerHTML += `
      <tr>
        <td>${i.produtoProdCod}</td>
        <td>${i.produtoDescr}</td>
        <td>${i.quantidade}</td>
        <td><span class="badge text-bg-success">salvo</span></td>
        <td><button class="btn btn-danger btn-sm" data-rm="${i.produtoProdCod}">Excluir</button></td>
      </tr>
    `;
  });

  tabelaItens.querySelectorAll("[data-rm]").forEach(btn => {
    btn.onclick = () => removerItem(id, btn.dataset.rm);
  });
}

// =========================
// REMOVER ITEM EXISTENTE
// =========================
async function removerItem(id, prod) {
  if (!confirm("Excluir item?")) return;

  const res = await fetch(API_LISTAS + `/${id}/itens/${prod}`, {
    method: "DELETE"
  });

  if (!res.ok) {
    Utils.toast("danger", "Erro ao remover item.");
    return;
  }

  Utils.toast("success", "Item removido!");
  carregarItensExistentes(id);
}

// =========================
// ADICIONAR ITEM (NOVA OU EXISTENTE)
// =========================
document.getElementById("btnAddItem").onclick = async () => {
  const prodCod = Number(produtoItem.value);
  const prodDescr = produtoItem.options[produtoItem.selectedIndex]?.text;
  const qt = Number(qtItem.value);

  if (!prodCod) return Utils.toast("warning", "Selecione um produto.");
  if (!qt || qt <= 0) return Utils.toast("warning", "Quantidade inválida.");

  // -----------------------------
  // NOVA LISTA → itensPendentes
  // -----------------------------
  if (!lcCod.value) {

    let existente = itensPendentes.find(i => i.produtoCod === prodCod);
    if (existente) {
      existente.quantidade += qt;
    } else {
      itensPendentes.push({ produtoCod: prodCod, produtoDescr: prodDescr, quantidade: qt });
    }

    renderItensPendentes();
    qtItem.value = "";
    Utils.showAlert(alertBox, "info", "Item adicionado (pendente).");

    return;
  }

  // -----------------------------
  // LISTA EXISTENTE → backend
  // SOMA QUANTIDADE automaticamente
  // -----------------------------
  const res = await fetch(`${API_LISTAS}/${lcCod.value}/itens`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ produtoCod: prodCod, quantidade: qt })
  });

  if (!res.ok) {
    Utils.toast("danger", "Erro ao salvar item.");
    return;
  }

  Utils.toast("success", "Item salvo.");
  carregarItensExistentes(lcCod.value);
  qtItem.value = "";
};

function renderItensPendentes() {
  tabelaItens.innerHTML = "";
  itensPendentes.forEach(i => {
    tabelaItens.innerHTML += `
      <tr>
        <td>${i.produtoCod}</td>
        <td>${i.produtoDescr}</td>
        <td>${i.quantidade}</td>
        <td><span class="badge text-bg-secondary">pendente</span></td>
        <td><button class="btn btn-danger btn-sm" onclick="removerItemPendente(${i.produtoCod})">X</button></td>
      </tr>
    `;
  });
}

function removerItemPendente(prodCod) {
  itensPendentes = itensPendentes.filter(i => i.produtoCod !== prodCod);
  renderItensPendentes();
}

// =========================
// LIMPAR FORM
// =========================
document.getElementById("btnLimpar").onclick = limparForm;

function limparForm() {
  lcCod.value = "";
  funcSearch.value = "";
  funcionarioCpf.value = "";
  descricao.value = "";
  statusAtendimento.value = "0";
  dataCriacao.value = hoje;
  itensPendentes = [];
  tabelaItens.innerHTML = "";
}

// =========================
// BUSCA
// =========================
document.getElementById("btnBuscar").onclick = carregarListas;
document.getElementById("btnResetBusca").onclick = () => {
  document.getElementById("buscar").value = "";
  carregarListas();
};

function bloquearCampos(status) {

  // pega botões de remover item
  const botoesRemover = document.querySelectorAll("#tabelaItens button[data-rm]");
  
  if (status === 0) {
    // ========== LISTA ABERTA ==========
    funcSearch.disabled = false;
    funcionarioCpf.disabled = false;
    dataCriacao.disabled = false;
    descricao.disabled = false;
    statusAtendimento.disabled = false;

    categoriaItem.disabled = false;
    produtoItem.disabled = false;
    qtItem.disabled = false;
    document.getElementById("btnAddItem").disabled = false;

    botoesRemover.forEach(b => b.disabled = false);
    return;
  }

  if (status === 1) {
    // ========== LISTA EM ANDAMENTO ==========
    Utils.toast("warning", "Listas EM ANDAMENTO só permitem alterar descrição e status.");

    funcSearch.disabled = true;
    funcionarioCpf.disabled = true;
    dataCriacao.disabled = true;

    categoriaItem.disabled = true;
    produtoItem.disabled = true;
    qtItem.disabled = true;
    document.getElementById("btnAddItem").disabled = true;

    botoesRemover.forEach(b => b.disabled = true);

    // somente esses 2 podem
    descricao.disabled = false;
    statusAtendimento.disabled = false;
    return;
  }

  if (status === 2) {
    // ========== LISTA CONCLUÍDA ==========
    Utils.toast("warning", "Listas CONCLUÍDAS não podem ser alteradas.");
      

    // BLOQUEIA TUDO
    funcSearch.disabled = true;
    funcionarioCpf.disabled = true;
    dataCriacao.disabled = true;
    descricao.disabled = true;
    statusAtendimento.disabled = true;

    categoriaItem.disabled = true;
    produtoItem.disabled = true;
    qtItem.disabled = true;
    document.getElementById("btnAddItem").disabled = true;

    botoesRemover.forEach(b => b.disabled = true);
    return;
  }
}



// =========================
// INIT
// =========================
carregarListas();
carregarCategorias();
