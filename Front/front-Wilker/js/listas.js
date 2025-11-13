const API_LISTAS = "http://localhost:8080/api/listas";
const API_FUNC = "http://localhost:8080/api/funcionarios";
const API_CATEG = "http://localhost:8080/api/categorias";
const API_PROD = "http://localhost:8080/api/produtos";

const alertBox = document.getElementById("alert-container");

const funcSearch = document.getElementById("funcSearch");
const funcAutocomplete = document.getElementById("funcAutocomplete");
const funcionarioCpf = document.getElementById("funcionarioCpf");
const lcCod = document.getElementById("lcCod");
const dataCriacao = document.getElementById("dataCriacao");
const descricao = document.getElementById("descricao");
const statusAtendimento = document.getElementById("statusAtendimento");

const tabelaListas = document.querySelector("#tabelaListas tbody");
const tipoBusca = document.getElementById("tipoBusca");
const buscar = document.getElementById("buscar");

const tabelaItens = document.querySelector("#tabelaItens tbody");
const categoriaItem = document.getElementById("categoriaItem");
const produtoItem = document.getElementById("produtoItem");
const qtItem = document.getElementById("qtItem");

let itensPendentes = [];

// DATA: máximo hoje
const hojeStr = new Date().toISOString().split("T")[0];
dataCriacao.max = hojeStr;
if (!dataCriacao.value) dataCriacao.value = hojeStr;

// AUTOCOMPLETE FUNCIONÁRIO
funcSearch.addEventListener("input", async () => {
  const termo = funcSearch.value.trim();
  if (termo.length < 2) {
    funcAutocomplete.innerHTML = "";
    return;
  }

  try {
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
  } catch {
    // ignora
  }
});

function statusToText(st) {
  return st === 0 ? "ABERTA" :
         st === 1 ? "EM ANDAMENTO" : "CONCLUÍDA";
}

// CARREGAR LISTAS
async function carregarListas() {
  let url = API_LISTAS;
  const t = tipoBusca.value;
  const termo = buscar.value.trim();

  if (termo !== "") {
    url += `?tipo=${t}&termo=${encodeURIComponent(termo)}`;
  }

  try {
    const res = await fetch(url);
    if (!res.ok) throw new Error("Erro ao carregar listas.");
    const lista = await res.json();

    tabelaListas.innerHTML = "";

    lista.forEach(l => {
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

    // ações
    tabelaListas.querySelectorAll("[data-view]").forEach(btn => {
      btn.onclick = () => visualizarLista(btn.dataset.view);
    });
    tabelaListas.querySelectorAll("[data-ed]").forEach(btn => {
      btn.onclick = () => editarLista(btn.dataset.ed);
    });
    tabelaListas.querySelectorAll("[data-del]").forEach(btn => {
      btn.onclick = () => excluirLista(btn.dataset.del);
    });

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// VISUALIZAR LISTA (MODAL)
async function visualizarLista(id) {
  try {
    const [resLista, resItens] = await Promise.all([
      fetch(API_LISTAS + "/" + id),
      fetch(API_LISTAS + "/" + id + "/itens")
    ]);

    if (!resLista.ok || !resItens.ok) {
      Utils.showAlert(alertBox, "danger", "Erro ao carregar dados para visualização.");
      return;
    }

    const lista = await resLista.json();
    const itens = await resItens.json();

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

    const modal = new bootstrap.Modal(document.getElementById("modalViewLista"));
    modal.show();

  } catch {
    Utils.showAlert(alertBox, "danger", "Erro ao visualizar lista.");
  }
}

// SALVAR LISTA (com itens pendentes quando nova)
document.getElementById("formLista").addEventListener("submit", async (e) => {
  e.preventDefault();

  const obj = {
    funcionarioFuncCpf: funcionarioCpf.value,
    dataCriacao: dataCriacao.value,
    descricao: descricao.value,
    statusAtendimento: Number(statusAtendimento.value)
  };

  let res;
  let nova = false;

  try {
    if (!obj.funcionarioFuncCpf) throw new Error("Selecione um funcionário.");
    if (!obj.descricao.trim()) throw new Error("Descrição é obrigatória.");
    if (!obj.dataCriacao) throw new Error("Data é obrigatória.");

    if (lcCod.value === "") {
      nova = true;
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
      let msg = "Erro ao salvar lista.";
      try {
        const erro = await res.json();
        if (erro.mensagem) msg = erro.mensagem;
      } catch {}
      throw new Error(msg);
    }

    const listaSalva = await res.json();
    const idLista = listaSalva.lcCod;

    if (nova && itensPendentes.length > 0) {
      for (const it of itensPendentes) {
        const bodyItem = {
          produtoCod: it.produtoCod,
          quantidade: it.quantidade
        };
        const resItem = await fetch(`${API_LISTAS}/${idLista}/itens`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(bodyItem)
        });
        if (!resItem.ok) {
          let msg = "Erro ao salvar um item.";
          try {
            const erro = await resItem.json();
            if (erro.mensagem) msg = erro.mensagem;
          } catch {}
          Utils.showAlert(alertBox, "warning", msg);
        }
      }
      itensPendentes = [];
    }

    Utils.showAlert(alertBox, "success", "Lista salva com sucesso.");
    limparForm();
    carregarListas();

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
});

// EDITAR LISTA
async function editarLista(id) {
  try {
    const res = await fetch(API_LISTAS + "/" + id);
    if (!res.ok) throw new Error("Erro ao carregar lista.");
    const l = await res.json();

    lcCod.value = l.lcCod;
    funcionarioCpf.value = l.funcionarioFuncCpf;
    funcSearch.value = l.funcNome;
    dataCriacao.value = l.dataCriacao;
    descricao.value = l.descricao;
    statusAtendimento.value = l.statusAtendimento;

    itensPendentes = [];
    await carregarCategorias();
    await carregarItens(id);

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// EXCLUIR LISTA
async function excluirLista(id) {
  if (!confirm("Excluir lista?")) return;

  try {
    const res = await fetch(API_LISTAS + "/" + id, { method: "DELETE" });
    if (!res.ok) throw new Error("Erro ao excluir lista.");
    Utils.showAlert(alertBox, "success", "Lista excluída.");
    limparForm();
    carregarListas();
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// CATEGORIAS (apenas com produtos)
async function carregarCategorias() {
  try {
    const [resCat, resProd] = await Promise.all([
      fetch(API_CATEG),
      fetch(API_PROD)
    ]);
    if (!resCat.ok || !resProd.ok) throw new Error("Erro ao carregar categorias/produtos.");

    const categorias = await resCat.json();
    const produtos = await resProd.json();

    const catComProduto = new Set(produtos.map(p => p.categoriaProdCod));

    categoriaItem.innerHTML = "";
    categorias
      .filter(c => catComProduto.has(c.catCod))
      .forEach(c => {
        categoriaItem.innerHTML += `<option value="${c.catCod}">${c.catDescr}</option>`;
      });

    carregarProdutos();
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

categoriaItem.onchange = carregarProdutos;

async function carregarProdutos() {
  const cat = categoriaItem.value;
  if (!cat) {
    produtoItem.innerHTML = "";
    return;
  }
  try {
    const res = await fetch(API_PROD + `?categoria=${cat}`);
    if (!res.ok) throw new Error("Erro ao carregar produtos.");
    const lista = await res.json();

    produtoItem.innerHTML = "";
    lista.forEach(p => {
      produtoItem.innerHTML += `<option value="${p.prodCod}">${p.prodDescr}</option>`;
    });
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// CARREGAR ITENS DE LISTA EXISTENTE
async function carregarItens(id) {
  try {
    const res = await fetch(API_LISTAS + "/" + id + "/itens");
    if (!res.ok) throw new Error("Erro ao carregar itens.");
    const lista = await res.json();

    tabelaItens.innerHTML = "";
    lista.forEach(i => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${i.produtoProdCod}</td>
        <td>${i.produtoDescr}</td>
        <td>${i.quantidade}</td>
        <td><span class="badge text-bg-success">salvo</span></td>
        <td>
          <button class="btn btn-danger btn-sm" data-rm="${i.produtoProdCod}">Excluir</button>
        </td>
      `;
      tabelaItens.appendChild(tr);
    });

    tabelaItens.querySelectorAll("[data-rm]").forEach(btn => {
      btn.onclick = () => removerItem(id, btn.dataset.rm);
    });

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// ITENS PENDENTES (lista nova)
function renderItensPendentes() {
  tabelaItens.innerHTML = "";
  itensPendentes.forEach(i => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${i.produtoCod}</td>
      <td>${i.produtoDescr}</td>
      <td>${i.quantidade}</td>
      <td><span class="badge text-bg-secondary">pendente</span></td>
      <td></td>
    `;
    tabelaItens.appendChild(tr);
  });
}

// REMOVER ITEM (lista existente)
async function removerItem(id, prod) {
  if (!confirm("Excluir item?")) return;

  try {
    const res = await fetch(API_LISTAS + `/${id}/itens/${prod}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Erro ao remover item.");
    Utils.showAlert(alertBox, "success", "Item removido");
    carregarItens(id);
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// ADICIONAR ITEM (nova ou existente)
document.getElementById("btnAddItem").onclick = async () => {
  const prod = produtoItem.value;
  const prodText = produtoItem.options[produtoItem.selectedIndex]?.text || "";
  const qt = Number(qtItem.value);

  if (!prod) {
    Utils.showAlert(alertBox, "warning", "Selecione um produto.");
    return;
  }
  if (!qt || qt <= 0) {
    Utils.showAlert(alertBox, "warning", "Informe uma quantidade válida.");
    return;
  }

  // Lista NOVA → itens pendentes
  if (!lcCod.value) {
    itensPendentes.push({
      produtoCod: Number(prod),
      produtoDescr: prodText,
      quantidade: qt
    });
    renderItensPendentes();
    qtItem.value = "";
    Utils.showAlert(alertBox, "info", "Item adicionado. Salve a lista para gravar no banco.");
    return;
  }

  // Lista EXISTENTE → grava direto no backend
  try {
    const body = {
      produtoCod: Number(prod),
      quantidade: qt
    };

    const res = await fetch(API_LISTAS + `/${lcCod.value}/itens`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      let msg = "Erro ao salvar item.";
      try {
        const erro = await res.json();
        if (erro.mensagem) msg = erro.mensagem;
      } catch {}
      throw new Error(msg);
    }

    Utils.showAlert(alertBox, "success", "Item salvo.");
    carregarItens(lcCod.value);
    qtItem.value = "";

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
};

// LIMPAR FORM
function limparForm() {
  lcCod.value = "";
  funcSearch.value = "";
  funcionarioCpf.value = "";
  descricao.value = "";
  statusAtendimento.value = "0";
  dataCriacao.value = hojeStr;
  itensPendentes = [];
  tabelaItens.innerHTML = "";
}

document.getElementById("btnLimpar").onclick = limparForm;

// BUSCA BOTOES
document.getElementById("btnBuscar").onclick = carregarListas;
document.getElementById("btnResetBusca").onclick = () => {
  buscar.value = "";
  carregarListas();
};

// INIT
carregarListas();
carregarCategorias();
