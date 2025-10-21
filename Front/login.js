const form = document.getElementById('loginForm');
const msg = document.getElementById('msg');
const error = document.getElementById('error');

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  msg.textContent = '';
  error.textContent = '';

  const email = form.email.value.trim();

  try {
    let response = await fetch(`http://localhost:8080/apis/funcionario/buscar?email=${encodeURIComponent(email)}`);

    if (!response.ok) {
      error.textContent = response.status === 404
        ? 'Funcionário não encontrado.'
        : 'Erro ao buscar funcionário.';
      return;
    }

    const funcionario = await response.json();
    console.log('Funcionario:', funcionario);

    // ✅ Salvando no sessionStorage
    sessionStorage.setItem('funcionarioId', funcionario.id);
    sessionStorage.setItem('funcionarioNome', funcionario.nome);

    msg.textContent = 'Login OK. Redirecionando para seleção da empresa...';
    setTimeout(() => {
      window.location.href = `empresa.html?funcEmail=${encodeURIComponent(funcionario.email)}&nivel=${funcionario.nivel}`;
    }, 1000);

  } catch (err) {
    error.textContent = 'Erro de conexão com o servidor.';
  }
});
