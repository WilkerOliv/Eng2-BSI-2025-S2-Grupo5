// utils.js

const Utils = (function () {

  // Cria alerta Bootstrap dentro de um container
  function showAlert(container, type, message) {
    if (!container) return;

    const div = document.createElement("div");
    div.className = "alert alert-" + type + " alert-wilker";
    div.setAttribute("role", "alert");
    div.innerText = message;

    // Remove alertas anteriores
    container.querySelectorAll(".alert-wilker").forEach(el => el.remove());
    container.prepend(div);

    // Auto-fechar em 5s
    setTimeout(() => {
      div.remove();
    }, 5000);
  }

  function limparErro(errorDiv) {
    if (!errorDiv) return;
    errorDiv.style.display = "none";
    errorDiv.innerText = "";
  }

  function exibirErro(errorDiv, mensagem) {
    if (!errorDiv) return;
    errorDiv.style.display = "block";
    errorDiv.innerText = mensagem;
  }

  // Campo obrigatório
  function validarObrigatorio(input, errorDiv, nomeCampo) {
    limparErro(errorDiv);
    if (!input) return false;
    const valor = input.value.trim();
    if (valor === "") {
      exibirErro(errorDiv, `O campo "${nomeCampo}" é obrigatório.`);
      input.focus();
      return false;
    }
    return true;
  }

  // Número positivo
  function validarNumeroPositivo(input, errorDiv, nomeCampo) {
    limparErro(errorDiv);
    if (!input) return false;
    const valor = input.value.trim();
    const numero = parseFloat(valor);
    if (isNaN(numero) || numero <= 0) {
      exibirErro(errorDiv, `O campo "${nomeCampo}" deve ser um número positivo.`);
      input.focus();
      return false;
    }
    return true;
  }

  // Data não vazia (se quiser, pode adaptar para não aceitar data futura/passada)
  function validarDataNaoVazia(input, errorDiv, nomeCampo) {
    limparErro(errorDiv);
    if (!input) return false;
    const valor = input.value;
    if (!valor) {
      exibirErro(errorDiv, `O campo "${nomeCampo}" é obrigatório.`);
      input.focus();
      return false;
    }
    return true;
  }

  // Tratamento simples para "wildcards" com asterisco (*)
  function limparAsteriscos(termo) {
    if (!termo) return "";
    return termo.replace(/\*/g, "").trim();
  }

  // Verifica se "text" contém "pattern" (insensível a maiúsc./minúsc.)
  function matchesPattern(text, pattern) {
    if (!pattern) return true;
    const p = limparAsteriscos(pattern).toLowerCase();
    if (p === "") return true;
    return text && text.toLowerCase().includes(p);
  }

  // Filtra uma lista por um campo texto (ex.: "catDescr")
  function filtrarPorNome(lista, campoNome, termo) {
    const clean = limparAsteriscos(termo);
    if (clean === "") return lista.slice();
    const lower = clean.toLowerCase();
    return lista.filter(item => {
      const valor = (item[campoNome] || "").toString().toLowerCase();
      return valor.includes(lower);
    });
  }

  return {
    showAlert,
    validarObrigatorio,
    validarNumeroPositivo,
    validarDataNaoVazia,
    matchesPattern,
    filtrarPorNome
  };
})();







// =============================================================
// TOAST MODERNO COMPACTO SALF
// =============================================================
function toast(tipo, mensagem, duracao = 4000) {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        document.body.appendChild(container);
    }

    const div = document.createElement("div");
    div.className = `toast-salf toast-${tipo}`;
    div.innerText = mensagem;

    container.appendChild(div);

    setTimeout(() => {
        div.style.transition = "opacity 0.4s";
        div.style.opacity = "0";
        setTimeout(() => div.remove(), 400);
    }, duracao);
}

Utils.toast = toast;
