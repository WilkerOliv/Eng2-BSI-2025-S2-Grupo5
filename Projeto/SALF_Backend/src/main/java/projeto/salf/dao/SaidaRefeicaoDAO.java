package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.SaidaRefeicao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaidaRefeicaoDAO {

    public Integer registrar(SaidaRefeicao.SaidaDTO dto, Conexao c) {

        // 1) Inserir saída principal
        Integer idSaida = ((Number) c.consultarValorUnico(
                "SELECT nextval('saida_refeicao_id_saida_refeicao_seq')")).intValue();

        c.manipular("""
                INSERT INTO saida_refeicao
                (id_saida_refeicao, data_registro, funcionario_func_cpf, observacao, idsaidarefeicao, dataregistro)
                VALUES (?, ?, ?, ?, ?, ?)
            """,
                idSaida,
                Date.valueOf(java.time.LocalDate.now()),
                dto.funcionarioCpf,
                dto.observacao,
                idSaida,
                Date.valueOf(java.time.LocalDate.now())
        );

        // 2) Inserir itens
        for (SaidaRefeicao.ItemDTO item : dto.itens) {

            // insert item
            c.manipular("""
                INSERT INTO estoque_saida_refeicao
                (estoque_est_cod, saida_refeicao_id_saida_refeicao, quantidade)
                VALUES (?, ?, ?)
            """, item.estCod, idSaida, item.quantidade);

            // update estoque
            c.manipular("""
                UPDATE estoque
                SET est_prod_quantidade = est_prod_quantidade - ?
                WHERE est_cod = ?
            """, item.quantidade, item.estCod);
        }

        return idSaida;
    }

    public List<SaidaRefeicao> listarSaidas(Conexao c) {

        String sql = """
            SELECT id_saida_refeicao, data_registro, funcionario_func_cpf, observacao
            FROM saida_refeicao
            ORDER BY id_saida_refeicao DESC
        """;

        List<SaidaRefeicao> lista = new ArrayList<>();

        for (Map<String,Object> r : c.consultar(sql)) {
            SaidaRefeicao s = new SaidaRefeicao();

            s.setIdSaida((Integer) r.get("id_saida_refeicao"));
            var dt = r.get("data_registro");
            if (dt instanceof java.sql.Date d) s.setDataRegistro(d.toLocalDate());
            s.setFuncionarioCpf((String) r.get("funcionario_func_cpf"));
            s.setObservacao((String) r.get("observacao"));

            lista.add(s);
        }

        return lista;
    }

    public List<SaidaRefeicao.ItemDTO> listarItens(Integer idSaida, Conexao c) {

        String sql = """
            SELECT esr.estoque_est_cod,
                   esr.quantidade,
                   p.prod_descr,
                   e.data_validade
            FROM estoque_saida_refeicao esr
            JOIN estoque e ON e.est_cod = esr.estoque_est_cod
            JOIN produto p ON p.prod_cod = e.produto_prod_cod
            WHERE esr.saida_refeicao_id_saida_refeicao = ?
        """;

        List<SaidaRefeicao.ItemDTO> lista = new ArrayList<>();

        for (Map<String,Object> r : c.consultar(sql, idSaida)) {

            SaidaRefeicao.ItemDTO i = new SaidaRefeicao.ItemDTO();
            i.estCod = (Integer) r.get("estoque_est_cod");
            i.quantidade = (Integer) r.get("quantidade");
            i.produtoNome = (String) r.get("prod_descr");

            var d = r.get("data_validade");
            if (d instanceof java.sql.Date dd) i.validade = dd.toLocalDate();

            lista.add(i);
        }

        return lista;
    }
}
