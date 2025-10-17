package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.CategoriaProduto;
import projeto.salf.repository.CategoriaProdutoRepository;

import java.util.List;

@Service
public class CategoriaProdutoService {
    @Autowired
    private CategoriaProdutoRepository repository;

    public List<CategoriaProduto> listarTodas() { return repository.findAll(); }
    public CategoriaProduto salvar(CategoriaProduto c) { return repository.save(c); }
    public void excluir(Integer id) { repository.deleteById(id); }
}
