package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.model.ItensDaLista;
import projeto.salf.model.ItensDaListaId;
import projeto.salf.model.ListaCompra;
import projeto.salf.repository.ItensDaListaRepository;
import projeto.salf.repository.ListaCompraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ListaCompraService {

    private final ListaCompraRepository listaRepository;
    private final ItensDaListaRepository itensRepository;

    public ListaCompraService(ListaCompraRepository listaRepository, ItensDaListaRepository itensRepository) {
        this.listaRepository = listaRepository;
        this.itensRepository = itensRepository;
    }

    // CRUD Lista de Compra
    public List<ListaCompra> listarListas() {
        return listaRepository.findAll();
    }

    public Optional<ListaCompra> buscarPorId(Integer id) {
        return listaRepository.findById(id);
    }

    public ListaCompra salvarLista(ListaCompra lista) {
        return listaRepository.save(lista);
    }

    public boolean excluirLista(Integer id) {
        if (listaRepository.existsById(id)) {
            listaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // CRUD Itens da Lista
    public List<ItensDaLista> listarItens() {
        return itensRepository.findAll();
    }

    public ItensDaLista salvarItem(ItensDaLista item) {
        return itensRepository.save(item);
    }

    public boolean excluirItem(ItensDaListaId id) {
        if (itensRepository.existsById(id)) {
            itensRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
