package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.Fornecedor;
import projeto.salf.repository.FornecedorRepository;

import java.util.List;

@Service
public class FornecedorService {
    @Autowired
    private FornecedorRepository repository;

    public List<Fornecedor> listarTodos() { return repository.findAll(); }
    public Fornecedor salvar(Fornecedor f) { return repository.save(f); }
    public void excluir(Integer id) { repository.deleteById(id); }
}