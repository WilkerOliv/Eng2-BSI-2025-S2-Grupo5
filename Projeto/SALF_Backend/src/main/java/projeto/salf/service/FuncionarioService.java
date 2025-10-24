package projeto.salf.service;

import java.util.List;
import java.util.Optional;
import projeto.salf.model.*;
import projeto.salf.dao.*;

public class FuncionarioService {
    private final FuncionarioDAO dao = new FuncionarioDAO();

    public Funcionario getFuncionarioPorEmail(String email) {
        return dao.buscaFuncEmail(email);
    }

    public List<Funcionario> listarTodos() { return dao.findAll(); }
    public Optional<Funcionario> buscarPorCpf(String cpf) { return Optional.ofNullable(dao.findById(cpf)); }
    public Funcionario salvar(Funcionario f) {
        dao.save(f);
        return f;
    }
    public void excluir(String cpf) { dao.deleteById(cpf); }
}
