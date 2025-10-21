package projeto.salf.utils;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {}

    public static synchronized boolean conectar() {
        // Fecha a antiga se tiver quebrada/abERTA
        close();
        conexao = new Conexao();
        boolean status = conexao.conectar(
                "jdbc:postgresql://localhost:5432/",
                "salf_db",
                "postgres",
                "postgres123"
        );
        if (!status) {
            System.err.println("Falha ao conectar: " + conexao.getMensagemErro());
            conexao = null;
        }
        return status;
    }

    public static synchronized Conexao getConexao() {
        try {
            if (conexao == null || !conexao.getEstadoConexao()) {
                System.out.println("🔄 Recriando conexão com o banco...");
                conectar();
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar/restabelecer conexão: " + e.getMessage());
            conectar();
        }
        return conexao;
    }

    public static synchronized void close() {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        } finally {
            conexao = null;
        }
    }

    static {
        // Fecha limpo ao encerrar a JVM
        Runtime.getRuntime().addShutdownHook(new Thread(SingletonDB::close));
    }
}
