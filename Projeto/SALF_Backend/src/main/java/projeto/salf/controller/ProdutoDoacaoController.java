package projeto.salf.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.dto.ProdutoDoacaoEstoqueDTO;
import projeto.salf.model.DoacaoProduto;
import projeto.salf.service.ProdutoDoacaoService;

@RestController
@RequestMapping("/api/doacao_prod")
@CrossOrigin(origins = "*")
public class ProdutoDoacaoController {

    private final ProdutoDoacaoService produtoDoacaoService;

    public ProdutoDoacaoController(ProdutoDoacaoService produtoDoacaoService) {
        this.produtoDoacaoService = produtoDoacaoService;
    }

    @PostMapping
    public ResponseEntity<?> insereProdDoacao(@RequestBody ProdutoDoacaoEstoqueDTO dto) {
        int ok = produtoDoacaoService.inserirProdDoacao(dto);

        if(ok > 0){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }



}
