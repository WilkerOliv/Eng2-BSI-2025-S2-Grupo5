package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ItensDaListaDAO;
import projeto.salf.dao.ListaCompraDAO;

import java.time.LocalDate;
import java.util.List;

public class ListaCompra {

    private Integer lcCod;
    private String funcionarioFuncCpf;
    private LocalDate dataCriacao;
    private String descricao;
    private Integer statusAtendimento;

    // campo só para exibição em telas/buscas
    private String funcNome;

    public Integer getLcCod() {
        return lcCod;
    }

    public void setLcCod(Integer lcCod) {
        this.lcCod = lcCod;
    }

    public String getFuncionarioFuncCpf() {
        return funcionarioFuncCpf;
    }

    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) {
        this.funcionarioFuncCpf = funcionarioFuncCpf;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getStatusAtendimento() {
        return statusAtendimento;
    }

    public void setStatusAtendimento(Integer statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }

    public String getFuncNome() {
        return funcNome;
    }

    public void setFuncNome(String funcNome) {
        this.funcNome = funcNome;
    }

    // =============== DAO ===============

    private static ListaCompraDAO getDAO() {
        Conexao c = SingletonDB.getConexao();
        return new ListaCompraDAO(c);
    }

    private static ItensDaListaDAO getItensDAO() {
        Conexao c = SingletonDB.getConexao();
        return new ItensDaListaDAO(c);
    }

    // =============== Operações ===============

    public static ListaCompra buscarPorId(Integer id) {
        return getDAO().findById(id);
    }

    public static List<ListaCompra> listarTodas() {
        return getDAO().findAll();
    }

    public static List<ListaCompra> buscarPorDescricao(String termo) {
        return getDAO().searchByDescricao(termo);
    }

    public static List<ListaCompra> buscarPorCpfFuncionario(String termo) {
        return getDAO().searchByCpf(termo);
    }

    public static List<ListaCompra> buscarPorNomeFuncionario(String termo) {
        return getDAO().searchByNomeFuncionario(termo);
    }

    public boolean salvar() {
        if (funcionarioFuncCpf == null || funcionarioFuncCpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do funcionário é obrigatório.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da lista é obrigatória.");
        }
        if (dataCriacao == null) {
            dataCriacao = LocalDate.now();
        }

        // data não pode ser futura
        if (dataCriacao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data da lista não pode ser maior que a data atual.");
        }

        if (statusAtendimento == null) {
            // 0=ABERTA, 1=EM ANDAMENTO, 2=CONCLUÍDA
            statusAtendimento = 0;
        }

        return getDAO().save(this);
    }


    // Exclui a lista e seus itens.
    public static boolean excluir(Integer id) {
        // primeiro exclui itens (por causa da FK)
        getItensDAO().deleteByLista(id);
        // depois exclui a lista
        return getDAO().deleteById(id);
    }
}
