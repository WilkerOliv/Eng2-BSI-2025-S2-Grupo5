package projeto.salf.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Parametrizacao;
import projeto.salf.service.ParametrizacaoService;

@RestController
@RequestMapping("/api/parametrizacao")
@CrossOrigin("*")
public class ParametrizacaoController {

    private final ParametrizacaoService parametrizacaoService;

    public ParametrizacaoController(ParametrizacaoService parametrizacaoService) {
        this.parametrizacaoService = parametrizacaoService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> salvarOuAtualizar(@RequestBody Parametrizacao parametrizacao) {

        boolean novo = parametrizacaoService.salvarOuAtualizar(parametrizacao);
        return ResponseEntity.ok(novo ? "Empresa cadastrada" : "Empresa atualizada");
    }

    @GetMapping
    public ResponseEntity<?> getParametrizacao(@RequestParam String email) {
        var pa = parametrizacaoService.getByEmail(email);
        return (pa != null) ? ResponseEntity.ok(pa)
                : ResponseEntity.status(404).body("Parametrização não encontrada.");
    }

    @GetMapping("/existeEmpresa")
    public ResponseEntity<Boolean> existeEmpresa() {
        return ResponseEntity.ok(parametrizacaoService.existeEmpresa());
    }

    // projeto.salf.controller.ParametrizacaoController

    @GetMapping("/unica")
    public ResponseEntity<?> getUnica() {
        var unica = parametrizacaoService.getUnica(); // novo método no service
        return (unica != null)
                ? ResponseEntity.ok(unica)
                : ResponseEntity.status(404).body("Parametrização não encontrada.");
    }



}
