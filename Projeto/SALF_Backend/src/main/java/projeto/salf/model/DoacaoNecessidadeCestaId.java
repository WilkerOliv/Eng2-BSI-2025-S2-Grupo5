package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class DoacaoNecessidadeCestaId implements Serializable {

    @Column(name = "doacao_doa_cod")
    private Integer doacaoDoaCod;

    @Column(name = "necessidadecesta_pessoacarente_pc_cpf", length = 18)
    private String necessidadePessoaCpf;

    @Column(name = "necessidadecesta_cestabasica_cb_cod")
    private Integer necessidadeCestaCod;

    public DoacaoNecessidadeCestaId() {}

    public DoacaoNecessidadeCestaId(Integer doacaoDoaCod, String necessidadePessoaCpf, Integer necessidadeCestaCod) {
        this.doacaoDoaCod = doacaoDoaCod;
        this.necessidadePessoaCpf = necessidadePessoaCpf;
        this.necessidadeCestaCod = necessidadeCestaCod;
    }

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }

    public String getNecessidadePessoaCpf() { return necessidadePessoaCpf; }
    public void setNecessidadePessoaCpf(String necessidadePessoaCpf) { this.necessidadePessoaCpf = necessidadePessoaCpf; }

    public Integer getNecessidadeCestaCod() { return necessidadeCestaCod; }
    public void setNecessidadeCestaCod(Integer necessidadeCestaCod) { this.necessidadeCestaCod = necessidadeCestaCod; }
}
