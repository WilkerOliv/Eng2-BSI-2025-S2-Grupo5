package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class NecessidadeCestaId implements Serializable {
    private String pessoaCpf;
    private Integer cestaId;

    public String getPessoaCpf() { return pessoaCpf; }
    public void setPessoaCpf(String pessoaCpf) { this.pessoaCpf = pessoaCpf; }

    public Integer getCestaId() { return cestaId; }
    public void setCestaId(Integer cestaId) { this.cestaId = cestaId; }
}