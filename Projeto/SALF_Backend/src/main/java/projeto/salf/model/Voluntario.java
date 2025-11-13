package projeto.salf.model;

import java.time.LocalDate;

public class Voluntario {

    private String cpf; // Chave primária no modelo original
    private String nome;
    private String telefone;
    private String email;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private Integer tipoAcesso;
    private String senha;
    private String username;
    private LocalDate dataInicioVoluntario;
    private LocalDate dataFimVoluntario; // Indica inatividade se preenchido

    public Voluntario() {
    }

    // Getters e Setters (simplificados para o contexto)
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public Integer getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(Integer tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDataInicioVoluntario() {
        return dataInicioVoluntario;
    }

    public void setDataInicioVoluntario(LocalDate dataInicioVoluntario) {
        this.dataInicioVoluntario = dataInicioVoluntario;
    }

    public LocalDate getDataFimVoluntario() {
        return dataFimVoluntario;
    }

    public void setDataFimVoluntario(LocalDate dataFimVoluntario) {
        this.dataFimVoluntario = dataFimVoluntario;
    }

    public boolean isAtivo() {
        return dataFimVoluntario == null;
    }
}
