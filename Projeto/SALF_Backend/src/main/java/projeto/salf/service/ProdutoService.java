package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.ProdutoDAO;
import projeto.salf.model.Produto;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoDAO produtoDAO;


    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public List<Produto> getLista() {
        return produtoDAO.getListaAll();
    }
}
