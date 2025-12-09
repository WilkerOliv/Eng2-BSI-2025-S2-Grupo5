const API_URL_CATEGORIAS = "http://localhost:8080/api/categorias";

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formCategoria");
  const catCod = document.getElementById("catCod");
  const catDescr = document.getElementById("catDescr");
  const catDescrError = document.getElementById("catDescrError");
  const alertContainer = document.getElementById("alert-container");
  const tabelaBody = document.querySelector("#tabelaCategorias tbody");
  const buscar = document.getElementById("buscar");
  const btnBuscar = document.getElementById("btnBuscar");
  const btnResetBusca = document.getElementById("btnResetBusca");

  let listaAtual = [];

  async function carregarCategorias() {
    try {
      const res = await fetch(API_URL_CATEGORIAS);
      if (!res.ok) throw new Error("Erro ao carregar categorias");
      listaAtual = await res.json();
      montarTabela(listaAtual);
    } catch (e) {
      Utils.showAlert(alertContainer, "danger", e.message);
    }
  }

  function montarTabela(lista) {
    tabelaBody.innerHTML = "";
    lista.forEach(cat => {
      const tr = document.createElement("tr");
      tr.innerHTML = [
        "<td>" + (cat.catCod ?? "") + "</td>",
        "<td>" + (cat.catDescr ?? "") + "</td>",
        "<td>" +
          "<button class=\"btn btn-warning btn-sm btn-small me-1\" data-edit=\"" + (cat.catCod ?? "") + "\">Editar</button>" +
          "<button class=\"btn btn-danger btn-sm btn-small\" data-delete=\"" + (cat.catCod ?? "") + "\">Excluir</button>" +
        "</td>"
      ].join("");
      tabelaBody.appendChild(tr);
    });
    tabelaBody.querySelectorAll("[data-edit]").forEach(btn => {
      btn.addEventListener("click", function () {
        const id = this.getAttribute("data-edit");
        const item = listaAtual.find(x => String(x.catCod) === String(id));
        if (item) {
          catCod.value = item.catCod;
          catDescr.value = item.catDescr;
          window.scrollTo({ top: 0, behavior: "smooth" });
        }
      });
    });
    tabelaBody.querySelectorAll("[data-delete]").forEach(btn => {
      btn.addEventListener("click", async function () {
        const id = this.getAttribute("data-delete");
        if (!confirm("Deseja excluir esta categoria?")) return;
        try {
          const res = await fetch(API_URL_CATEGORIAS + "/" + id, { method: "DELETE" });
          if (!res.ok) throw new Error("Erro ao excluir categoria");
          Utils.showAlert(alertContainer, "success", "Categoria excluída com sucesso.");
          await carregarCategorias();
        } catch (e) {
          Utils.showAlert(alertContainer, "danger", e.message);
        }
      });
    });
  }

  function validarFormulario() {
    catDescrError.style.display = "none";
    const vObrig = Utils.validarObrigatorio(catDescr.value, "Descrição");
    if (vObrig) {
      catDescrError.style.display = "block";
      catDescrError.innerText = vObrig;
      return false;
    }
    return true;
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    if (!validarFormulario()) return;
    const payload = {
      catCod: catCod.value ? parseInt(catCod.value) : null,
      catDescr: catDescr.value.trim()
    };
    try {
      const method = payload.catCod ? "PUT" : "POST";
      const url = payload.catCod ? API_URL_CATEGORIAS + "/" + payload.catCod : API_URL_CATEGORIAS;
      const res = await fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error("Erro ao salvar categoria");
      Utils.showAlert(alertContainer, "success", "Categoria salva com sucesso.");
      form.reset();
      catCod.value = "";
      await carregarCategorias();
    } catch (err) {
      Utils.showAlert(alertContainer, "danger", err.message);
    }
  });

  document.getElementById("btnLimpar").addEventListener("click", function () {
    form.reset();
    catCod.value = "";
    catDescrError.style.display = "none";
  });

  btnBuscar.addEventListener("click", function () {
    const termo = buscar.value || "";
    const filtrado = Utils.filtrarPorNome(listaAtual, "catDescr", termo);
    montarTabela(filtrado);
  });

  btnResetBusca.addEventListener("click", function () {
    buscar.value = "";
    montarTabela(listaAtual);
  });

  // init
  carregarCategorias();
});
