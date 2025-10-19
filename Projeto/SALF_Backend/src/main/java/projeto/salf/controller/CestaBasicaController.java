package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.CestaBasica;
import projeto.salf.service.CestaBasicaService;

import java.util.List;

@RestController
@RequestMapping("/api/cestas")
@CrossOrigin(origins = "*")
public class CestaBasicaController {
    @Autowired
    private CestaBasicaService service;

    @GetMapping
    public List<CestaBasica> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public CestaBasica salvar(@RequestBody CestaBasica cesta) { return service.salvar(cesta); }

    @PutMapping("/{id}")
    public ResponseEntity<CestaBasica> atualizar(@PathVariable Integer id, @RequestBody CestaBasica cesta) {
        cesta.setCbCod(id);
        return ResponseEntity.ok(service.salvar(cesta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
