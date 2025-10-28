package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ListaCompraDAO;
import projeto.salf.model.ListaCompra;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@CrossOrigin(origins = "*")
public class ListaCompraController {

    private Conexao conexao;
    private ListaCompraDAO dao;

    private void abrirConexao() {
        if (SingletonDB.getConexao() == null || !SingletonDB.getConexao().getEstadoConexao()) {
            SingletonDB.conectar();
        }
        conexao = SingletonDB.getConexao();
        dao = new ListaCompraDAO(conexao);
    }

    private void fecharConexao() {
        SingletonDB.close();
        conexao = null;
        dao = null;
    }

    @GetMapping
    public ResponseEntity<List<ListaCompra>> listarTodas() {
        try {
            abrirConexao();
            return ResponseEntity.ok(dao.findAll());
        } finally {
            fecharConexao();
        }
    }

    @PostMapping
    public ResponseEntity<ListaCompra> salvar(@RequestBody ListaCompra lista) {
        try {
            abrirConexao();
            boolean ok = dao.save(lista);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(lista);
        } finally {
            fecharConexao();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaCompra> atualizar(@PathVariable Integer id, @RequestBody ListaCompra lista) {
        try {
            abrirConexao();
            lista.setLcCod(id);
            boolean ok = dao.save(lista);
            if (!ok) return ResponseEntity.status(500).build();
            return ResponseEntity.ok(lista);
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
