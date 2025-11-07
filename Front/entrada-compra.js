// =====================================================
// ESTADO GLOBAL (memória da tela)
// =====================================================
let itensDaCompra = [];
let indiceEditando = null;

let cacheCotacoes = null;
let cacheFornecedoresDiretos = null;
const cacheFornecPorCotacao = new Map(); // cotacaoId (String) -> Array<Fornecedor>

// =====================================================
// Helpers
// =====================================================
const el = (sel) => document.querySelector(sel);

function msg(texto, tipo) {
  const box = el('#mensagens');
  if (!box) return;
  const div = document.createElement('div');
  div.className = `alert alert-${tipo} alert-dismissible fade show`;
  div.role = 'alert';
  div.innerHTML = `
    ${texto}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
  `;
  box.appendChild(div);
}

function fmt(valor) {
  if (isNaN(valor)) valor = 0;
  return valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// Máscara CPF (000.000.000-00)
function aplicarMascaraCPF(valorCru) {
  let d = valorCru.replace(/\D/g, '').slice(0, 11);
  if (d.length > 9) return d.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, "$1.$2.$3-$4");
  if (d.length > 6) return d.replace(/(\d{3})(\d{3})(\d{0,3})/, "$1.$2.$3");
  if (d.length > 3) return d.replace(/(\d{3})(\d{0,3})/, "$1.$2");
  return d;
}

// =====================================================
// Carregamentos (produtos, cotações, fornecedores)
// =====================================================
async function carregarProdutos() {
  try {
    const resp = await fetch("http://localhost:8080/api/produtos/lista");
    if (!resp.ok) throw new Error("HTTP " + resp.status);
    const lista = await resp.json();
    const select = el("#listaProdutos");
    if (!select) return;
    select.innerHTML = `<option value="" selected disabled>Selecione...</option>`;
    lista.forEach(p => {
      const opt = document.createElement("option");
      opt.value = p.prodCod;
      opt.textContent = p.prodDescr;
      opt.setAttribute("data-descr", p.prodDescr);
      select.appendChild(opt);
    });
  } catch (erro) {
    console.error("Falha ao buscar produtos:", erro);
    const select = el("#listaProdutos");
    if (select) select.innerHTML = `<option value="" selected disabled>(falha ao carregar)</option>`;
  }
}

async function carregarCotacoes() {
  if (cacheCotacoes) {
    popularSelectCotacoes(cacheCotacoes);
    return;
  }
  try {
    const resp = await fetch("http://localhost:8080/api/cotacao/lista");
    const select = el("#cotacaoId");
    if (!select) return;
    if (resp.ok) {
      const lista = await resp.json();
      cacheCotacoes = lista;
      popularSelectCotacoes(lista);
    } else {
      select.innerHTML = `<option value="" selected disabled>(falha ao carregar)</option>`;
    }
  } catch (erro) {
    console.error("Falha ao buscar cotações:", erro);
    const select = el("#cotacaoId");
    if (select) select.innerHTML = `<option value="" selected disabled>(falha ao carregar)</option>`;
  }
}

function popularSelectCotacoes(lista) {
  const select = el("#cotacaoId");
  if (!select) return;
  select.innerHTML = `<option value="" selected disabled>Selecione...</option>`;
  lista.forEach(cot => {
    const idCot = cot.cotacaoId ?? cot.id ?? cot.id_cotacao ?? cot.idCotacao ?? cot.codigo;
    if (idCot == null) {
      console.warn("Cotação sem id esperado:", cot);
      return;
    }
    const opt = document.createElement("option");
    opt.value = String(idCot);
    const abertura = cot.dataAbertura ?? cot.data_abertura ?? "";
    opt.textContent = `COTAÇÃO ${opt.value}${abertura ? " - " + abertura : ""}`;
    select.appendChild(opt);
  });
}

async function carregarFornecedoresPorCotacao() {
  const selectFor = el("#fornecedorCotacao");
  if (selectFor) {
    selectFor.innerHTML = `<option selected disabled>carregando...</option>`;
    selectFor.disabled = true;
  }
  try {
    const resp = await fetch("http://localhost:8080/api/fornecedor/todosPorCotacao");
    if (!resp.ok) throw new Error("HTTP " + resp.status);

    const obj = await resp.json();

    // Preenche o Map com chaves STRING
    Object.entries(obj).forEach(([cotacaoId, lista]) => {
      cacheFornecPorCotacao.set(String(cotacaoId), Array.isArray(lista) ? lista : []);
    });

    console.log("Mapa de fornecedores por cotação:", cacheFornecPorCotacao);
  } catch (erro) {
    console.error("Falha ao buscar fornecedores por cotação:", erro);
  }
}

function popularSelectFornecCotacao(lista) {
  const select = el("#fornecedorCotacao");
  if (!select) return;

  if (!lista || lista.length === 0) {
    select.innerHTML = `<option value="" selected disabled>(sem fornecedores)</option>`;
    select.disabled = true;
    return;
  }

  select.innerHTML = `<option value="" selected disabled>Selecione...</option>`;
  lista.forEach(f => {
    const opt = document.createElement("option");
    opt.value = f.id_fornecedor ?? f.idFornecedor ?? f.fornecedorId ?? f.id;
    opt.textContent = f.nome ?? f.nome_fantasia ?? f.razao_social ?? `Fornecedor ${opt.value}`;
    select.appendChild(opt);
  });
  select.disabled = false;
}

async function carregarFornecedoresDiretos() {
  if (cacheFornecedoresDiretos) {
    popularSelectFornecDiretos(cacheFornecedoresDiretos);
    return;
  }
  try {
    const resp = await fetch("http://localhost:8080/api/fornecedor/all");
    if (resp.ok) {
      const lista = await resp.json();
      cacheFornecedoresDiretos = lista;
      popularSelectFornecDiretos(lista);
    } else {
      console.error("HTTP fornecedores diretos:", resp.status);
      popularSelectFornecDiretos([]);
    }
  } catch (erro) {
    console.error("Falha ao buscar fornecedores diretos:", erro);
    popularSelectFornecDiretos([]);
  }
}

function popularSelectFornecDiretos(lista) {
  const select = el("#fornecedorDireto");
  if (!select) return;
  select.innerHTML = `<option value="" selected disabled>Selecione...</option>`;
  lista.forEach(f => {
    const opt = document.createElement("option");
    opt.value = f.id_fornecedor ?? f.idFornecedor ?? f.fornecedorId ?? f.id;
    opt.textContent = f.nome ?? f.descricao ?? `Fornecedor ${opt.value}`;
    select.appendChild(opt);
  });
}

// =====================================================
// Totais
// =====================================================
function atualizarTotal() {
  const total = itensDaCompra.reduce((acc, it) => acc + (it.qtd * it.valorUnit), 0);
  const span = el('#totalGeralCompra');
  if (span) span.textContent = fmt(total);
  return total;
}

// =====================================================
// Tabela de itens
// =====================================================
function redesenharTabela() {
  const tbody = el('#tabelaItens tbody');
  if (!tbody) return;
  tbody.innerHTML = "";

  itensDaCompra.forEach((item, i) => {
    const emEdicao = (indiceEditando === i);
    const subtotal = item.qtd * item.valorUnit;

    if (!emEdicao) {
      tbody.innerHTML += `
        <tr>
          <td>${item.descrProduto}</td>
          <td class="text-end">${item.qtd}</td>
          <td class="text-end">R$ ${fmt(item.valorUnit)}</td>
          <td class="text-end">R$ ${fmt(subtotal)}</td>
          <td>${item.validade}</td>
          <td class="text-center d-flex flex-column flex-sm-row gap-1 justify-content-center">
            <button class="btn btn-sm btn-outline-primary" data-acao="editar" data-idx="${i}">
              <i class="bi bi-pencil-square"></i> Editar
            </button>
            <button class="btn btn-sm btn-outline-danger" data-acao="remover" data-idx="${i}">
              <i class="bi bi-trash"></i> Excluir
            </button>
          </td>
        </tr>
      `;
    } else {
      tbody.innerHTML += `
        <tr class="table-warning">
          <td>${item.descrProduto}</td>
          <td class="text-end">
            <input type="number" min="1" class="form-control form-control-sm text-end" id="edit-qtd-${i}" value="${item.qtd}">
          </td>
          <td class="text-end">
            <input type="number" min="0" step="0.01" class="form-control form-control-sm text-end" id="edit-valor-${i}" value="${item.valorUnit}">
          </td>
          <td class="text-end">R$ ${fmt(subtotal)}</td>
          <td>
            <input type="date" class="form-control form-control-sm" id="edit-validade-${i}" value="${item.validade}">
          </td>
          <td class="text-center d-flex flex-column flex-sm-row gap-1 justify-content-center">
            <button class="btn btn-sm btn-success" data-acao="salvar-edicao" data-idx="${i}">
              <i class="bi bi-check2-circle"></i> Salvar
            </button>
            <button class="btn btn-sm btn-secondary" data-acao="cancelar-edicao">
              <i class="bi bi-x-circle"></i> Cancelar
            </button>
          </td>
        </tr>
      `;
    }
  });

  atualizarTotal();
}

// =====================================================
// CRUD de itens (frontend)
// =====================================================
function adicionarItem() {
  const selProd = el('#listaProdutos');
  const qtdEl = el('#quantidade');
  const valEl = el('#valorUnit');
  const valData = el('#validade');
  if (!selProd || !qtdEl || !valEl || !valData) return msg('Campo de item não encontrado.', 'danger');

  const prodCod = selProd.value;
  const prodDescr = selProd.options[selProd.selectedIndex]?.getAttribute('data-descr')
                  || selProd.options[selProd.selectedIndex]?.textContent || "";
  const qtd = parseInt(qtdEl.value, 10);
  const valorUnit = parseFloat(valEl.value);
  const validade = valData.value;

  if (!prodCod || !qtd || !valorUnit || !validade) return msg('Preencha produto, quantidade, valor e validade.', 'danger');

  itensDaCompra.push({ prodCod, descrProduto: prodDescr, qtd, valorUnit, validade });
  indiceEditando = null;

  redesenharTabela();

  selProd.value = '';
  qtdEl.value = '';
  valEl.value = '';
  valData.value = '';

 
}

function entrarEdicao(idx) { indiceEditando = idx; redesenharTabela(); }
function cancelarEdicao()  { indiceEditando = null; redesenharTabela(); }

function salvarEdicao(idx) {
  const qtdInput = el(`#edit-qtd-${idx}`);
  const valInput = el(`#edit-valor-${idx}`);
  const valData  = el(`#edit-validade-${idx}`);
  if (!qtdInput || !valInput || !valData) return msg('Campos de edição não encontrados.', 'danger');

  const novaQtd = parseInt(qtdInput.value, 10);
  const novoValUnit = parseFloat(valInput.value);
  const novaValidade = valData.value;

  if (!novaQtd || novaQtd <= 0) return msg('Quantidade inválida.', 'danger');
  if (isNaN(novoValUnit) || novoValUnit < 0) return msg('Valor inválido.', 'danger');
  if (!novaValidade) return msg('Informe a validade.', 'danger');

  Object.assign(itensDaCompra[idx], { qtd: novaQtd, valorUnit: novoValUnit, validade: novaValidade });
  indiceEditando = null;
  redesenharTabela();
  msg('Item atualizado.', 'success');
}

function removerItem(idx) {
  itensDaCompra.splice(idx, 1);
  indiceEditando = null;
  redesenharTabela();
  msg('Item removido.', 'warning');
}

// =====================================================
// Origem da compra (direta / cotação)
// =====================================================
function atualizarOrigem() {
  const origem = document.querySelector('input[name="origemCompra"]:checked')?.value || 'direta';
  const blocoDireta  = el('#blocoDireta');
  const blocoCotacao = el('#blocoCotacao');

  if (origem === 'direta') {
    blocoDireta?.classList.remove('d-none');
    blocoCotacao?.classList.add('d-none');
    carregarFornecedoresDiretos();
  } else {
    blocoDireta?.classList.add('d-none');
    blocoCotacao?.classList.remove('d-none');
    carregarCotacoes();

    const selFor = el('#fornecedorCotacao');
    if (selFor) {
      selFor.innerHTML = `<option value="" selected disabled>(selecione uma cotação)</option>`;
      selFor.disabled = true;
    }
  }
}

// =====================================================
// Buscar Funcionário por CPF (mask-friendly)
// =====================================================
async function buscarFuncionario() {
  const campoCPF  = el("#funcionarioCpf");
  const campoNome = el("#funcionarioNome");
  if (!campoCPF) return msg('Campo CPF não encontrado na tela.', 'danger');

  const cpfValor = campoCPF.value;
  try {
    const resp = await fetch("http://localhost:8080/api/funcionarios/buscaCPF?cpf=" + encodeURIComponent(cpfValor));
    if (resp.ok) {
      const funcionario = await resp.json();
      if (campoNome) campoNome.value = funcionario.funcNome || funcionario.nome || "";
      msg('Funcionário encontrado.', 'success');
    } else {
      if (campoNome) campoNome.value = "";
      msg('Funcionário não encontrado para o CPF informado.', 'danger');
    }
  } catch (e) {
    console.error(e);
    msg('Erro ao buscar funcionário.', 'danger');
  }
}

// =====================================================
// Limpar
// =====================================================
function limparTudo() {
  itensDaCompra = [];
  indiceEditando = null;

  el('#dataCompra')        && (el('#dataCompra').value        = '');
  el('#funcionarioCpf')    && (el('#funcionarioCpf').value    = '');
  el('#funcionarioNome')   && (el('#funcionarioNome').value   = '');
  el('#fornecedorDireto')  && (el('#fornecedorDireto').value  = '');
  el('#cotacaoId')         && (el('#cotacaoId').value         = '');
  el('#fornecedorCotacao') && (el('#fornecedorCotacao').value = '');

  const rDireta = el('#origemDireta');
  if (rDireta) rDireta.checked = true;
  atualizarOrigem();

  redesenharTabela();
  msg('Formulário limpo.', 'warning');
}

// =====================================================
// SALVAR ITENS — envia 1 POST por item para /api/itens_compra/
// =====================================================


// =====================================================
// Salvar (validações básicas) – CABEÇALHO + ITENS
// =====================================================
async function salvarCompra() {
  if (!Array.isArray(itensDaCompra) || itensDaCompra.length === 0) {
    msg('Adicione pelo menos 1 item.', 'danger'); return;
  }
  if (typeof indiceEditando === 'number') {
    msg('Conclua a edição antes de salvar.', 'warning'); return;
  }

  const dataCompra = el('#dataCompra')?.value || '';
  const funcCpf    = el('#funcionarioCpf')?.value || '';
  if (!dataCompra) { msg('Informe a data da compra.', 'danger'); return; }
  if (!funcCpf)    { msg('Informe o funcionário.', 'danger');   return; }

  const origem = document.querySelector('input[name="origemCompra"]:checked')?.value || 'direta';

  const total = itensDaCompra.reduce((acc, it) => {
    const qtd = Number(it.qtd);
    const val = Number(it.valorUnit);
    return acc + (Number.isFinite(qtd) && Number.isFinite(val) ? qtd * val : 0);
  }, 0);

  if (!Number.isFinite(total) || total < 0) {
    msg('Total inválido.', 'danger'); return;
  }

  const body = {
    dataCompra: dataCompra,
    compraValorTt: total,
    funcionarioFuncCpf: funcCpf,
    fornecedorId: null,
    fornecCotacaoFornecedorId: null,
    fornecCotacaoCotacaoId: null
  };

  if (origem === 'direta') {
    const forn = Number(el('#fornecedorDireto')?.value ?? '');
    if (!Number.isInteger(forn)) { msg('Selecione o fornecedor (compra direta).', 'danger'); return; }
    body.fornecedorId = forn;
  } else {
    const cot  = Number(el('#cotacaoId')?.value ?? '');
    const forn = Number(el('#fornecedorCotacao')?.value ?? '');
    if (!Number.isInteger(cot) || !Number.isInteger(forn)) {
      msg('Preencha cotação e fornecedor da cotação.', 'danger'); return;
    }
    body.fornecCotacaoCotacaoId = cot;
    body.fornecCotacaoFornecedorId = forn;
  }


  try {
// 1) Salva CABEÇALHO
const resp = await fetch('http://localhost:8080/api/compra', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(body)
});

if (!resp.ok) { msg('Erro ao salvar a compra (cabeçalho).', 'danger'); return; }

// === CAPTURA ROBUSTA DO compra_cod ===
let compraCod = null;
const ct = (resp.headers.get('content-type') || '').toLowerCase();

try {
  if (ct.includes('application/json')) {
    const data = await resp.json();
    // pode vir número puro (ex.: 123) ou objeto (se você mudar o controller futuramente)
    compraCod = (typeof data === 'number')
      ? data
      : (data.compra_cod ?? data.compraCod ?? data.id ?? data.compraId ?? null);
  } else {
    const txt = (await resp.text()).trim(); // ex.: text/plain "123"
    const n = Number(txt);
    if (Number.isInteger(n)) compraCod = n;
  }
} catch (e) {
  console.error('Falha ao interpretar resposta do cabeçalho:', e);
}

if (!compraCod) {
  msg('Cabeçalho salvo, mas não consegui ler o número da compra (compra_cod).', 'warning');
  return;
}

window.compra_cod = compraCod;
msg(`Cabeçalho da compra salvo. Nº ${compraCod}.`, 'success');
console.log('compra_cod:', compraCod, 'payload_enviado:', body);

// 2) Salva ITENS
const okItens = await salvarItensCompra(compraCod);
if (!okItens) return;

// 3) Finalização
msg('Compra concluída com sucesso!', 'success');
limparTudo();


  } catch (e) {
    console.error(e);
    msg('Falha na comunicação com o backend.', 'danger');
  }
}

async function salvarItensCompra(compraCod) {
  if (!compraCod) { msg('compra_cod não encontrado para salvar itens.', 'danger'); return false; }

  const total = itensDaCompra.length;
  let okCount = 0;
  el('#btnSalvar')?.setAttribute('disabled', 'disabled');

  for (let i = 0; i < itensDaCompra.length; i++) {
    const it = itensDaCompra[i];

    // Corpo sem validade (só o que a tabela itens_compra precisa)
    const payload = {
      produtoProdCod: Number(it.prodCod),
      compraCompraCod: Number(compraCod),
      valor: Number(it.valorUnit),
      quantidade: Number(it.qtd)
    };

    // Validade vai como query param (yyyy-MM-dd do input type="date")
    const validadeParam = it.validade ? `?validade=${encodeURIComponent(it.validade)}` : '';
    const url = `http://localhost:8080/api/compra/itens${validadeParam}`;

    try {
      console.log(`[${i + 1}/${total}] POST ${url}`, payload);

      const resp = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!resp.ok) {
        const t = await resp.text().catch(() => '');
        throw new Error(`HTTP ${resp.status} ${t}`);
      }

      okCount++;
     

    } catch (e) {
      console.error(`Falha no item ${i + 1}:`, e);
      msg(`Erro ao inserir item ${i + 1}/${total}: ${e.message}`, 'danger');
      // Se quiser abortar no primeiro erro:
      // el('#btnSalvar')?.removeAttribute('disabled');
      // return false;
    }
  }

  el('#btnSalvar')?.removeAttribute('disabled');

  if (okCount === total) {
    msg('Todos os itens foram inseridos com sucesso.', 'success');
    return true;
  } else if (okCount > 0) {
    msg(`Alguns itens foram inseridos (${okCount}/${total}).`, 'warning');
    return false;
  } else {
    msg('Nenhum item foi inserido.', 'danger');
    return false;
  }
}



// =====================================================
// Eventos
// =====================================================
document.addEventListener('DOMContentLoaded', () => {
  carregarProdutos();
  carregarFornecedoresPorCotacao(); // pré-carrega o mapa cotação -> fornecedores

  el('#origemDireta')?.addEventListener('change', atualizarOrigem);
  el('#origemCotacao')?.addEventListener('change', atualizarOrigem);
  atualizarOrigem();

  // Mudança de cotação: popula fornecedores dessa cotação usando o Map.
  el('#cotacaoId')?.addEventListener('change', async (e) => {
    const cotacaoId = String(e.target.value);
    if (cacheFornecPorCotacao.size === 0) await carregarFornecedoresPorCotacao();
    const fornecedores = cacheFornecPorCotacao.get(cotacaoId) || [];
    popularSelectFornecCotacao(fornecedores);
  });

  const inputCPF = el("#funcionarioCpf");
  if (inputCPF) {
    inputCPF.addEventListener("input", (e) => {
      e.target.value = aplicarMascaraCPF(e.target.value);
    });
  }

  el('#btnBuscarFuncionario')?.addEventListener('click', (e) => {
    e.preventDefault();
    buscarFuncionario();
  });

  el('#btnAddItem')?.addEventListener('click', (e) => {
    e.preventDefault();
    adicionarItem();
  });

  el('#btnSalvar')?.addEventListener('click', (e) => {
    e.preventDefault();
    salvarCompra();
  });

  el('#btnLimparTudo')?.addEventListener('click', (e) => {
    e.preventDefault();
    limparTudo();
  });

  el('#tabelaItens tbody')?.addEventListener('click', (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    const acao = btn.getAttribute('data-acao');
    const idx  = parseInt(btn.getAttribute('data-idx'), 10);
    if (acao === 'remover')             removerItem(idx);
    else if (acao === 'editar')         entrarEdicao(idx);
    else if (acao === 'salvar-edicao')  salvarEdicao(idx);
    else if (acao === 'cancelar-edicao') cancelarEdicao();
  });
});
