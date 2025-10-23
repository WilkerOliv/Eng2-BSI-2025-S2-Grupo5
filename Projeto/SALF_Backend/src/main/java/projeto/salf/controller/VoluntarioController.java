package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Voluntario;
import projeto.salf.service.VoluntarioService;

import java.util.List;

@RestController
@RequestMapping("/api/voluntarios")
@CrossOrigin(origins = {"*"})
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @GetMapping
    public List<Voluntario> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Voluntario> buscar(@PathVariable String cpf) {
        Voluntario v = service.buscarPorCpf(cpf);
        return v == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(v);
    }

    @PostMapping
    public Voluntario salvar(@RequestBody Voluntario voluntario) {
        return service.salvar(voluntario);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Voluntario> atualizar(@PathVariable String cpf, @RequestBody Voluntario voluntario) {
        voluntario.setVolCpf(cpf); // ajuste conforme o nome do campo no teu Model
        return ResponseEntity.ok(service.salvar(voluntario));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        service.excluir(cpf);
        return ResponseEntity.noContent().build();
    }
}
