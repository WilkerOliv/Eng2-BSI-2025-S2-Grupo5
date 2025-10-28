// Registrar necessidade de produto para pessoa carente
document.addEventListener("DOMContentLoaded", () => {
  carregarProdutos();
  carregarPessoas();
  carregarNecessidades();
  document.getElementById("formNecessidade").addEventListener("submit", criarNecessidade);
});

async function carregarProdutos() {
  const sel = document.getElementById("selProduto");
  sel.innerHTML = "<option>Carregando...</option>";

  try {
    const produtos = await apiGet("/api/produtos");
    if (!produtos || produtos.length === 0) {
      sel.innerHTML = "<option>Nenhum produto</option>";
      return;
    }
    sel.innerHTML = produtos
      .map(p => `<option value="${p.prodCod}">${p.prodDescr}</option>`)
      .join("");
  } catch {
    sel.innerHTML = "<option>Erro ao carregar</option>";
  }
}

async function carregarPessoas() {
  const sel = document.getElementById("selPessoa");
  sel.innerHTML = "<option>Carregando...</option>";

  try {
    const pessoas = await apiGet("/api/pessoas");
    if (!pessoas || pessoas.length === 0) {
      sel.innerHTML = "<option>Nenhuma pessoa cadastrada</option>";
      return;
    }
    sel.innerHTML = pessoas
      .map(p => `<option value="${p.pcCpf}">${p.pcNome} (${p.pcCpf})</option>`)
      .join("");
  } catch {
    sel.innerHTML = "<option>Erro ao carregar</option>";
  }
}

async function carregarNecessidades() {
  const tbody = document.getElementById("tabelaNecessidadesBody");
  tbody.innerHTML = "<tr><td colspan='5' class='text-center'>Carregando...</td></tr>";

  try {
    const necessidades = await apiGet("/api/necessidades/produtos");
    if (!necessidades || necessidades.length === 0) {
      tbody.innerHTML = "<tr><td colspan='5' class='text-center'>Nenhuma necessidade cadastrada</td></tr>";
      return;
    }

    tbody.innerHTML = "";
    necessidades.forEach(n => {
      const cpf = n.pessoaCarente?.pcCpf;
      const produtoNome = n.produto?.prodDescr;
      const produtoId = n.produto?.prodCod;

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${cpf}</td>
        <td>${produtoNome || produtoId}</td>
        <td>${n.quantidade}</td>
        <td>${n.data || ""}</td>
        <td class="text-center">
          <button class="btn btn-sm btn-danger" onclick="deletarNecessidade('${cpf}', ${produtoId})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan='5' class='text-danger text-center'>Erro: ${err.message}</td></tr>`;
  }
}

async function criarNecessidade(ev) {
  ev.preventDefault();

  const pessoaCpf = document.getElementById("selPessoa").value;
  const produtoId = parseInt(document.getElementById("selProduto").value, 10);
  const quantidade = parseInt(document.getElementById("quantidadeNecessidade").value, 10);
  const data = document.getElementById("dataNecessidade").value;
  const observacao = document.getElementById("obsNecessidade").value || null;

  if (!pessoaCpf || !produtoId || !quantidade || !data) {
    alert("Preencha todos os campos obrigatórios!");
    return;
  }

  const payload = {
    pessoaCarente: { pcCpf: pessoaCpf },
    produto: { prodCod: produtoId },
    quantidade,
    data,
    observacao
  };

  try {
    await apiPost("/api/necessidades/produtos", payload);
    alert("Necessidade registrada com sucesso!");
    document.getElementById("formNecessidade").reset();
    await carregarNecessidades();
  } catch (err) {
    alert("Erro ao registrar necessidade: " + err.message);
  }
}

async function deletarNecessidade(cpf, produtoId) {
  if (!confirm("Deseja realmente excluir esta necessidade?")) return;

  const idObj = {
    pessoaCarentePcCpf: cpf,
    produtoProdCod: produtoId
  };

  try {
    await apiDelete("/api/necessidades/produtos", idObj);
    alert("Necessidade excluída com sucesso!");
    await carregarNecessidades();
  } catch (err) {
    alert("Erro ao excluir necessidade: " + err.message);
  }
}
