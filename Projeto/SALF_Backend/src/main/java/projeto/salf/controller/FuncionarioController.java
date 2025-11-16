package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.FuncionarioDAO;
import projeto.salf.model.Funcionario;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private Conexao conexao;
    private FuncionarioDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new FuncionarioDAO(conexao);
    }

    private void fecharConexao() {
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        try {
            abrirConexao();
            return ResponseEntity.ok(dao.findAll());
        } finally {
            fecharConexao();
        }
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Funcionario> buscarPorCpf(@PathVariable String cpf) {
        try {
            abrirConexao();
            Funcionario f = dao.findById(cpf);
            if (f == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(f);
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody Funcionario funcionario) {
        try {
            abrirConexao();
            boolean ok = dao.save(funcionario);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(funcionario);
        } finally {
            fecharConexao();
        }
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable String cpf, @RequestBody Funcionario funcionario) {
        try {
            abrirConexao();
            if (dao.findById(cpf) == null) return ResponseEntity.notFound().build();
            funcionario.setFuncCpf(cpf);
            boolean ok = dao.save(funcionario);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(funcionario);
        } finally {
            fecharConexao();
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        try {
            abrirConexao();
            dao.deleteById(cpf);
            return ResponseEntity.noContent().build();
        } finally {
            fecharConexao();
        }
    }
}
