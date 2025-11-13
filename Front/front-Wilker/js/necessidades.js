const API_NEC = "http://localhost:8080/api/necessidades/produtos";
const API_PC = "http://localhost:8080/api/pessoas-carentes";
const API_CATEG = "http://localhost:8080/api/categorias";
const API_PROD = "http://localhost:8080/api/produtos";

const alertBox = document.getElementById("alert-container");

// AUTOCOMPLETE PESSOA
const pcSearch = document.getElementById("pcSearch");
const pcAutocomplete = document.getElementById("pcAutocomplete");
const pcCpf = document.getElementById("pcCpf");

pcSearch.addEventListener("input", async () => {
  const termo = pcSearch.value.trim();
  if (termo.length < 2) {
    pcAutocomplete.innerHTML = "";
    return;
  }

  try {
    const res = await fetch(API_PC + "/busca?termo=" + encodeURIComponent(termo));
    if (!res.ok) return;
    const lista = await res.json();

    pcAutocomplete.innerHTML = "";
    lista.forEach(p => {
      const item = document.createElement("div");
      item.className = "autocomplete-item";
      item.innerText = `${p.pcCpf} - ${p.pcNome}`;
      item.onclick = () => {
        pcSearch.value = p.pcNome;
        pcCpf.value = p.pcCpf; // travado
        pcAutocomplete.innerHTML = "";
      };
      pcAutocomplete.appendChild(item);
    });
  } catch {
    // ignora
  }
});

// CATEGORIAS E PRODUTOS
const categoria = document.getElementById("categoria");
const produto = document.getElementById("produto");

async function carregarCategorias() {
  try {
    const [resCat, resProd] = await Promise.all([
      fetch(API_CATEG),
      fetch(API_PROD)
    ]);
    if (!resCat.ok || !resProd.ok) throw new Error("Erro ao carregar categorias/produtos.");

    const categorias = await resCat.json();
    const produtos = await resProd.json();

    const catComProduto = new Set(produtos.map(p => p.categoriaProdCod));

    categoria.innerHTML = "";
    categorias
      .filter(c => catComProduto.has(c.catCod))
      .forEach(c => {
        categoria.innerHTML += `<option value="${c.catCod}">${c.catDescr}</option>`;
      });

    carregarProdutos();
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

categoria.onchange = carregarProdutos;

async function carregarProdutos() {
  const catId = categoria.value;
  if (!catId) {
    produto.innerHTML = "";
    return;
  }

  try {
    const res = await fetch(API_PROD + "?categoria=" + catId);
    if (!res.ok) throw new Error("Erro ao carregar produtos.");
    const lista = await res.json();

    produto.innerHTML = "";
    lista.forEach(p => {
      produto.innerHTML += `<option value="${p.prodCod}">${p.prodDescr}</option>`;
    });
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// TABELA DE NECESSIDADES
const tabelaNec = document.querySelector("#tabelaNec tbody");

async function carregarNecessidades() {
  try {
    const res = await fetch(API_NEC);
    if (!res.ok) throw new Error("Erro ao carregar necessidades.");
    const lista = await res.json();

    montarTabelaNec(lista);
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

function montarTabelaNec(lista) {
  tabelaNec.innerHTML = "";
  lista.forEach(n => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${n.pessoaCarentePcCpf}</td>
      <td>${n.pessoaNome}</td>
      <td>${n.produtoDescr}</td>
      <td>${n.quantidade}</td>
      <td>${n.data}</td>
      <td>${n.observacao || ""}</td>
      <td>
        <button class="btn-visualizar" data-view="${n.pessoaCarentePcCpf}-${n.produtoProdCod}" title="Visualizar">&#128065;</button>
        <button class="btn btn-danger btn-sm" data-del="${n.pessoaCarentePcCpf}-${n.produtoProdCod}">Excluir</button>
      </td>
    `;
    tabelaNec.appendChild(tr);
  });

  tabelaNec.querySelectorAll("[data-del]").forEach(btn => {
    btn.onclick = () => excluirNec(btn.dataset.del);
  });

  tabelaNec.querySelectorAll("[data-view]").forEach(btn => {
    btn.onclick = () => visualizarNec(btn.dataset.view);
  });
}

async function excluirNec(key) {
  const [cpf, prod] = key.split("-");
  if (!confirm("Excluir necessidade?")) return;

  try {
    const body = { pessoaCarentePcCpf: cpf, produtoProdCod: Number(prod) };
    const res = await fetch(API_NEC, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (!res.ok) throw new Error("Erro ao excluir necessidade.");
    Utils.showAlert(alertBox, "success", "Necessidade excluída.");
    carregarNecessidades();
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// SALVAR NECESSIDADE
const dataInput = document.getElementById("data");
const hojeStr = new Date().toISOString().split("T")[0];
dataInput.value = hojeStr;

document.getElementById("formNec").addEventListener("submit", async (e) => {
  e.preventDefault();

  const obj = {
    pessoaCarentePcCpf: pcCpf.value,
    produtoProdCod: Number(produto.value),
    quantidade: Number(document.getElementById("quantidade").value),
    data: dataInput.value,
    observacao: document.getElementById("observacao").value
  };

  try {
    if (!obj.pessoaCarentePcCpf) throw new Error("Selecione uma pessoa carente.");
    if (!obj.produtoProdCod) throw new Error("Selecione um produto.");
    if (!obj.quantidade || obj.quantidade <= 0) throw new Error("Informe uma quantidade válida.");

    const res = await fetch(API_NEC, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(obj)
    });

    if (!res.ok) {
      let msg = "Erro ao registrar necessidade.";
      try {
        const erro = await res.json();
        if (erro.mensagem) msg = erro.mensagem;
      } catch {}
      throw new Error(msg);
    }

    Utils.showAlert(alertBox, "success", "Necessidade registrada com sucesso.");
    limparForm();
    carregarNecessidades();

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
});

// BUSCA
document.getElementById("btnBuscar").onclick = async () => {
  const termo = document.getElementById("buscar").value.trim();
  if (termo === "") {
    carregarNecessidades();
    return;
  }

  try {
    const res = await fetch(API_NEC + "?termo=" + encodeURIComponent(termo));
    if (!res.ok) throw new Error("Erro na busca.");
    const lista = await res.json();
    montarTabelaNec(lista);
  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
};

document.getElementById("btnResetBusca").onclick = () => {
  document.getElementById("buscar").value = "";
  carregarNecessidades();
};

// LIMPAR FORM
function limparForm() {
  pcSearch.value = "";
  pcCpf.value = "";
  document.getElementById("quantidade").value = "";
  document.getElementById("observacao").value = "";
  dataInput.value = hojeStr; // sempre hoje
}

document.getElementById("btnLimpar").onclick = limparForm;

// VISUALIZAR NECESSIDADE
async function visualizarNec(key) {
  const [cpf, prod] = key.split("-");

  try {
    const res = await fetch(API_NEC + "?cpf=" + encodeURIComponent(cpf));
    if (!res.ok) throw new Error("Erro ao buscar necessidade.");
    const lista = await res.json();

    const item = lista.find(n => String(n.produtoProdCod) === String(prod));
    if (!item) return;

    document.getElementById("vnPessoa").innerText = `${item.pessoaNome} (${item.pessoaCarentePcCpf})`;
    document.getElementById("vnProd").innerText = item.produtoDescr;
    document.getElementById("vnQtd").innerText = item.quantidade;
    document.getElementById("vnData").innerText = item.data;
    document.getElementById("vnObs").innerText = item.observacao || "—";

    const modal = new bootstrap.Modal(document.getElementById("modalViewNec"));
    modal.show();

  } catch (err) {
    Utils.showAlert(alertBox, "danger", err.message);
  }
}

// INIT
carregarCategorias();
carregarNecessidades();
limparForm();
