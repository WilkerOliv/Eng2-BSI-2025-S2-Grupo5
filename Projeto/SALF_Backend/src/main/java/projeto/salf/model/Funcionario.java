package projeto.salf.model;

public class Funcionario {
    private String funcCpf;
    private String funcNome;
    private String funcSenha;
    private String funcEmail;
    private String funcTelefone;
    // campos adicionais não necessários nas rotas simples; acrescente se quiser

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
}
