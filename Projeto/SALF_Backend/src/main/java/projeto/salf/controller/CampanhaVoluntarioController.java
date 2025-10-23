package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.model.CampanhaVoluntarioId;
import projeto.salf.service.CampanhaVoluntarioService;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas/{idCampanha}/responsaveis")
@CrossOrigin(origins = {"*"})
public class CampanhaVoluntarioController {

    @Autowired
    private CampanhaVoluntarioService service;

    @GetMapping
    public List<CampanhaVoluntario> listar(@PathVariable Integer idCampanha) {
        return service.listarPorCampanha(idCampanha);
    }

    // Vincula 1 responsável por requisição (manda o JSON da entidade CampanhaVoluntario)
    @PostMapping
    public ResponseEntity<CampanhaVoluntario> vincular(@PathVariable Integer idCampanha,
                                                       @RequestBody CampanhaVoluntario entidade) {
        // garante o id composto coerente com o path
        CampanhaVoluntarioId id = entidade.getId();
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        id.setCampanhaIdCampanha(idCampanha);
        entidade.setId(id);
        return ResponseEntity.ok(service.vincular(entidade));
    }

    // Remove vínculo
    @DeleteMapping("/{cpfVoluntario}")
    public ResponseEntity<Void> desvincular(@PathVariable Integer idCampanha,
                                            @PathVariable String cpfVoluntario) {
        service.desvincular(idCampanha, cpfVoluntario);
        return ResponseEntity.noContent().build();
    }
}
