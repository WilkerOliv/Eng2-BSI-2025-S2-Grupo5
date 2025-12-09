const API_BASE = "http://localhost:8080";

const ROTA_FUNC_DOA = (cpf) => `${API_BASE}/apis/funcionario/${cpf}`;
const ROTA_PC_DOA        = (cpf) => `${API_BASE}/api/pessoas/${cpf}`;
const ROTA_PRODS_DOA     = `${API_BASE}/api/produtos`;
const ROTA_DOAR_PC       = `${API_BASE}/apis/doacao_pc/registrar`;
const ROTA_NECESSIDADES  = (cpf) => `${API_BASE}/api/necessidades/produtos/pessoa/${cpf}`;
const ROTA_CESTAS_LISTAR = `${API_BASE}/apis/cesta/listar`;
const ROTA_CESTA_ITENS   = (id) => `${API_BASE}/apis/cesta/itens/${id}`;
const ROTA_CESTA_ALT     = (id) => `${API_BASE}/apis/cesta/alterar/${id}`;
const ROTA_CESTA_DEL     = (id) => `${API_BASE}/apis/cesta/excluir/${id}`;

let cacheProdutos = [];
let necessidades = [];
let cestasDisponiveis = [];
let cestaSelecionada = null;
let itensCestaSel = [];

const els = {};

const el = (id) => document.getElementById(id);

function toastDoa(msg, tipo = "danger") {
    const box = el("alert-container");
    const div = document.createElement("div");
    div.className = `alert alert-${tipo} alert-dismissible fade show`;
    div.role = "alert";
    div.innerHTML = `
    ${msg}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
  `;
    (box || document.body).appendChild(div);
    setTimeout(() => {
        if (div) div.remove();
    }, 5000);
}

const onlyDigits = (s) => (s || "").replace(/\D/g, "");

const maskCpf = (v) =>
    onlyDigits(v)
        .slice(0, 11)
        .replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, (_, a, b, c, d) => `${a}.${b}.${c}-${d}`);

function isValidCpf(cpfRaw) {
    const cpf = onlyDigits(cpfRaw);
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

    const calc = (base) => {
        let s = 0;
        for (let i = 0; i < base.length; i++) {
            s += +base[i] * (base.length + 1 - i);
        }
        const r = s % 11;
        return r < 2 ? 0 : 11 - r;
    };

    const d1 = calc(cpf.slice(0, 9));
    const d2 = calc(cpf.slice(0, 9) + d1);
    return cpf.endsWith(`${d1}${d2}`);
}

async function loadProdutos() {
    if (cacheProdutos.length) return cacheProdutos;
    const resp = await fetch(ROTA_PRODS_DOA);
    if (!resp.ok) throw new Error("Falha ao carregar produtos");
    cacheProdutos = await resp.json();
    return cacheProdutos;
}

function getProdutoByCodLocal(cod) {
    cod = Number(cod);
    return cacheProdutos.find((p) => Number(p.prodCod) === cod) || null;
}

function getProdutoByNomeLocal(nome) {
    const n = (nome || "").trim().toLowerCase();
    if (!n) return null;
    return (
        cacheProdutos.find((p) => (p.prodDescr || "").trim().toLowerCase() === n) ||
        null
    );
}

async function buscarFuncionario(cpfMasked) {
    const input = els.cpfFuncDoa;
    const label = els.nomeFuncDoa;

    if (!isValidCpf(cpfMasked)) {
        input.classList.add("is-invalid");
        label.textContent = "CPF inválido";
        return;
    }
    input.classList.remove("is-invalid");

    try {
        const resp = await fetch(ROTA_FUNC_DOA(cpfMasked));
        if (!resp.ok) {
            label.textContent = "Funcionário não encontrado";
            return;
        }

        const data = await resp.json();
        label.textContent =
            data.func_nome || data.funcNome || "Funcionário encontrado";

    } catch {
        label.textContent = "Erro ao buscar funcionário";
    }
}


async function buscarPessoa(cpfMasked) {
    const input = els.cpfPcDoa;
    const label = els.nomePcDoa;

    if (!isValidCpf(cpfMasked)) {
        input.classList.add("is-invalid");
        label.textContent = "CPF inválido";
        return;
    }
    input.classList.remove("is-invalid");

    try {
        const resp = await fetch(ROTA_PC_DOA(cpfMasked));
        if (!resp.ok) {
            label.textContent = "Pessoa não encontrada";
            return;
        }
        const data = await resp.json();
        label.textContent = data.pcNome || "Pessoa encontrada";
    } catch {
        label.textContent = "Erro ao buscar pessoa";
    }
}

async function carregarNecessidadesPessoa() {
    const cpfMasked = els.cpfPcDoa.value;
    if (!isValidCpf(cpfMasked)) {
        toastDoa("CPF da pessoa carente inválido para carregar necessidades", "warning");
        els.cpfPcDoa.classList.add("is-invalid");
        return;
    }
    els.cpfPcDoa.classList.remove("is-invalid");

    const cpfLimpo = onlyDigits(cpfMasked);

    try {
        await loadProdutos();

        const resp = await fetch(ROTA_NECESSIDADES(cpfLimpo));
        if (!resp.ok) {
            toastDoa("Nenhuma necessidade encontrada para esta pessoa", "info");
            necessidades = [];
            renderTabelaNecessidades();
            return;
        }

        const lista = await resp.json();
        if (!Array.isArray(lista) || !lista.length) {
            toastDoa("Nenhuma necessidade cadastrada para esta pessoa", "info");
            necessidades = [];
            renderTabelaNecessidades();
            return;
        }

        // lista vem com {produtoCod, quantidade, observacao}
        necessidades = lista.map((nec) => {
            const p = getProdutoByCodLocal(nec.produtoCod);
            return {
                produtoCod: Number(nec.produtoCod),
                quantidade: Number(nec.quantidade || 0),
                observacao: nec.observacao || "",
                produtoDescr: p ? p.prodDescr : `Produto #${nec.produtoCod}`,
            };
        });

        renderTabelaNecessidades();
        toastDoa("Necessidades carregadas com sucesso.", "success");
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao carregar necessidades: " + e.message, "danger");
    }
}

function renderTabelaNecessidades() {
    const tbody = els.tbodyNecessidades;
    tbody.innerHTML = "";

    if (!necessidades.length) {
        const tr = document.createElement("tr");
        tr.innerHTML =
            '<td colspan="2" class="text-muted text-center">Nenhuma necessidade para exibir.</td>';
        tbody.appendChild(tr);
        return;
    }

    necessidades.forEach((nec) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${nec.produtoDescr}</td>
            <td style="width:120px; text-align:center;">${nec.quantidade}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function carregarCestas() {
    try {
        const resp = await fetch(ROTA_CESTAS_LISTAR);
        if (!resp.ok) {
            throw new Error("Falha ao listar cestas");
        }
        const lista = await resp.json();
        // backend retorna: cb_cod, descricao, total_itens
        cestasDisponiveis = Array.isArray(lista) ? lista : [];
        renderTabelaCestas(cestasDisponiveis);
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao carregar cestas: " + e.message, "danger");
    }
}

function renderTabelaCestas(listaFiltrada) {
    const tbody = els.tbodyCestasDoacao;
    tbody.innerHTML = "";

    if (!listaFiltrada.length) {
        const tr = document.createElement("tr");
        tr.innerHTML =
            '<td colspan="4" class="text-center text-muted">Nenhuma cesta encontrada.</td>';
        tbody.appendChild(tr);
        return;
    }

    listaFiltrada.forEach((c) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${c.cb_cod}</td>
      <td>${c.descricao}</td>
      <td>${c.total_itens}</td>
      <td>
        <button type="button"
                class="btn btn-sm btn-info me-1"
                data-view-cesta="${c.cb_cod}">
          Visualizar
        </button>
        <button type="button"
                class="btn btn-sm btn-success me-1"
                data-add-cesta="${c.cb_cod}">
          Adicionar
        </button>
        <button type="button"
                class="btn btn-sm btn-warning me-1"
                data-edit-cesta="${c.cb_cod}">
          Alterar
        </button>
        <button type="button"
                class="btn btn-sm btn-danger"
                data-del-cesta="${c.cb_cod}">
          Excluir
        </button>
      </td>
    `;
        tbody.appendChild(tr);
    });

    tbody.querySelectorAll("[data-add-cesta]").forEach((btn) => {
        btn.onclick = () => {
            const id = Number(btn.dataset.addCesta);
            selecionarCestaParaDoacao(id);
        };
    });

    tbody.querySelectorAll("[data-edit-cesta]").forEach((btn) => {
        btn.onclick = () => {
            const id = Number(btn.dataset.editCesta);
            alterarDescricaoCesta(id);
        };
    });

    tbody.querySelectorAll("[data-del-cesta]").forEach((btn) => {
        btn.onclick = () => {
            const id = Number(btn.dataset.delCesta);
            excluirCesta(id);
        };
    });

    tbody.querySelectorAll("[data-view-cesta]").forEach((btn) => {
        btn.onclick = () => {
            const id = Number(btn.dataset.viewCesta);
            visualizarCesta(id);
        };
    });
}

function buscarCestasPorDescricao() {
    const termo = els.buscarCestaDoacao.value.trim().toLowerCase();
    if (!termo) {
        renderTabelaCestas(cestasDisponiveis);
        return;
    }
    const filtradas = cestasDisponiveis.filter((c) =>
        (c.descricao || "").toLowerCase().includes(termo)
    );
    renderTabelaCestas(filtradas);
}

async function alterarDescricaoCesta(id) {
    const cesta = cestasDisponiveis.find((c) => Number(c.cb_cod) === Number(id));
    const atual = cesta ? cesta.descricao : "";
    const nova = prompt("Nova descrição da cesta:", atual || "");
    if (!nova || !nova.trim()) return;

    try {
        const resp = await fetch(ROTA_CESTA_ALT(id), {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ descricao: nova.trim() }),
        });
        const data = await resp.json().catch(() => ({}));

        if (!resp.ok || data.sucesso === false) {
            toastDoa(data.mensagem || "Falha ao alterar cesta", "danger");
            return;
        }

        toastDoa(data.mensagem || "Cesta atualizada com sucesso", "success");
        await carregarCestas();
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao alterar cesta: " + e.message, "danger");
    }
}

async function excluirCesta(id) {
    if (!confirm("Deseja realmente excluir esta cesta?")) return;
    try {
        const resp = await fetch(ROTA_CESTA_DEL(id), { method: "DELETE" });
        const data = await resp.json().catch(() => ({}));

        if (!resp.ok || data.sucesso === false) {
            toastDoa(data.mensagem || "Falha ao excluir cesta", "danger");
            return;
        }

        toastDoa(data.mensagem || "Cesta excluída com sucesso", "success");
        await carregarCestas();
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao excluir cesta: " + e.message, "danger");
    }
}

async function selecionarCestaParaDoacao(cbCod) {
    try {
        await loadProdutos();

        const cesta = cestasDisponiveis.find(
            (c) => Number(c.cb_cod) === Number(cbCod)
        );
        if (!cesta) {
            toastDoa("Cesta não encontrada na lista carregada.", "warning");
            return;
        }

        const resp = await fetch(ROTA_CESTA_ITENS(cbCod));
        if (!resp.ok) {
            toastDoa("Falha ao carregar itens da cesta.", "danger");
            return;
        }
        const listaItens = await resp.json();

        itensCestaSel = (listaItens || []).map((row) => {
            const cod = Number(row.produto_cod || row.produtoCod || 0);
            const qtd = Number(row.quantidade || 0);
            const p = getProdutoByCodLocal(cod);
            return {
                produtoCod: cod,
                quantidade: qtd > 0 ? qtd : 0,
                descr: p ? p.prodDescr : row.produto || `Produto #${cod}`,
            };
        });

        cestaSelecionada = {
            cb_cod: cesta.cb_cod,
            descricao: cesta.descricao,
            total_itens: cesta.total_itens,
        };

        els.cestaSelecionadaDescricao.textContent = cestaSelecionada.descricao;
        els.secaoCestaSelecionada.style.display = "block";

        renderItensCestaSelecionada();
        toastDoa("Cesta selecionada para doação.", "success");
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao selecionar cesta: " + e.message, "danger");
    }
}

function renderItensCestaSelecionada() {
    const tbody = els.tbodyItensCestaSel;
    tbody.innerHTML = "";

    if (!itensCestaSel.length) {
        const tr = document.createElement("tr");
        tr.innerHTML =
            '<td colspan="3" class="text-center text-muted">Nenhum item na cesta selecionada.</td>';
        tbody.appendChild(tr);
        els.contadorItensCestaSel.textContent = "0 item(s)";
        return;
    }

    itensCestaSel.forEach((it, idx) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${it.descr}</td>
      <td style="width:120px;">${it.quantidade}</td>
      <td style="width:200px;">
        <button type="button"
                class="btn btn-sm btn-outline-success me-1"
                data-add-item="${idx}">
          Adicionar
        </button>
        <button type="button"
                class="btn btn-sm btn-outline-warning me-1"
                data-edit-item="${idx}">
          Alterar
        </button>
        <button type="button"
                class="btn btn-sm btn-outline-danger"
                data-del-item="${idx}">
          Remover
        </button>
      </td>
    `;
        tbody.appendChild(tr);
    });

    els.contadorItensCestaSel.textContent = `${itensCestaSel.length} item(s)`;

    // Ações
    tbody.querySelectorAll("[data-add-item]").forEach((btn) => {
        btn.onclick = () => {
            const idx = Number(btn.dataset.addItem);
            itensCestaSel[idx].quantidade += 1;
            renderItensCestaSelecionada();
        };
    });

    tbody.querySelectorAll("[data-edit-item]").forEach((btn) => {
        btn.onclick = () => {
            const idx = Number(btn.dataset.editItem);
            editarItemCesta(idx);
        };
    });

    tbody.querySelectorAll("[data-del-item]").forEach((btn) => {
        btn.onclick = () => {
            const idx = Number(btn.dataset.delItem);
            itensCestaSel.splice(idx, 1);
            renderItensCestaSelecionada();
        };
    });
}

function editarItemCesta(idx) {
    const item = itensCestaSel[idx];
    if (!item) return;

    const novoNome = prompt("Novo nome do produto:", item.descr || "");
    if (!novoNome || !novoNome.trim()) return;

    let novaQtd = parseInt(
        prompt("Nova quantidade:", String(item.quantidade || 1)),
        10
    );
    if (isNaN(novaQtd) || novaQtd <= 0) {
        alert("Quantidade inválida.");
        return;
    }

    const prodEncontrado = getProdutoByNomeLocal(novoNome);
    if (!prodEncontrado) {
        alert("Produto não encontrado. Digite exatamente como está cadastrado.");
        return;
    }

    item.produtoCod = prodEncontrado.prodCod;
    item.descr = prodEncontrado.prodDescr;
    item.quantidade = novaQtd;

    renderItensCestaSelecionada();
}

function cancelarCestaSelecionada() {
    cestaSelecionada = null;
    itensCestaSel = [];
    els.secaoCestaSelecionada.style.display = "none";
    els.cestaSelecionadaDescricao.textContent = "";
    els.contadorItensCestaSel.textContent = "0 item(s)";
}

function configurarBuscaProdutoCestaSel() {
    const input = els.buscarProdutoDoa;
    const lista = els.listaBuscaProdDoa;

    input.addEventListener("input", () => {
        const texto = input.value.trim().toLowerCase();

        if (texto.length < 2) {
            lista.style.display = "none";
            lista.innerHTML = "";
            return;
        }

        const filtrados = cacheProdutos.filter((p) =>
            (p.prodDescr || "").toLowerCase().includes(texto)
        );

        if (!filtrados.length) {
            lista.innerHTML =
                '<div class="list-group-item">Nenhum produto encontrado.</div>';
            lista.style.display = "block";
            return;
        }

        lista.innerHTML = "";
        filtrados.forEach((p) => {
            const btn = document.createElement("button");
            btn.type = "button";
            btn.className = "list-group-item list-group-item-action";
            btn.textContent = p.prodDescr;
            btn.onclick = () => selecionarProdutoBuscaDoa(p);
            lista.appendChild(btn);
        });
        lista.style.display = "block";
    });

    document.addEventListener("click", (ev) => {
        if (!lista.contains(ev.target) && ev.target !== input) {
            lista.style.display = "none";
        }
    });
}

function selecionarProdutoBuscaDoa(prod) {
    els.buscarProdutoDoa.value = prod.prodDescr;
    els.buscarProdutoDoa.dataset.produtoId = prod.prodCod;
    els.listaBuscaProdDoa.style.display = "none";
}

function adicionarItemCestaSelecionada() {
    if (!cestaSelecionada) {
        toastDoa("Selecione uma cesta antes de adicionar itens.", "warning");
        return;
    }

    const nomeInput = els.buscarProdutoDoa;
    const qtdInput = els.qtdProdutoDoa;

    const prodId = nomeInput.dataset.produtoId;
    let qtd = parseInt(qtdInput.value, 10);

    let valido = true;

    if (!prodId) {
        nomeInput.classList.add("is-invalid");
        valido = false;
    } else {
        nomeInput.classList.remove("is-invalid");
    }

    if (isNaN(qtd) || qtd <= 0) {
        qtdInput.classList.add("is-invalid");
        valido = false;
    } else {
        qtdInput.classList.remove("is-invalid");
    }

    if (!valido) return;

    const prod = getProdutoByCodLocal(prodId);
    if (!prod) {
        toastDoa("Produto não encontrado no catálogo.", "danger");
        return;
    }
    const idxExistente = itensCestaSel.findIndex(
        (it) => Number(it.produtoCod) === Number(prodId)
    );
    if (idxExistente >= 0) {
        itensCestaSel[idxExistente].quantidade += qtd;
    } else {
        itensCestaSel.push({
            produtoCod: Number(prodId),
            descr: prod.prodDescr,
            quantidade: qtd,
        });
    }

    renderItensCestaSelecionada();

    // limpar campos
    nomeInput.value = "";
    nomeInput.dataset.produtoId = "";
    qtdInput.value = 1;
}

async function visualizarCesta(cbCod) {
    try {
        const resp = await fetch(ROTA_CESTA_ITENS(cbCod));
        if (!resp.ok) {
            toastDoa("Erro ao carregar itens da cesta.", "danger");
            return;
        }

        const itens = await resp.json();

        if (!Array.isArray(itens) || !itens.length) {
            alert("Esta cesta está vazia.");
            return;
        }

        let texto = "Itens da Cesta:\n\n";
        itens.forEach((it) => {
            texto += `• ${it.produto} — ${it.quantidade}\n`;
        });

        alert(texto);

    } catch (e) {
        console.error(e);
        toastDoa("Erro ao visualizar cesta: " + e.message, "danger");
    }
}

async function efetuarDoacao() {
    const cpfFuncMasked = els.cpfFuncDoa.value;
    const cpfPcMasked = els.cpfPcDoa.value;

    let ok = true;

    if (!isValidCpf(cpfFuncMasked)) {
        els.cpfFuncDoa.classList.add("is-invalid");
        ok = false;
    } else {
        els.cpfFuncDoa.classList.remove("is-invalid");
    }

    if (!isValidCpf(cpfPcMasked)) {
        els.cpfPcDoa.classList.add("is-invalid");
        ok = false;
    } else {
        els.cpfPcDoa.classList.remove("is-invalid");
    }

    if (!ok) {
        toastDoa("Preencha corretamente os CPFs.", "warning");
        return;
    }

    if (!cestaSelecionada || !itensCestaSel.length) {
        toastDoa(
            "Selecione uma cesta e garanta que ela tenha pelo menos um item.",
            "warning"
        );
        return;
    }

    const funcCpf = onlyDigits(cpfFuncMasked);
    const pcCpf = onlyDigits(cpfPcMasked);

    const itensValidos = itensCestaSel
        .filter((it) => it.quantidade > 0 && it.produtoCod)
        .map((it) => ({
            produtoCod: it.produtoCod,
            quantidade: it.quantidade,
        }));

    if (!itensValidos.length) {
        toastDoa("Nenhum item válido para enviar na doação.", "warning");
        return;
    }

    const payload = {
        funcCpf: funcCpf,
        pcCpf: pcCpf,
        cbCod: cestaSelecionada.cb_cod, // <-- usado para apagar a cesta no backend
        itens: itensValidos,
    };

    try {
        const resp = await fetch(ROTA_DOAR_PC, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        const data = await resp.json().catch(() => ({}));

        if (!resp.ok || data.sucesso === false) {
            toastDoa(data.mensagem || "Erro ao registrar doação.", "danger");
            return;
        }

        toastDoa(
            data.mensagem || "Doação registrada e estoque baixado com sucesso!",
            "success"
        );


        cancelarCestaSelecionada();

        els.cpfFuncDoa.value = "";
        els.cpfPcDoa.value = "";

        els.nomeFuncDoa.textContent = "";
        els.nomePcDoa.textContent = "";

        els.cpfFuncDoa.classList.remove("is-invalid");
        els.cpfPcDoa.classList.remove("is-invalid");

        necessidades = [];
        renderTabelaNecessidades();

        els.buscarCestaDoacao.value = "";

        // recarrega cestas para que a usada suma da lista
        await carregarCestas();

    } catch (e) {
        console.error(e);
        toastDoa("Falha ao enviar doação: " + e.message, "danger");
    }
}

window.addEventListener("DOMContentLoaded", async () => {

    els.cpfFuncDoa = el("cpfFuncDoa");
    els.nomeFuncDoa = el("nomeFuncDoa");
    els.cpfPcDoa = el("cpfPcDoa");
    els.nomePcDoa = el("nomePcDoa");

    els.tbodyNecessidades = el("tbodyNecessidades");
    els.btnCarregarNecessidades = el("btnCarregarNecessidades");

    els.buscarCestaDoacao = el("buscarCestaDoacao");
    els.btnBuscarCestaDoacao = el("btnBuscarCestaDoacao");
    els.tabelaCestasDoacao = el("tabelaCestasDoacao");
    els.tbodyCestasDoacao = el("tbodyCestasDoacao");

    els.secaoCestaSelecionada = el("secaoCestaSelecionada");
    els.cestaSelecionadaDescricao = el("cestaSelecionadaDescricao");
    els.buscarProdutoDoa = el("buscarProdutoDoa");
    els.listaBuscaProdDoa = el("listaBuscaProdDoa");
    els.qtdProdutoDoa = el("qtdProdutoDoa");
    els.btnAddItemCestaSel = el("btnAddItemCestaSel");
    els.tbodyItensCestaSel = el("tbodyItensCestaSel");
    els.contadorItensCestaSel = el("contadorItensCestaSel");
    els.btnCancelarCestaSel = el("btnCancelarCestaSel");
    els.btnEfetuarDoacao = el("btnEfetuarDoacao");

    els.cpfFuncDoa.addEventListener("input", () => {
        els.cpfFuncDoa.value = maskCpf(els.cpfFuncDoa.value);
    });
    els.cpfPcDoa.addEventListener("input", () => {
        els.cpfPcDoa.value = maskCpf(els.cpfPcDoa.value);
    });

    els.cpfFuncDoa.addEventListener("blur", () => {
        if (els.cpfFuncDoa.value.trim() !== "") {
            buscarFuncionario(els.cpfFuncDoa.value);
        }
    });
    els.cpfPcDoa.addEventListener("blur", () => {
        if (els.cpfPcDoa.value.trim() !== "") {
            buscarPessoa(els.cpfPcDoa.value);
        }
    });

    els.btnCarregarNecessidades.addEventListener("click", (e) => {
        e.preventDefault();
        carregarNecessidadesPessoa();
    });

    els.btnBuscarCestaDoacao.addEventListener("click", (e) => {
        e.preventDefault();
        buscarCestasPorDescricao();
    });
    els.buscarCestaDoacao.addEventListener("keyup", (e) => {
        if (e.key === "Enter") {
            buscarCestasPorDescricao();
        }
    });

    els.btnAddItemCestaSel.addEventListener("click", (e) => {
        e.preventDefault();
        adicionarItemCestaSelecionada();
    });


    els.btnCancelarCestaSel.addEventListener("click", (e) => {
        e.preventDefault();
        cancelarCestaSelecionada();
    });

    els.btnEfetuarDoacao.addEventListener("click", (e) => {
        e.preventDefault();
        efetuarDoacao();
    });

    try {
        await loadProdutos();
    } catch (e) {
        console.error(e);
        toastDoa("Erro ao carregar catálogo de produtos: " + e.message, "danger");
    }
    configurarBuscaProdutoCestaSel();

    await carregarCestas();

    renderTabelaNecessidades();
    renderItensCestaSelecionada();
});
