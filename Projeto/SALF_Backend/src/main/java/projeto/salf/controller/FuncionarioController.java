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



    @GetMapping("/buscaCPF")
    public ResponseEntity<Funcionario> buscarporCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(service.buscaCPF(cpf));
    }
}