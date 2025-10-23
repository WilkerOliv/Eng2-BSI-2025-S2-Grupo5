package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.model.CategoriaProduto;
import projeto.salf.repository.CategoriaProdutoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private final CategoriaProdutoRepository repository;

    public CategoriaProdutoService(CategoriaProdutoRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaProduto> listarTodas() {
        return repository.findAll();
    }

    public Optional<CategoriaProduto> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public CategoriaProduto salvar(CategoriaProduto categoria) {
        return repository.save(categoria);
    }

    public boolean excluir(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
