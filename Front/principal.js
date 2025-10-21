document.addEventListener('DOMContentLoaded', async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const emailEmpresa = urlParams.get('emailEmpresa');
  const logoContainer = document.getElementById('logoEmpresa');

  if (emailEmpresa && logoContainer) {
    try {
      const response = await fetch(`http://localhost:8080/salf/param?email=${encodeURIComponent(emailEmpresa)}`);
      if (response.ok) {
        const empresa = await response.json();
        if (empresa.logotipo && Array.isArray(empresa.logotipo)) {
          const byteArray = new Uint8Array(empresa.logotipo);
          const blob = new Blob([byteArray], { type: 'image/png' });
          const reader = new FileReader();
          reader.onloadend = () => {
            const img = document.createElement('img');
            img.src = reader.result;
            img.alt = 'Logo da Empresa';
            img.classList.add('logo-dinamica');
            logoContainer.innerHTML = '';
            logoContainer.appendChild(img);
          };
          reader.readAsDataURL(blob);
        }
      }
    } catch (err) {
      console.warn('Erro ao carregar a logo da empresa:', err);
    }
  }

  const btnMenuCompras = document.getElementById('btn-entrada-compra');
  if (btnMenuCompras) {
    btnMenuCompras.addEventListener('click', carregarCompras);
  }
});

async function carregarCompras(e) {
  if (e) e.preventDefault();

  // Mostrar a aba corretamente
  showTab('entrada-compra');

  // Obter referência correta para a aba de entrada de compras
  const tabEntradaCompra = document.getElementById('entrada-compra');
  tabEntradaCompra.innerHTML = '<p>Carregando compras...</p>';

  try {
    const res = await fetch('http://localhost:8080/apis/compra');
    const compras = await res.json();

    if (!Array.isArray(compras) || compras.length === 0 || compras[0].erro) {
      tabEntradaCompra.innerHTML = '<p>Nenhuma compra encontrada.</p>';
      return;
    }

    let html = `<h2>Compras Realizadas</h2><table border="1" cellpadding="10">
                  <tr>
                    <th>ID</th>
                    <th>Descrição</th>
                    <th>Fornecedor</th>
                    <th>Data</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>`;

    compras.forEach(c => {
      html += `<tr id="linha-compra-${c.id}">
        <td>${c.id}</td>
        <td>${c.descricao}</td>
        <td>${c.fornecedorNome}</td>
        <td>${c.data}</td>
        <td>R$ ${parseFloat(c.valorTotal).toFixed(2)}</td>
        <td>${c.confirmada ? '✅ Confirmada' : '⏳ Pendente'}</td>
        <td><button onclick="exibirItensCompra(${c.id}, ${c.confirmada})">Exibir Itens</button></td>
      </tr>`;
    });

    html += '</table>';
    tabEntradaCompra.innerHTML = html;

  } catch (err) {
    tabEntradaCompra.innerHTML = '<p>Erro ao carregar compras.</p>';
    console.error(err);
  }
}


window.exibirItensCompra = async function (compraId, confirmada) {
  showTab('entrada-compra');
  const tabEntradaCompra = document.getElementById('entrada-compra');
  tabEntradaCompra.innerHTML = `<p>Carregando itens da compra #${compraId}...</p>`;

  window.__compraIdAtual = compraId;

  try {
    const resCompra = await fetch(`http://localhost:8080/apis/compra/${compraId}`);
    window.__compraAtual = await resCompra.json();

    const res = await fetch(`http://localhost:8080/apis/compra/${compraId}/produtos`);
    const produtos = await res.json();

    if (!Array.isArray(produtos) || produtos.length === 0) {
      tabEntradaCompra.innerHTML = '<p>Esta compra não possui itens.</p>';
      return;
    }

    let html = `<h2>Itens da Compra #${compraId}</h2>
                <table>
                  <tr>
                    <th>Produto</th>
                    <th>Quantidade</th>
                    <th>Valor Unitário</th>
                    <th>Subtotal</th>
                    <th>Faltando</th>
                    <th>Ações</th>
                  </tr>`;

    let todosCompletos = true;

    produtos.forEach((p, index) => {
      const faltando = p.faltando ?? 0;
      if (faltando > 0) todosCompletos = false;

      html += `<tr id="linha-produto-${index}">
        <td>${p.prod_desc}</td>
        <td><input type="number" min="1" value="${p.cpc_qtd}" onchange="atualizarQuantidade(${index}, this.value)"></td>
        <td><input type="number" step="0.01" min="0" value="${parseFloat(p.cpc_valorcompra).toFixed(2)}" onchange="atualizarValorUnitario(${index}, this.value)"></td>
        <td id="subtotal-${index}">R$ ${(p.cpc_qtd * p.cpc_valorcompra).toFixed(2)}</td>
        <td style="text-align:center;">${faltando}</td>
        <td><button class="btn-acao" onclick="removerProduto(${index})">🗑️</button></td>
      </tr>`;
    });

    html += `</table><br><div class="acoes-container">`;

    const aindaFalta = produtos.some(p => (p.faltando ?? 0) > 0);
    if (aindaFalta) {
      html += `<button class="btn-acao" onclick="confirmarEntradaCompra(${compraId})">✅ Confirmar Entrada no Estoque</button>`;
    }

    if (confirmada && todosCompletos) {
      html += `<button class="btn-acao" onclick="desfazerEntrada(${compraId})">❌ Desfazer Entrada</button>`;
    }

    html += `<button class="btn-acao voltar" onclick="carregarCompras()">← Voltar</button></div>`;
    tabEntradaCompra.innerHTML = html;
    window.__itensCompraAtual = produtos;

  } catch (err) {
    tabEntradaCompra.innerHTML = '<p>Erro ao carregar os produtos da compra.</p>';
    console.error(err);
  }
};

window.atualizarQuantidade = function (index, novaQtd) {
  novaQtd = parseInt(novaQtd);
  if (isNaN(novaQtd) || novaQtd <= 0) return;
  const produto = window.__itensCompraAtual[index];
  produto.cpc_qtd = novaQtd;
  atualizarSubtotal(index);
};

window.atualizarValorUnitario = function (index, novoValor) {
  novoValor = parseFloat(novoValor);
  if (isNaN(novoValor) || novoValor < 0) return;
  const produto = window.__itensCompraAtual[index];
  produto.cpc_valorcompra = novoValor;
  atualizarSubtotal(index);
};

function atualizarSubtotal(index) {
  const produto = window.__itensCompraAtual[index];
  const subtotal = (produto.cpc_qtd * produto.cpc_valorcompra).toFixed(2);
  document.getElementById(`subtotal-${index}`).innerText = `R$ ${subtotal}`;
}

window.removerProduto = async function (index) {
  const produto = window.__itensCompraAtual[index];
  const confirmacao = confirm(`Deseja remover o item "${produto.prod_desc}" da compra?`);
  if (!confirmacao) return;

  try {
    const resposta = await fetch('http://localhost:8080/apis/compra/item', {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        lcCod: produto.lc_cod,
        prodCod: produto.prod_cod,
        cotCod: produto.cot_cod,
        fornCod: produto.forn_cod,
        compraCod: window.__compraIdAtual
      })
    });

    const resultado = await resposta.json();

    if (resposta.ok) {
      alert('✅ Item excluído com sucesso.');
      exibirItensCompra(window.__compraIdAtual, false);
    } else {
      throw new Error(resultado.erro || 'Erro desconhecido ao excluir.');
    }

  } catch (err) {
    alert(`Erro ao excluir item: ${err.message}`);
    console.error(err);
  }
};

window.confirmarEntradaCompra = async function (compraId) {
  if (!compraId) {
    alert('ID da compra não informado. Tente novamente.');
    return;
  }

  const contentDiv = document.querySelector('.content');
  const itens = window.__itensCompraAtual;
  const dadosCompra = window.__compraAtual;

  try {
    for (const p of itens) {
      if ((p.faltando ?? 0) > 0 && p.cpc_qtd > p.faltando) {
        throw new Error(`A quantidade de "${p.prod_desc}" excede o necessário (faltam apenas ${p.faltando}).`);
      }
    }

    const res = await fetch('http://localhost:8080/apis/entrada/confirmarEstoque', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        compraId: compraId,
        itens: itens.map(p => ({
          prodCod: p.prod_cod,
          qtd: Math.min(p.cpc_qtd, p.faltando ?? p.cpc_qtd),
          valorUnitario: parseFloat(p.cpc_valorcompra),
          lcCod: p.lc_cod,
          cotCod: dadosCompra.cot_forn_cotacao_cot_cod,
          fornCod: dadosCompra.cot_forn_fornecedor_forn_cod,
          faltando: p.faltando ?? 0
        }))
      })
    });

    const resposta = await res.json();

    if (res.ok) {
      let html = `<p style="color:green;">✅ Estoque atualizado com sucesso para a compra #${compraId}</p>`;
      html += `<h3>Itens atualizados no estoque:</h3><ul>`;
      itens.forEach(p => {
        html += `<li>${Math.min(p.cpc_qtd, p.faltando ?? p.cpc_qtd)}x ${p.prod_desc}</li>`;
      });
      html += `</ul><br><button onclick="carregarCompras()">← Voltar</button>`;
      contentDiv.innerHTML = html;
    } else {
      throw new Error(resposta.erro || 'Erro ao confirmar entrada');
    }

  } catch (err) {
    contentDiv.innerHTML = `<p style="color:red;">Erro ao confirmar entrada no estoque: ${err.message}</p>`;
    console.error(err);
  }
};

window.desfazerEntrada = async function (compraId) {
  const contentDiv = document.querySelector('.content');
  const itens = window.__itensCompraAtual;

  try {
    const res = await fetch('http://localhost:8080/apis/compra/desfazerEstoque', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        compraId: compraId,
        itens: itens.map(p => ({
          prodCod: p.prod_cod,
          lcCod: p.lc_cod,
          cotCod: window.__compraAtual.cot_forn_cotacao_cot_cod,
          fornCod: window.__compraAtual.cot_forn_fornecedor_forn_cod
        }))
      })
    });

    const resposta = await res.json();

    if (res.ok) {
      let html = `<p style="color:orange;">🧹 Estoque revertido com sucesso para a compra #${compraId}</p>`;
      html += `<h3>Itens revertidos no estoque:</h3><ul>`;
      itens.forEach(p => {
        html += `<li>${p.cpc_qtd}x ${p.prod_desc}</li>`;
      });
      html += `</ul><br><button onclick="carregarCompras()">← Voltar</button>`;
      contentDiv.innerHTML = html;
    } else {
      throw new Error(resposta.erro || 'Erro ao desfazer entrada');
    }

  } catch (err) {
    contentDiv.innerHTML = `<p style="color:red;">Erro ao desfazer entrada do estoque: ${err.message}</p>`;
    console.error(err);
  }
};
