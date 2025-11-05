package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Compra;
import projeto.salf.model.ItensCompra;
import projeto.salf.service.CompraService;

import java.time.LocalDate;

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

    @PostMapping("/itens") // ok se o controller base for /api/itens_compra
    public ResponseEntity<?> IncluirItensCompra(@RequestParam(required = false) LocalDate validade,
                                                @RequestBody ItensCompra iC){
        boolean ok = compraService.insereItens(iC, validade);
        return ok ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
