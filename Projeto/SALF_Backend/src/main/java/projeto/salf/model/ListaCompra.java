package projeto.salf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lista_compra")
public class ListaCompra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lc_cod")
    private Integer lcCod;

    @Column(name = "funcionario_func_cpf", nullable = false, length = 14)
    private String funcionarioFuncCpf;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

    @Column(name = "status_atendimento", nullable = false)
    private Integer statusAtendimento;

    @OneToMany(mappedBy = "listaCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensDaLista> itens;

    public ListaCompra() {}

    public Integer getLcCod() {
        return lcCod;
    }

    public void setLcCod(Integer lcCod) {
        this.lcCod = lcCod;
    }

    public String getFuncionarioFuncCpf() {
        return funcionarioFuncCpf;
    }

    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) {
        this.funcionarioFuncCpf = funcionarioFuncCpf;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getStatusAtendimento() {
        return statusAtendimento;
    }

    public void setStatusAtendimento(Integer statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }

    public List<ItensDaLista> getItens() {
        return itens;
    }

    public void setItens(List<ItensDaLista> itens) {
        this.itens = itens;
    }
}
