package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Produto;
import projeto.salf.service.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Produto>> getListProdutos() {
        List<Produto> produtos = service.getLista();
        return ResponseEntity.ok(produtos);
    }
}
