package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.FornecedorDAO;
import projeto.salf.model.Fornecedor;

import java.util.List;
import java.util.Map;

@Service
public class FornecedorService {

    FornecedorDAO fornecedorDAO;

    public FornecedorService(FornecedorDAO forn) {
        this.fornecedorDAO = forn;
    }

    public List<Fornecedor> getlistaFornecedores(){

        return fornecedorDAO.getAll();
    }

    public Map<Integer, List<Fornecedor>> getlistaFornecedoresPorCotacao(){
        return fornecedorDAO.getListaAllCotacao();
    }
}
