package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Campanha;
import projeto.salf.model.ResultadoCampanha;
import projeto.salf.service.ResultadoCampanhaService;
import projeto.salf.utils.Mensagem;

import java.util.List;

@RestController
@RequestMapping("/resultado-campanha")
public class ResultadoCampanhaController {

    @Autowired
    private ResultadoCampanhaService resultadoCampanhaService;

    @GetMapping("/campanhas-finalizadas")
    public ResponseEntity<List<Campanha>> buscarCampanhasFinalizadas() {
        List<Campanha> campanhas = resultadoCampanhaService.buscarCampanhasFinalizadas();
        return ResponseEntity.ok(campanhas);
    }

    @PostMapping
    public ResponseEntity<Mensagem> registrarResultado(@RequestBody ResultadoCampanha resultado) {
        Mensagem mensagem = resultadoCampanhaService.registrarResultado(resultado);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }
}
