package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;

@Repository
public class EstoqueDAO {





    public boolean insereItensEstoque(int quantidade, LocalDate validade, int produtoCod, Connection conn) {

        try {
            // 1) Verifica se já existe (comparação correta para DATE e TIMESTAMP)
            final String SQL_CHECK =
                    "SELECT est_prod_quantidade FROM estoque " +
                            "WHERE produto_prod_cod = ? " +
                            (validade != null ? "AND data_validade::date = ?" : "AND data_validade IS NULL");

            PreparedStatement check = conn.prepareStatement(SQL_CHECK);
            check.setInt(1, produtoCod);

            if (validade != null) {
                check.setDate(2, java.sql.Date.valueOf(validade));
            }

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Já existe → UPDATE somando quantidade
                int qtdAtual = rs.getInt("est_prod_quantidade");
                int novaQtd = qtdAtual + quantidade;

                final String SQL_UPDATE =
                        "UPDATE estoque SET est_prod_quantidade = ? " +
                                "WHERE produto_prod_cod = ? " +
                                (validade != null ? "AND data_validade::date = ?" : "AND data_validade IS NULL");

                PreparedStatement upd = conn.prepareStatement(SQL_UPDATE);
                upd.setInt(1, novaQtd);
                upd.setInt(2, produtoCod);

                if (validade != null) {
                    upd.setDate(3, java.sql.Date.valueOf(validade));
                }

                return upd.executeUpdate() > 0;

            } else {
                // Não existe → INSERT normal
                final String SQL_INSERT =
                        "INSERT INTO estoque (est_prod_quantidade, produto_prod_cod, data_validade) VALUES (?,?,?)";

                PreparedStatement ins = conn.prepareStatement(SQL_INSERT);

                ins.setInt(1, quantidade);
                ins.setInt(2, produtoCod);

                if (validade != null) {
                    ins.setDate(3, java.sql.Date.valueOf(validade));
                } else {
                    ins.setNull(3, java.sql.Types.DATE);
                }

                return ins.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerItensEstoque(int quantidade, LocalDate validade, int produtoCod, Connection conn) {

        try {
            // 1) Verifica estoque existente
            final String SQL_CHECK =
                    "SELECT est_prod_quantidade FROM estoque " +
                            "WHERE produto_prod_cod = ? " +
                            (validade != null ? "AND data_validade::date = ?" : "AND data_validade IS NULL");

            PreparedStatement check = conn.prepareStatement(SQL_CHECK);
            check.setInt(1, produtoCod);

            if (validade != null)
                check.setDate(2, java.sql.Date.valueOf(validade));

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                // Nada para remover
                return true;
            }

            int qtdAtual = rs.getInt("est_prod_quantidade");

            int novaQtd = qtdAtual - quantidade;
            if (novaQtd < 0) novaQtd = 0;

            // 2) Se zerou → excluir linha
            if (novaQtd == 0) {
                final String SQL_DEL =
                        "DELETE FROM estoque WHERE produto_prod_cod = ? " +
                                (validade != null ? "AND data_validade::date = ?" : "AND data_validade IS NULL");

                PreparedStatement del = conn.prepareStatement(SQL_DEL);
                del.setInt(1, produtoCod);
                if (validade != null)
                    del.setDate(2, java.sql.Date.valueOf(validade));

                return del.executeUpdate() > 0;
            }

            // 3) Caso contrário → atualizar quantidade
            final String SQL_UPD =
                    "UPDATE estoque SET est_prod_quantidade = ? " +
                            "WHERE produto_prod_cod = ? " +
                            (validade != null ? "AND data_validade::date = ?" : "AND data_validade IS NULL");

            PreparedStatement upd = conn.prepareStatement(SQL_UPD);
            upd.setInt(1, novaQtd);
            upd.setInt(2, produtoCod);

            if (validade != null)
                upd.setDate(3, java.sql.Date.valueOf(validade));

            return upd.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }








}
