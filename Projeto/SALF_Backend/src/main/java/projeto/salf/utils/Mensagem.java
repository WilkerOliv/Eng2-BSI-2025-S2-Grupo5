package projeto.salf.utils;

public class Mensagem {
    private String mensagem;
    private String tipo; // sucesso, erro, aviso
    private int codigo;  // opcional: código HTTP ou interno

    public Mensagem(String mensagem, boolean b) {
        this.mensagem = mensagem;
        this.tipo = "info";
        this.codigo = 0;
    }

    public Mensagem(String mensagem, String tipo) {
        this.mensagem = mensagem;
        this.tipo = tipo;
    }

    public Mensagem(String mensagem, String tipo, int codigo) {
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /** Gera mensagem de sucesso */
    public static Mensagem sucesso(String texto) {
        return new Mensagem(texto, "sucesso", 200);
    }

    /** Gera mensagem de erro */
    public static Mensagem erro(String texto) {
        return new Mensagem(texto, "erro", 500);
    }

    /** Gera mensagem de aviso */
    public static Mensagem aviso(String texto) {
        return new Mensagem(texto, "aviso", 300);
    }

    @Override
    public String toString() {
        return "[" + tipo.toUpperCase() + "] " + mensagem + " (código: " + codigo + ")";
    }


    public boolean isSucesso() {
    }
}
