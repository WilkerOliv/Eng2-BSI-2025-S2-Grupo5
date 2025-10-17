package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.ItensDaLista;
import projeto.salf.model.ListaCompra;
import projeto.salf.service.ListaCompraService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listas")
@CrossOrigin(origins = "*")
public class ListaCompraController {

    private final ListaCompraService service;

    public ListaCompraController(ListaCompraService service) {
        this.service = service;
    }

    @GetMapping
    public List<ListaCompra> listarListas() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaCompra> buscarPorId(@PathVariable Integer id) {
        Optional<ListaCompra> lista = service.buscarPorId(id);
        return lista.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaCompra> criarLista(@RequestBody ListaCompra lista) {
        return ResponseEntity.ok(service.salvar(lista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaCompra> atualizarLista(@PathVariable Integer id, @RequestBody ListaCompra listaAtualizada) {
        ListaCompra atualizado = service.atualizar(id, listaAtualizada);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLista(@PathVariable Integer id) {
        boolean removido = service.excluir(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<ItensDaLista> adicionarItem(@PathVariable Integer id, @RequestBody ItensDaLista item) {
        Optional<ListaCompra> lista = service.buscarPorId(id);
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        item.setListaCompra(lista.get());
        return ResponseEntity.ok(service.adicionarItem(item));
    }

    @DeleteMapping("/{id}/itens")
    public ResponseEntity<Void> removerItem(@RequestBody ItensDaLista item) {
        service.removerItem(item);
        return ResponseEntity.noContent().build();
    }
}
