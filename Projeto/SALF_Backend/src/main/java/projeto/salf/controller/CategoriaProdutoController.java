package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.CategoriaProdutoDAO;
import projeto.salf.model.CategoriaProduto;

import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProdutoController {

    private Conexao conexao;
    private CategoriaProdutoDAO dao;

    private void verificarConexao() {
        conexao = SingletonDB.getConexao();

        if (conexao == null || !conexao.getEstadoConexao()) {
            out.println("Nenhuma conexão ativa. Conectando...");
            SingletonDB.conectar();
            conexao = SingletonDB.getConexao();
        } else {
            out.println("Conexão já ativa, reutilizando.");
        }

        dao = new CategoriaProdutoDAO(conexao);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProduto>> listarTodas() {
        verificarConexao();
        List<CategoriaProduto> result = dao.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CategoriaProduto> salvar(@RequestBody CategoriaProduto categoria) {
        verificarConexao();
        boolean ok = dao.save(categoria);
        if (!ok) return ResponseEntity.status(500).build();
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProduto> atualizar(@PathVariable Integer id, @RequestBody CategoriaProduto categoria) {
        verificarConexao();
        categoria.setCatCod(id);
        boolean ok = dao.save(categoria);
        if (!ok) return ResponseEntity.status(500).build();
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> excluir(@PathVariable Integer id) {
        verificarConexao();
        boolean ok = dao.deleteById(id);
        if (!ok)
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir categoria"));
        return ResponseEntity.ok(Map.of("mensagem", "Categoria excluída com sucesso"));
    }
}
