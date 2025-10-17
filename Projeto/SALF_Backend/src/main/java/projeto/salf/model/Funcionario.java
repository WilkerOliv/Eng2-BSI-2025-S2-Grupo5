package projeto.salf.model;


import jakarta.persistence.*;
import java.time.LocalDate;

/* Funcionario */
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @Column(name = "func_cpf", length = 14)
    private String funcCpf;

    @Column(name = "func_nome", length = 60, nullable = false)
    private String funcNome;

    @Column(name = "func_senha", length = 20, nullable = false)
    private String funcSenha;

    @Column(name = "func_email", length = 30, nullable = false)
    private String funcEmail;

    @Column(name = "func_telefone", length = 20, nullable = false)
    private String funcTelefone;

    @Column(name = "tipo_acesso", nullable = false)
    private Integer tipoAcesso;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao", nullable = false)
    private LocalDate dataDemissao;

    @Column(name = "rua", length = 45, nullable = false)
    private String rua;

    @Column(name = "bairro", length = 45, nullable = false)
    private String bairro;

    @Column(name = "cidade", length = 45, nullable = false)
    private String cidade;

    @Column(name = "username", length = 20, nullable = false)
    private String userName;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;

    @Column(name = "cep", length = 10, nullable = false)
    private String cep;

    @Column(name = "cargp", length = 45, nullable = false)
    private String cargo;

    public Funcionario() {}

    public Funcionario(String funcCpf, String funcNome, String funcSenha, String funcEmail, String funcTelefone,
                       Integer tipoAcesso, LocalDate dataAdmissao, LocalDate dataDemissao, String rua, String bairro,
                       String cidade, String userName, String uf, String cep, String cargo) {
        this.funcCpf = funcCpf;
        this.funcNome = funcNome;
        this.funcSenha = funcSenha;
        this.funcEmail = funcEmail;
        this.funcTelefone = funcTelefone;
        this.tipoAcesso = tipoAcesso;
        this.dataAdmissao = dataAdmissao;
        this.dataDemissao = dataDemissao;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.userName = userName;
        this.uf = uf;
        this.cep = cep;
        this.cargo = cargo;
    }

    // getters e setters
    public String getFuncCpf() { return funcCpf; }
    public void setFuncCpf(String funcCpf) { this.funcCpf = funcCpf; }

    public String getFuncNome() { return funcNome; }
    public void setFuncNome(String funcNome) { this.funcNome = funcNome; }

    public String getFuncSenha() { return funcSenha; }
    public void setFuncSenha(String funcSenha) { this.funcSenha = funcSenha; }

    public String getFuncEmail() { return funcEmail; }
    public void setFuncEmail(String funcEmail) { this.funcEmail = funcEmail; }

    public String getFuncTelefone() { return funcTelefone; }
    public void setFuncTelefone(String funcTelefone) { this.funcTelefone = funcTelefone; }

    public Integer getTipoAcesso() { return tipoAcesso; }
    public void setTipoAcesso(Integer tipoAcesso) { this.tipoAcesso = tipoAcesso; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public LocalDate getDataDemissao() { return dataDemissao; }
    public void setDataDemissao(LocalDate dataDemissao) { this.dataDemissao = dataDemissao; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}