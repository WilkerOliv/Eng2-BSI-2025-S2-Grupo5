package projeto.salf.service;


import projeto.salf.model.ListaCompra;
import projeto.salf.dao.ListaCompraDAO;

import java.util.List;

public class ListaCompraService {
    private final ListaCompraDAO dao = new ListaCompraDAO();

    public List<ListaCompra> listarListas() { return dao.findAll(); }
    public ListaCompra salvarLista(ListaCompra l) {
        dao.save(l);
        return l;
    }
    public void excluirLista(Integer id) { dao.deleteById(id); }
}
