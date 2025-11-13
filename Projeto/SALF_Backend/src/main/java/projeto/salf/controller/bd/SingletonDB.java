package projeto.salf.controller.bd;


public class SingletonDB {

    private static Conexao conexao;

    private static final String URL  = "jdbc:postgresql://localhost:5432/salf_db";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    private SingletonDB() { }

    // Abre a conexão caso ainda não exista
    private static synchronized void conectar() {
        if (conexao == null || !conexao.getEstadoConexao()) {
            conexao = new Conexao(URL, USER, PASS);
        }
    }


    public static Conexao getConexao() {
        if (conexao == null || !conexao.getEstadoConexao()) {
            conectar();
        }
        return conexao;
    }


    public static synchronized void close() {
        if (conexao != null && conexao.getEstadoConexao()) {
            conexao.close();
        }
        conexao = null;
    }
}
