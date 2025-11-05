package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.dao.CompraDAO;
import projeto.salf.model.Compra;
import projeto.salf.model.ItensCompra;

import java.time.LocalDate;
import java.util.Date;

@Service
public class CompraService {

    private final CompraDAO compraDAO;
    public CompraService(CompraDAO compraDAO){
        this.compraDAO = compraDAO;
    }


    public Integer insereCompra(Compra compra){
        return compraDAO.insereCompra(compra);
    }

    public boolean insereItens( ItensCompra itensCompra, LocalDate validade){
        return compraDAO.insereItens(itensCompra, validade);
    }

}
