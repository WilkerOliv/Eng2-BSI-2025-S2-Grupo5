package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/* Cotacao */
@Entity
@Table(name = "cotacao")
public class Cotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcotacao")
    private Integer idCotacao;

    @Column(name = "dataabertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "datafechamento", nullable = false)
    private LocalDate dataFechamento;

    public Cotacao() {}

    public Cotacao(Integer idCotacao, LocalDate dataAbertura, LocalDate dataFechamento) {
        this.idCotacao = idCotacao;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
    }

    public Integer getIdCotacao() { return idCotacao; }
    public void setIdCotacao(Integer idCotacao) { this.idCotacao = idCotacao; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }
}
