document.addEventListener('DOMContentLoaded', () => {
    const listaSaidaTab = document.getElementById('lista-saida');
    listaSaidaTab.innerHTML = `
        <div class="container-fluid">
            <h2 class="mb-4">Lista de Doações para Pessoas Carentes</h2>
            <div class="row mb-4">
                <div class="col-md-3">
                    <label for="filtroFuncSaida" class="form-label">Funcionário (ID):</label>
                    <input type="number" class="form-control" id="filtroFuncSaida" placeholder="Ex: 2">
                </div>
                <div class="col-md-3">
                    <label for="filtroPcSaida" class="form-label">Pessoa Carente (ID):</label>
                    <input type="number" class="form-control" id="filtroPcSaida" placeholder="Ex: 1">
                </div>
                <div class="col-md-3">
                    <label for="filtroIdSaida" class="form-label">ID da Doação:</label>
                    <input type="number" class="form-control" id="filtroIdSaida" placeholder="Ex: 10">
                </div>
                <div class="col-md-3">
                    <button class="btn btn-primary w-100" onclick="carregarDoacoesSaida()" style="margin-top: 2rem;">
                        <i class="bi bi-funnel"></i> Aplicar Filtros
                    </button>
                </div>
            </div>
            <div id="doacoesSaidaContainer"></div>
        </div>
    `;

    const funcCache = {};
    const pcCache = {};

    window.carregarDoacoesSaida = async function () {
        console.log('=== FUNÇÃO CHAMADA ==='); // Teste básico

        const container = document.getElementById('doacoesSaidaContainer');
        container.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Carregando...</span></div></div>';

        const filtroFuncElement = document.getElementById('filtroFuncSaida');
        const filtroPcElement = document.getElementById('filtroPcSaida');
        const filtroIdElement = document.getElementById('filtroIdSaida');

        console.log('Elementos encontrados:', {
            func: filtroFuncElement,
            pc: filtroPcElement,
            id: filtroIdElement
        });

        const filtroFunc = filtroFuncElement?.value || '';
        const filtroPc = filtroPcElement?.value || '';
        const filtroId = filtroIdElement?.value || '';

        console.log('Filtros:', { filtroFunc, filtroPc, filtroId }); // Debug

        let url = 'http://localhost:8080/apis/doacao_pc/listar';

        // Se tem filtro específico, usar endpoint correspondente
        if (filtroId) {
            // Buscar por ID específico primeiro
            url = `http://localhost:8080/apis/doacao_pc/${filtroId}`;
        } else if (filtroFunc) {
            url = `http://localhost:8080/apis/doacao_pc/funcionario/${filtroFunc}`;
        } else if (filtroPc) {
            url = `http://localhost:8080/apis/doacao_pc/pessoa_carente/${filtroPc}`;
        }

        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Erro ao buscar doações');
            let doacoes = await response.json();

            // Aplicar filtro por ID da doação, se existir
            if (filtroId) {
                doacoes = doacoes.filter(d => d.doapcCod.toString() === filtroId);
            }

            // Ordenar doações da mais recente para a mais antiga
            doacoes.sort((a, b) => b.doapcCod - a.doapcCod);

            if (!doacoes.length) {
                container.innerHTML = '<div class="alert alert-info text-center">Nenhuma doação encontrada com os filtros aplicados.</div>';
                return;
            }

            container.innerHTML = '';
            for (const d of doacoes) {
                // Buscar nome do funcionário
                let nomeFunc = '...';
                if (funcCache[d.funcCod]) {
                    nomeFunc = funcCache[d.funcCod];
                } else {
                    try {
                        const fRes = await fetch(`http://localhost:8080/apis/funcionario/${d.funcCod}`);
                        if (fRes.ok) {
                            const fData = await fRes.json();
                            nomeFunc = fData.nome || '(Sem nome)';
                            funcCache[d.funcCod] = nomeFunc;
                        } else {
                            nomeFunc = '(Erro)';
                        }
                    } catch {
                        nomeFunc = '(Erro)';
                    }
                }

                // Buscar nome da pessoa carente
                let nomePc = '...';
                if (pcCache[d.pcCod]) {
                    nomePc = pcCache[d.pcCod];
                } else {
                    try {
                        const pcRes = await fetch(`http://localhost:8080/apis/pessoa_carente/${d.pcCod}`);
                        if (pcRes.ok) {
                            const pcData = await pcRes.json();
                            nomePc = pcData.pcNome || '(Sem nome)';
                            pcCache[d.pcCod] = nomePc;
                        } else {
                            nomePc = '(Erro)';
                        }
                    } catch {
                        nomePc = '(Erro)';
                    }
                }

                const div = document.createElement('div');
                div.className = 'card mb-3';
                div.innerHTML = `
                    <div class="card-header d-flex justify-content-between align-items-center bg-light">
                        <div>
                            <h5 class="mb-0">
                                <span class="badge bg-primary">Saída #${d.doapcCod}</span>
                                <small class="text-muted ms-2">${d.doapcData || '---'}</small>
                            </h5>
                        </div>
                        <button 
                            class="btn btn-outline-danger btn-sm" 
                            onclick="deletarDoacaoSaida(${d.doapcCod})"
                            title="Deletar doação (reverte estoque)"
                        >
                            <i class="bi bi-trash"></i> Deletar
                        </button>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <strong>Funcionário:</strong> ${nomeFunc} 
                                <span class="badge bg-secondary">ID: ${d.funcCod}</span>
                            </div>
                            <div class="col-md-6">
                                <strong>Pessoa Carente:</strong> ${nomePc} 
                                <span class="badge bg-success">ID: ${d.pcCod}</span>
                            </div>
                        </div>
                        <strong>Produtos doados:</strong>
                        <ul class="list-group list-group-flush mt-2">
                            ${d.produtos.map(p => `
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    ${p.descricao || 'Produto'}
                                    <span class="badge bg-primary rounded-pill">${p.quantidade}</span>
                                </li>
                            `).join('')}
                        </ul>
                    </div>
                `;
                container.appendChild(div);
            }
        } catch (err) {
            container.innerHTML = '<div class="alert alert-danger text-center">Erro ao carregar doações.</div>';
            console.error('Erro:', err);
        }
    };

    // Função para deletar doação
    window.deletarDoacaoSaida = async function(doapcCod) {
        if (!confirm(`Tem certeza que deseja deletar a doação #${doapcCod}? Esta ação não pode ser desfeita e o estoque será revertido.`)) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/apis/doacao_pc/excluir/${doapcCod}`, {
                method: 'DELETE'
            });

            const result = await response.json();

            if (result.status === 'ok') {
                mostrarSucesso('Doação deletada com sucesso! Estoque foi revertido.');
                // Recarrega a lista para atualizar
                carregarDoacoesSaida();
            } else {
                mostrarErro('Erro ao deletar doação: ' + result.mensagem);
            }
        } catch (err) {
            mostrarErro('Erro na requisição: ' + err.message);
        }
    };

    // Carrega a lista completa ao abrir a aba
    if (!document.hidden) {
        carregarDoacoesSaida();
    }
});