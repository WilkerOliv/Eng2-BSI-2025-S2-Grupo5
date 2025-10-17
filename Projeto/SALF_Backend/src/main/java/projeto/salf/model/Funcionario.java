package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {
    @Id
    @Column(length = 14)
    private String funcCpf;

    @Column(nullable = false, length = 60)
    private String funcNome;

    @Column(nullable = false, length = 20)
    private String funcSenha;

    @Column(nullable = false, length = 30)
    private String funcEmail;

    @Column(nullable = false, length = 20)
    private String funcTelefone;

    @Column(nullable = false)
    private Integer tipoAcesso;

    @Temporal(TemporalType.DATE)
    private Date dataAdmissao;

    @Temporal(TemporalType.DATE)
    private Date dataDemissao;

    private String rua;
    private String bairro;
    private String cidade;
    private String username;
    private String uf;
    private String cep;
    private String cargo;

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

    public Date getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(Date dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public Date getDataDemissao() { return dataDemissao; }
    public void setDataDemissao(Date dataDemissao) { this.dataDemissao = dataDemissao; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}
