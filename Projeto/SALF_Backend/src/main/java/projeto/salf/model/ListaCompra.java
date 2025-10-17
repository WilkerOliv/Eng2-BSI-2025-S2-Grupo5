package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/* ListaCompra */
@Entity
@Table(name = "lista_compra")
public class ListaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lc_cod")
    private Integer lcCod;

    @Column(name = "funcionario_func_cpf", length = 18, nullable = false)
    private String funcionarioFuncCpf; // coluna presente no script; relação FK definida em outro local

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "descricao", length = 45, nullable = false)
    private String descricao;

    @Column(name = "status_atendimento", nullable = false)
    private Integer statusAtendimento;

    @Column(name = "funcionario_func_cpf1", length = 14, nullable = false)
    private String funcionarioFuncCpf1; // campo FK para funcionario (script tinha duplicidade)

    @OneToMany(mappedBy = "listaCompra")
    private List<ItensDaLista> itens;

    public ListaCompra() {}

    public ListaCompra(Integer lcCod, String funcionarioFuncCpf, LocalDate dataCriacao, String descricao,
                       Integer statusAtendimento, String funcionarioFuncCpf1) {
        this.lcCod = lcCod;
        this.funcionarioFuncCpf = funcionarioFuncCpf;
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
        this.statusAtendimento = statusAtendimento;
        this.funcionarioFuncCpf1 = funcionarioFuncCpf1;
    }

    public Integer getLcCod() { return lcCod; }
    public void setLcCod(Integer lcCod) { this.lcCod = lcCod; }

    public String getFuncionarioFuncCpf() { return funcionarioFuncCpf; }
    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) { this.funcionarioFuncCpf = funcionarioFuncCpf; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getStatusAtendimento() { return statusAtendimento; }
    public void setStatusAtendimento(Integer statusAtendimento) { this.statusAtendimento = statusAtendimento; }

    public String getFuncionarioFuncCpf1() { return funcionarioFuncCpf1; }
    public void setFuncionarioFuncCpf1(String funcionarioFuncCpf1) { this.funcionarioFuncCpf1 = funcionarioFuncCpf1; }

    public List<ItensDaLista> getItens() { return itens; }
    public void setItens(List<ItensDaLista> itens) { this.itens = itens; }
}
