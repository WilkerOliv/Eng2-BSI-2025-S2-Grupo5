
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  if (!form) {
    console.error('Form "#loginForm" não encontrado no DOM.');
    return;
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email")?.value ?? "";
    const senha = document.getElementById("senha")?.value ?? "";

    const msg = document.getElementById("msg");
    const err = document.getElementById("error");
    if (msg) msg.textContent = "";
    if (err) err.textContent = "";

    try {
      const r = await fetch("http://localhost:8080/api/funcionarios/login", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ email, senha }).toString()
      });

      if (r.ok) {
        const func = await r.json();
        if (msg) msg.textContent = `Bem-vindo, ${func.funcNome || func.nome || "usuário"}!`;
        window.location.href = "empresa.html";
      } else if (r.status === 401) {
        if (err) err.textContent = "Senha incorreta.";
      } else if (r.status === 404) {
        if (err) err.textContent = "Funcionário não encontrado.";
      } else {
        if (err) err.textContent = "Erro no login.";
      }
    } catch (e2) {
      if (err) err.textContent = "Erro de conexão com o servidor.";
      console.error(e2);
    }
  });
});

