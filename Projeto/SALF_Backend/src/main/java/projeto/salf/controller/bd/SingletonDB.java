//package projeto.salf.controller.bd;
//
//public class SingletonDB {
//    private static Conexao conexao = null;
//
//    private SingletonDB() {}
//
//    public static synchronized boolean conectar() {
//        close(); // garante que não há outra aberta
//        conexao = new Conexao();
//        boolean status = conexao.conectar(
//                "jdbc:postgresql://localhost:5432/",
//                "salf_db",
//                "postgres",
//                "postgres"
//        );
//        if (!status) {
//            System.err.println("Falha ao conectar: " + conexao.getMensagemErro());
//            conexao = null;
//        } else {
//            System.out.println("✅ Conectado ao banco com sucesso!");
//        }
//        return status;
//    }
//
//    public static synchronized Conexao getConexao() {
//        return conexao;
//    }
//
//    public static synchronized void close() {
//        try {
//            if (conexao != null) {
//                conexao.close();
//                System.out.println("🔒 Conexão fechada com sucesso.");
//            }
//        } catch (Exception e) {
//            System.err.println("Erro ao fechar conexão: " + e.getMessage());
//        } finally {
//            conexao = null;
//        }
//    }
//
//    static {
//        Runtime.getRuntime().addShutdownHook(new Thread(SingletonDB::close));
//    }
//}

package projeto.salf.controller.bd;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {}

    public static synchronized Conexao getConexao() {
        return conexao;
    }

    public static synchronized boolean conectar() {
        try {
            if (conexao == null || !conexao.getEstadoConexao()) {
                conexao = new Conexao();
                boolean conectado = conexao.conectar(
                        "jdbc:postgresql://localhost:5432/",
                        "salf_db",
                        "postgres",
                        "postgres"
                );
                if (!conectado) {
                    System.err.println("❌ Falha ao conectar: " + conexao.getMensagemErro());
                    conexao = null;
                    return false;
                } else {
                    System.out.println("✅ Banco conectado com sucesso!");
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    public static synchronized void close() {
        if (conexao != null) {
            conexao.close();
            conexao = null;
            System.out.println("🔒 Conexão fechada (manual)");
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SingletonDB::close));
    }
}
