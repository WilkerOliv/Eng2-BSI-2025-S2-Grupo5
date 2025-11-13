package projeto.salf.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;
import projeto.salf.model.Doacao;
import projeto.salf.model.Produto;
import projeto.salf.service.DoacaoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doacao")
@CrossOrigin(origins = "*")

public class DoacaoController {

    DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }


    @PostMapping("/inserir")
    public ResponseEntity<Integer> inserirDoacao(@RequestBody Doacao doacao) {
        return ResponseEntity.ok(doacaoService.inserirDoacao(doacao));
    }



    @GetMapping("/getListaDoacao")
    public ResponseEntity<List<DoacaoDTO>> getListaDoacao() {
        return ResponseEntity.ok(doacaoService.getListaDoacao());
    }


    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemDoacaoDTO>> listarItensDoacao(@PathVariable int id) {
        List<ItemDoacaoDTO> itens = doacaoService.getItensDaDoacao(id);
        return ResponseEntity.ok(itens);
    }




}
