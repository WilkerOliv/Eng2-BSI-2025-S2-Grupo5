package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.ItensDaLista;
import projeto.salf.model.ListaCompra;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/listas")
@CrossOrigin(origins = "*")
public class ListaCompraController {

    private void garantirConexao() {
        SingletonDB.getConexao();
    }

    @GetMapping
    public ResponseEntity<List<ListaCompra>> listar(
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "termo", required = false) String termo) {
        try {
            garantirConexao();

            List<ListaCompra> listas;
            if (tipo == null || termo == null || termo.isBlank()) {
                listas = ListaCompra.listarTodas();
            } else {
                switch (tipo.toLowerCase()) {
                    case "descricao" -> listas = ListaCompra.buscarPorDescricao(termo);
                    case "cpf"       -> listas = ListaCompra.buscarPorCpfFuncionario(termo);
                    case "nome"      -> listas = ListaCompra.buscarPorNomeFuncionario(termo);
                    default          -> listas = ListaCompra.listarTodas();
                }
            }

            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaCompra> buscarPorId(@PathVariable Integer id) {
        try {
            garantirConexao();
            ListaCompra lista = ListaCompra.buscarPorId(id);
            if (lista == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ListaCompra lista) {
        try {
            garantirConexao();
            lista.setLcCod(null); // força insert
            lista.salvar();
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao salvar lista de compras."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody ListaCompra lista) {
        try {
            garantirConexao();
            lista.setLcCod(id);
            lista.salvar();
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao atualizar lista de compras."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            garantirConexao();
            boolean ok = ListaCompra.excluir(id);
            if (!ok) {
                return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir lista de compras."));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir lista de compras."));
        }
    }

    // ================= ITENS DA LISTA =================

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItensDaLista>> listarItens(@PathVariable Integer id) {
        try {
            garantirConexao();
            List<ItensDaLista> itens = ItensDaLista.listarPorLista(id);
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * DTO simples para receber item no corpo.
     */
    public static class ItemListaDTO {
        public Integer produtoCod;
        public Integer quantidade;
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<?> adicionarOuAtualizarItem(@PathVariable Integer id,
                                                      @RequestBody ItemListaDTO dto) {
        try {
            garantirConexao();
            ItensDaLista.adicionarOuAtualizarItem(id, dto.produtoCod, dto.quantidade);
            return ResponseEntity.ok(Map.of("mensagem", "Item salvo com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao salvar item da lista."));
        }
    }

    @DeleteMapping("/{id}/itens/{prodCod}")
    public ResponseEntity<?> removerItem(@PathVariable Integer id,
                                         @PathVariable Integer prodCod) {
        try {
            garantirConexao();
            ItensDaLista.removerItem(id, prodCod);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao remover item da lista."));
        }
    }
}
