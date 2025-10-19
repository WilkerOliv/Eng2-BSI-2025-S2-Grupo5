package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.ListaCompra;
import projeto.salf.service.ListaCompraService;

import java.util.List;


@RestController
@RequestMapping("/api/listas")
@CrossOrigin(origins = "*")
public class ListaCompraController {
    @Autowired
    private ListaCompraService service;

    @GetMapping
    public List<ListaCompra> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public ListaCompra salvar(@RequestBody ListaCompra lista) { return service.salvar(lista); }

    @PutMapping("/{id}")
    public ResponseEntity<ListaCompra> atualizar(@PathVariable Integer id, @RequestBody ListaCompra lista) {
        lista.setLcCod(id);
        return ResponseEntity.ok(service.salvar(lista));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}