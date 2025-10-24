package projeto.salf.service;

import projeto.salf.dao.CategoriaProdutoDAO;
import projeto.salf.model.CategoriaProduto;

import java.util.List;

public class CategoriaProdutoService {
    private final CategoriaProdutoDAO dao = new CategoriaProdutoDAO();

    public List<CategoriaProduto> listarTodas() { return dao.findAll(); }
    public CategoriaProduto salvar(CategoriaProduto c) {
        dao.save(c);
        return c;
    }
    public void excluir(Integer id) { dao.deleteById(id); }
}
