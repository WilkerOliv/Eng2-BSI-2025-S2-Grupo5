package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.CompraDAO;
import projeto.salf.model.Compra;

@Service
public class CompraService {

    private final CompraDAO compraDAO;
    public CompraService(CompraDAO compraDAO){
        this.compraDAO = compraDAO;
    }


    public Integer insereCompra(Compra compra){
        return compraDAO.insereCompra(compra);
    }

}
