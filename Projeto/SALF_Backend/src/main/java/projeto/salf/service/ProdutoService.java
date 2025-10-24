package projeto.salf.service;

import projeto.salf.model.Produto;
import projeto.salf.dao.ProdutoDAO;

import java.util.List;

public class ProdutoService {
    private final ProdutoDAO dao = new ProdutoDAO();

    public List<Produto> listarTodos() { return dao.findAll(); }
    public Produto salvar(Produto p) {
        dao.save(p);
        return p;
    }
    public void excluir(Integer id) { dao.deleteById(id); }
}
