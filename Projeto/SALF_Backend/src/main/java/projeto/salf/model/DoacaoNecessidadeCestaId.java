package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class DoacaoNecessidadeCestaId implements Serializable {
    private Integer doacaoDoaCod;
    private String necessidadeCestaPessoaCarentePcCpf;
    private Integer necessidadeCestaCestaBasicaCbCod;

    public DoacaoNecessidadeCestaId() {}
    public DoacaoNecessidadeCestaId(Integer a, String b, Integer c) { this.doacaoDoaCod = a; this.necessidadeCestaPessoaCarentePcCpf = b; this.necessidadeCestaCestaBasicaCbCod = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoacaoNecessidadeCestaId)) return false;
        DoacaoNecessidadeCestaId that = (DoacaoNecessidadeCestaId) o;
        return Objects.equals(doacaoDoaCod, that.doacaoDoaCod) &&
                Objects.equals(necessidadeCestaPessoaCarentePcCpf, that.necessidadeCestaPessoaCarentePcCpf) &&
                Objects.equals(necessidadeCestaCestaBasicaCbCod, that.necessidadeCestaCestaBasicaCbCod);
    }

    @Override
    public int hashCode() { return Objects.hash(doacaoDoaCod, necessidadeCestaPessoaCarentePcCpf, necessidadeCestaCestaBasicaCbCod); }

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer v) { this.doacaoDoaCod = v; }
    public String getNecessidadeCestaPessoaCarentePcCpf() { return necessidadeCestaPessoaCarentePcCpf; }
    public void setNecessidadeCestaPessoaCarentePcCpf(String v) { this.necessidadeCestaPessoaCarentePcCpf = v; }
    public Integer getNecessidadeCestaCestaBasicaCbCod() { return necessidadeCestaCestaBasicaCbCod; }
    public void setNecessidadeCestaCestaBasicaCbCod(Integer v) { this.necessidadeCestaCestaBasicaCbCod = v; }
}
