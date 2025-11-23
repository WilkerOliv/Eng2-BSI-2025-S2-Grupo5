package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.PessoaCarenteDAO;

import java.util.List;

public class PessoaCarente {

    private String pcCpf;
    private String pcNome;
    private String pcTelefone;

    public String getPcCpf() {
        return pcCpf;
    }

    public void setPcCpf(String pcCpf) {
        this.pcCpf = pcCpf;
    }

    public String getPcNome() {
        return pcNome;
    }

    public void setPcNome(String pcNome) {
        this.pcNome = pcNome;
    }

    public String getPcTelefone() {
        return pcTelefone;
    }

    public void setPcTelefone(String pcTelefone) {
        this.pcTelefone = pcTelefone;
    }

    // =============== DAO ===============

    private static PessoaCarenteDAO getDAO() {
        return new PessoaCarenteDAO();
    }

    // =============== Buscas ===============

    public static PessoaCarente buscarPorCpf(String cpf, Conexao c) {
        return getDAO().findByCpf(cpf, c);
    }

    public static List<PessoaCarente> buscarPorCpfOuNome(String termo, Conexao c) {
        return getDAO().searchByCpfOrNome(termo, c);
    }

    public static List<PessoaCarente> listarTodos(Conexao c) {
        return getDAO().findAll(c);
    }
}
