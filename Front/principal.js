// principal.js
// Responsabilidade deste arquivo agora:
// - carregar logotipo da empresa SE existir container na página
// - controlar navegação entre abas internas da dashboard (home, entrada, saida, etc.)
// - redirecionar o usuário para a página entrada-compra.html quando ele clicar em "Entrada de Compras"
// TODA a lógica de compras (listar compras, editar itens, confirmar, desfazer, etc)
// foi movida para entrada-compra.js, que roda só em entrada-compra.html.

document.addEventListener('DOMContentLoaded', async function () {
  // ======== 1. Logo dinâmica por empresa (opcional) ========
  // Se a URL tiver ?emailEmpresa=... e existir um <div id="logoEmpresa"> na página,
  // tentamos buscar o logotipo via backend e mostrar.
  // OBS: no index.html atual não tem #logoEmpresa, mas deixei a checagem para não quebrar.
  var urlParams = new URLSearchParams(window.location.search);
  var emailEmpresa = urlParams.get('emailEmpresa');
  var logoContainer = document.getElementById('logoEmpresa'); // pode ser null aqui

  if (emailEmpresa && logoContainer) {
    try {
      const response = await fetch(
        'http://localhost:8080/salf/param?email=' + encodeURIComponent(emailEmpresa)
      );

      if (response.ok) {
        const empresa = await response.json();

        // empresa.logotipo deve ser um array de bytes (byte[]) vindo do backend
        if (empresa.logotipo && Array.isArray(empresa.logotipo)) {
          const byteArray = new Uint8Array(empresa.logotipo);
          const blob = new Blob([byteArray], { type: 'image/png' });

          const reader = new FileReader();
          reader.onloadend = function () {
            var img = document.createElement('img');
            img.src = reader.result;
            img.alt = 'Logo da Empresa';
            img.classList.add('logo-dinamica');

            logoContainer.innerHTML = '';
            logoContainer.appendChild(img);
          };

          reader.readAsDataURL(blob);
        }
      }
    } catch (err) {
      console.warn('Erro ao carregar a logo da empresa:', err);
    }
  }

  // ======== 2. Botão "Entrada de Compras" no menu lateral ========
  // No index.html esse botão é um <a id="btn-entrada-compra" href="entrada-compra.html">
  // Antigamente a gente interceptava pra abrir uma aba SPA interna (#entrada-compra),
  // mas essa aba não existe mais no index.html.
  //
  // Agora o fluxo é:
  // - Clicou -> vai para entrada-compra.html
  // - Toda a lógica de listar compras, editar itens etc. roda em entrada-compra.js
  //
  // Abaixo eu só garanto o redirecionamento usando JS pra manter compatibilidade.
  var btnMenuCompras = document.getElementById('btn-entrada-compra');
  if (btnMenuCompras) {
    btnMenuCompras.addEventListener('click', function (e) {
      e.preventDefault();
      window.location.href = 'entrada-compra.html';
    });
  }

  // ======== 3. Função de aviso genérico em módulos não prontos ========
  // (index.html chama mostrarAviso('Módulo em desenvolvimento'))
  window.mostrarAviso = function (msg) {
    alert(msg);
  };

  // ======== 4. Trocar abas internas da dashboard (home, entrada, saida, etc.) ========
  // OBS: isso continua igual ao que você já tinha inline,
  // mas deixei disponível em window pra você poder chamar se quiser.
  window.showTab = function (tabId) {
    // Esconde todas as tabs
    var tabs = document.querySelectorAll('.tab');
    for (var i = 0; i < tabs.length; i++) {
      tabs[i].classList.remove('active');
      tabs[i].classList.add('hidden');
    }

    // Remove "active" de todos os botões da sidebar
    var btns = document.querySelectorAll('.nav-btn');
    for (var j = 0; j < btns.length; j++) {
      btns[j].classList.remove('active');
    }

    // Mostra a tab que o usuário clicou
    var selectedTab = document.getElementById(tabId);
    if (selectedTab) {
      selectedTab.classList.remove('hidden');
      selectedTab.classList.add('active');
    }

    // Destaca botão correspondente
    var correspondingBtn = document.getElementById('btn-' + tabId);
    if (correspondingBtn) {
      correspondingBtn.classList.add('active');
    }
  };
});
