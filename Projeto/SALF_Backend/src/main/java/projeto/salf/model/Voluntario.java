//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "voluntario")
//public class Voluntario implements Serializable {
//
//    @Id
//    @Column(name = "vol_cpf", length = 14)
//    private String volCpf;
//
//    @Column(name = "vol_nome", nullable = false, length = 60)
//    private String volNome;
//
//    @Column(name = "vol_telefone", nullable = false, length = 20)
//    private String volTelefone;
//
//    @Column(name = "rua", nullable = false, length = 45)
//    private String rua;
//
//    @Column(name = "bairro", nullable = false, length = 45)
//    private String bairro;
//
//    @Column(name = "cidade", nullable = false, length = 45)
//    private String cidade;
//
//    @Column(name = "email", nullable = false, length = 45)
//    private String email;
//
//    @Column(name = "tipo_acesso", nullable = false)
//    private Integer tipoAcesso;
//
//    @Column(name = "senha", nullable = false, length = 30)
//    private String senha;
//
//    @Column(name = "data_fim_voluntario")
//    private LocalDate dataFimVoluntario;
//
//    @Column(name = "data_inicio_voluntario", nullable = false)
//    private LocalDate dataInicioVoluntario;
//
//    @Column(name = "uf", nullable = false, length = 2)
//    private String uf;
//
//    @Column(name = "cep", nullable = false, length = 10)
//    private String cep;
//
//    @Column(name = "username", nullable = false, length = 20)
//    private String username;
//
//    public String getVolCpf() {
//        return volCpf;
//    }
//
//    public void setVolCpf(String volCpf) {
//        this.volCpf = volCpf;
//    }
//
//    public String getVolNome() {
//        return volNome;
//    }
//
//    public void setVolNome(String volNome) {
//        this.volNome = volNome;
//    }
//
//    public String getVolTelefone() {
//        return volTelefone;
//    }
//
//    public void setVolTelefone(String volTelefone) {
//        this.volTelefone = volTelefone;
//    }
//
//    public String getRua() {
//        return rua;
//    }
//
//    public void setRua(String rua) {
//        this.rua = rua;
//    }
//
//    public String getBairro() {
//        return bairro;
//    }
//
//    public void setBairro(String bairro) {
//        this.bairro = bairro;
//    }
//
//    public String getCidade() {
//        return cidade;
//    }
//
//    public void setCidade(String cidade) {
//        this.cidade = cidade;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public Integer getTipoAcesso() {
//        return tipoAcesso;
//    }
//
//    public void setTipoAcesso(Integer tipoAcesso) {
//        this.tipoAcesso = tipoAcesso;
//    }
//
//    public String getSenha() {
//        return senha;
//    }
//
//    public void setSenha(String senha) {
//        this.senha = senha;
//    }
//
//    public LocalDate getDataFimVoluntario() {
//        return dataFimVoluntario;
//    }
//
//    public void setDataFimVoluntario(LocalDate dataFimVoluntario) {
//        this.dataFimVoluntario = dataFimVoluntario;
//    }
//
//    public LocalDate getDataInicioVoluntario() {
//        return dataInicioVoluntario;
//    }
//
//    public void setDataInicioVoluntario(LocalDate dataInicioVoluntario) {
//        this.dataInicioVoluntario = dataInicioVoluntario;
//    }
//
//    public String getUf() {
//        return uf;
//    }
//
//    public void setUf(String uf) {
//        this.uf = uf;
//    }
//
//    public String getCep() {
//        return cep;
//    }
//
//    public void setCep(String cep) {
//        this.cep = cep;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//}
