package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Voluntario;
import projeto.salf.service.VoluntarioService;

@RestController
@RequestMapping("/voluntarios")
@CrossOrigin
public class VoluntarioController {

    private final VoluntarioService voluntarioService;

    public VoluntarioController(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    @PostMapping
    public ResponseEntity<Voluntario> criarOuAtualizar(@RequestBody Voluntario v) {
        return ResponseEntity.ok(voluntarioService.criarOuAtualizar(v));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Voluntario> buscar(@PathVariable String cpf) {
        Voluntario v = voluntarioService.buscar(cpf);
        return v == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(v);
    }
}
