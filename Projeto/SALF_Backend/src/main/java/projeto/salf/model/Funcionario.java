package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.FuncionarioDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class Funcionario {

    private String funcCpf;
    private String funcNome;
    private String funcSenha;
    private String funcEmail;
    private String funcTelefone;
    private Integer tipoAcesso;
    private Date dataAdmissao;
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


    public static class FuncionarioModel {

        private final FuncionarioDAO dao;
        private final Conexao conexao;

        public FuncionarioModel(Conexao conexao) {
            this.conexao = conexao;
            this.dao = new FuncionarioDAO(conexao); // 🔥 Model instancia o DAO
        }

        private static Date toSqlDate(Object o) {
            if (o == null) return null;
            String s = o.toString();
            if (s.isBlank()) return null;
            try {
                LocalDate ld = LocalDate.parse(s);
                return Date.valueOf(ld);
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        private static String toStr(Object o) {
            return o != null ? o.toString().trim() : null;
        }

        private Funcionario mapToFuncionario(Map<String, Object> dados) {
            Funcionario f = new Funcionario();

            f.setFuncCpf(toStr(dados.get("funcCpf")));
            f.setFuncNome(toStr(dados.get("funcNome")));
            f.setFuncSenha(toStr(dados.get("funcSenha")));
            f.setFuncEmail(toStr(dados.get("funcEmail")));
            f.setFuncTelefone(toStr(dados.get("funcTelefone")));
            f.setUsername(toStr(dados.get("username")));
            f.setCargo(toStr(dados.get("cargo")));

            String tipoAcessoStr = toStr(dados.get("tipoAcesso"));
            if (tipoAcessoStr != null && !tipoAcessoStr.isBlank()) {
                try { f.setTipoAcesso(Integer.parseInt(tipoAcessoStr)); }
                catch (Exception ignored) {}
            }

            f.setRua(toStr(dados.get("rua")));
            f.setBairro(toStr(dados.get("bairro")));
            f.setCidade(toStr(dados.get("cidade")));
            f.setUf(toStr(dados.get("uf")));
            f.setCep(toStr(dados.get("cep")));

            f.setDataAdmissao(toSqlDate(dados.get("dataAdmissao")));
            f.setDataDemissao(toSqlDate(dados.get("dataDemissao")));

            return f;
        }

        private String validar(Funcionario f, boolean isUpdate) {
            if (f.getFuncCpf() == null || f.getFuncCpf().isBlank())
                return "CPF é obrigatório";
            if (f.getFuncNome() == null || f.getFuncNome().isBlank())
                return "Nome é obrigatório";
            if (f.getFuncEmail() == null || f.getFuncEmail().isBlank())
                return "Email é obrigatório";
            if (f.getFuncSenha() == null || f.getFuncSenha().isBlank())
                return "Senha é obrigatória";
            if (f.getFuncTelefone() == null || f.getFuncTelefone().isBlank())
                return "Telefone é obrigatório";
            if (f.getUsername() == null || f.getUsername().isBlank())
                return "Username é obrigatório";
            if (f.getCargo() == null || f.getCargo().isBlank())
                return "Cargo é obrigatório";
            if (f.getTipoAcesso() == null)
                return "Tipo de acesso é obrigatório";
            if (f.getRua() == null || f.getRua().isBlank())
                return "Rua é obrigatória";
            if (f.getBairro() == null || f.getBairro().isBlank())
                return "Bairro é obrigatório";
            if (f.getCidade() == null || f.getCidade().isBlank())
                return "Cidade é obrigatória";
            if (f.getUf() == null || f.getUf().isBlank())
                return "UF é obrigatória";
            if (f.getCep() == null || f.getCep().isBlank())
                return "CEP é obrigatório";
            if (f.getDataAdmissao() == null)
                return "Data de admissão é obrigatória";

            return null;
        }

        public Map<String, Object> cadastrar(Map<String, Object> dados) {
            Funcionario f = mapToFuncionario(dados);

            String erro = validar(f, false);
            if (erro != null)
                return Map.of("sucesso", false, "mensagem", erro);

            try {
                conexao.iniciarTransacao();

                if (dao.buscarPorCpf(f.getFuncCpf()) != null) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Já existe funcionário com esse CPF");
                }

                Funcionario existeEmail = dao.buscarPorEmail(f.getFuncEmail());
                if (existeEmail != null) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Já existe funcionário com esse e-mail");
                }

                Funcionario existeUser = dao.buscarPorUsername(f.getUsername());
                if (existeUser != null) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Já existe funcionário com esse username");
                }

                boolean ok = dao.inserir(f);
                if (!ok) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Falha ao inserir funcionário");
                }

                conexao.commit();
                return Map.of("sucesso", true, "mensagem", "Funcionário cadastrado com sucesso!");

            } catch (Exception e) {
                conexao.rollback();
                return Map.of("sucesso", false, "mensagem", "Erro inesperado ao cadastrar funcionário");
            }
        }

        public Map<String, Object> atualizar(String cpf, Map<String, Object> dados) {
            Funcionario f = mapToFuncionario(dados);
            f.setFuncCpf(cpf);

            String erro = validar(f, true);
            if (erro != null)
                return Map.of("sucesso", false, "mensagem", erro);

            try {
                conexao.iniciarTransacao();

                Map<String, Object> atual = dao.buscarPorCpf(cpf);
                if (atual == null) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Funcionário não encontrado");
                }

                Funcionario existeEmail = dao.buscarPorEmail(f.getFuncEmail());
                if (existeEmail != null &&
                        existeEmail.getFuncCpf() != null &&
                        !existeEmail.getFuncCpf().equals(f.getFuncCpf())) {

                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Já existe funcionário com esse e-mail");
                }

                Funcionario existeUser = dao.buscarPorUsername(f.getUsername());
                if (existeUser != null &&
                        existeUser.getFuncCpf() != null &&
                        !existeUser.getFuncCpf().equals(f.getFuncCpf())) {

                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Já existe funcionário com esse username");
                }

                boolean ok = dao.atualizar(f);
                if (!ok) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Falha ao atualizar funcionário");
                }

                conexao.commit();
                return Map.of("sucesso", true, "mensagem", "Funcionário atualizado com sucesso!");

            } catch (Exception e) {
                conexao.rollback();
                return Map.of("sucesso", false, "mensagem", "Erro inesperado ao atualizar funcionário");
            }
        }

        public Map<String, Object> excluir(String cpf) {
            if (cpf == null || cpf.isBlank())
                return Map.of("sucesso", false, "mensagem", "CPF inválido");

            try {
                conexao.iniciarTransacao();

                Map<String, Object> row = dao.buscarPorCpf(cpf);
                if (row == null) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Funcionário não encontrado");
                }

                int tipo = 0;
                Object tipoObj = row.get("tipo_acesso");
                if (tipoObj instanceof Number n)
                    tipo = n.intValue();
                else if (tipoObj != null) {
                    try { tipo = Integer.parseInt(tipoObj.toString()); }
                    catch (Exception ignored) {}
                }

                if (tipo == 1) {
                    int qtdAdmins = dao.contarAdmins();
                    if (qtdAdmins <= 1) {
                        conexao.rollback();
                        return Map.of(
                                "sucesso", false,
                                "mensagem", "Não é possível excluir o único administrador do sistema."
                        );
                    }
                }

                boolean ok = dao.excluir(cpf);
                if (!ok) {
                    conexao.rollback();
                    return Map.of("sucesso", false, "mensagem", "Falha ao excluir funcionário");
                }

                conexao.commit();
                return Map.of("sucesso", true, "mensagem", "Funcionário excluído com sucesso");

            } catch (Exception e) {
                conexao.rollback();
                return Map.of("sucesso", false, "mensagem", "Erro inesperado ao excluir funcionário");
            }
        }

        public List<Map<String, Object>> listar() {
            return dao.listarTodos();
        }

        public Map<String, Object> buscarCpf(String cpf) {
            if (cpf == null || cpf.isBlank()) return null;
            return dao.buscarPorCpf(cpf);
        }
    }
}
