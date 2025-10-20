package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

    @Id
    @Column(name = "func_cpf", length = 14)
    private String funcCpf;

    @Column(name = "func_nome", nullable = false, length = 60)
    private String funcNome;

    @Column(name = "func_senha", nullable = false, length = 20)
    private String funcSenha;

    @Column(name = "func_email", nullable = false, length = 30)
    private String funcEmail;

    @Column(name = "func_telefone", nullable = false, length = 20)
    private String funcTelefone;

    @OneToMany
    @JoinColumn(name = "funcionario_func_cpf", referencedColumnName = "func_cpf")
    private List<ListaCompra> listasCompra;

    public Funcionario() {}

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

    public List<ListaCompra> getListasCompra() { return listasCompra; }
    public void setListasCompra(List<ListaCompra> listasCompra) { this.listasCompra = listasCompra; }
}
