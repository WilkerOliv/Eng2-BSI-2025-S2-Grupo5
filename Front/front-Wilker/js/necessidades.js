// Front/front-Wilker/js/necessidades.js
const API_URL_NECESSIDADES = "http://localhost:8080/api/necessidades/produtos";

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("formNecessidade");
  const pessoaCpf = document.getElementById("pessoaCpf");
  const produtoId = document.getElementById("produtoId");
  const quantidade = document.getElementById("quantidade");
  const data = document.getElementById("data");
  const observacao = document.getElementById("observacao");
  const alertContainer = document.getElementById("alert-container");
  const tabelaBody = document.querySelector("#tabelaNecessidades tbody");

  const buscar = document.getElementById("buscar");
  const btnBuscar = document.getElementById("btnBuscar");
  const btnResetBusca = document.getElementById("btnResetBusca");

  let listaAtual = [];

  async function carregarNecessidades() {
    try {
      const res = await fetch(API_URL_NECESSIDADES);
      if (!res.ok) throw new Error("Erro ao carregar necessidades");
      listaAtual = await res.json();
      montarTabela(listaAtual);
    } catch (e) {
      Utils.showAlert(alertContainer, "danger", e.message);
    }
  }

  function montarTabela(lista) {
    tabelaBody.innerHTML = "";
    lista.forEach(n => {
      const tr = document.createElement("tr");
      tr.innerHTML = [
        "<td>" + (n.pessoaCpf ?? "") + "</td>",
        "<td>" + (n.produtoId ?? "") + "</td>",
        "<td>" + (n.data ?? "") + "</td>",
        "<td>" + (n.quantidade ?? "") + "</td>",
        "<td>" + (n.observacao ?? "") + "</td>",
        "<td>" +
          "<button class=\"btn btn-danger btn-sm btn-small\" data-delete-cpf=\"" + (n.pessoaCpf ?? "") + "\" data-delete-prod=\"" + (n.produtoId ?? "") + "\">Excluir</button>" +
        "</td>"
      ].join("");
      tabelaBody.appendChild(tr);
    });

    tabelaBody.querySelectorAll("[data-delete-cpf]").forEach(btn => {
      btn.addEventListener("click", async function () {
        const cpf = this.getAttribute("data-delete-cpf");
        const prod = this.getAttribute("data-delete-prod");
        if (!confirm("Deseja excluir esta necessidade?")) return;
        try {
          const res = await fetch(API_URL_NECESSIDADES, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ pessoaCpf: cpf, produtoId: Number(prod) })
          });
          if (!res.ok) throw new Error("Erro ao excluir necessidade");
          Utils.showAlert(alertContainer, "success", "Necessidade excluída com sucesso.");
          await carregarNecessidades();
        } catch (e) {
          Utils.showAlert(alertContainer, "danger", e.message);
        }
      });
    });
  }

  function validarFormulario() {
    // limpar erros
    ["pessoaCpfError","produtoIdError","quantidadeError","dataError"].forEach(id => {
      document.getElementById(id).style.display = "none";
    });

    let ok = true;
    let v = Utils.validarObrigatorio(pessoaCpf.value, "CPF da Pessoa");
    if (v) { document.getElementById("pessoaCpfError").style.display = "block"; document.getElementById("pessoaCpfError").innerText = v; ok = false; }

    v = Utils.validarObrigatorio(produtoId.value, "ID do Produto");
    if (v) { document.getElementById("produtoIdError").style.display = "block"; document.getElementById("produtoIdError").innerText = v; ok = false; }
    else {
      v = Utils.validarNumeroPositivo(produtoId.value, "ID do Produto");
      if (v) { document.getElementById("produtoIdError").style.display = "block"; document.getElementById("produtoIdError").innerText = v; ok = false; }
    }

    v = Utils.validarObrigatorio(quantidade.value, "Quantidade");
    if (v) { document.getElementById("quantidadeError").style.display = "block"; document.getElementById("quantidadeError").innerText = v; ok = false; }
    else {
      v = Utils.validarNumeroPositivo(quantidade.value, "Quantidade");
      if (v) { document.getElementById("quantidadeError").style.display = "block"; document.getElementById("quantidadeError").innerText = v; ok = false; }
    }

    v = Utils.validarObrigatorio(data.value, "Data");
    if (v) { document.getElementById("dataError").style.display = "block"; document.getElementById("dataError").innerText = v; ok = false; }
    else {
      v = Utils.validarDataNaoPassada(data.value, "Data");
      if (v) { document.getElementById("dataError").style.display = "block"; document.getElementById("dataError").innerText = v; ok = false; }
    }

    return ok;
  }

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    if (!validarFormulario()) return;
    const payload = {
      pessoaCpf: pessoaCpf.value.trim(),
      produtoId: Number(produtoId.value),
      data: data.value,
      quantidade: Number(quantidade.value),
      observacao: observacao.value.trim()
    };

    try {
      const res = await fetch(API_URL_NECESSIDADES, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error("Erro ao salvar necessidade");
      Utils.showAlert(alertContainer, "success", "Necessidade salva com sucesso.");
      form.reset();
      await carregarNecessidades();
    } catch (err) {
      Utils.showAlert(alertContainer, "danger", err.message);
    }
  });

  document.getElementById("btnLimpar").addEventListener("click", function () {
    form.reset();
  });

  btnBuscar.addEventListener("click", function () {
    const termo = buscar.value || "";
    // filtramos por cpf ou observação automaticamente
    const filtrado = listaAtual.filter(item => {
      return Utils.matchesPattern(item.pessoaCpf || "", termo) || Utils.matchesPattern(item.observacao || "", termo);
    });
    montarTabela(filtrado);
  });

  btnResetBusca.addEventListener("click", function () {
    buscar.value = "";
    montarTabela(listaAtual);
  });

  // init
  carregarNecessidades();
});
