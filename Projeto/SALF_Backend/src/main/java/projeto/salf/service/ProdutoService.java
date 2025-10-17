package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.Produto;
import projeto.salf.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;

    public List<Produto> listarTodos() { return repository.findAll(); }
    public Produto salvar(Produto p) { return repository.save(p); }
    public void excluir(Integer id) { repository.deleteById(id); }
}