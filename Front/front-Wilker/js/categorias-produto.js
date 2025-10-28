document.addEventListener("DOMContentLoaded", () => {
  loadCategorias();
  document.getElementById("formCategoria").addEventListener("submit", salvarOuAtualizar);
});

let modoEdicao = false;

// ====== CARREGAR CATEGORIAS ======
async function loadCategorias() {
  const tabela = document.getElementById("tabelaCategoriasBody");
  tabela.innerHTML = "<tr><td colspan='3' class='text-center'>Carregando...</td></tr>";

  try {
    const categorias = await apiGet("/api/categorias");

    if (!categorias || categorias.length === 0) {
      tabela.innerHTML = "<tr><td colspan='3' class='text-center text-muted'>Nenhuma categoria cadastrada</td></tr>";
      return;
    }

    tabela.innerHTML = "";
    categorias.forEach(c => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${c.catCod}</td>
        <td>${c.catDescr}</td>
        <td class="text-center">
          <button class="btn btn-sm btn-primary me-1" onclick="editarCategoria(${c.catCod}, '${c.catDescr.replace(/'/g, "\\'")}')">Editar</button>
          <button class="btn btn-sm btn-danger" onclick="deletarCategoria(${c.catCod})">Excluir</button>
        </td>
      `;
      tabela.appendChild(tr);
    });
  } catch (err) {
    tabela.innerHTML = `<tr><td colspan="3" class="text-danger text-center">Erro: ${err.message}</td></tr>`;
  }
}

// ====== SALVAR OU ATUALIZAR ======
async function salvarOuAtualizar(ev) {
  ev.preventDefault();

  const descr = document.getElementById("categoriaDescricao").value.trim();
  const idEdicao = document.getElementById("categoriaIdEdicao").value;

  if (!descr) {
    alert("Descrição obrigatória!");
    return;
  }

  try {
    if (modoEdicao && idEdicao) {
      await apiPut(`/api/categorias/${idEdicao}`, { catDescr: descr });
      alert("Categoria atualizada com sucesso!");
    } else {
      await apiPost("/api/categorias", { catDescr: descr });
      alert("Categoria criada com sucesso!");
    }

    resetarFormulario();
    await loadCategorias(); // atualiza tabela
  } catch (err) {
    alert("Erro ao salvar categoria: " + err.message);
  }
}

// ====== EDITAR ======
function editarCategoria(id, descr) {
  document.getElementById("categoriaIdEdicao").value = id;
  document.getElementById("categoriaDescricao").value = descr;
  document.getElementById("btnSalvar").textContent = "Salvar Alteração";
  document.getElementById("btnSalvar").classList.remove("btn-success");
  document.getElementById("btnSalvar").classList.add("btn-warning");
  modoEdicao = true;
}

// ====== RESETAR ======
function resetarFormulario() {
  document.getElementById("categoriaIdEdicao").value = "";
  document.getElementById("categoriaDescricao").value = "";
  document.getElementById("btnSalvar").textContent = "Adicionar";
  document.getElementById("btnSalvar").classList.remove("btn-warning");
  document.getElementById("btnSalvar").classList.add("btn-success");
  modoEdicao = false;
}

// ====== DELETAR ======
async function deletarCategoria(id) {
  if (!confirm("Deseja realmente excluir esta categoria?")) return;

  try {
    await apiDelete(`/api/categorias/${id}`);
    alert("Categoria excluída com sucesso!");
    await loadCategorias(); // 🔄 ATUALIZA A TABELA AQUI!
  } catch (err) {
    alert("Erro ao excluir categoria: " + err.message);
  }
}

// ====== API BASE ======
const API_BASE = "http://localhost:8080";

// ====== API MÉTODOS ======
async function apiGet(url) {
  const res = await fetch(API_BASE + url);
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiPost(url, body) {
  const res = await fetch(API_BASE + url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiPut(url, body) {
  const res = await fetch(API_BASE + url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiDelete(url) {
  const res = await fetch(API_BASE + url, { method: "DELETE" });
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json(); 
}

