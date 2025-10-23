package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parametrizacao")
public class Parametrizacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametrizacao")
    private Integer idParametrizacao;

    @Column(name = "razao_social", nullable = false, length = 90)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false, length = 90)
    private String nomeFantasia;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "site", nullable = false, length = 60)
    private String site;

    @Column(name = "email", nullable = false, length = 40)
    private String email;

    @Column(name = "rua", length = 45)
    private String rua;

    @Column(name = "bairro", length = 45)
    private String bairro;

    @Column(name = "cidade", length = 45)
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name="logotipo_small", columnDefinition="text")
    private String logotipoSmall;
    @Column(name="logotipo_big", columnDefinition="text")
    private String logotipoBig;



    public Integer getIdParametrizacao() {
        return idParametrizacao;
    }

    public void setIdParametrizacao(Integer idParametrizacao) {
        this.idParametrizacao = idParametrizacao;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogotipoSmall() {
        return logotipoSmall;
    }

    public void setLogotipoSmall(String logotipoSmall) {
        this.logotipoSmall = logotipoSmall;
    }

    public String getLogotipoBig() {
        return logotipoBig;
    }

    public void setLogotipoBig(String logotipoBig) {
        this.logotipoBig = logotipoBig;
    }
}
