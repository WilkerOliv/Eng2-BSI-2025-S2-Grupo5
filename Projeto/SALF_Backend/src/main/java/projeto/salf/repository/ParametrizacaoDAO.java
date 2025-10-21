package projeto.salf.repository;

import org.springframework.stereotype.Repository;
import projeto.salf.model.Parametrizacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class ParametrizacaoDAO {


    public boolean existeRegistro(Parametrizacao pa){

        String email = pa.getEmail();

        String SQL =  "SELECT * FROM parametrizacao where email = ?";

        try{
            Connection conn = SingletonDB.getConexao().getConnect();

            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, pa.getEmail());

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        }catch(Exception e){
            throw new RuntimeException(e);
        }


    }

    public void gravar(Parametrizacao pa) {
        String sql = "INSERT INTO parametrizacao (" +
                "razao_social, nome_fantasia, telefone, site, email, rua, bairro, cidade, uf, cep, logotipo_small, logotipo_big" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        try (Connection con = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1,  pa.getRazaoSocial());
            stmt.setString(2,  pa.getNomeFantasia());
            stmt.setString(3,  pa.getTelefone());
            stmt.setString(4,  pa.getSite());
            stmt.setString(6,  pa.getRua());
            stmt.setString(7,  pa.getBairro());
            stmt.setString(8,  pa.getCidade());
            stmt.setString(9,  pa.getUf());
            stmt.setString(10, pa.getCep());
            stmt.setString(11, pa.getLogotipoSmall());
            stmt.setString(12, pa.getLogotipoBig());
            stmt.setString(13, pa.getEmail());

            if (stmt.executeUpdate() > 0) return pa;
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean ExisteEmpresas() {
        String SQL = "SELECT COUNT(*) FROM parametrizacao";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

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
        String SQL = "SELECT * FROM parametrizacao WHERE pa_email = ?";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Parametrizacao pa = new Parametrizacao();


                return pa;
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização por email", e);
        }

        return null;
    }
}
