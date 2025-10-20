document.addEventListener('DOMContentLoaded', () => {
    const listaEntradaTab = document.getElementById('lista-entrada');
    listaEntradaTab.innerHTML = `
        <div class="container-fluid">
            <h2 class="mb-4">Lista de Doações</h2>
            <div class="row mb-4">
                <div class="col-md-4">
                    <label for="filtroFunc" class="form-label">Funcionário (ID):</label>
                    <input type="number" class="form-control" id="filtroFunc" placeholder="Ex: 2">
                </div>
                <div class="col-md-4">
                    <label for="filtroDoacao" class="form-label">ID da Doação:</label>
                    <input type="number" class="form-control" id="filtroDoacao" placeholder="Ex: 10">
                </div>
                <div class="col-md-4">
                    <button class="btn btn-primary w-100" onclick="carregarDoacoes()" style="margin-top: 2rem;">
                        <i class="bi bi-funnel"></i> Aplicar Filtros
                    </button>
                </div>
            </div>
            <div id="doacoesContainer"></div>
        </div>
    `;

    const funcCache = {};

    window.carregarDoacoes = async function () {
        const container = document.getElementById('doacoesContainer');
        container.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Carregando...</span></div></div>';

        const filtroFunc = document.getElementById('filtroFunc').value;
        const filtroDoacao = document.getElementById('filtroDoacao').value;

        let url = 'http://localhost:8080/apis/doacoes';
        if (filtroFunc) {
            url = `http://localhost:8080/apis/doacoes/funcionario/${filtroFunc}`;
        }

        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Erro ao buscar Doações');
            let doacoes = await response.json();

            // Aplicar filtro por ID da doação, se existir
            if (filtroDoacao) {
                doacoes = doacoes.filter(d => d.doacaoCod.toString() === filtroDoacao);
            }

            // Ordenar doações da mais recente para a mais antiga (maior para menor)
            doacoes.sort((a, b) => b.doacaoCod - a.doacaoCod);

            if (!doacoes.length) {
                container.innerHTML = '<div class="alert alert-info text-center">Nenhuma doação encontrada com os filtros aplicados.</div>';
                return;
            }

            container.innerHTML = '';
            for (const d of doacoes) {
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

                const div = document.createElement('div');
                div.className = 'card mb-3';
                div.innerHTML = `
                    <div class="card-header d-flex justify-content-between align-items-center bg-light">
                        <div>
                            <h5 class="mb-0">
                                <span class="badge bg-success">Entrada #${d.doacaoCod}</span>
                                <small class="text-muted ms-2">${d.data || '---'}</small>
                            </h5>
                        </div>
                        <button 
                            class="btn btn-outline-danger btn-sm" 
                            onclick="deletarDoacao(${d.doacaoCod})"
                            title="Deletar doação (reverte estoque)"
                        >
                            <i class="bi bi-trash"></i> Deletar
                        </button>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <strong>Funcionário:</strong> ${nomeFunc} 
                                <span class="badge bg-secondary">ID: ${d.funcCod}</span>
                            </div>
                        </div>
                        <strong>Produtos doados:</strong>
                        <ul class="list-group list-group-flush mt-2">
                            ${d.produtos.map(p => `
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    ${p.descricao || 'Produto'}
                                    <span class="badge bg-success rounded-pill">${p.quantidade}</span>
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
    window.deletarDoacao = async function(doaCod) {
        if (!confirm(`Tem certeza que deseja deletar a doação #${doaCod}? Esta ação não pode ser desfeita e o estoque será ajustado.`)) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/apis/doacoes/${doaCod}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                mostrarSucesso('Doação deletada com sucesso!');
                // Recarrega a lista para atualizar
                carregarDoacoes();
            } else {
                const errorText = await response.text();
                mostrarErro('Erro ao deletar doação: ' + errorText);
            }
        } catch (err) {
            mostrarErro('Erro na requisição: ' + err.message);
        }
    };

    // Carrega a lista completa ao abrir a aba
    if (!document.hidden) {
        carregarDoacoes();
    }
});