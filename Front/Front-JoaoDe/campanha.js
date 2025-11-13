document.addEventListener('DOMContentLoaded', () => {
    const API_URL = 'http://localhost:8080/campanha';
    const tableBody = document.getElementById('campanhaTableBody');
    const modal = new bootstrap.Modal(document.getElementById('campanhaModal'));
    const form = document.getElementById('campanhaForm');
    const modalTitle = document.getElementById('campanhaModalLabel');
    const formMessage = document.getElementById('formMessage');
    const voluntariosContainer = document.getElementById('voluntariosContainer');
    const statusField = document.getElementById('statusField');
    
    let todosVoluntarios = [];

    // Função para buscar e listar todos os voluntários ativos
    async function carregarVoluntarios() {
        try {
            const response = await fetch(`${API_URL}/voluntarios/ativos`);
            todosVoluntarios = await response.json();
        } catch (error) {
            console.error('Erro ao carregar voluntários:', error);
            alert('Erro ao carregar a lista de voluntários.');
        }
    }

    // Função para renderizar os voluntários no modal
    function renderizarVoluntarios(voluntariosDaCampanha = []) {
        voluntariosContainer.innerHTML = '';
        
        todosVoluntarios.forEach(voluntario => {
            const vinculoExistente = voluntariosDaCampanha.find(cv => cv.cpfVoluntario === voluntario.cpf);
            const cargo = vinculoExistente ? vinculoExistente.cargoCampanha : '';

            const div = document.createElement('div');
            div.className = 'form-check mb-2';
            div.innerHTML = `
                <input class="form-check-input voluntario-checkbox" type="checkbox" value="${voluntario.cpf}" id="voluntario-${voluntario.cpf}" ${vinculoExistente ? 'checked' : ''}>
                <label class="form-check-label" for="voluntario-${voluntario.cpf}">
                    ${voluntario.nome} (${voluntario.cpf})
                </label>
                <input type="text" class="form-control form-control-sm mt-1 cargo-input" data-cpf="${voluntario.cpf}" placeholder="Cargo na Campanha (Obrigatório se selecionado)" value="${cargo}" ${vinculoExistente ? '' : 'disabled'}>
            `;
            voluntariosContainer.appendChild(div);
        });

        // Adiciona listeners para habilitar/desabilitar o campo de cargo
        document.querySelectorAll('.voluntario-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', (e) => {
                const cpf = e.target.value;
                const cargoInput = document.querySelector(`.cargo-input[data-cpf="${cpf}"]`);
                cargoInput.disabled = !e.target.checked;
                if (!e.target.checked) {
                    cargoInput.value = '';
                }
            });
        });
    }

    // Função para buscar e listar todas as campanhas
    async function listarCampanhas() {
        try {
            const response = await fetch(API_URL);
            const campanhas = await response.json();
            
            tableBody.innerHTML = '';
            
            campanhas.forEach(campanha => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = campanha.id;
                row.insertCell().textContent = campanha.nome;
                row.insertCell().textContent = campanha.dataInicio;
                row.insertCell().textContent = campanha.dataFim;
                
                const statusCell = row.insertCell();
                const statusBadge = document.createElement('span');
                statusBadge.className = `status-badge status-${campanha.status.replace(/\s/g, '-')}`;
                statusBadge.textContent = campanha.status;
                statusCell.appendChild(statusBadge);
                
                const actionsCell = row.insertCell();
                
                // Botão Editar
                const editButton = document.createElement('button');
                editButton.className = 'btn btn-sm btn-warning me-2';
                editButton.innerHTML = '<i class="bi bi-pencil"></i>';
                editButton.onclick = () => preencherFormulario(campanha);
                actionsCell.appendChild(editButton);
                
                // Botão Inativar (Excluir Lógico)
                const deleteButton = document.createElement('button');
                deleteButton.className = 'btn btn-sm btn-danger';
                deleteButton.innerHTML = '<i class="bi bi-trash"></i>';
                deleteButton.onclick = () => inativarCampanha(campanha.id, campanha.nome);
                actionsCell.appendChild(deleteButton);
            });
        } catch (error) {
            console.error('Erro ao buscar campanhas:', error);
            alert('Erro ao carregar a lista de campanhas.');
        }
    }

    // Função para preencher o formulário para edição
    async function preencherFormulario(campanha) {
        modalTitle.textContent = 'Editar Campanha';
        document.getElementById('campanhaId').value = campanha.id;
        document.getElementById('nome').value = campanha.nome;
        document.getElementById('dataInicio').value = campanha.dataInicio;
        document.getElementById('dataFim').value = campanha.dataFim;
        document.getElementById('observacao').value = campanha.observacao || '';
        document.getElementById('status').value = campanha.status;
        statusField.style.display = 'block'; // Mostra o campo de status na edição

        // Carregar voluntários vinculados
        try {
            const response = await fetch(`${API_URL}/${campanha.id}/voluntarios`);
            const voluntariosDaCampanha = await response.json();
            renderizarVoluntarios(voluntariosDaCampanha);
        } catch (error) {
            console.error('Erro ao carregar voluntários da campanha:', error);
            renderizarVoluntarios([]);
        }

        formMessage.classList.add('d-none');
        modal.show();
    }

    // Limpar formulário ao abrir para novo cadastro
    document.getElementById('campanhaModal').addEventListener('show.bs.modal', event => {
        if (event.relatedTarget && event.relatedTarget.id !== 'editButton') {
            modalTitle.textContent = 'Cadastrar Campanha';
            form.reset();
            document.getElementById('campanhaId').value = '';
            statusField.style.display = 'none'; // Esconde o campo de status no cadastro
            renderizarVoluntarios([]); // Limpa os voluntários
            formMessage.classList.add('d-none');
        }
    });

    // Submissão do formulário (Cadastro ou Edição)
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const id = document.getElementById('campanhaId').value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_URL}/${id}` : API_URL;
        
        const campanha = {
            id: id ? parseInt(id) : null,
            nome: document.getElementById('nome').value,
            dataInicio: document.getElementById('dataInicio').value,
            dataFim: document.getElementById('dataFim').value,
            observacao: document.getElementById('observacao').value,
            status: id ? document.getElementById('status').value : 'Em Andamento',
            ativo: true
        };

        try {
            // 1. Salvar a Campanha
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(campanha)
            });

            const result = await response.json();
            
            if (response.ok) {
                // Se for um novo cadastro, o ID é retornado na mensagem (assumindo que o backend retorna o ID)
                const campanhaId = id || result.id; 
                
                // 2. Vincular Voluntários
                const voluntariosVinculados = [];
                document.querySelectorAll('.voluntario-checkbox:checked').forEach(checkbox => {
                    const cpf = checkbox.value;
                    const cargoInput = document.querySelector(`.cargo-input[data-cpf="${cpf}"]`);
                    
                    // Regra de Negócio: Cargo é obrigatório se o voluntário for selecionado
                    if (!cargoInput.value.trim()) {
                        throw new Error(`O cargo para o voluntário ${document.querySelector(`label[for="voluntario-${cpf}"]`).textContent.split('(')[0].trim()} é obrigatório.`);
                    }

                    voluntariosVinculados.push({
                        idCampanha: campanhaId,
                        cpfVoluntario: cpf,
                        cargoCampanha: cargoInput.value.trim()
                    });
                });

                const voluntariosResponse = await fetch(`${API_URL}/${campanhaId}/voluntarios`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(voluntariosVinculados)
                });

                const voluntariosResult = await voluntariosResponse.json();

                if (voluntariosResponse.ok) {
                    formMessage.textContent = result.mensagem + " " + voluntariosResult.mensagem;
                    formMessage.classList.remove('d-none', 'alert-danger');
                    formMessage.classList.add('alert-success');
                    listarCampanhas(); // Atualiza a lista
                    setTimeout(() => modal.hide(), 1500); 
                } else {
                    // Se a campanha salvou, mas o vínculo falhou, exibe a mensagem de erro do vínculo
                    throw new Error(voluntariosResult.mensagem);
                }
                
            } else {
                formMessage.textContent = result.mensagem;
                formMessage.classList.remove('d-none', 'alert-success');
                formMessage.classList.add('alert-danger');
            }

        } catch (error) {
            console.error('Erro ao salvar campanha:', error);
            formMessage.textContent = error.message || 'Erro de comunicação com o servidor.';
            formMessage.classList.remove('d-none', 'alert-success');
            formMessage.classList.add('alert-danger');
        }
    });

    // Função para inativar (Excluir Lógico)
    async function inativarCampanha(id, nome) {
        if (confirm(`Tem certeza que deseja inativar a campanha "${nome}"?`)) {
            try {
                const response = await fetch(`${API_URL}/${id}`, {
                    method: 'DELETE'
                });

                const result = await response.json();
                
                if (response.ok) {
                    alert(result.mensagem);
                    listarCampanhas(); // Atualiza a lista
                } else {
                    alert(`Erro ao inativar: ${result.mensagem}`);
                }
            } catch (error) {
                console.error('Erro ao inativar campanha:', error);
                alert('Erro de comunicação com o servidor ao inativar.');
            }
        }
    }

    // Inicializa a listagem e carrega os voluntários
    carregarVoluntarios().then(listarCampanhas);
});
