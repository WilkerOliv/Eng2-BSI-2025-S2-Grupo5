package projeto.salf.service;

import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;
import projeto.salf.dao.NecessidadeProdutoDAO;

import java.util.List;

public class NecessidadeProdutoService {
    private final NecessidadeProdutoDAO dao = new NecessidadeProdutoDAO();

    public List<NecessidadeProduto> listarTodas() { return dao.findAll(); }
    public NecessidadeProduto salvar(NecessidadeProduto n) {
        dao.save(n);
        return n;
    }
    public void excluir(NecessidadeProdutoId id) { dao.deleteById(id); }
}
