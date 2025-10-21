document.addEventListener('DOMContentLoaded', () => {
    const entradaTab = document.getElementById('entrada');

    entradaTab.innerHTML = `
        <div class="container-fluid">
            <h2 class="mb-4">Registrar Entrada de Doações</h2>
            <div class="row">
                <!-- Coluna esquerda: pesquisa -->
                <div class="col-lg-6 border-end">
                    <div class="mb-3">
                        <label for="funcCod" class="form-label">Código do Funcionário:</label>
                        <input type="number" class="form-control" id="funcCod" placeholder="Ex: 1" />
                        <span id="funcNome" class="fw-bold text-primary mt-1"></span>
                    </div>
                    <hr>
                    <h5 class="mb-3">Pesquisar Produtos</h5>
                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label for="filtroId" class="form-label">ID:</label>
                            <input type="text" class="form-control" id="filtroId" placeholder="ID do produto" />
                        </div>
                        <div class="col-md-4">
                            <label for="filtroDescricao" class="form-label">Descrição:</label>
                            <input type="text" class="form-control" id="filtroDescricao" placeholder="Nome do produto" />
                        </div>
                        <div class="col-md-4">
                            <label for="filtroCategoria" class="form-label">Categoria:</label>
                            <select class="form-select" id="filtroCategoria">
                                <option value="">Todas as categorias</option>
                                <option value="Alimentos">Alimentos</option>
                                <option value="Roupas">Roupas</option>
                                <option value="Higiene Pessoal">Higiene Pessoal</option>
                                <option value="Limpeza">Limpeza</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-primary mb-3" onclick="buscarProdutos()">
                        <i class="bi bi-search"></i> Pesquisar
                    </button>
                    <div id="resultados" class="border rounded p-3 bg-light" style="min-height: 200px; max-height: 400px; overflow-y: auto;"></div>
                </div>

                <!-- Coluna direita: lista de doações -->
                <div class="col-lg-6">
                    <h5 class="mb-3">Produtos Selecionados</h5>
                    <div class="border rounded p-3 bg-light mb-3" style="min-height: 300px; max-height: 400px; overflow-y: auto;">
                        <ul id="listaDoacao" class="list-unstyled"></ul>
                    </div>
                    <button class="btn btn-success btn-lg w-100" onclick="confirmarEntrada()">
                        <i class="bi bi-check-circle"></i> Confirmar Entrada
                    </button>
                </div>
            </div>
        </div>
    `;

    window.listaProdutosSelecionados = [];

    document.getElementById('funcCod').addEventListener('change', async () => {
        const funcCod = document.getElementById('funcCod').value;
        const spanNome = document.getElementById('funcNome');
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

    window.buscarProdutos = async function () {
        const filtroId = document.getElementById('filtroId').value;
        const filtroDescricao = document.getElementById('filtroDescricao').value.toLowerCase();
        const filtroCategoria = document.getElementById('filtroCategoria').value;
        const resultadosDiv = document.getElementById('resultados');
        resultadosDiv.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Carregando...</span></div></div>';

        try {
            const response = await fetch('http://localhost:8080/apis/estoque');
            const produtos = await response.json();

            // Ordenar produtos por ID
            produtos.sort((a, b) => a.produto.id - b.produto.id);

            const filtrados = produtos.filter(p => {
                const idOk = filtroId === '' || p.produto.id.toString() === filtroId;
                const descOk = filtroDescricao === '' || p.produto.nome.toLowerCase().includes(filtroDescricao);
                const catOk = filtroCategoria === '' || (p.produto.categoria && p.produto.categoria === filtroCategoria);
                return idOk && descOk && catOk;
            });

            if (filtrados.length === 0) {
                resultadosDiv.innerHTML = '<div class="alert alert-info text-center">Nenhum produto encontrado.</div>';
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
                            <span class="badge bg-info">Estoque: ${p.quantidade}</span>
                        </h6>
                        <p class="card-text mb-2">
                            <small class="text-muted">Categoria: ${p.produto.categoria || '---'}</small>
                        </p>
                        <div class="row align-items-end">
                            <div class="col-md-6">
                                <label for="qtd_${p.produto.id}" class="form-label">Quantidade:</label>
                                <input type="number" class="form-control" id="qtd_${p.produto.id}" min="1" value="1" />
                            </div>
                            <div class="col-md-6">
                                <button class="btn btn-outline-success w-100" onclick="adicionarProduto(${p.produto.id}, '${p.produto.nome}')">
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

    window.adicionarProduto = function (id, nome) {
        const quantidade = parseInt(document.getElementById(`qtd_${id}`).value);
        const existente = listaProdutosSelecionados.find(p => p.produtoProdCod === id);
        if (existente) {
            existente.doaProdQtd += quantidade;
        } else {
            listaProdutosSelecionados.push({
                produtoProdCod: id,
                produtoNome: nome,
                doaProdQtd: quantidade
            });
        }
        renderizarLista();
    };

    window.removerProduto = function (id) {
        listaProdutosSelecionados = listaProdutosSelecionados.filter(p => p.produtoProdCod !== id);
        renderizarLista();
    };

    function renderizarLista() {
        const lista = document.getElementById('listaDoacao');
        if (listaProdutosSelecionados.length === 0) {
            lista.innerHTML = '<div class="text-center text-muted">Nenhum produto selecionado</div>';
            return;
        }

        lista.innerHTML = '';
        listaProdutosSelecionados.forEach(p => {
            const item = document.createElement('li');
            item.className = 'border rounded p-2 mb-2 bg-white';
            item.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${p.produtoNome}</strong>
                        <br>
                        <small class="text-muted">ID: ${p.produtoProdCod} | Quantidade: ${p.doaProdQtd}</small>
                    </div>
                    <button class="btn btn-outline-danger btn-sm" onclick="removerProduto(${p.produtoProdCod})">
                        <i class="bi bi-trash"></i> Remover
                    </button>
                </div>
            `;
            lista.appendChild(item);
        });
    }

    window.confirmarEntrada = async function () {
        const funcCodInput = document.getElementById('funcCod').value;

        if (!funcCodInput) {
            mostrarAviso('Por favor, informe o código do funcionário.');
            return;
        }

        if (listaProdutosSelecionados.length === 0) {
            mostrarAviso('Por favor, adicione pelo menos um produto.');
            return;
        }

        const payload = {
            funcCod: parseInt(funcCodInput),
            produtos: listaProdutosSelecionados
        };

        try {
            const response = await fetch('http://localhost:8080/apis/doacoes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                mostrarSucesso('Doação registrada com sucesso!');
                listaProdutosSelecionados = [];
                renderizarLista();
                // Limpar formulário
                document.getElementById('funcCod').value = '';
                document.getElementById('funcNome').innerText = '';
                document.getElementById('resultados').innerHTML = '';
            } else {
                const error = await response.text();
                mostrarErro('Erro ao registrar doação: ' + error);
            }
        } catch (err) {
            mostrarErro('Erro na requisição: ' + err.message);
        }
    };

    // Inicializar lista vazia
    renderizarLista();
});