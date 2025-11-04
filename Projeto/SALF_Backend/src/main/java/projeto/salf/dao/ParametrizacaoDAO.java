package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.model.Parametrizacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class ParametrizacaoDAO {

    public boolean existeRegistro(Parametrizacao pa) {
        String SQL = "SELECT 1 FROM parametrizacao WHERE email = ?";

        Connection conn = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, pa.getEmail());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void gravar(Parametrizacao pa) {
        String sql = "INSERT INTO parametrizacao (" +
                "razao_social, nome_fantasia, telefone, site, email, rua, bairro, cidade, uf, cep, logotipo_small, logotipo_big" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // pega conexão singleton, não fecha
        Connection conn = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!ExisteEmpresas()) {
                stmt.setString(1,  pa.getRazaoSocial());
                stmt.setString(2,  pa.getNomeFantasia());
                stmt.setString(3,  pa.getTelefone());
                stmt.setString(4,  pa.getSite());
                stmt.setString(5,  pa.getEmail());
                stmt.setString(6,  pa.getRua());
                stmt.setString(7,  pa.getBairro());
                stmt.setString(8,  pa.getCidade());
                stmt.setString(9,  pa.getUf());
                stmt.setString(10, pa.getCep());
                stmt.setString(11, pa.getLogotipoSmall());
                stmt.setString(12, pa.getLogotipoBig());

                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Parametrizacao alterar(Parametrizacao pa) {
        String sql = "UPDATE parametrizacao SET " +
                "razao_social = ?, nome_fantasia = ?, telefone = ?, site = ?, " +
                "rua = ?, bairro = ?, cidade = ?, uf = ?, cep = ?, " +
                "logotipo_small = ?, logotipo_big = ? " +
                "WHERE email = ?";

        Connection con = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1,  pa.getRazaoSocial());
            stmt.setString(2,  pa.getNomeFantasia());
            stmt.setString(3,  pa.getTelefone());
            stmt.setString(4,  pa.getSite());
            stmt.setString(5,  pa.getRua());
            stmt.setString(6,  pa.getBairro());
            stmt.setString(7,  pa.getCidade());
            stmt.setString(8,  pa.getUf());
            stmt.setString(9,  pa.getCep());
            stmt.setString(10, pa.getLogotipoSmall());
            stmt.setString(11, pa.getLogotipoBig());
            stmt.setString(12, pa.getEmail()); // WHERE email = ?

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                return pa;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar parametrização", e);
        }
    }

    public boolean ExisteEmpresas() {
        String SQL = "SELECT COUNT(*) FROM parametrizacao";

        Connection con = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = con.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Parametrizacao getRegistroEmail(String email) {
        String SQL = "SELECT * FROM parametrizacao WHERE email = ?";

        Connection con = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = con.prepareStatement(SQL)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Parametrizacao pa = new Parametrizacao();

                    // ⚠️ Aqui tinha bug no seu código:
                    // você tava fazendo pa.setEmail(rs.getString("razao_social"));
                    // Isso tá invertido. Arrumei tudo abaixo:

                    pa.setRazaoSocial(rs.getString("razao_social"));
                    pa.setNomeFantasia(rs.getString("nome_fantasia"));
                    pa.setTelefone(rs.getString("telefone"));
                    pa.setSite(rs.getString("site"));
                    pa.setEmail(rs.getString("email"));
                    pa.setRua(rs.getString("rua"));
                    pa.setBairro(rs.getString("bairro"));
                    pa.setCidade(rs.getString("cidade"));
                    pa.setUf(rs.getString("uf"));
                    pa.setCep(rs.getString("cep"));
                    pa.setLogotipoSmall(rs.getString("logotipo_small"));
                    pa.setLogotipoBig(rs.getString("logotipo_big"));

                    return pa;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização por email", e);
        }

        return null;
    }

    public Parametrizacao getUnicaEmp() {
        String SQL = "SELECT * FROM parametrizacao LIMIT 1";

        Connection con = SingletonDB.getConexao().getConnect();

        try (PreparedStatement stmt = con.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Parametrizacao pa = new Parametrizacao();
                pa.setRazaoSocial(rs.getString("razao_social"));
                pa.setNomeFantasia(rs.getString("nome_fantasia"));
                pa.setTelefone(rs.getString("telefone"));
                pa.setSite(rs.getString("site"));
                pa.setEmail(rs.getString("email"));
                pa.setRua(rs.getString("rua"));
                pa.setBairro(rs.getString("bairro"));
                pa.setCidade(rs.getString("cidade"));
                pa.setUf(rs.getString("uf"));
                pa.setCep(rs.getString("cep"));
                pa.setLogotipoSmall(rs.getString("logotipo_small"));
                pa.setLogotipoBig(rs.getString("logotipo_big"));
                return pa;
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização única", e);
        }

        return null;
    }
}
