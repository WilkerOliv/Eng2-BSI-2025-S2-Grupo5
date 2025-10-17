package projeto.salf.model;

import jakarta.persistence.*;

/* Fornecedor */
@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfornecedor")
    private Integer idFornecedor;

    @Column(name = "nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "email", length = 45, nullable = false)
    private String email;

    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @Column(name = "contato", length = 45, nullable = false)
    private String contato;

    @Column(name = "descricao", length = 45, nullable = false)
    private String descricao;

    public Fornecedor() {}

    public Fornecedor(Integer idFornecedor, String nome, String email, String telefone, String contato, String descricao) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.contato = contato;
        this.descricao = descricao;
    }

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
}