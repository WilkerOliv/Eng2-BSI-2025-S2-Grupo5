package projetoSalf.mvc.util;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {
    }

    public static boolean conectar() {
        conexao = new Conexao();
        boolean status = conexao.conectar("jdbc:postgresql://localhost:5432/", "salf_db", "postgres", "postgres");
        if (!status) {
            System.err.println("Falha ao conectar: " + conexao.getMensagemErro());
            conexao = null;
        }
        return status;
    }

    public static Conexao getConexao() {
        try {
            if (conexao == null || conexao.getConnect() == null || conexao.getConnect().isClosed()) {
                System.out.println("🔄 Recriando conexão com o banco...");
                conectar(); // chama o método de cima para reconectar
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar ou restabelecer conexão: " + e.getMessage());
            conectar();
        }
        return conexao;
    }
}
