// Função para mostrar/esconder abas
function showTab(tabId) {
    document.querySelectorAll('.tab').forEach(tab => tab.classList.add('hidden'));
    document.getElementById(tabId).classList.remove('hidden');
}

// Sistema de notificações Bootstrap
function mostrarNotificacao(mensagem, tipo = 'success', duracao = 4000) {
    // Criar container de notificações se não existir
    let container = document.getElementById('notificacao-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'notificacao-container';
        container.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            max-width: 400px;
        `;
        document.body.appendChild(container);
    }

    // Determinar classe e ícone baseado no tipo
    const config = {
        success: {
            classe: 'alert-success',
            icone: 'bi-check-circle-fill',
            titulo: 'Sucesso!'
        },
        error: {
            classe: 'alert-danger',
            icone: 'bi-exclamation-triangle-fill',
            titulo: 'Erro!'
        },
        warning: {
            classe: 'alert-warning',
            icone: 'bi-exclamation-circle-fill',
            titulo: 'Atenção!'
        },
        info: {
            classe: 'alert-info',
            icone: 'bi-info-circle-fill',
            titulo: 'Informação'
        }
    };

    const { classe, icone, titulo } = config[tipo] || config.info;

    // Criar elemento da notificação
    const notificacao = document.createElement('div');
    notificacao.className = `alert ${classe} alert-dismissible fade show mb-2`;
    notificacao.style.cssText = `
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        border: none;
        animation: slideInRight 0.3s ease-out;
    `;

    notificacao.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="bi ${icone} me-2"></i>
            <div class="flex-grow-1">
                <strong>${titulo}</strong><br>
                <small>${mensagem}</small>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;

    // Adicionar ao container
    container.appendChild(notificacao);

    // Auto-remover após duração especificada
    setTimeout(() => {
        if (notificacao.parentNode) {
            notificacao.classList.remove('show');
            setTimeout(() => {
                if (notificacao.parentNode) {
                    notificacao.remove();
                }
            }, 150);
        }
    }, duracao);
}

// Adicionar CSS para animação
if (!document.querySelector('#notificacao-styles')) {
    const styles = document.createElement('style');
    styles.id = 'notificacao-styles';
    styles.textContent = `
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
    `;
    document.head.appendChild(styles);
}

// Funções de conveniência (globais)
function mostrarSucesso(mensagem) {
    mostrarNotificacao(mensagem, 'success');
}

function mostrarErro(mensagem) {
    mostrarNotificacao(mensagem, 'error');
}

function mostrarAviso(mensagem) {
    mostrarNotificacao(mensagem, 'warning');
}

function mostrarInfo(mensagem) {
    mostrarNotificacao(mensagem, 'info');
}

// Teste das notificações (remova depois de testar)
document.addEventListener('DOMContentLoaded', () => {

    setTimeout(() => mostrarSucesso('Sistema carregado com sucesso!'), 1000);
});