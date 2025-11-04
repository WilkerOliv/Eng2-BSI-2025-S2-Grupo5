package projeto.salf.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.salf.model.Fornecedor;
import projeto.salf.service.FornecedorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fornecedor")
@CrossOrigin(origins = "*")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService forn) {
        this.fornecedorService = forn;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Fornecedor>> listaFornecedores() {
        return ResponseEntity.ok(fornecedorService.getlistaFornecedores());
    }

    @GetMapping("/todosPorCotacao")
    public ResponseEntity<Map<Integer, List<Fornecedor>>> listaFornecedoresPorCotacao() {
        return ResponseEntity.ok(fornecedorService.getlistaFornecedoresPorCotacao());
    }
}
