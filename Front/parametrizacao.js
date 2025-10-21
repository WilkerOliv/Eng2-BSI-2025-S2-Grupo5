document.addEventListener('DOMContentLoaded', async () => {
  const form = document.getElementById('paramForm');
  const msg = document.getElementById('msg');
  const error = document.getElementById('error');
  const submitBtn = document.getElementById('submitBtn');

  const urlParams = new URLSearchParams(window.location.search);
  const emailEmpresa = urlParams.get('emailEmpresa');
  const nivel = urlParams.get('nivel'); // '1' pode editar

  if (emailEmpresa) {
    form.email.value = emailEmpresa;
  }

  if (emailEmpresa) {
    try {
      const response = await fetch(`http://localhost:8080/salf/param?email=${encodeURIComponent(emailEmpresa)}`);
      if (response.ok) {
        const empresa = await response.json();
        form.nomeEmpresa.value = empresa.nomeEmpresa || '';
        form.cnpj.value = empresa.cnpj || '';
        form.endereco.value = empresa.endereco || '';
        form.telefone.value = empresa.telefone || '';
        form.email.value = empresa.email || '';
      }
    } catch (e) {
      console.error('Erro ao buscar dados da empresa:', e);
    }
  }

  IMask(form.cnpj, {
    mask: '00.000.000/0000-00'
  });

  IMask(form.telefone, {
    mask: '(00) 00000-0000'
  });

  IMask(form.email, {
    mask: /^\S*@?\S*$/
  });

  if (nivel !== '1') {
    Array.from(form.elements).forEach(el => el.disabled = true);
    submitBtn.style.display = 'none';
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    msg.textContent = '';
    error.textContent = '';

    let isValid = true;
    let firstInvalid = null;

    Array.from(form.elements).forEach(el => el.classList.remove('error'));

    Array.from(form.elements).forEach(el => {
      if (el.dataset.validation === 'required' && !el.value.trim()) {
        el.classList.add('error');
        if (!firstInvalid) firstInvalid = el;
        isValid = false;
      }
    });

    // Validação para o arquivo de logotipo
    const logotipoFile = form.logotipo.files[0];
    if (!logotipoFile) {
      error.textContent = 'Por favor, selecione o logotipo da empresa.';
      form.logotipo.classList.add('error');
      if (!firstInvalid) firstInvalid = form.logotipo;
      isValid = false;
    } else {
      form.logotipo.classList.remove('error');
    }

    if (!isValid) {
      error.textContent = error.textContent || 'Preencha todos os campos obrigatórios.';
      firstInvalid.focus();
      return;
    }

    // Montar FormData para enviar o arquivo e os outros dados
    const formData = new FormData();
    formData.append('nomeEmpresa', form.nomeEmpresa.value.trim());
    formData.append('cnpj', form.cnpj.value.trim());
    formData.append('endereco', form.endereco.value.trim());
    formData.append('telefone', form.telefone.value.trim());
    formData.append('email', form.email.value.trim());
    formData.append('logotipo', logotipoFile);

    try {
      const response = await fetch('http://localhost:8080/salf/param', {
        method: 'POST',
        body: formData  // Sem headers, deixa o navegador definir
      });

      if (!response.ok) {
        throw new Error('Erro ao salvar dados da empresa.');
      }

      msg.textContent = 'Empresa cadastrada/atualizada com sucesso!';
      setTimeout(() => {
        window.location.href = `empresa.html?funcEmail=${encodeURIComponent(form.email.value.trim())}&nivel=${nivel}`;
      }, 1500);

    } catch (err) {
      error.textContent = err.message || 'Erro ao salvar dados.';
    }
  });

});
