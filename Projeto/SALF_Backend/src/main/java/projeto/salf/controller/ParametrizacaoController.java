package projeto.salf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Parametrizacao;
import projeto.salf.service.ParametrizacaoService;

// pacote: projeto.salf.controller
@RestController
@RequestMapping("/api/parametrizacao")
@CrossOrigin("*")
public class ParametrizacaoController {

    @Autowired
    ParametrizacaoService parametrizacaoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> salvarOuAtualizar(Parametrizacao parametrizacao){

        boolean novo = parametrizacaoService.salvarOuAtualizar(parametrizacao);
        return ResponseEntity.ok(novo ? "Empresa cadastrada" : "Empresa atualizada");
    }

    @GetMapping
    public ResponseEntity<?> getParametrizacao(@RequestParam String email) {
        Parametrizacao pa = parametrizacaoService.getByEmail(email);
        return (pa != null) ? ResponseEntity.ok(pa)
                : ResponseEntity.status(404).body("Parametrização não encontrada.");
    }

    @GetMapping("/existeEmpresa")
    public ResponseEntity<Boolean> existeEmpresa() {
        return ResponseEntity.ok(parametrizacaoService.existeEmpresa());
    }
}
