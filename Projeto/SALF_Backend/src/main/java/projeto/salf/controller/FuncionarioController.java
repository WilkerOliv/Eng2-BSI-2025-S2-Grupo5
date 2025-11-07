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



    @GetMapping("/buscaCPF")
    public ResponseEntity<Funcionario> buscarporCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(service.buscaCPF(cpf));
    }
}