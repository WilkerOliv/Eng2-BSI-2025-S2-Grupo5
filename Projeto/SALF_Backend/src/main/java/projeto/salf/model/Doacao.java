package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doacao")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doa_cod")
    private Integer doaCod;

    @Column(name = "pessoacarente_pc_cpf", length = 18, nullable = false)
    private String pessoaCarentePcCpf; // script used varchar(18 for FK)

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "observacao", length = 100)
    private String observacao;

    public Doacao() {}

    public Doacao(Integer doaCod, String pessoaCarentePcCpf, LocalDate data, String observacao) {
        this.doaCod = doaCod;
        this.pessoaCarentePcCpf = pessoaCarentePcCpf;
        this.data = data;
        this.observacao = observacao;
    }

    public Integer getDoaCod() { return doaCod; }
    public void setDoaCod(Integer doaCod) { this.doaCod = doaCod; }

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
