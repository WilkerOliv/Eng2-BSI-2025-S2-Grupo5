package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.dao.NecessidadeProdutoDAO;

import java.time.LocalDate;
import java.util.List;

/**
 * Model de Necessidade de Produto para Pessoa Carente.
 * Usa a tabela necessidade_produto.
 */
public class NecessidadeProduto {

    private String pessoaCarentePcCpf;
    private Integer produtoProdCod;
    private LocalDate data;
    private Integer quantidade;
    private String observacao;

    // Campos extras para exibição
    private String pessoaNome;
    private String produtoDescr;
    private Integer categoriaProdCod;

    public String getPessoaCarentePcCpf() {
        return pessoaCarentePcCpf;
    }

    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) {
        this.pessoaCarentePcCpf = pessoaCarentePcCpf;
    }

    public Integer getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(Integer produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public Integer getCategoriaProdCod() {
        return categoriaProdCod;
    }

    public void setCategoriaProdCod(Integer categoriaProdCod) {
        this.categoriaProdCod = categoriaProdCod;
    }

    // =============== DAO ===============

    private static NecessidadeProdutoDAO getDAO() {
        Conexao c = SingletonDB.getConexao();
        return new NecessidadeProdutoDAO(c);
    }

    // =============== Operações ===============

    public static List<NecessidadeProduto> listarTodas() {
        return getDAO().findAll();
    }

    public static List<NecessidadeProduto> listarPorPessoa(String cpf) {
        return getDAO().findByPessoa(cpf);
    }

    // Busca por termo
    public static List<NecessidadeProduto> buscarPorTermo(String termo) {
        return getDAO().search(termo);
    }

    /* Salva (insert ou update) uma necessidade.
      Valida pessoa, produto, quantidade e data. */
    public boolean salvar() {
        if (pessoaCarentePcCpf == null || pessoaCarentePcCpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF da pessoa carente é obrigatório.");
        }
        if (produtoProdCod == null || produtoProdCod <= 0) {
            throw new IllegalArgumentException("ID do produto é obrigatório.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        if (data == null) {
            data = LocalDate.now();
        }
        if (observacao == null) {
            observacao = "";
        }

        // Valida se pessoa carente existe
        PessoaCarente pessoa = PessoaCarente.buscarPorCpf(pessoaCarentePcCpf);
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa carente não cadastrada.");
        }

        // Valida se produto existe
        Produto produto = Produto.buscarPorId(produtoProdCod);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não cadastrado.");
        }

        return getDAO().save(this);
    }

    /**
     * Exclui a necessidade específica (pessoa + produto).
     */
    public static boolean excluir(String cpf, Integer prodCod) {
        if (cpf == null || cpf.trim().isEmpty() || prodCod == null) {
            return false;
        }
        return getDAO().delete(cpf, prodCod);
    }
}
