package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.service.CampanhaVoluntarioService;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas/{idCampanha}/responsaveis")
@CrossOrigin(origins = {"*"})
public class CampanhaVoluntarioController {

    private final CampanhaVoluntarioService service;

    public CampanhaVoluntarioController(CampanhaVoluntarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<CampanhaVoluntario> listar(@PathVariable Integer idCampanha) {
        return service.listarPorCampanha(idCampanha);
    }

    @PostMapping
    public ResponseEntity<CampanhaVoluntario> vincular(@PathVariable Integer idCampanha,
                                                       @RequestBody CampanhaVoluntario entidade) {
        if (entidade.getVoluntarioVolCpf() == null || entidade.getVoluntarioVolCpf().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        entidade.setCampanhaIdCampanha(idCampanha);

        CampanhaVoluntario salvo = service.vincular(entidade);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{cpfVoluntario}")
    public ResponseEntity<Void> desvincular(@PathVariable Integer idCampanha,
                                            @PathVariable String cpfVoluntario) {
        service.desvincular(idCampanha, cpfVoluntario);
        return ResponseEntity.noContent().build();
    }
}
