package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.NecessidadeCesta;
import projeto.salf.model.NecessidadeCestaId;
import projeto.salf.service.NecessidadeCestaService;

import java.util.List;

@RestController
@RequestMapping("/api/necessidades/cestas")
@CrossOrigin(origins = "*")
public class NecessidadeCestaController {
    @Autowired
    private NecessidadeCestaService service;

    @GetMapping
    public List<NecessidadeCesta> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public NecessidadeCesta salvar(@RequestBody NecessidadeCesta necessidade) { return service.salvar(necessidade); }

    @DeleteMapping
    public ResponseEntity<Void> excluir(@RequestBody NecessidadeCestaId id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}