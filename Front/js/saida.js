document.addEventListener('DOMContentLoaded', () => {
    const saidaTab = document.getElementById('saida');

    saidaTab.innerHTML = `
        <div class="container-fluid">
            <h2 class="mb-4">Registrar Saída de Doação para Pessoas Carentes</h2>
            <div class="row">
                <!-- Coluna esquerda: pesquisa -->
                <div class="col-lg-6 border-end">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="funcCodSaida" class="form-label">Código do Funcionário:</label>
                            <input type="number" class="form-control" id="funcCodSaida" placeholder="Ex: 1" />
                            <span id="funcNomeSaida" class="fw-bold text-primary mt-1"></span>
                        </div>
                        <div class="col-md-6">
                            <label for="pcCodSaida" class="form-label">Código da Pessoa Carente:</label>
                            <input type="number" class="form-control" id="pcCodSaida" placeholder="Ex: 1" />
                            <span id="pcNomeSaida" class="fw-bold text-success mt-1"></span>
                        </div>
                    </div>
                    <hr>
                    <h5 class="mb-3">Pesquisar Produtos Disponíveis</h5>
                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label for="filtroIdSaida" class="form-label">ID:</label>
                            <input type="text" class="form-control" id="filtroIdSaida" placeholder="ID do produto" />
                        </div>
                        <div class="col-md-4">
                            <label for="filtroDescricaoSaida" class="form-label">Descrição:</label>
                            <input type="text" class="form-control" id="filtroDescricaoSaida" placeholder="Nome do produto" />
                        </div>
                        <div class="col-md-4">
                            <label for="filtroCategoriaSaida" class="form-label">Categoria:</label>
                            <select class="form-select" id="filtroCategoriaSaida">
                                <option value="">Todas as categorias</option>
                                <option value="Alimentos">Alimentos</option>
                                <option value="Roupas">Roupas</option>
                                <option value="Higiene Pessoal">Higiene Pessoal</option>
                                <option value="Limpeza">Limpeza</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-primary mb-3" onclick="buscarProdutosSaida()">
                        <i class="bi bi-search"></i> Pesquisar
                    </button>
                    <div id="resultadosSaida" class="border rounded p-3 bg-light" style="min-height: 200px; max-height: 400px; overflow-y: auto;"></div>
                </div>

                <!-- Coluna direita: lista de produtos selecionados -->
                <div class="col-lg-6">
                    <h5 class="mb-3">Produtos Selecionados para Saída</h5>
                    <div class="border rounded p-3 bg-light mb-3" style="min-height: 300px; max-height: 400px; overflow-y: auto;">
                        <ul id="listaSaida" class="list-unstyled"></ul>
                    </div>
                    <button class="btn btn-danger btn-lg w-100" onclick="confirmarSaida()">
                        <i class="bi bi-box-arrow-right"></i> Confirmar Saída
                    </button>
                </div>
            </div>
        </div>
    `;

    window.listaProdutosSelecionadosSaida = [];

    // Buscar nome do funcionário
    document.getElementById('funcCodSaida').addEventListener('change', async () => {
        const funcCod = document.getElementById('funcCodSaida').value;
        const spanNome = document.getElementById('funcNomeSaida');
        spanNome.innerText = '...';
        try {
            const res = await fetch(`http://localhost:8080/apis/funcionario/${funcCod}`);
            if (res.ok) {
                const func = await res.json();
                spanNome.innerText = func.nome || '(Nome não encontrado)';
            } else {
                spanNome.innerText = '(Funcionário não encontrado)';
            }
        } catch (err) {
            spanNome.innerText = '(Erro na busca)';
        }
    });

    // Buscar nome da pessoa carente
    document.getElementById('pcCodSaida').addEventListener('change', async () => {
        const pcCod = document.getElementById('pcCodSaida').value;
        const spanNome = document.getElementById('pcNomeSaida');
        spanNome.innerText = '...';
        try {
            const res = await fetch(`http://localhost:8080/apis/pessoa_carente/${pcCod}`);
            if (res.ok) {
                const pc = await res.json();
                spanNome.innerText = pc.pcNome || '(Nome não encontrado)';
            } else {
                spanNome.innerText = '(Pessoa não encontrada)';
            }
        } catch (err) {
            spanNome.innerText = '(Erro na busca)';
        }
    });

    window.buscarProdutosSaida = async function () {
        const filtroId = document.getElementById('filtroIdSaida').value;
        const filtroDescricao = document.getElementById('filtroDescricaoSaida').value.toLowerCase();
        const filtroCategoria = document.getElementById('filtroCategoriaSaida').value;
        const resultadosDiv = document.getElementById('resultadosSaida');
        resultadosDiv.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Carregando...</span></div></div>';

        try {
            const response = await fetch('http://localhost:8080/apis/estoque');
            const produtos = await response.json();

            // Ordenar produtos por ID e guardar no cache
            produtos.sort((a, b) => a.produto.id - b.produto.id);
            window.produtosAtualCache = produtos;

            // Filtrar apenas produtos com estoque > 0
            const filtrados = produtos.filter(p => {
                const temEstoque = p.quantidade > 0;
                const idOk = filtroId === '' || p.produto.id.toString() === filtroId;
                const descOk = filtroDescricao === '' || p.produto.nome.toLowerCase().includes(filtroDescricao);
                const catOk = filtroCategoria === '' || (p.produto.categoria && p.produto.categoria === filtroCategoria);
                return temEstoque && idOk && descOk && catOk;
            });

            if (filtrados.length === 0) {
                resultadosDiv.innerHTML = '<div class="alert alert-warning text-center">Nenhum produto com estoque encontrado.</div>';
                return;
            }

            resultadosDiv.innerHTML = '';
            filtrados.forEach(p => {
                const div = document.createElement('div');
                div.className = 'card mb-2';
                div.innerHTML = `
                    <div class="card-body">
                        <h6 class="card-title">
                            ${p.produto.nome} 
                            <span class="badge bg-secondary">ID: ${p.produto.id}</span>
                            <span class="badge bg-success">Estoque: ${p.quantidade}</span>
                        </h6>
                        <p class="card-text mb-2">
                            <small class="text-muted">Categoria: ${p.produto.categoria || '---'}</small>
                        </p>
                        <div class="row align-items-end">
                            <div class="col-md-6">
                                <label for="qtdSaida_${p.produto.id}" class="form-label">Quantidade:</label>
                                <input type="number" class="form-control" id="qtdSaida_${p.produto.id}" 
                                       min="1" max="${p.quantidade}" value="1" />
                                <small class="text-muted">Máximo: ${p.quantidade}</small>
                            </div>
                            <div class="col-md-6">
                                <button class="btn btn-outline-danger w-100" onclick="adicionarProdutoSaida(${p.produto.id}, '${p.produto.nome}', ${p.quantidade})">
                                    <i class="bi bi-plus-circle"></i> Adicionar
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                resultadosDiv.appendChild(div);
            });
        } catch (err) {
            resultadosDiv.innerHTML = '<div class="alert alert-danger text-center">Erro ao buscar produtos.</div>';
        }
    };

    window.adicionarProdutoSaida = function (id, nome, estoqueDisponivel) {
        const quantidadeInput = document.getElementById(`qtdSaida_${id}`);
        const quantidade = parseInt(quantidadeInput.value);

        if (quantidade > estoqueDisponivel) {
            mostrarAviso(`Quantidade solicitada (${quantidade}) é maior que o estoque disponível (${estoqueDisponivel})`);
            return;
        }

        const existente = listaProdutosSelecionadosSaida.find(p => p.produtoProdCod === id);
        if (existente) {
            const novaQuantidade = existente.doaProdQtd + quantidade;
            if (novaQuantidade > estoqueDisponivel) {
                mostrarAviso(`Quantidade total (${novaQuantidade}) excede o estoque disponível (${estoqueDisponivel})`);
                return;
            }
            existente.doaProdQtd = novaQuantidade;
        } else {
            // Buscar a categoria do produto da lista atual
            const produtoAtual = window.produtosAtualCache?.find(p => p.produto.id === id);
            const categoria = produtoAtual?.produto?.categoria;

            // Mapear categoria para código
            const categoriaMap = {
                'Alimentos': 1,
                'Roupas': 2,
                'Higiene Pessoal': 3,
                'Limpeza': 4
            };

            listaProdutosSelecionadosSaida.push({
                produtoProdCod: id,
                produtoNome: nome,
                doaProdQtd: quantidade,
                estoqueDisponivel: estoqueDisponivel,
                doaProdCatCod: categoriaMap[categoria] || 1
            });
        }
        renderizarListaSaida();
    };

    window.removerProdutoSaida = function (id) {
        listaProdutosSelecionadosSaida = listaProdutosSelecionadosSaida.filter(p => p.produtoProdCod !== id);
        renderizarListaSaida();
    };

    function renderizarListaSaida() {
        const lista = document.getElementById('listaSaida');
        if (listaProdutosSelecionadosSaida.length === 0) {
            lista.innerHTML = '<div class="text-center text-muted">Nenhum produto selecionado</div>';
            return;
        }

        lista.innerHTML = '';
        listaProdutosSelecionadosSaida.forEach(p => {
            const item = document.createElement('li');
            item.className = 'border rounded p-2 mb-2 bg-white';
            item.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${p.produtoNome}</strong>
                        <br>
                        <small class="text-muted">
                            ID: ${p.produtoProdCod} | 
                            Quantidade: ${p.doaProdQtd} | 
                            Disponível: ${p.estoqueDisponivel}
                        </small>
                    </div>
                    <button class="btn btn-outline-danger btn-sm" onclick="removerProdutoSaida(${p.produtoProdCod})">
                        <i class="bi bi-trash"></i> Remover
                    </button>
                </div>
            `;
            lista.appendChild(item);
        });
    }

    window.confirmarSaida = async function () {
        const funcCodInput = document.getElementById('funcCodSaida').value;
        const pcCodInput = document.getElementById('pcCodSaida').value;

        if (!funcCodInput) {
            mostrarAviso('Por favor, informe o código do funcionário.');
            return;
        }

        if (!pcCodInput) {
            mostrarAviso('Por favor, informe o código da pessoa carente.');
            return;
        }

        if (listaProdutosSelecionadosSaida.length === 0) {
            mostrarAviso('Por favor, adicione pelo menos um produto.');
            return;
        }

        const payload = {
            funcCod: parseInt(funcCodInput),
            pcCod: parseInt(pcCodInput),
            produtos: listaProdutosSelecionadosSaida.map(p => ({
                produtoProdCod: p.produtoProdCod,
                doaProdQtd: p.doaProdQtd,
                doaProdCatCod: p.doaProdCatCod || 1  // Adicionar categoria se não tiver
            }))
        };

        try {
            const response = await fetch('http://localhost:8080/apis/doacao_pc/registrar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorText = await response.text();
                mostrarErro('Erro HTTP: ' + response.status + ' - ' + errorText);
                return;
            }

            const result = await response.json();
            console.log('Resposta da API:', result); // Para debug

            if (result.status === 'ok') {
                mostrarSucesso('Saída de doação registrada com sucesso!');
                listaProdutosSelecionadosSaida = [];
                renderizarListaSaida();
                // Limpar formulário
                document.getElementById('funcCodSaida').value = '';
                document.getElementById('pcCodSaida').value = '';
                document.getElementById('funcNomeSaida').innerText = '';
                document.getElementById('pcNomeSaida').innerText = '';
                document.getElementById('resultadosSaida').innerHTML = '';
            } else {
                mostrarErro('Erro ao registrar saída: ' + (result.mensagem || 'Erro desconhecido'));
                console.error('Erro da API:', result);
            }
        } catch (err) {
            mostrarErro('Erro na requisição: ' + err.message);
            console.error('Erro catch:', err);
        }
    };

    // Inicializar lista vazia
    renderizarListaSaida();
});