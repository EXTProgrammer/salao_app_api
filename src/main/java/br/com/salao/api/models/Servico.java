package br.com.salao.api.models;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome_servico;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer duracaoMin;

    //Métodos
    public Servico(){}

    public Servico(String nome_servico, String descricao, BigDecimal preco, Integer duracaoMin){
        this.nome_servico = nome_servico;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMin = duracaoMin;
    }

    //Métodos Especiais
    public Long getId(){
        return this.id;
    }

    public void setId(Long id){this.id = id;}

    public void setNome_servico(String nome_servico){
        this.nome_servico = nome_servico;
    }

    public String getNome_servico(){
        return this.nome_servico;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public void setPreco(BigDecimal preco){
        this.preco = preco;
    }

    public BigDecimal getPreco(){
        return this.preco;
    }

    public void setDuracaoMin(Integer duracaoMin){
        this.duracaoMin = duracaoMin;
    }

    public Integer getDuracaoMin(){
        return this.duracaoMin;
    }
}
