package projeto.salf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.dao.FuncionarioDAO;
import projeto.salf.model.Funcionario;
import projeto.salf.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
    @Autowired
    private FuncionarioService service;




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        Funcionario f = service.getFuncionarioPorEmail(email.trim());
        if (f == null)
            return ResponseEntity.status(404).body("Funcionário não encontrado");
        if (!f.getFuncSenha().equals(senha))
            return ResponseEntity.status(401).body("Senha incorreta");
        return ResponseEntity.ok(f);
    }

    @GetMapping
    public List<Funcionario> listarTodos() { return service.listarTodos(); }

    @GetMapping("/{cpf}")
    public ResponseEntity<Funcionario> buscarPorCpf(@PathVariable String cpf) {
        return service.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscaCPF")
    public ResponseEntity<Funcionario> buscarporCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(service.buscaCPF(cpf));
    }

    @PostMapping
    public Funcionario salvar(@RequestBody Funcionario funcionario) { return service.salvar(funcionario); }

    @PutMapping("/{cpf}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable String cpf, @RequestBody Funcionario funcionario) {
        if (!service.buscarPorCpf(cpf).isPresent()) return ResponseEntity.notFound().build();
        funcionario.setFuncCpf(cpf);
        return ResponseEntity.ok(service.salvar(funcionario));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        service.excluir(cpf);
        return ResponseEntity.noContent().build();
    }
}