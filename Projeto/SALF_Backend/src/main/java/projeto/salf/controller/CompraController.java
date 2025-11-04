package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Compra;
import projeto.salf.service.CompraService;

@RestController
@RequestMapping("/api/compra")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService){
        this.compraService = compraService;
    }


    @PostMapping()
    public ResponseEntity<Integer> IncluirCompra(@RequestBody Compra compra){

        return ResponseEntity.ok(compraService.insereCompra(compra));

    }
}
