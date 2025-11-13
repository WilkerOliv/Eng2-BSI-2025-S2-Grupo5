package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Fornecedor;
import projeto.salf.service.FornecedorService;
import projeto.salf.utils.Mensagem;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<Mensagem> salvar(@RequestBody Fornecedor fornecedor) {
        Mensagem mensagem = fornecedorService.salvar(fornecedor);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @GetMapping
    public ResponseEntity<List<Fornecedor>> buscarTodos() {
        List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        if (fornecedor != null) {
            return ResponseEntity.ok(fornecedor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        fornecedor.setId(id); // Garante que o ID do path seja usado
        Mensagem mensagem = fornecedorService.salvar(fornecedor);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mensagem> inativar(@PathVariable Long id) {
        Mensagem mensagem = fornecedorService.inativar(id);
        if (mensagem.isSucesso()) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }
}
