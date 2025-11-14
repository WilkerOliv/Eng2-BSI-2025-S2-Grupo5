package projeto.salf.model;

import jakarta.persistence.*;
import projeto.salf.dao.CotacaoDAO;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cotacao")
public class Cotacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotacao")
    private Integer idCotacao;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_fechamento", nullable = false)
    private LocalDate dataFechamento;

    public Integer getIdCotacao() { return idCotacao; }
    public void setIdCotacao(Integer idCotacao) { this.idCotacao = idCotacao; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }

    // ------------------------------------------
    //    MÉTODO QUE CHAMA O DAO
    // ------------------------------------------
    public List<Cotacao> getListaCotacao(Connection conn) {
        CotacaoDAO dao = new CotacaoDAO();
        return dao.getCotacao(conn);
    }
}
