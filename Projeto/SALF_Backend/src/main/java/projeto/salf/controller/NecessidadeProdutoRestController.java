package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;

import java.util.*;

@RestController
@RequestMapping("/api/necessidades/produtos")
@CrossOrigin(origins = "*")
public class NecessidadeProdutoRestController {



    @GetMapping("/pessoa/{cpf}")
    public ResponseEntity<List<Map<String, Object>>> listarPorPessoa(@PathVariable String cpf) {
        try {
            if (!SingletonDB.conectar()) {
                return ResponseEntity
                        .status(500)
                        .body(List.of(Map.of("erro", "Falha ao conectar ao banco")));
            }

            Conexao cx = SingletonDB.getConexao();

            String sql =
                    "select " +
                            "  produto_prod_cod as produto_cod, " +
                            "  quantidade, " +
                            "  observacao " +
                            "from necessidade_produto " +
                            "where regexp_replace(trim(pessoa_carente_pc_cpf), '[^0-9]', '', 'g') = " +
                            "      regexp_replace(trim(?),                      '[^0-9]', '', 'g')";

            List<Map<String, Object>> rows = cx.consultar(sql, cpf);

            List<Map<String, Object>> saida = new ArrayList<>();

            for (Map<String, Object> r : rows) {
                Map<String, Object> item = new HashMap<>();

                Object prodObj = r.get("produto_cod");
                Integer produtoCod = null;
                if (prodObj instanceof Number n) {
                    produtoCod = n.intValue();
                } else if (prodObj != null) {
                    try {
                        produtoCod = Integer.valueOf(prodObj.toString());
                    } catch (Exception ignored) {}
                }

                Object qtdObj = r.get("quantidade");
                Integer quantidade = null;
                if (qtdObj instanceof Number n) {
                    quantidade = n.intValue();
                } else if (qtdObj != null) {
                    try {
                        quantidade = Integer.valueOf(qtdObj.toString());
                    } catch (Exception ignored) {}
                }

                String observacao = (String) r.get("observacao");

                if (produtoCod != null && quantidade != null && quantidade > 0) {
                    item.put("produtoCod", produtoCod);
                    item.put("quantidade", quantidade);
                    item.put("observacao", observacao);
                    saida.add(item);
                }
            }

            return ResponseEntity.ok(saida);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(List.of(Map.of("erro", "Erro ao buscar necessidades: " + e.getMessage())));
        } finally {
            SingletonDB.close();
        }
    }
}
