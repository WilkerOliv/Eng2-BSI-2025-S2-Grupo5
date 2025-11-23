package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.FuncionarioDAO;

import java.util.List;


public class Funcionario {

    private String funcCpf;
    private String funcNome;
    private String funcEmail;
    private String funcTelefone;

    public String getFuncCpf() {
        return funcCpf;
    }

    public void setFuncCpf(String funcCpf) {
        this.funcCpf = funcCpf;
    }

    public String getFuncNome() {
        return funcNome;
    }

    public void setFuncNome(String funcNome) {
        this.funcNome = funcNome;
    }

    public String getFuncEmail() {
        return funcEmail;
    }

    public void setFuncEmail(String funcEmail) {
        this.funcEmail = funcEmail;
    }

    public String getFuncTelefone() {
        return funcTelefone;
    }

    public void setFuncTelefone(String funcTelefone) {
        this.funcTelefone = funcTelefone;
    }

    // ================= DAO =================

    private static FuncionarioDAO getDAO() {
        return new FuncionarioDAO();
    }

    // ================= BUSCAS =================


    public static Funcionario buscarPorCpf(String cpf, Conexao c) {
        return getDAO().findByCpf(cpf, c);
    }

    // Busca funcionários por CPF ou nome contendo o termo.
    public static List<Funcionario> buscarPorCpfOuNome(String termo, Conexao c) {
        return getDAO().searchByCpfOrNome(termo, c);
    }

    // Retorna todos os funcionários
    public static List<Funcionario> listarTodos(Conexao c) {
        return getDAO().findAll(c);
    }
}
