package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "parametrizacao")
public class Parametrizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idparametrizacao")
    private Integer idParametrizacao;

    @Column(name = "razaosocial", length = 90, nullable = false)
    private String razaoSocial;

    @Column(name = "nomefantasia", length = 90, nullable = false)
    private String nomeFantasia;

    @Column(name = "telefone", length = 20, nullable = false)
    private String telefone;

    @Column(name = "site", length = 60, nullable = false)
    private String site;

    @Column(name = "email", length = 40, nullable = false)
    private String email;

    @Column(name = "rua", length = 45)
    private String rua;

    @Column(name = "bairro", length = 45)
    private String bairro;

    @Column(name = "cidade", length = 45) // script had GENERATED ALWAYS — left as simple column
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "logotiposmall", length = 45)
    private String logotipoSmall;

    @Column(name = "logotipobig", length = 45)
    private String logotipoBig;

    public Parametrizacao() {}

    public Parametrizacao(Integer idParametrizacao, String razaoSocial, String nomeFantasia, String telefone, String site,
                          String email, String rua, String bairro, String cidade, String uf, String cep, String logotipoSmall,
                          String logotipoBig) {
        this.idParametrizacao = idParametrizacao;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.telefone = telefone;
        this.site = site;
        this.email = email;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.logotipoSmall = logotipoSmall;
        this.logotipoBig = logotipoBig;
    }

    public Integer getIdParametrizacao() { return idParametrizacao; }
    public void setIdParametrizacao(Integer idParametrizacao) { this.idParametrizacao = idParametrizacao; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogotipoSmall() { return logotipoSmall; }
    public void setLogotipoSmall(String logotipoSmall) { this.logotipoSmall = logotipoSmall; }

    public String getLogotipoBig() { return logotipoBig; }
    public void setLogotipoBig(String logotipoBig) { this.logotipoBig = logotipoBig; }
}
