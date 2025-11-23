package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.dao.NecessidadeProdutoDAO;

import java.time.LocalDate;
import java.util.List;

public class NecessidadeProduto {

    private Integer necId;
    private String pessoaCpf;
    private LocalDate data;
    private String observacao;
    private Integer produtoCod;
    private Integer quantidade;
    private String pessoaNome;
    private String produtoDescr;
    private String categoriaNome;

    // DTO LISTA
    public static class ListaDTO {
        public String cpf;
        public LocalDate data;
        public String observacao;
        public List<ItemDTO> itens;
    }

    public static class ItemDTO {
        public Integer produtoCod;
        public Integer quantidade;
    }

    private static NecessidadeProdutoDAO getDAO() {
        return new NecessidadeProdutoDAO();
    }

    // === OPERACOES PRINCIPAIS ===

    public static List<NecessidadeProduto> listarAgrupado(Conexao c) {
        return getDAO().listarAgrupado(c);
    }

    public static List<NecessidadeProduto> listarItens(Integer necId, Conexao c) {
        return getDAO().listarItens(necId, c);
    }

    public static Integer criarLista(ListaDTO dto, Conexao c) {
        return getDAO().criarLista(dto, c);
    }

    public static void atualizarLista(Integer necId, ListaDTO dto, Conexao c) {
        getDAO().atualizarLista(necId, dto, c);
    }

    public static void excluirLista(Integer necId, Conexao c) {
        getDAO().excluirLista(necId, c);
    }

    public static void excluirItem(Integer necId, Integer produtoCod, Conexao c) {
        getDAO().excluirItem(necId, produtoCod, c);
    }

    public Integer getNecId() {
        return necId;
    }

    public void setNecId(Integer necId) {
        this.necId = necId;
    }

    public String getPessoaCpf() {
        return pessoaCpf;
    }

    public void setPessoaCpf(String pessoaCpf) {
        this.pessoaCpf = pessoaCpf;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getProdutoCod() {
        return produtoCod;
    }

    public void setProdutoCod(Integer produtoCod) {
        this.produtoCod = produtoCod;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getPessoaNome() {
        return pessoaNome;
    }

    public void setPessoaNome(String pessoaNome) {
        this.pessoaNome = pessoaNome;
    }

    public String getProdutoDescr() {
        return produtoDescr;
    }

    public void setProdutoDescr(String produtoDescr) {
        this.produtoDescr = produtoDescr;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}
