package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.AcertoEstoque;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AcertoEstoqueDAO {

    // Registrar lista de acertos
    public void registrar(AcertoEstoque.AcertoDTO dto, Conexao c) {

        Integer idAcerto = ((Number) c.consultarValorUnico(
                "SELECT nextval('acerto_estoque_id_acerto_seq')")).intValue();

        for (AcertoEstoque.ItemDTO item : dto.itens) {

            // 1) REGISTRA ACERTO
            c.manipular("""
            INSERT INTO acerto_estoque
            (id_acerto, estoque_est_cod, quantidade_anterior, nova_quantidade, motivo, data_acerto, usuario_responsavel)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """,
                    idAcerto,
                    item.estCod,
                    item.quantidadeAnterior,
                    item.novaQuantidade,
                    item.motivo,
                    java.sql.Date.valueOf(dto.dataAcerto),   // AQUI ESTÁ A DATA
                    dto.funcionarioCpf
            );

            // 2) ATUALIZA O ESTOQUE
            c.manipular("""
            UPDATE estoque
            SET est_prod_quantidade = ?
            WHERE est_cod = ?
        """, item.novaQuantidade, item.estCod);
        }
    }

    // Lista principal
    public List<AcertoEstoque> listarAgrupado(Conexao c) {

        String sql = """
            SELECT DISTINCT a.id_acerto,
                            a.usuario_responsavel,
                            a.data_acerto
            FROM acerto_estoque a
            ORDER BY a.data_acerto DESC, a.id_acerto DESC
        """;

        List<AcertoEstoque> lista = new ArrayList<>();

        for (Map<String, Object> row : c.consultar(sql)) {
            AcertoEstoque a = new AcertoEstoque();
            a.setIdAcerto((Integer) row.get("id_acerto"));
            a.setFuncionarioCpf((String) row.get("usuario_responsavel"));
            a.setData(((Date) row.get("data_acerto")).toLocalDate());
            lista.add(a);
        }

        return lista;
    }

    // Itens de um acerto
    public List<AcertoEstoque> listarItens(Integer idAcerto, Conexao c) {

        String sql = """
            SELECT a.estoque_est_cod,
                   a.quantidade_anterior,
                   a.nova_quantidade,
                   a.motivo,
                   e.data_validade,
                   p.prod_descr
            FROM acerto_estoque a
            JOIN estoque e ON e.est_cod = a.estoque_est_cod
            JOIN produto p ON p.prod_cod = e.produto_prod_cod
            WHERE id_acerto = ?
            ORDER BY p.prod_descr
        """;

        List<AcertoEstoque> lista = new ArrayList<>();

        for (Map<String, Object> row : c.consultar(sql, idAcerto)) {
            AcertoEstoque item = new AcertoEstoque();
            item.setEstCod((Integer) row.get("estoque_est_cod"));
            item.setQuantidadeAnterior((Integer) row.get("quantidade_anterior"));
            item.setNovaQuantidade((Integer) row.get("nova_quantidade"));
            item.setMotivo((String) row.get("motivo"));
            item.setProdutoNome((String) row.get("prod_descr"));

            Date val = (Date) row.get("data_validade");
            if (val != null) item.setValidade(val.toLocalDate());

            lista.add(item);
        }

        return lista;
    }
}
