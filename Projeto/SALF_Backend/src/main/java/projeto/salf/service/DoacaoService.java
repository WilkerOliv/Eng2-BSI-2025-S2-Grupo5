package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.DoacaoDAO;
import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;
import projeto.salf.model.Doacao;
import projeto.salf.model.Produto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DoacaoService {


    DoacaoDAO doacaoDAO;

    public DoacaoService(DoacaoDAO doacaoDAO) {
        this.doacaoDAO = doacaoDAO;
    }

    public Integer inserirDoacao(Doacao doacao){
        return doacaoDAO.inserirDoacao(doacao);
    }

    public List<DoacaoDTO> getListaDoacao(){
        return doacaoDAO.getAllDoacoes();
    }

    public List<ItemDoacaoDTO> getItensDaDoacao(int idDoacao) {
        return doacaoDAO.getItensPorDoacao(idDoacao);
    }


}
