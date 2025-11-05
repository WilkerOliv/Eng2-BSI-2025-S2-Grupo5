/* ==========================================================
   utils.js – Funções globais de validação e mensagens
   Usado nas funcionalidades de Wilker (RF-B2, RF-F5, RF-F10)
   ========================================================== */

/* ---------------------- Funções Globais ---------------------- */

/**
 * Valida se um campo obrigatório foi preenchido.
 */
function validarCampoObrigatorio(campo, nome) {
  const valor = campo.value.trim();
  if (valor === "") {
    exibirMensagem("error", `O campo "${nome}" é obrigatório.`);
    campo.focus();
    return false;
  }
  return true;
}

/**
 * Valida se um número é positivo (não negativo e não nulo).
 */
function validarNumeroPositivo(valor, nomeCampo) {
  const numero = parseFloat(valor);
  if (isNaN(numero) || numero <= 0) {
    exibirMensagem("error", `O campo "${nomeCampo}" deve ser um número positivo.`);
    return false;
  }
  return true;
}

/**
 * Impede datas antigas – só permite hoje ou futuras.
 */
function validarDataFutura(campo) {
  const valor = campo.value;
  if (!valor) return false;

  const hoje = new Date();
  const dataSelecionada = new Date(valor + "T00:00:00");
  hoje.setHours(0, 0, 0, 0);

  if (dataSelecionada < hoje) {
    exibirMensagem("error", "A data informada não pode ser anterior à data atual.");
    campo.focus();
    return false;
  }
  return true;
}

/**
 * Exibe mensagens visuais de sucesso, erro ou informação.
 */
function exibirMensagem(tipo, texto) {
  let classe = "alert-info";
  if (tipo === "success") classe = "alert-success";
  if (tipo === "error") classe = "alert-danger";

  let container = document.getElementById("mensagem-container");
  if (!container) {
    container = document.createElement("div");
    container.id = "mensagem-container";
    container.style.position = "fixed";
    container.style.top = "15px";
    container.style.right = "15px";
    container.style.zIndex = "9999";
    document.body.appendChild(container);
  }

  const alerta = document.createElement("div");
  alerta.className = `alert ${classe} shadow-sm alert-wilker`;
  alerta.role = "alert";
  alerta.textContent = texto;

  container.appendChild(alerta);
  setTimeout(() => alerta.remove(), 4000);
}

/**
 * Limpa o container de mensagens.
 */
function limparMensagens() {
  const container = document.getElementById("mensagem-container");
  if (container) container.innerHTML = "";
}

/* ---------------------- Utils Namespace ---------------------- */

const Utils = (function () {

  function showAlert(container, type, message) {
    // Cria alerta Bootstrap customizado dentro de um container
    const div = document.createElement("div");
    div.className = "alert alert-" + type + " alert-wilker";
    div.setAttribute("role", "alert");
    div.innerText = message;

    // Remove alertas anteriores
    const prev = container.querySelectorAll(".alert-wilker");
    prev.forEach(p => p.remove());
    container.prepend(div);

    // Auto-fechar após 5s
    setTimeout(() => {
      try { div.remove(); } catch (e) {}
    }, 5000);
  }

  function isEmpty(val) {
    return val === null || val === undefined || (typeof val === "string" && val.trim() === "");
  }

  function validarObrigatorio(value, fieldName) {
    if (isEmpty(value)) {
      return fieldName + " é obrigatório.";
    }
    return null;
  }

  function validarNumeroPositivo(value, fieldName) {
    if (value === null || value === undefined || value === "") return null;
    const n = Number(value);
    if (Number.isNaN(n)) return fieldName + " deve ser um número válido.";
    if (n < 0) return fieldName + " não pode ser negativo.";
    return null;
  }

  function validarDataNaoPassada(value, fieldName) {
    if (isEmpty(value)) return null;
    const inputDate = new Date(value);
    inputDate.setHours(0, 0, 0, 0);
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    if (inputDate < hoje) {
      return fieldName + " não pode ser uma data passada.";
    }
    return null;
  }

  function limparAsteriscos(termo) {
    if (!termo) return "";
    return termo.replace(/\*/g, "").trim();
  }

  function matchesPattern(text, pattern) {
    if (!pattern) return true;
    const p = limparAsteriscos(pattern).toLowerCase();
    if (p === "") return true;
    return text && text.toLowerCase().includes(p);
  }

  function filtrarPorNome(lista, campoNome, termo) {
    const clean = limparAsteriscos(termo);
    if (clean === "") return lista.slice();
    return lista.filter(item => {
      const val = (item[campoNome] || "").toString();
      return matchesPattern(val, termo);
    });
  }

  return {
    showAlert,
    validarObrigatorio,
    validarNumeroPositivo,
    validarDataNaoPassada,
    filtrarPorNome,
    matchesPattern
  };
})();
