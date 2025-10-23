package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Campanha;
import projeto.salf.service.CampanhaService;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
@CrossOrigin(origins = {"*"})
public class CampanhaController {

    @Autowired
    private CampanhaService service;

    @GetMapping
    public List<Campanha> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campanha> buscar(@PathVariable Integer id) {
        Campanha c = service.buscarPorId(id);
        return c == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(c);
    }

    @PostMapping
    public Campanha salvar(@RequestBody Campanha campanha) {
        return service.salvar(campanha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campanha> atualizar(@PathVariable Integer id, @RequestBody Campanha campanha) {
        campanha.setIdCampanha(id); // ajuste conforme o nome do getter/setter no teu Model
        return ResponseEntity.ok(service.salvar(campanha));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // FINALIZAR (lançar resultado)
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Campanha> finalizar(@PathVariable Integer id, @RequestBody Campanha payload) {
        // usa apenas o campo total (ignora demais, pois a ideia é lançar resultado)
        Campanha c = service.finalizar(id, payload.getCampanhaTotalArrecado());
        return c == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(c);
    }
}
