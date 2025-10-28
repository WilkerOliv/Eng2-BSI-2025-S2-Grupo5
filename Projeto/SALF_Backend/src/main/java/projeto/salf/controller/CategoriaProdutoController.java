package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.CategoriaProdutoDAO;
import projeto.salf.model.CategoriaProduto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProdutoController {

    private Conexao conexao;
    private CategoriaProdutoDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new CategoriaProdutoDAO(conexao);
    }

    private void fecharConexao() {
        // fecha a conexão gerenciada pelo Singleton
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProduto>> listarTodas() {
        try {
            abrirConexao();
            List<CategoriaProduto> result = dao.findAll();
            return ResponseEntity.ok(result);
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<CategoriaProduto> salvar(@RequestBody CategoriaProduto categoria) {
        try {
            abrirConexao();
            boolean ok = dao.save(categoria);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(categoria);
        } finally {
            fecharConexao();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProduto> atualizar(@PathVariable Integer id, @RequestBody CategoriaProduto categoria) {
        try {
            abrirConexao();
            categoria.setCatCod(id);
            boolean ok = dao.save(categoria);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(categoria);
        } finally {
            fecharConexao();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> excluir(@PathVariable Integer id) {
        try {
            abrirConexao();
            boolean ok = dao.deleteById(id);
            if (!ok) return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir"));
            return ResponseEntity.ok(Map.of("mensagem", "Categoria excluída com sucesso"));
        } finally {
            fecharConexao();
        }
    }
}
