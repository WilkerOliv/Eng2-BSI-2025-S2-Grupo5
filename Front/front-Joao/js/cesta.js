const API_CESTA = "http://localhost:8080/apis/cesta";
const API_PRODUTOS = "http://localhost:8080/api/produtos";

let produtosCache = [];
let itensCesta = [];
let cestasCache = [];
let cestaEditando = null;

async function carregarProdutos() {
  try {
    const resp = await fetch(API_PRODUTOS);
    produtosCache = await resp.json();
  } catch (e) {
    console.error("Erro ao carregar produtos:", e);
  }
}
carregarProdutos();

document.getElementById("buscarProduto").addEventListener("input", function () {
  const texto = this.value.trim().toLowerCase();
  const lista = document.getElementById("listaBusca");

  if (texto.length < 2) {
    lista.style.display = "none";
    lista.innerHTML = "";
    return;
  }

  const filtrados = produtosCache.filter(p =>
      p.prodDescr.toLowerCase().includes(texto)
  );

  lista.innerHTML = "";
  filtrados.forEach(prod => {
    const item = document.createElement("button");
    item.type = "button";
    item.classList.add("list-group-item", "list-group-item-action");
    item.textContent = prod.prodDescr;
    item.onclick = () => selecionarProduto(prod);
    lista.appendChild(item);
  });

  lista.style.display = "block";
});

function selecionarProduto(prod) {
  const campo = document.getElementById("buscarProduto");
  campo.value = prod.prodDescr;
  campo.dataset.produtoId = prod.prodCod;
  document.getElementById("listaBusca").style.display = "none";
}

function adicionarProduto() {
  const nome = document.getElementById("buscarProduto");
  const quantidade = parseInt(document.getElementById("quantidadeProduto").value, 10);

  const prodCod = nome.dataset.produtoId;

  if (!prodCod) {
    nome.classList.add("is-invalid");
    return;
  } else {
    nome.classList.remove("is-invalid");
  }

  if (!quantidade || quantidade <= 0) {
    document.getElementById("quantidadeProduto").classList.add("is-invalid");
    return;
  } else {
    document.getElementById("quantidadeProduto").classList.remove("is-invalid");
  }

  const existente = itensCesta.find(i => i.produtoProdCod === Number(prodCod));

  if (existente) {
    existente.quantidade += quantidade;
  } else {
    itensCesta.push({
      produtoProdCod: Number(prodCod),
      quantidade: quantidade
    });
  }

  atualizarTabelaItens();

  nome.value = "";
  nome.dataset.produtoId = "";
  document.getElementById("quantidadeProduto").value = "";
}

function atualizarTabelaItens() {
  const tabela = document.querySelector("#tabelaItens tbody");
  tabela.innerHTML = "";

  itensCesta.forEach((item, index) => {
    const prod = produtosCache.find(p => p.prodCod === item.produtoProdCod);

    tabela.innerHTML += `
      <tr>
        <td>${prod ? prod.prodDescr : "?"}</td>
        <td>${item.quantidade}</td>
        <td>
          <button class="btn btn-warning btn-sm" onclick="alterarItem(${index})">Alterar</button>
          <button class="btn btn-danger btn-sm" onclick="removerItem(${index})">Remover</button>
        </td>
      </tr>
    `;
  });
}

function alterarItem(index) {
  const item = itensCesta[index];
  const prodAtual = produtosCache.find(p => p.prodCod === item.produtoProdCod);

  const novoNome = prompt("Novo nome:", prodAtual ? prodAtual.prodDescr : "");
  if (!novoNome) return;

  const novaQtd = parseInt(prompt("Nova quantidade:", item.quantidade), 10);
  if (!novaQtd || novaQtd <= 0) {
    alert("Quantidade inválida.");
    return;
  }

  const produtoEncontrado = produtosCache.find(p =>
      p.prodDescr.toLowerCase() === novoNome.toLowerCase()
  );

  if (!produtoEncontrado) {
    alert("Produto não encontrado! Digite exatamente como está cadastrado.");
    return;
  }

  item.produtoProdCod = produtoEncontrado.prodCod;
  item.quantidade = novaQtd;

  atualizarTabelaItens();
}

function removerItem(i) {
  itensCesta.splice(i, 1);
  atualizarTabelaItens();
}

function limparFormularioCesta() {
  document.getElementById("descricaoCesta").value = "";
  document.getElementById("buscarProduto").value = "";
  document.getElementById("buscarProduto").dataset.produtoId = "";
  document.getElementById("quantidadeProduto").value = "";
  itensCesta = [];
  cestaEditando = null;
  atualizarTabelaItens();
}

async function registrarCesta() {
  const descInput = document.getElementById("descricaoCesta");
  const desc = descInput.value.trim();

  if (desc === "") {
    descInput.classList.add("is-invalid");
    return;
  } else {
    descInput.classList.remove("is-invalid");
  }

  if (itensCesta.length === 0) {
    alert("A cesta deve conter ao menos 1 item.");
    return;
  }

  try {
    if (cestaEditando === null) {
      // CRIAÇÃO
      const body = {
        descricao: desc,
        itens: itensCesta
      };

      const resp = await fetch(`${API_CESTA}/registrar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      });

      const data = await resp.json();
      alert(data.mensagem || "Cesta registrada!");

    } else {

      await fetch(`${API_CESTA}/alterar/${cestaEditando}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ descricao: desc })
      });

      await fetch(`${API_CESTA}/itens/${cestaEditando}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ itens: itensCesta })
      });

      alert("Cesta atualizada com sucesso!");
    }

    limparFormularioCesta();
    listarCestas();

  } catch (e) {
    console.error("Erro ao registrar/atualizar cesta:", e);
    alert("Erro ao registrar/atualizar cesta.");
  }
}

async function listarCestas() {
  try {
    const resp = await fetch(`${API_CESTA}/listar`);
    cestasCache = await resp.json();
    renderTabelaCestas(cestasCache);
  } catch (e) {
    console.error("Erro ao listar cestas:", e);
  }
}

function renderTabelaCestas(lista) {
  const tabela = document.querySelector("#tabelaCestas tbody");
  tabela.innerHTML = "";

  lista.forEach(c => {
    tabela.innerHTML += `
      <tr>
        <td>${c.cb_cod}</td>
        <td>${c.descricao}</td>
        <td>${c.total_itens}</td>
        <td>
          <button class="btn btn-info btn-sm" onclick="visualizarCesta(${c.cb_cod})">Visualizar</button>
          <button class="btn btn-warning btn-sm" onclick="alterarCesta(${c.cb_cod})">Alterar</button>
          <button class="btn btn-danger btn-sm" onclick="excluirCesta(${c.cb_cod})">Excluir</button>
        </td>
      </tr>
    `;
  });
}

listarCestas();

function buscarCestas() {
  const termo = document.getElementById("buscarCesta").value.trim().toLowerCase();

  if (termo === "") {
    renderTabelaCestas(cestasCache);
    return;
  }

  const filtradas = cestasCache.filter(c =>
      c.descricao.toLowerCase().includes(termo)
  );

  renderTabelaCestas(filtradas);
}

async function alterarCesta(id) {
  try {
    // pega cesta da lista em memória
    const cesta = cestasCache.find(c => c.cb_cod === id);
    if (!cesta) {
      alert("Cesta não encontrada!");
      return;
    }

    document.getElementById("descricaoCesta").value = cesta.descricao;

    const respItens = await fetch(`${API_CESTA}/itens/${id}`);
    const itens = await respItens.json();

    itensCesta = itens.map(i => ({
      produtoProdCod: i.produto_cod,
      quantidade: i.quantidade
    }));

    cestaEditando = id;
    atualizarTabelaItens();

    window.scrollTo({ top: 0, behavior: "smooth" });

  } catch (e) {
    console.error("Erro ao carregar cesta para alteração:", e);
    alert("Erro ao carregar cesta.");
  }
}

async function visualizarCesta(id) {
  try {
    const resp = await fetch(`${API_CESTA}/listar`);
    const todas = await resp.json();

    const cesta = todas.find(c => c.cb_cod === id);
    if (!cesta) {
      alert("Cesta não encontrada!");
      return;
    }

    document.getElementById("visuDescricao").textContent = cesta.descricao;

    const respItens = await fetch(`${API_CESTA}/itens/${id}`);
    const itens = await respItens.json();

    const tabela = document.getElementById("visuTabelaItens");
    tabela.innerHTML = "";

    itens.forEach(i => {
      tabela.innerHTML += `
        <tr>
          <td>${i.produto}</td>
          <td>${i.quantidade}</td>
        </tr>
      `;
    });

    const modal = new bootstrap.Modal(document.getElementById("modalVisualizar"));
    modal.show();

  } catch (e) {
    console.error("Erro ao visualizar cesta:", e);
    alert("Erro ao carregar detalhes da cesta.");
  }
}

async function excluirCesta(id) {
  if (!confirm("Deseja realmente excluir?")) return;

  try {
    await fetch(`${API_CESTA}/excluir/${id}`, {
      method: "DELETE"
    });
    listarCestas();
  } catch (e) {
    console.error("Erro ao excluir cesta:", e);
    alert("Erro ao excluir cesta.");
  }
}
