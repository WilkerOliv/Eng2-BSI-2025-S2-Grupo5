package projeto.salf.model;

import jakarta.persistence.*;
import projeto.salf.dao.FornecedorDAO;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Integer idFornecedor;

    @Column(name = "nome", nullable = false, length = 45)
    private String nome;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "contato", nullable = false, length = 45)
    private String contato;

    @Column(name = "descricao", length = 45)
    private String descricao;

    // GETTERS E SETTERS
    public Integer getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Integer idFornecedor) { this.idFornecedor = idFornecedor; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // -----------------------------------------
    //   MÉTODOS DE NEGÓCIO QUE CHAMAM O DAO
    // -----------------------------------------

    public List<Fornecedor> getListaFornecedores(Connection conn) {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.getAll(conn);
    }

    public Map<Integer, List<Fornecedor>> getListaFornecedoresPorCotacao(Connection conn) {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.getListaAllCotacao(conn);
    }
}
