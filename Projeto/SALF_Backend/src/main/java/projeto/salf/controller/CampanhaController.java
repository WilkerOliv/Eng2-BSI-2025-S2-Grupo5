package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Campanha;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.model.Voluntario;
import projeto.salf.service.CampanhaService;
import projeto.salf.utils.Mensagem;

import java.util.List;

@RestController
@RequestMapping("/campanha")
public class CampanhaController {

    @Autowired
    private CampanhaService campanhaService;

    // CRUD Básico de Campanha
    @PostMapping
    public ResponseEntity<Mensagem> salvar(@RequestBody Campanha campanha) {
        Mensagem mensagem = campanhaService.salvar(campanha);
        if (mensagem.Sucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @GetMapping
    public ResponseEntity<List<Campanha>> buscarTodos() {
        List<Campanha> campanhas = campanhaService.buscarTodos();
        return ResponseEntity.ok(campanhas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campanha> buscarPorId(@PathVariable Long id) {
        Campanha campanha = campanhaService.buscarPorId(id);
        if (campanha != null) {
            return ResponseEntity.ok(campanha);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mensagem> inativar(@PathVariable Long id) {
        Mensagem mensagem = campanhaService.inativar(id);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    // Endpoints de Voluntários
    @GetMapping("/voluntarios/ativos")
    public ResponseEntity<List<Voluntario>> buscarVoluntariosAtivos() {
        List<Voluntario> voluntarios = campanhaService.buscarVoluntariosAtivos();
        return ResponseEntity.ok(voluntarios);
    }

    @PostMapping("/{idCampanha}/voluntarios")
    public ResponseEntity<Mensagem> vincularVoluntarios(@PathVariable Long idCampanha, @RequestBody List<CampanhaVoluntario> voluntarios) {
        Mensagem mensagem = campanhaService.vincularVoluntarios(idCampanha, voluntarios);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @GetMapping("/{idCampanha}/voluntarios")
    public ResponseEntity<List<CampanhaVoluntario>> buscarVoluntariosDaCampanha(@PathVariable Long idCampanha) {
        List<CampanhaVoluntario> voluntarios = campanhaService.buscarVoluntariosDaCampanha(idCampanha);
        return ResponseEntity.ok(voluntarios);
    }
}
