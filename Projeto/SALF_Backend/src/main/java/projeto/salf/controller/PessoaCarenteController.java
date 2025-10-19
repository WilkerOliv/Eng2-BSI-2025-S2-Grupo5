package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.PessoaCarente;
import projeto.salf.service.PessoaCarenteService;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*")
public class PessoaCarenteController {
    @Autowired
    private PessoaCarenteService service;

    @GetMapping
    public List<PessoaCarente> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public PessoaCarente salvar(@RequestBody PessoaCarente pessoa) { return service.salvar(pessoa); }

    @PutMapping("/{cpf}")
    public ResponseEntity<PessoaCarente> atualizar(@PathVariable String cpf, @RequestBody PessoaCarente pessoa) {
        pessoa.setPcCpf(cpf);
        return ResponseEntity.ok(service.salvar(pessoa));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        service.excluir(cpf);
        return ResponseEntity.noContent().build();
    }
}