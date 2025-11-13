package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.ProdutoDoacaoDAO;
import projeto.salf.dto.ProdutoDoacaoEstoqueDTO;
import projeto.salf.model.Doacao;
import projeto.salf.model.DoacaoProduto;

import java.util.List;
import java.util.Map;

@Service
public class ProdutoDoacaoService {

    private final ProdutoDoacaoDAO produtoDoacaoDAO;
    public ProdutoDoacaoService(ProdutoDoacaoDAO produtoDoacaoDAO) {
        this.produtoDoacaoDAO = produtoDoacaoDAO;
    }

    public int inserirProdDoacao(ProdutoDoacaoEstoqueDTO doacaoProduto){
        return produtoDoacaoDAO.inserirDoacaoProdutos(doacaoProduto);
    }


}
