
//categoria.js

const API_URL_CATEGORIAS = "http://localhost:8080/api/categorias";

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formCategoria");
  const catCod = document.getElementById("catCod");
  const catDescr = document.getElementById("catDescr");
  const catDescrError = document.getElementById("catDescrError");
  const alertContainer = document.getElementById("alert-container");
  const tabelaBody = document.querySelector("#tabelaCategorias tbody");
  const buscar = document.getElementById("buscar");
  const btnBuscar = document.getElementById("btnBuscar");
  const btnResetBusca = document.getElementById("btnResetBusca");
  const btnLimpar = document.getElementById("btnLimpar");
  const btnSalvar = document.getElementById("btnSalvar");

  let listaAtual = [];

  // ==========================================
  // CARREGAR
  // ==========================================
  async function carregarCategorias() {
    try {
      const res = await fetch(API_URL_CATEGORIAS);
      if (!res.ok) throw new Error("Erro ao carregar categorias.");
      listaAtual = await res.json();
      montarTabela(listaAtual);
    } catch (err) {
      Utils.toast("danger", err.message);
    }
  }

  // ==========================================
  // MONTAR TABELA
  // ==========================================
  function montarTabela(lista) {
    tabelaBody.innerHTML = "";

    lista.forEach(cat => {
      const tr = document.createElement("tr");

      tr.innerHTML = `
        <td>${cat.catCod ?? ""}</td>
        <td>${cat.catDescr ?? ""}</td>
        <td>
          <button class="btn-visualizar" data-view="${cat.catCod}" title="Visualizar">&#128065;</button>
          <button class="btn btn-warning btn-sm" data-ed="${cat.catCod}">Editar</button>
          <button class="btn btn-danger btn-sm" data-del="${cat.catCod}">Excluir</button>
        </td>
      `;

      tabelaBody.appendChild(tr);
    });

    // VISUALIZAR
    tabelaBody.querySelectorAll("[data-view]").forEach(btn => {
      btn.addEventListener("click", () => visualizarCat(btn.dataset.view));
    });

    // EDITAR
    tabelaBody.querySelectorAll("[data-ed]").forEach(btn => {
      btn.addEventListener("click", () => {
        const id = btn.dataset.ed;
        const item = listaAtual.find(x => String(x.catCod) === String(id));
        if (item) {
          catCod.value = item.catCod ?? "";
          catDescr.value = item.catDescr ?? "";
          catDescr.focus();
        }
      });
    });

    // EXCLUIR
    tabelaBody.querySelectorAll("[data-del]").forEach(btn => {
      btn.addEventListener("click", async () => {
        const id = btn.dataset.del;

        if (!confirm("Deseja realmente excluir esta categoria?")) return;

        try {
          const res = await fetch(`${API_URL_CATEGORIAS}/${id}`, { method: "DELETE" });
          if (!res.ok) throw new Error("Erro ao excluir categoria.");

          Utils.toast("success", "Categoria excluída com sucesso.");
          carregarCategorias();

        } catch (err) {
          Utils.toast("danger", err.message);
        }
      });
    });
  }

  // ==========================================
  // SALVAR
  // ==========================================
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!Utils.validarObrigatorio(catDescr, catDescrError, "Descrição")) return;

    const payload = {
      catDescr: catDescr.value.trim()
    };

    const id = catCod.value ? Number(catCod.value) : null;
    let url = API_URL_CATEGORIAS;
    let method = "POST";

    if (id) {
      url = `${API_URL_CATEGORIAS}/${id}`;
      method = "PUT";
      payload.catCod = id;
    }

    try {
      const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        let msg = "Erro ao salvar categoria.";
        try {
          const erro = await res.json();
          if (erro.mensagem) msg = erro.mensagem;
        } catch (_) {}
        throw new Error(msg);
      }

      Utils.toast("success", "Categoria salva com sucesso.");
      limparFormulario();
      carregarCategorias();

    } catch (err) {
      Utils.toast("danger", err.message);
    }
  });

  function limparFormulario() {
    form.reset();
    catCod.value = "";
    catDescrError.innerText = "";
    catDescrError.style.display = "none";
  }

  btnLimpar.addEventListener("click", limparFormulario);

  // ==========================================
  // BUSCA
  // ==========================================
  btnBuscar.addEventListener("click", () => {
    const termo = buscar.value.trim();
    const filtrado = Utils.filtrarPorNome(listaAtual, "catDescr", termo);
    montarTabela(filtrado);
  });

  btnResetBusca.addEventListener("click", () => {
    buscar.value = "";
    montarTabela(listaAtual);
  });

  // ==========================================
  // VISUALIZAR CATEGORIA (MODAL)
  // ==========================================
  async function visualizarCat(id) {
    const res = await fetch(API_URL_CATEGORIAS + "/" + id);
    if (!res.ok) {
      Utils.toast("danger", "Erro ao carregar categoria.");
      return;
    }
    const cat = await res.json();

    document.getElementById("vcCod").innerText = cat.catCod ?? "";
    document.getElementById("vcDesc").innerText = cat.catDescr ?? "";

    const modal = new bootstrap.Modal(document.getElementById("modalViewCat"));
    modal.show();
  }

  // ==========================================
  // INIT
  // ==========================================
  carregarCategorias();
});


