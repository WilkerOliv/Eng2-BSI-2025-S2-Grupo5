document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);
  const nivel = urlParams.get('nivel');
  const funcEmail = urlParams.get('funcEmail');

  const form = document.getElementById('empresaForm');
  const msg = document.getElementById('msg');
  const error = document.getElementById('error');
  const resultado = document.getElementById('empresaResultado');
  const logoContainer = document.getElementById('logoEmpresa');
  const dadosContainer = document.getElementById('dadosEmpresa');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    msg.textContent = '';
    error.textContent = '';
    resultado.style.display = 'none';
    logoContainer.innerHTML = '';
    dadosContainer.innerHTML = '';

    const emailEmpresa = form.emailEmpresa.value.trim();
    if (!emailEmpresa) {
      error.textContent = 'Informe o e-mail da empresa.';
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/salf/param?email=${encodeURIComponent(emailEmpresa)}`);

      if (response.status === 404) {
        const existeResp = await fetch('http://localhost:8080/salf/param/existeEmpresa');
        const existeEmpresa = await existeResp.json();

        if (existeEmpresa) {
          error.textContent = 'Já existe uma empresa cadastrada. Você não pode cadastrar outra.';
          return;
        }

        // Redireciona para parametrização se ainda não existe
        window.location.href = `parametrizacao.html?emailEmpresa=${encodeURIComponent(emailEmpresa)}&nivel=${nivel}`;
        return;
      }

      if (!response.ok) {
        throw new Error('Erro ao buscar empresa.');
      }

      const empresa = await response.json();

      if (empresa.logotipoBase64) {
        const img = document.createElement('img');
        img.src = `data:image/png;base64,${empresa.logotipoBase64}`;
        img.alt = 'Logo da Empresa';
        logoContainer.appendChild(img);
      }

      dadosContainer.innerHTML = `
        <strong>Empresa encontrada:</strong><br>
        <b>Nome:</b> ${empresa.nomeEmpresa || 'Não informado'}<br>
        <b>CNPJ:</b> ${empresa.cnpj || 'Não informado'}<br>
        <b>Endereço:</b> ${empresa.endereco || 'Não informado'}<br>
        <b>Telefone:</b> ${empresa.telefone || 'Não informado'}<br>
        <b>E-mail:</b> ${empresa.email || 'Não informado'}<br><br>

        <div class="d-flex gap-3 justify-content-center mt-3">
          <button class="btn btn-success" onclick="irParaSistema('${emailEmpresa}', '${nivel}', '${funcEmail}')">Ir para o Sistema</button>
          <button class="btn btn-secondary" onclick="irParaParametrizacao('${emailEmpresa}', '${nivel}')">Ir para Parametrização</button>
        </div>
      `;

      resultado.style.display = 'block';
      form.style.display = 'none';

    } catch (err) {
      error.textContent = err.message || 'Erro inesperado ao buscar empresa.';
    }
  });
});

function irParaSistema(email, nivel, funcEmail) {
  window.location.href = `index.html?emailEmpresa=${encodeURIComponent(email)}&nivel=${nivel}&funcEmail=${encodeURIComponent(funcEmail)}`;
}

function irParaParametrizacao(email, nivel) {
  window.location.href = `parametrizacao.html?emailEmpresa=${encodeURIComponent(email)}&nivel=${nivel}`;
}
