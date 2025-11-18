// ===============================
// Config
// ===============================
const API = {
  PRODUTOS: "http://localhost:8080/api/produtos/lista",
  FUNC_POR_CPF: (cpf) => "http://localhost:8080/api/funcionarios/buscaCPF?cpf=" + encodeURIComponent(cpf),
  DOACAO_CAB: "http://localhost:8080/api/doacao/inserir",
  DOACAO_ITENS: "http://localhost:8080/api/doacao_prod",
  DOACOES_LISTA: "http://localhost:8080/api/doacao/getListaDoacao"
};

// ===============================
// Estado da tela
// ===============================
let itensDaDoacao = [];
let indiceEditando = null;
let listaProdutosCache = [];
let linhaDetalhesAberta = null;

// ===============================
// Helpers
// ===============================
const el = (sel) => document.querySelector(sel);

function hojeISO() {
  const d = new Date();
  d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
  return d.toISOString().slice(0, 10);
}
function compareISO(a, b) { if (a === b) return 0; return a < b ? -1 : 1; }
function isPastDate(dateISO) { return compareISO(dateISO, hojeISO()) < 0; }
function isFutureDate(dateISO) { return compareISO(dateISO, hojeISO()) > 0; }

// Alerts
function msg(texto, tipo = "danger", opts = {}) {
  const tempoPadrao = { danger: 7000, warning: 6000, info: 5000, success: 3500 };
  const timeoutMs = Number.isFinite(opts.timeoutMs) ? opts.timeoutMs : (tempoPadrao[tipo] ?? 5000);
  const sticky = !!opts.sticky;
  const maxVisiveis = opts.max ?? 4;

  const box = el('#mensagens');
  if (!box) return;

  const existentes = Array.from(box.querySelectorAll('.alert'));
  if (existentes.length >= maxVisiveis) {
    const excedente = existentes.length - maxVisiveis + 1;
    existentes.slice(0, excedente).forEach(a => fecharAlert(a));
  }

  const div = document.createElement('div');
  div.className = `alert alert-${tipo} alert-dismissible fade show`;
  div.role = "alert";
  div.innerHTML = `${texto}<button class="btn-close" data-bs-dismiss="alert"></button>`;
  div.style.marginBottom = "0.5rem";
  box.appendChild(div);

  function fecharAlert(node = div) {
    if (!node || node.dataset.closing === "1") return;
    node.dataset.closing = "1";
    node.classList.remove('show');
    setTimeout(() => node.remove(), 200);
  }

  if (!sticky && timeoutMs > 0) {
    let restante = timeoutMs;
    let start = Date.now();
    let timer = setTimeout(() => fecharAlert(), restante);

    const pausa = () => { clearTimeout(timer); restante -= (Date.now() - start); };
    const retoma = () => { start = Date.now(); timer = setTimeout(() => fecharAlert(), Math.max(0, restante)); };

    div.addEventListener('mouseenter', pausa);
    div.addEventListener('mouseleave', retoma);
  }

  div.addEventListener('closed.bs.alert', () => div.remove());
  return div;
}

// CPF mask
function aplicarMascaraCPF(valorCru) {
  let d = valorCru.replace(/\D/g, '').slice(0, 11);
  if (d.length > 9) return d.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, "$1.$2.$3-$4");
  if (d.length > 6) return d.replace(/(\d{3})(\d{3})(\d{0,3})/, "$1.$2.$3");
  if (d.length > 3) return d.replace(/(\d{3})(\d{0,3})/, "$1.$2");
  return d;
}

// ===============================
// Carregamento de Produtos
// ===============================
async function carregarProdutos() {
  try {
    const resp = await fetch(API.PRODUTOS);
    if (!resp.ok) throw new Error("HTTP " + resp.status);

    listaProdutosCache = await resp.json();
    preencherSelect(listaProdutosCache);
  } catch (e) {
    console.error(e);
    el("#listaProdutos").innerHTML = `<option disabled>(erro ao carregar)</option>`;
  }
}

function preencherSelect(lista) {
  const select = el("#listaProdutos");
  select.innerHTML = `<option value="" disabled selected>Selecione...</option>`;
  lista.forEach(p => {
    const opt = document.createElement("option");
    opt.value = p.prodCod ?? p.prod_cod;
    opt.textContent = p.prodDescr ?? p.prod_descr ?? "Produto";
    opt.setAttribute("data-descr", opt.textContent);
    select.appendChild(opt);
  });
}

function filtrarProdutos() {
  const termo = el("#buscaProduto").value.trim().toLowerCase();
  if (!termo) return preencherSelect(listaProdutosCache);

  const filtrados = listaProdutosCache.filter(p =>
    (p.prodDescr ?? "").toLowerCase().includes(termo)
  );

  preencherSelect(filtrados);
  if (filtrados.length === 1) el("#listaProdutos").value = filtrados[0].prodCod;
}

// ===============================
// Listagem de Doações
// ===============================
async function carregarDoacoes() {
  try {
    const resp = await fetch(API.DOACOES_LISTA);
    if (!resp.ok) throw new Error("HTTP " + resp.status);

    const lista = await resp.json();
    renderizarDoacoes(lista);

  } catch (e) {
    console.error(e);
    msg("Erro ao carregar doações.", "danger");
  }
}

function renderizarDoacoes(lista) {
  const tbody = document.querySelector("#tabelaDoacoes tbody");
  tbody.innerHTML = "";

  if (!Array.isArray(lista) || lista.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">Nenhuma doação cadastrada.</td></tr>`;
    return;
  }

  lista.forEach(item => {
    const tr = document.createElement("tr");

    const dataFmt = item.dataDoacao?.split("-").reverse().join("/") || "-";

    tr.innerHTML = `
      <td>${item.doaCod}</td>
      <td>${dataFmt}</td>
      <td>${item.funcionarioCPF ?? "—"}</td>
      <td>${item.observacao ?? ""}</td>
      <td class="text-center">
        <button class="btn btn-sm btn-outline-primary" onclick="abrirDetalhesDoacao(${item.doaCod}, this)">
          <i class="bi bi-eye"></i> Ver
        </button>
      </td>
    `;

    tbody.appendChild(tr);
  });
}

// ===============================
// EXPANSÃO: Itens da doação
// ===============================
async function abrirDetalhesDoacao(doaCod, botao) {

  const linha = botao.closest("tr");
  const tabela = document.querySelector("#tabelaDoacoes tbody");

  if (linhaDetalhesAberta) {
    linhaDetalhesAberta.remove();
    linhaDetalhesAberta = null;
  }

  if (linha.nextSibling && linha.nextSibling.classList.contains("linha-detalhes")) {
    return;
  }

  try {
    const resp = await fetch(`http://localhost:8080/api/doacao/${doaCod}/itens`);
    if (!resp.ok) throw new Error("Erro HTTP " + resp.status);

    const itens = await resp.json();

    if (!Array.isArray(itens) || itens.length === 0) {
      msg("Nenhum item vinculado a esta doação.", "warning");
      return;
    }

    const trDetalhes = document.createElement("tr");
    trDetalhes.classList.add("linha-detalhes");

    let htmlTabela = `
      <td colspan="5">
        <div class="p-3" style="background:#f7f7f7; border-radius:6px;">
          <h6 class="mb-3"><i class="bi bi-box"></i> Itens da Doação ${doaCod}</h6>
          <table class="table table-sm">
            <thead>
              <tr>
                <th>Produto</th>
                <th class="text-end">Quantidade</th>
              </tr>
            </thead>
            <tbody>
    `;

    itens.forEach(it => {
      htmlTabela += `
        <tr>
          <td>${it.produto}</td>
          <td class="text-end">${it.quantidade}</td>
        </tr>
      `;
    });

    htmlTabela += `
            </tbody>
          </table>
          <button class="btn btn-secondary btn-sm" onclick="fecharDetalhes()">
            <i class="bi bi-chevron-up"></i> Fechar
          </button>
        </div>
      </td>
    `;

    trDetalhes.innerHTML = htmlTabela;

    tabela.insertBefore(trDetalhes, linha.nextSibling);
    linhaDetalhesAberta = trDetalhes;
    msg("Itens carregados!", "success", { timeoutMs: 1500 });

  } catch (e) {
    console.error(e);
    msg("Erro ao carregar itens da doação.", "danger");
  }
}

function fecharDetalhes() {
  if (linhaDetalhesAberta) {
    linhaDetalhesAberta.remove();
    linhaDetalhesAberta = null;
  }
}

// ===============================
// Mescla / Upsert (NOVO)
// ===============================
function chaveItem(it) {
  return `${String(it.prodCod)}|${it.validade}`;
}

function upsertItem(novo) {
  const key = chaveItem(novo);
  const idx = itensDaDoacao.findIndex(x => chaveItem(x) === key);

  if (idx === -1) {
    itensDaDoacao.push({ ...novo });
  } else {
    itensDaDoacao[idx].qtd += novo.qtd;
  }
}

function mesclarDuplicados() {
  const mapa = new Map();
  for (const it of itensDaDoacao) {
    const key = chaveItem(it);
    if (!mapa.has(key)) {
      mapa.set(key, { ...it });
    } else {
      mapa.set(key, {
        ...mapa.get(key),
        qtd: mapa.get(key).qtd + it.qtd
      });
    }
  }
  itensDaDoacao = Array.from(mapa.values());
}

// ===============================
// Buscar Funcionário
// ===============================
async function buscarFuncionario() {
  const campoCPF = el("#funcionarioCpf");
  const campoNome = el("#funcionarioNome");

  const cpfValor = campoCPF.value;
  try {
    const resp = await fetch(API.FUNC_POR_CPF(cpfValor));
    if (!resp.ok) throw new Error();

    const func = await resp.json();
    campoNome.value = func.funcNome || func.nome || "";
    msg("Funcionário encontrado!", "success");

  } catch {
    campoNome.value = "";
    msg("Funcionário não encontrado.", "danger");
  }
}

// ===============================
// Tabela de Itens
// ===============================
function redesenharTabela() {
  const tbody = el('#tabelaItens tbody');
  tbody.innerHTML = "";

  itensDaDoacao.forEach((item, i) => {
    const emEdicao = indiceEditando === i;

    if (!emEdicao) {
      tbody.innerHTML += `
        <tr>
          <td>${item.descrProduto}</td>
          <td class="text-end">${item.qtd}</td>
          <td>${item.validade}</td>
          <td class="text-center">
            <button class="btn btn-sm btn-outline-primary" data-acao="editar" data-idx="${i}">Editar</button>
            <button class="btn btn-sm btn-outline-danger" data-acao="remover" data-idx="${i}">Excluir</button>
          </td>
        </tr>`;
    } else {
      tbody.innerHTML += `
        <tr class="table-warning">
          <td>${item.descrProduto}</td>
          <td><input id="edit-qtd-${i}" type="number" min="1" class="form-control form-control-sm" value="${item.qtd}"></td>
          <td><input id="edit-validade-${i}" type="date" class="form-control form-control-sm" value="${item.validade}"></td>
          <td class="text-center">
            <button class="btn btn-sm btn-success" data-acao="salvar-edicao" data-idx="${i}">Salvar</button>
            <button class="btn btn-sm btn-secondary" data-acao="cancelar-edicao">Cancelar</button>
          </td>
        </tr>`;
    }
  });
}

// ===============================
// CRUD de Itens
// ===============================
function adicionarItem() {
  const selProd = el("#listaProdutos");
  const qtdEl = el("#quantidade");
  const valEl = el("#validade");

  const prodCod = selProd.value;
  const qtd = Number(qtdEl.value);
  const validade = valEl.value;
  const descr = selProd.options[selProd.selectedIndex]?.getAttribute("data-descr");

  if (!prodCod || !qtd || !validade) return msg("Preencha produto, quantidade e validade.", "danger");
  if (isPastDate(validade)) return msg("Validade inválida.", "danger");

  // *** ALTERAÇÃO AQUI ***
  upsertItem({ prodCod, descrProduto: descr, qtd, validade });
  mesclarDuplicados();

  indiceEditando = null;
  redesenharTabela();

  selProd.value = "";
  qtdEl.value = "";
  valEl.value = "";
}

function entrarEdicao(i) { indiceEditando = i; redesenharTabela(); }
function cancelarEdicao() { indiceEditando = null; redesenharTabela(); }

function salvarEdicao(i) {
  const qtd = Number(el(`#edit-qtd-${i}`).value);
  const validade = el(`#edit-validade-${i}`).value;

  if (!qtd || qtd <= 0) return msg("Quantidade inválida.", "danger");
  if (!validade) return msg("Informe validade.", "danger");

  itensDaDoacao[i].qtd = qtd;
  itensDaDoacao[i].validade = validade;

  // *** ALTERAÇÃO AQUI ***
  mesclarDuplicados();

  indiceEditando = null;
  redesenharTabela();
}

function removerItem(i) {
  itensDaDoacao.splice(i, 1);
  indiceEditando = null;
  redesenharTabela();
}

// ===============================
// Limpar Formulário
// ===============================
function limparTudo() {
  itensDaDoacao = [];
  indiceEditando = null;

  el("#dataDoacao").value = hojeISO();
  el("#observacao").value = "";

  redesenharTabela();

  msg("Formulário limpo!", "info");
}

// ===============================
// Validação
// ===============================
function validarFormulario() {
  if (itensDaDoacao.length === 0) return msg("Adicione ao menos 1 item.", "danger");

  const data = el("#dataDoacao").value;
  const cpf = el("#funcionarioCpf").value;

  if (!data) return msg("Informe a data.", "danger");
  if (isFutureDate(data)) return msg("Data inválida.", "danger");
  if (!cpf) return msg("Informe o CPF.", "danger");

  return true;
}

// ===============================
// Envio do Cabeçalho
// ===============================
function montarCabecalho() {
  return {
    dataDoacao: el("#dataDoacao").value,
    observacao: el("#observacao").value.trim()
  };
}

function montarPayloadItensDTO(doaCod) {
  return {
    estoques: itensDaDoacao.map(it => ({
      produtoProdCod: Number(it.prodCod),
      estProdQuantidade: Number(it.qtd),
      dataValidade: it.validade
    })),
    doacaoProduto: { doacaoDoaCod: Number(doaCod) }
  };
}

async function salvarCabecalho(cabecalho) {
  try {
    const resp = await fetch(API.DOACAO_CAB, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(cabecalho)
    });

    if (!resp.ok) throw new Error("Falha ao criar cabeçalho");

    const json = await resp.json();
    return json.doaCod ?? json;

  } catch (err) {
    console.error(err);
    msg("Erro ao salvar cabeçalho.", "danger");
    return null;
  }
}

async function salvarItensDaDoacao(doaCod) {
  try {
    const payload = montarPayloadItensDTO(doaCod);

    const resp = await fetch(API.DOACAO_ITENS, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!resp.ok) throw new Error("Erro ao salvar itens");

    return true;
  } catch (err) {
    console.error(err);
    msg("Erro ao salvar itens da doação.", "danger");
    return false;
  }
}

async function salvarDoacao() {
  if (!validarFormulario()) return;

  const cab = montarCabecalho();
  const doaCod = await salvarCabecalho(cab);

  if (!doaCod) return;

  const ok = await salvarItensDaDoacao(doaCod);
  if (!ok) return;

  msg("Doação registrada com sucesso!", "success");
  limparTudo();
  carregarDoacoes();
}

// ===============================
// Eventos
// ===============================
document.addEventListener("DOMContentLoaded", () => {

  el("#dataDoacao").value = hojeISO();

  carregarProdutos();
  carregarDoacoes();

  el("#buscaProduto")?.addEventListener("input", filtrarProdutos);

  el("#funcionarioCpf")?.addEventListener("input", e => {
    e.target.value = aplicarMascaraCPF(e.target.value);
  });

  el("#btnBuscarFuncionario")?.addEventListener("click", buscarFuncionario);

  el("#btnAddItem")?.addEventListener("click", adicionarItem);
  el("#btnSalvar")?.addEventListener("click", salvarDoacao);
  el("#btnLimparTudo")?.addEventListener("click", limparTudo);

  el('#tabelaItens tbody')?.addEventListener("click", e => {
    const btn = e.target.closest("button");
    if (!btn) return;

    const acao = btn.dataset.acao;
    const idx = Number(btn.dataset.idx);

    if (acao === "remover") removerItem(idx);
    if (acao === "editar") entrarEdicao(idx);
    if (acao === "salvar-edicao") salvarEdicao(idx);
    if (acao === "cancelar-edicao") cancelarEdicao();
  });
});
