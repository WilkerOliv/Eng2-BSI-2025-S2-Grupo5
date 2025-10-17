package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.ItensLista;
import projeto.salf.model.ListaCompra;
import projeto.salf.repository.ListaCompraRepository;

import java.util.List;

@Service
public class ListaCompraService {
    @Autowired
    private ListaCompraRepository repository;

    public List<ListaCompra> listarTodas() { return repository.findAll(); }
    public ListaCompra salvar(ListaCompra l) { return repository.save(l); }
    public void excluir(Integer id) { repository.deleteById(id); }
}