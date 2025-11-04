package projeto.salf.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import projeto.salf.dao.CotacaoDAO;
import projeto.salf.model.Cotacao;

import java.util.List;

@Service
public class CotacaoService {

    private final CotacaoDAO cotacaoDAO;

    public CotacaoService(CotacaoDAO cotacaoDAO) {
        this.cotacaoDAO = cotacaoDAO;
    }

    public List<Cotacao> getCotacao() {
        return cotacaoDAO.getCotacao();
    }
}
