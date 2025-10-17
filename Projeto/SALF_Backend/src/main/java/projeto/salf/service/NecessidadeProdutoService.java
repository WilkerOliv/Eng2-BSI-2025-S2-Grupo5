package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;
import projeto.salf.repository.NecessidadeProdutoRepository;

import java.util.List;

@Service
public class NecessidadeProdutoService {
    @Autowired
    private NecessidadeProdutoRepository repository;

    public List<NecessidadeProduto> listarTodas() { return repository.findAll(); }
    public NecessidadeProduto salvar(NecessidadeProduto n) { return repository.save(n); }
    public void excluir(NecessidadeProdutoId id) { repository.deleteById(id); }
}