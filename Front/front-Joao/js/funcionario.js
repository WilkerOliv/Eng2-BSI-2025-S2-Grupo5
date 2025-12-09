const API = "http://localhost:8080/apis/funcionario";

const tabela = document.getElementById("tabelaFuncionarios");
const btnSalvar = document.getElementById("btnSalvar");
const btnLimpar = document.getElementById("btnLimpar");
const campoBusca = document.getElementById("busca");

let funcionariosCache = [];
let cpfEditando = null;

function mostrarAlerta(msg, tipo = "sucesso") {
    const alerta = document.getElementById("alerta");

    alerta.textContent = msg;
    alerta.classList.remove("d-none", "alert-success", "alert-danger");

    alerta.classList.add(tipo === "erro" ? "alert-danger" : "alert-success");

    setTimeout(() => alerta.classList.add("d-none"), 3500);
}

function normalizarTexto(txt) {
    return txt
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .toLowerCase();
}

function limparCPF(str) {
    return (str || "").replace(/\D/g, "");
}

function somenteNumeros(str) {
    return (str || "").replace(/\D/g, "");
}

function formatarCPF(v) {
    v = somenteNumeros(v).slice(0, 11);
    if (v.length <= 3) return v;
    if (v.length <= 6) return v.replace(/(\d{3})(\d+)/, "$1.$2");
    if (v.length <= 9) return v.replace(/(\d{3})(\d{3})(\d+)/, "$1.$2.$3");
    return v.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, "$1.$2.$3-$4");
}

function formatarTelefone(v) {
    v = somenteNumeros(v).slice(0, 11);
    if (v.length <= 2) return v;
    if (v.length <= 6) return v.replace(/(\d{2})(\d+)/, "($1) $2");
    if (v.length <= 10) return v.replace(/(\d{2})(\d{4})(\d+)/, "($1) $2-$3");
    return v.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
}

function formatarCEP(v) {
    v = somenteNumeros(v).slice(0, 8);
    if (v.length <= 5) return v;
    return v.replace(/(\d{5})(\d+)/, "$1-$2");
}

// máscaras
document.getElementById("funcCpf").addEventListener("input", (e) => {
    e.target.value = formatarCPF(e.target.value);
});

document.getElementById("funcTelefone").addEventListener("input", (e) => {
    e.target.value = formatarTelefone(e.target.value);
});

document.getElementById("cep").addEventListener("input", (e) => {
    e.target.value = formatarCEP(e.target.value);
});

[
    "funcCpf", "funcNome", "funcEmail", "username", "funcTelefone",
    "funcSenha", "cargo", "tipoAcesso", "rua", "bairro", "cidade",
    "uf", "cep", "dataAdmissao"
].forEach(id => {
    const el = document.getElementById(id);
    if (el) {
        el.addEventListener("input", () => {
            el.classList.remove("is-invalid");
        });
        if (el.tagName.toLowerCase() === "select") {
            el.addEventListener("change", () => {
                el.classList.remove("is-invalid");
            });
        }
    }
});

async function carregarFuncionarios() {
    try {
        const resp = await fetch(`${API}/listar`);
        funcionariosCache = await resp.json();
        renderTabelaFuncionarios(funcionariosCache);
    } catch {
        mostrarAlerta("Erro ao carregar funcionários.", "erro");
    }
}

function renderTabelaFuncionarios(lista) {
    tabela.innerHTML = "";

    const totalAdmins = lista.filter(f => Number(f.tipo_acesso) === 1).length;

    lista.forEach(func => {
        const tr = document.createElement("tr");
        const ehAdmin = Number(func.tipo_acesso) === 1;
        const podeExcluir = !(ehAdmin && totalAdmins <= 1);

        tr.innerHTML = `
            <td>${func.func_cpf || "-"}</td>
            <td>${func.func_nome || "-"}</td>
            <td>${func.func_email || "-"}</td>
            <td>${func.cargo || "-"}</td>
            <td>${func.tipo_acesso || "-"}</td>
            <td>${func.cidade || "-"}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarFuncionario('${func.func_cpf}')">
                    Editar
                </button>
                ${podeExcluir ? `
                <button class="btn btn-danger btn-sm" onclick="excluirFuncionario('${func.func_cpf}')">
                    Excluir
                </button>` : `
                <button class="btn btn-danger btn-sm" disabled title="Não é possível excluir o único administrador.">
                    Excluir
                </button>`}
            </td>
        `;

        tabela.appendChild(tr);
    });
}

function pegarDadosFormulario() {
    return {
        funcCpf: document.getElementById("funcCpf").value.trim(),
        funcNome: document.getElementById("funcNome").value.trim(),
        funcSenha: document.getElementById("funcSenha").value.trim(),
        funcEmail: document.getElementById("funcEmail").value.trim(),
        funcTelefone: document.getElementById("funcTelefone").value.trim(),
        username: document.getElementById("username").value.trim(),
        cargo: document.getElementById("cargo").value.trim(),
        tipoAcesso: document.getElementById("tipoAcesso").value,
        rua: document.getElementById("rua").value.trim(),
        bairro: document.getElementById("bairro").value.trim(),
        cidade: document.getElementById("cidade").value.trim(),
        uf: document.getElementById("uf").value.trim(),
        cep: document.getElementById("cep").value.trim(),
        dataAdmissao: document.getElementById("dataAdmissao").value,
        dataDemissao: document.getElementById("dataDemissao").value
    };
}

function limparFormulario() {
    document.getElementById("formFuncionario").reset();
    document.getElementById("rua").value = "";
    document.getElementById("bairro").value = "";
    document.getElementById("cidade").value = "";
    document.getElementById("uf").value = "";
    document.getElementById("cep").value = "";
    document.getElementById("dataAdmissao").value = "";
    document.getElementById("dataDemissao").value = "";
    cpfEditando = null;
    btnSalvar.textContent = "Salvar";

    // remove is-invalid
    document.querySelectorAll(".is-invalid").forEach(el => el.classList.remove("is-invalid"));
}

btnLimpar.addEventListener("click", limparFormulario);

function validarFormularioFront(dados) {
    let invalido = false;

    function marcaInvalido(id) {
        const el = document.getElementById(id);
        if (el) {
            el.classList.add("is-invalid");
            invalido = true;
        }
    }

    if (!dados.funcCpf) marcaInvalido("funcCpf");
    if (!dados.funcNome) marcaInvalido("funcNome");
    if (!dados.funcEmail) marcaInvalido("funcEmail");
    if (!dados.funcSenha) marcaInvalido("funcSenha");
    if (!dados.funcTelefone) marcaInvalido("funcTelefone");
    if (!dados.username) marcaInvalido("username");
    if (!dados.cargo) marcaInvalido("cargo");
    if (!dados.tipoAcesso) marcaInvalido("tipoAcesso");
    if (!dados.rua) marcaInvalido("rua");
    if (!dados.bairro) marcaInvalido("bairro");
    if (!dados.cidade) marcaInvalido("cidade");
    if (!dados.uf) marcaInvalido("uf");
    if (!dados.cep) marcaInvalido("cep");
    if (!dados.dataAdmissao) marcaInvalido("dataAdmissao");

    const cpfNum = limparCPF(dados.funcCpf);
    if (cpfNum.length !== 11) {
        marcaInvalido("funcCpf");
    }

    if (dados.funcEmail && !dados.funcEmail.includes("@")) {
        marcaInvalido("funcEmail");
    }

    if (invalido) {
        mostrarAlerta("Preencha corretamente os campos obrigatórios.", "erro");
        return false;
    }
    return true;
}

btnSalvar.addEventListener("click", async () => {
    const dados = pegarDadosFormulario();

    if (!validarFormularioFront(dados)) {
        return;
    }

    const url = cpfEditando
        ? `${API}/atualizar/${cpfEditando}`
        : `${API}/cadastrar`;

    const metodo = cpfEditando ? "PUT" : "POST";

    try {
        const resp = await fetch(url, {
            method: metodo,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        const result = await resp.json();

        if (result.sucesso) {
            mostrarAlerta(result.mensagem, "sucesso");
            limparFormulario();
            carregarFuncionarios();
        } else {
            mostrarAlerta(result.mensagem || "Falha ao salvar funcionário.", "erro");
        }
    } catch {
        mostrarAlerta("Erro ao salvar funcionário.", "erro");
    }
});

async function editarFuncionario(cpf) {
    try {
        const resp = await fetch(`${API}/buscar/${cpf}`);
        const dados = await resp.json();

        cpfEditando = dados.func_cpf;
        btnSalvar.textContent = "Atualizar";

        document.getElementById("funcCpf").value = dados.func_cpf;
        document.getElementById("funcNome").value = dados.func_nome;
        document.getElementById("funcSenha").value = dados.func_senha;
        document.getElementById("funcEmail").value = dados.func_email;
        document.getElementById("funcTelefone").value = dados.func_telefone;

        document.getElementById("username").value = dados.username || "";
        document.getElementById("cargo").value = dados.cargo || "";
        document.getElementById("tipoAcesso").value = dados.tipo_acesso;

        document.getElementById("rua").value = dados.rua || "";
        document.getElementById("bairro").value = dados.bairro || "";
        document.getElementById("cidade").value = dados.cidade || "";
        document.getElementById("uf").value = dados.uf || "";
        document.getElementById("cep").value = dados.cep || "";

        document.getElementById("dataAdmissao").value = dados.data_admissao || "";
        document.getElementById("dataDemissao").value = dados.data_demissao || "";
    } catch {
        mostrarAlerta("Erro ao carregar dados.", "erro");
    }
}

async function excluirFuncionario(cpf) {
    if (!confirm("Deseja excluir este funcionário?")) return;

    try {
        const resp = await fetch(`${API}/excluir/${cpf}`, { method: "DELETE" });
        const result = await resp.json();

        if (result.sucesso) {
            mostrarAlerta(result.mensagem, "sucesso");
            carregarFuncionarios();
        } else {
            mostrarAlerta(result.mensagem || "Falha ao excluir funcionário.", "erro");
        }
    } catch {
        mostrarAlerta("Erro ao excluir.", "erro");
    }
}

campoBusca.addEventListener("input", () => {
    const termo = campoBusca.value.trim();

    if (termo === "") {
        renderTabelaFuncionarios(funcionariosCache);
        return;
    }

    const termoNormal = normalizarTexto(termo);
    const termoCpf = limparCPF(termo);

    const filtrados = funcionariosCache.filter(f => {
        const nomeNormal = normalizarTexto(f.func_nome || "");
        const cpfNormal = limparCPF(f.func_cpf || "");

        return (
            nomeNormal.includes(termoNormal) ||
            cpfNormal.includes(termoCpf)
        );
    });

    renderTabelaFuncionarios(filtrados);
});

carregarFuncionarios();
