document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value.trim();

    const msg = document.getElementById("msg");
    const err = document.getElementById("error");

    msg.textContent = "";
    err.textContent = "";

    try {
      const resp = await fetch("http://localhost:8080/apis/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, senha })
      });

      const json = await resp.json();

      if (json.sucesso) {
        msg.textContent = json.mensagem;

        localStorage.setItem("funcionarioCpf", json.func.cpf);
        localStorage.setItem("funcionarioNome", json.func.nome);
        localStorage.setItem("tipoAcesso", json.func.tipoAcesso);

        setTimeout(() => {
          window.location.href = "parametrizacao.html";
        }, 500);

      } else {
        err.textContent = json.mensagem;
      }
    } catch (e2) {
      console.error(e2);
      err.textContent = "Falha ao conectar ao servidor.";
    }
  });
});
