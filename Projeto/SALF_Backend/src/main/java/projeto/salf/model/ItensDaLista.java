package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ItensDaListaDAO;

import java.util.List;

public class ItensDaLista {

    private Integer produtoProdCod;
    private Integer listaCompraLcCod;
    private String listaCompraFuncionarioFuncCpf;
    private Integer quantidade;

    // campos extras pra exibição
    private String produtoDescr;
    private Integer categoriaProdCod;

    public Integer getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(Integer produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public Integer getListaCompraLcCod() {
        return listaCompraLcCod;
    }

    public void setListaCompraLcCod(Integer listaCompraLcCod) {
        this.listaCompraLcCod = listaCompraLcCod;
    }

    public String getListaCompraFuncionarioFuncCpf() {
        return listaCompraFuncionarioFuncCpf;
    }

    public void setListaCompraFuncionarioFuncCpf(String listaCompraFuncionarioFuncCpf) {
        this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getProdutoDescr() {
        return produtoDescr;
    }

    public void setProdutoDescr(String produtoDescr) {
        this.produtoDescr = produtoDescr;
    }

    public Integer getCategoriaProdCod() {
        return categoriaProdCod;
    }

    public void setCategoriaProdCod(Integer categoriaProdCod) {
        this.categoriaProdCod = categoriaProdCod;
    }

    // =============== DAO ===============

    private static ItensDaListaDAO getDAO() {
        Conexao c = SingletonDB.getConexao();
        return new ItensDaListaDAO(c);
    }

    // =============== Operações ===============

    public static List<ItensDaLista> listarPorLista(Integer lcCod) {
        return getDAO().findByLista(lcCod);
    }

    public static void adicionarOuAtualizarItem(Integer lcCod, Integer produtoCod, Integer quantidade) {
        if (lcCod == null || lcCod <= 0) {
            throw new IllegalArgumentException("Lista inválida.");
        }
        if (produtoCod == null || produtoCod <= 0) {
            throw new IllegalArgumentException("Produto inválido.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        // busca lista pra descobrir o CPF do funcionário
        ListaCompra lista = ListaCompra.buscarPorId(lcCod);
        if (lista == null) {
            throw new IllegalArgumentException("Lista de compras não encontrada.");
        }

        ItensDaLista item = new ItensDaLista();
        item.setListaCompraLcCod(lcCod);
        item.setListaCompraFuncionarioFuncCpf(lista.getFuncionarioFuncCpf());
        item.setProdutoProdCod(produtoCod);
        item.setQuantidade(quantidade);

        getDAO().save(item);
    }

    public static void removerItem(Integer lcCod, Integer produtoCod) {
        if (lcCod == null || produtoCod == null) return;
        getDAO().deleteItem(lcCod, produtoCod);
    }

    public static void removerTodosDeLista(Integer lcCod) {
        if (lcCod == null) return;
        getDAO().deleteByLista(lcCod);
    }
}
