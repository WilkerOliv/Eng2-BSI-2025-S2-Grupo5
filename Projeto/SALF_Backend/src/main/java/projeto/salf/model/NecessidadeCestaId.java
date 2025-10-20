package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NecessidadeCestaId implements Serializable {

    @Column(name = "pessoa_carente_pc_cpf", length = 14)
    private String pessoaCarentePcCpf;

    @Column(name = "cesta_basica_cb_cod")
    private Integer cestaBasicaCbCod;

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public Integer getCestaBasicaCbCod() { return cestaBasicaCbCod; }
    public void setCestaBasicaCbCod(Integer cestaBasicaCbCod) { this.cestaBasicaCbCod = cestaBasicaCbCod; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NecessidadeCestaId)) return false;
        NecessidadeCestaId that = (NecessidadeCestaId) o;
        return Objects.equals(pessoaCarentePcCpf, that.pessoaCarentePcCpf) &&
                Objects.equals(cestaBasicaCbCod, that.cestaBasicaCbCod);
    }

    @Override
    public int hashCode() { return Objects.hash(pessoaCarentePcCpf, cestaBasicaCbCod); }
}
