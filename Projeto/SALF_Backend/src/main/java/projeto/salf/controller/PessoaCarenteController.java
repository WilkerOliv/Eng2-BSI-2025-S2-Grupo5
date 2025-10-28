package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.PessoaCarenteDAO;
import projeto.salf.model.PessoaCarente;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*")
public class PessoaCarenteController {

    private Conexao conexao;
    private PessoaCarenteDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new PessoaCarenteDAO(conexao);
    }

    private void fecharConexao() {
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<PessoaCarente>> listarTodas() {
        try {
            abrirConexao();
            return ResponseEntity.ok(dao.findAll());
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<PessoaCarente> salvar(@RequestBody PessoaCarente pessoa) {
        try {
            abrirConexao();
            boolean ok = dao.save(pessoa);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(pessoa);
        } finally {
            fecharConexao();
        }
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<PessoaCarente> atualizar(@PathVariable String cpf, @RequestBody PessoaCarente pessoa) {
        try {
            abrirConexao();
            pessoa.setPcCpf(cpf);
            boolean ok = dao.save(pessoa);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(pessoa);
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
