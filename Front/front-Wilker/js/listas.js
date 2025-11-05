// Front/front-Wilker/js/listas.js
const API_URL_LISTAS = "http://localhost:8080/api/listas";

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formLista");
  const lcCod = document.getElementById("lcCod");
  const funcionarioCpf = document.getElementById("funcionarioCpf");
  const dataCriacao = document.getElementById("dataCriacao");
  const descricao = document.getElementById("descricao");
  const statusAtendimento = document.getElementById("statusAtendimento");
  const alertContainer = document.getElementById("alert-container");
  const tabelaBody = document.querySelector("#tabelaListas tbody");

  const buscar = document.getElementById("buscar");
  const btnBuscar = document.getElementById("btnBuscar");
  const btnResetBusca = document.getElementById("btnResetBusca");

  let listaAtual = [];

  async function carregarListas() {
    try {
      const res = await fetch(API_URL_LISTAS);
      if (!res.ok) throw new Error("Erro ao carregar listas");
      listaAtual = await res.json();
      montarTabela(listaAtual);
    } catch (e) {
      Utils.showAlert(alertContainer, "danger", e.message);
    }
  }

  function montarTabela(lista) {
    tabelaBody.innerHTML = "";
    lista.forEach(l => {
      const tr = document.createElement("tr");
      tr.innerHTML = [
        "<td>" + (l.lcCod ?? "") + "</td>",
        "<td>" + (l.funcionarioCpf ?? "") + "</td>",
        "<td>" + (l.dataCriacao ?? "") + "</td>",
        "<td>" + (l.descricao ?? "") + "</td>",
        "<td>" + (l.statusAtendimento ?? "") + "</td>",
        "<td>" +
          "<button class=\"btn btn-warning btn-sm btn-small me-1\" data-edit=\"" + (l.lcCod ?? "") + "\">Editar</button>" +
          "<button class=\"btn btn-danger btn-sm btn-small\" data-delete=\"" + (l.lcCod ?? "") + "\">Excluir</button>" +
        "</td>"
      ].join("");
      tabelaBody.appendChild(tr);
    });

    tabelaBody.querySelectorAll("[data-edit]").forEach(btn => {
      btn.addEventListener("click", function () {
        const id = this.getAttribute("data-edit");
        const item = listaAtual.find(x => String(x.lcCod) === String(id));
        if (item) {
          lcCod.value = item.lcCod;
          funcionarioCpf.value = item.funcionarioCpf;
          dataCriacao.value = item.dataCriacao;
          descricao.value = item.descricao;
          statusAtendimento.value = item.statusAtendimento ?? 0;
          window.scrollTo({ top: 0, behavior: "smooth" });
        }
      });
    });

    tabelaBody.querySelectorAll("[data-delete]").forEach(btn => {
      btn.addEventListener("click", async function () {
        const id = this.getAttribute("data-delete");
        if (!confirm("Deseja excluir esta lista?")) return;
        try {
          const res = await fetch(API_URL_LISTAS + "/" + id, { method: "DELETE" });
          if (!res.ok) throw new Error("Erro ao excluir lista");
          Utils.showAlert(alertContainer, "success", "Lista excluída com sucesso.");
          await carregarListas();
        } catch (e) {
          Utils.showAlert(alertContainer, "danger", e.message);
        }
      });
    });
  }

  function validarFormulario() {
    // limpar mensagens
    document.getElementById("funcCpfError").style.display = "none";
    document.getElementById("dataError").style.display = "none";
    document.getElementById("descError").style.display = "none";
    document.getElementById("statusError").style.display = "none";

    let ok = true;
    let v = Utils.validarObrigatorio(funcionarioCpf.value, "CPF do funcionário");
    if (v) { document.getElementById("funcCpfError").style.display = "block"; document.getElementById("funcCpfError").innerText = v; ok = false; }

    v = Utils.validarObrigatorio(dataCriacao.value, "Data");
    if (v) { document.getElementById("dataError").style.display = "block"; document.getElementById("dataError").innerText = v; ok = false; }
    else {
      v = Utils.validarDataNaoPassada(dataCriacao.value, "Data");
      if (v) { document.getElementById("dataError").style.display = "block"; document.getElementById("dataError").innerText = v; ok = false; }
    }

    v = Utils.validarObrigatorio(descricao.value, "Descrição");
    if (v) { document.getElementById("descError").style.display = "block"; document.getElementById("descError").innerText = v; ok = false; }

    v = Utils.validarNumeroPositivo(statusAtendimento.value, "Status");
    if (v) { document.getElementById("statusError").style.display = "block"; document.getElementById("statusError").innerText = v; ok = false; }

    return ok;
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    if (!validarFormulario()) return;
    const payload = {
      lcCod: lcCod.value ? parseInt(lcCod.value) : null,
      funcionarioCpf: funcionarioCpf.value.trim(),
      dataCriacao: dataCriacao.value,
      descricao: descricao.value.trim(),
      statusAtendimento: statusAtendimento.value ? parseInt(statusAtendimento.value) : 0
    };

    try {
      const method = payload.lcCod ? "PUT" : "POST";
      const url = payload.lcCod ? API_URL_LISTAS + "/" + payload.lcCod : API_URL_LISTAS;
      const res = await fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error("Erro ao salvar lista");
      Utils.showAlert(alertContainer, "success", "Lista salva com sucesso.");
      form.reset();
      lcCod.value = "";
      await carregarListas();
    } catch (err) {
      Utils.showAlert(alertContainer, "danger", err.message);
    }
  });

  document.getElementById("btnLimpar").addEventListener("click", function () {
    form.reset();
    lcCod.value = "";
  });

  btnBuscar.addEventListener("click", function () {
    const termo = buscar.value || "";
    const filtrado = Utils.filtrarPorNome(listaAtual, "descricao", termo);
    montarTabela(filtrado);
  });

  btnResetBusca.addEventListener("click", function () {
    buscar.value = "";
    montarTabela(listaAtual);
  });

  // init
  carregarListas();
});
