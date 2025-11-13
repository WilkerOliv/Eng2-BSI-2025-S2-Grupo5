document.addEventListener('DOMContentLoaded', () => {
    const API_URL = 'http://localhost:8080/fornecedor';
    const tableBody = document.getElementById('fornecedorTableBody');
    const modal = new bootstrap.Modal(document.getElementById('fornecedorModal'));
    const form = document.getElementById('fornecedorForm');
    const modalTitle = document.getElementById('fornecedorModalLabel');
    const formMessage = document.getElementById('formMessage');

    // Função para buscar e listar todos os fornecedores
    async function listarFornecedores() {
        try {
            const response = await fetch(API_URL);
            const fornecedores = await response.json();
            
            tableBody.innerHTML = ''; // Limpa a tabela
            
            fornecedores.forEach(fornecedor => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = fornecedor.id;
                row.insertCell().textContent = fornecedor.nome;
                row.insertCell().textContent = fornecedor.cnpj;
                row.insertCell().textContent = fornecedor.telefone;
                row.insertCell().textContent = fornecedor.email;
                
                const actionsCell = row.insertCell();
                
                // Botão Editar
                const editButton = document.createElement('button');
                editButton.className = 'btn btn-sm btn-warning me-2';
                editButton.innerHTML = '<i class="bi bi-pencil"></i>';
                editButton.onclick = () => preencherFormulario(fornecedor);
                actionsCell.appendChild(editButton);
                
                // Botão Inativar (Excluir Lógico)
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.innerHTML = '<i class="bi bi-trash"></i>';
                deleteButton.onclick = () => inativarFornecedor(fornecedor.id, fornecedor.nome);
                actionsCell.appendChild(deleteButton);
            });
        } catch (error) {
            console.error('Erro ao buscar fornecedores:', error);
            alert('Erro ao carregar a lista de fornecedores.');
        }
    }

    // Função para preencher o formulário para edição
    function preencherFormulario(fornecedor) {
        modalTitle.textContent = 'Editar Fornecedor';
        document.getElementById('fornecedorId').value = fornecedor.id;
        document.getElementById('nome').value = fornecedor.nome;
        document.getElementById('cnpj').value = fornecedor.cnpj;
        document.getElementById('telefone').value = fornecedor.telefone;
        document.getElementById('email').value = fornecedor.email;
        document.getElementById('contato').value = fornecedor.contato || '';
        document.getElementById('descricao').value = fornecedor.descricao || '';
        formMessage.classList.add('d-none');
        modal.show();
    }

    // Limpar formulário ao abrir para novo cadastro
    document.getElementById('fornecedorModal').addEventListener('show.bs.modal', event => {
        if (event.relatedTarget && event.relatedTarget.id !== 'editButton') {
            modalTitle.textContent = 'Cadastrar Fornecedor';
            form.reset();
            document.getElementById('fornecedorId').value = '';
            formMessage.classList.add('d-none');
        }
    });

    // Submissão do formulário (Cadastro ou Edição)
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const id = document.getElementById('fornecedorId').value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_URL}/${id}` : API_URL;
        
        const fornecedor = {
            id: id ? parseInt(id) : null,
            nome: document.getElementById('nome').value,
            cnpj: document.getElementById('cnpj').value,
            telefone: document.getElementById('telefone').value,
            email: document.getElementById('email').value,
            contato: document.getElementById('contato').value,
            descricao: document.getElementById('descricao').value,
            ativo: true // Sempre true no cadastro/edição
        };

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(fornecedor)
            });

            const result = await response.json();
            
            formMessage.textContent = result.mensagem;
            formMessage.classList.remove('d-none', 'alert-success', 'alert-danger');
            
            if (response.ok) {
                formMessage.classList.add('alert-success');
                listarFornecedores(); // Atualiza a lista
                // Fecha o modal após um pequeno delay para o usuário ver a mensagem de sucesso
                setTimeout(() => modal.hide(), 1500); 
            } else {
                formMessage.classList.add('alert-danger');
            }

        } catch (error) {
            console.error('Erro ao salvar fornecedor:', error);
            formMessage.textContent = 'Erro de comunicação com o servidor.';
            formMessage.classList.remove('d-none', 'alert-success');
            formMessage.classList.add('alert-danger');
        }
    });

    // Função para inativar (Excluir Lógico)
    async function inativarFornecedor(id, nome) {
        if (confirm(`Tem certeza que deseja inativar o fornecedor "${nome}"?`)) {
            try {
                const response = await fetch(`${API_URL}/${id}`, {
                    method: 'DELETE'
                });

                const result = await response.json();
                
                if (response.ok) {
                    alert(result.mensagem);
                    listarFornecedores(); // Atualiza a lista
                } else {
                    alert(`Erro ao inativar: ${result.mensagem}`);
                }
            } catch (error) {
                console.error('Erro ao inativar fornecedor:', error);
                alert('Erro de comunicação com o servidor ao inativar.');
            }
        }
    }

    // Inicializa a listagem
    listarFornecedores();
});
