package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.dto.AtribuirVoluntariosDTO;
import projeto.salf.controller.dto.CriarCampanhaDTO;
import projeto.salf.controller.dto.FinalizarCampanhaDTO;
import projeto.salf.model.*;
import projeto.salf.service.CampanhaService;
import projeto.salf.service.CampanhaVoluntarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/campanhas")
@CrossOrigin
public class CampanhaController {

    private final CampanhaService campanhaService;
    private final CampanhaVoluntarioService campanhaVoluntarioService;

    public CampanhaController(CampanhaService campanhaService,
                              CampanhaVoluntarioService campanhaVoluntarioService) {
        this.campanhaService = campanhaService;
        this.campanhaVoluntarioService = campanhaVoluntarioService;
    }

    @PostMapping
    public ResponseEntity<Campanha> criar(@RequestBody CriarCampanhaDTO dto) {
        Campanha c = campanhaService.criarCampanha(
                dto.descricao,
                dto.dataInicio,
                dto.dataFim,
                dto.funcionarioCpf,
                dto.observacao
        );
        return ResponseEntity.ok(c);
    }

    @PostMapping("/{id}/responsaveis")
    public ResponseEntity<List<CampanhaVoluntario>> atribuirVoluntarios(@PathVariable Integer id,
                                                                        @RequestBody AtribuirVoluntariosDTO dto) {
        List<CampanhaVoluntario> itens = new ArrayList<>();
        for (AtribuirVoluntariosDTO.Item it : dto.voluntarios) {
            CampanhaVoluntarioId key = new CampanhaVoluntarioId(id, it.cpfVoluntario);
            CampanhaVoluntario cv = new CampanhaVoluntario();
            cv.setId(key);
            cv.setCargoCampanha(it.cargo);
            itens.add(cv);
        }
        return ResponseEntity.ok(campanhaVoluntarioService.atribuir(id, itens));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Campanha> finalizar(@PathVariable Integer id,
                                              @RequestBody FinalizarCampanhaDTO dto) {
        Campanha c = campanhaService.finalizarCampanha(
                id,
                dto.totalArrecadado,
                dto.dataFim,
                dto.observacao
        );
        return ResponseEntity.ok(c);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campanha> get(@PathVariable Integer id) {
        return ResponseEntity.ok(campanhaService.buscarPorId(id));
    }
}
