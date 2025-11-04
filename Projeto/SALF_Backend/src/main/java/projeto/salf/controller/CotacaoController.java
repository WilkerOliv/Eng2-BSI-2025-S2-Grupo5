package projeto.salf.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Cotacao;
import projeto.salf.service.CotacaoService;

import java.util.List;

@RestController
@RequestMapping("/api/cotacao")
@CrossOrigin(origins = "*")
public class CotacaoController {

    private final CotacaoService cotacaoService;
    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }



    @GetMapping("/lista")
    public ResponseEntity<List<Cotacao>> getCotacao() {
        return ResponseEntity.ok(cotacaoService.getCotacao());
    }

}
