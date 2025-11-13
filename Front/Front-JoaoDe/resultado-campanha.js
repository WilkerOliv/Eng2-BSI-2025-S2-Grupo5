document.addEventListener('DOMContentLoaded', () => {
    const API_URL = 'http://localhost:8080/resultado-campanha';
    const form = document.getElementById('resultadoForm');
    const campanhaSelect = document.getElementById('campanhaId');
    const formMessage = document.getElementById('formMessage');

    // Função para carregar campanhas finalizadas
    async function carregarCampanhasFinalizadas() {
        try {
            const response = await fetch(`${API_URL}/campanhas-finalizadas`);
            const campanhas = await response.json();
            
            campanhaSelect.innerHTML = '<option value="">Selecione uma Campanha</option>';
            
            if (campanhas.length === 0) {
                campanhaSelect.innerHTML += '<option value="" disabled>Nenhuma campanha finalizada disponível</option>';
            } else {
                campanhas.forEach(campanha => {
                    const option = document.createElement('option');
                    option.value = campanha.id;
                    option.textContent = `${campanha.nome} (ID: ${campanha.id})`;
                    campanhaSelect.appendChild(option);
                });
            }
        } catch (error) {
            console.error('Erro ao carregar campanhas finalizadas:', error);
            exibirMensagem('Erro ao carregar a lista de campanhas.', false);
        }
    }

    // Função para exibir mensagens de feedback
    function exibirMensagem(mensagem, sucesso) {
        formMessage.textContent = mensagem;
        formMessage.classList.remove('d-none', 'alert-success', 'alert-danger');
        if (sucesso) {
            formMessage.classList.add('alert-success');
        } else {
            formMessage.classList.add('alert-danger');
        }
    }

    // Submissão do formulário
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const resultado = {
            idCampanha: parseInt(campanhaSelect.value),
            valorArrecadado: parseFloat(document.getElementById('valorArrecadado').value),
            familiasAtendidas: parseInt(document.getElementById('familiasAtendidas').value),
            produtosArrecadados: parseInt(document.getElementById('produtosArrecadados').value) || 0,
            observacao: document.getElementById('observacao').value
        };

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(resultado)
            });

            const result = await response.json();
            
            if (response.ok) {
                exibirMensagem(result.mensagem, true);
                form.reset();
                carregarCampanhasFinalizadas(); // Recarrega para remover a campanha recém-registrada
            } else {
                exibirMensagem(result.mensagem, false);
            }

        } catch (error) {
            console.error('Erro ao registrar resultado:', error);
            exibirMensagem('Erro de comunicação com o servidor ao registrar resultado.', false);
        }
    });

    // Inicializa o carregamento das campanhas
    carregarCampanhasFinalizadas();
});
