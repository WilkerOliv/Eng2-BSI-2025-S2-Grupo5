package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ProdutoDAO;
import projeto.salf.model.Produto;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private Conexao conexao;
    private ProdutoDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new ProdutoDAO(conexao);
    }

    private void fecharConexao() {
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        try {
            abrirConexao();
            return ResponseEntity.ok(dao.findAll());
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        try {
            abrirConexao();
            boolean ok = dao.save(produto);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(produto);
        } finally {
            fecharConexao();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto produto) {
        try {
            abrirConexao();
            produto.setProdCod(id);
            boolean ok = dao.save(produto);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(produto);
        } finally {
            fecharConexao();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            abrirConexao();
            dao.deleteById(id);
            return ResponseEntity.noContent().build();
        } finally {
            fecharConexao();
        }
    }
}
