package br.com.salao.api.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ServicoDTO {
    @NotBlank(message = "Você deve informar o nome do serviço!")
    private String nome_servico;

    private String descricao;

    @Positive(message = "O preço do serviço deve ser positivo!")
    private BigDecimal preco;

    @Min(15)
    private Integer duracaoMin;

    //Métodos
    public ServicoDTO(){}

    //Métodos Especiais
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
