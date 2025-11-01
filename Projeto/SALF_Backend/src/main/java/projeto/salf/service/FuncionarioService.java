package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import projeto.salf.dao.FuncionarioDAO;
import projeto.salf.model.*;
import projeto.salf.repository.*;

@Service
public class FuncionarioService {
    private final FuncionarioRepository repository;
    @Autowired
    FuncionarioDAO funcionarioDAO;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Funcionario getFuncionarioPorEmail(String email) {
        return funcionarioDAO.buscaFuncEmail(email);
    }
    public List<Funcionario> listarTodos() { return repository.findAll(); }
    public Optional<Funcionario> buscarPorCpf(String cpf) { return repository.findById(cpf); }
    public Funcionario salvar(Funcionario f) { return repository.save(f); }
    public void excluir(String cpf) { repository.deleteById(cpf); }
}