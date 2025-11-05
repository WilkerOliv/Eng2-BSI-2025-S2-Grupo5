package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.NecessidadeProdutoDAO;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;

import java.util.List;

@RestController
@RequestMapping("/api/necessidades/produtos")
@CrossOrigin(origins = "*")
public class NecessidadeProdutoController {

    private Conexao conexao;
    private NecessidadeProdutoDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new NecessidadeProdutoDAO(conexao);
    }

    private void fecharConexao() {
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<NecessidadeProduto>> listarTodas() {
        try {
            abrirConexao();
            return ResponseEntity.ok(dao.findAll());
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<NecessidadeProduto> salvar(@RequestBody NecessidadeProduto necessidade) {
        try {
            abrirConexao();
            boolean ok = dao.save(necessidade);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(necessidade);
        } finally {
            fecharConexao();
        }
    }

//    @DeleteMapping
//    public ResponseEntity<Void> excluir(@RequestBody NecessidadeProdutoId id) {
//        try {
//            abrirConexao();
//            dao.deleteById(id);
//            return ResponseEntity.noContent().build();
//        } finally {
//            fecharConexao();
//        }
//    }

    @DeleteMapping
    public ResponseEntity<Void> excluir(@RequestBody NecessidadeProdutoId id) {
        try {
            abrirConexao();
            boolean ok = dao.deleteById(id);
            if (!ok) return ResponseEntity.status(404).build();
            return ResponseEntity.noContent().build();
        } finally {
            fecharConexao();
        }
    }

}
