package br.com.salao.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AvaliacaoDTO {
    @NotNull(message = "A nota é obrigatória.")
    @Min(message = "A nota mínima é de 1 estrela.", value = 1)
    @Max(message = "A nota máxima é de 5 estrelas.", value = 5)
    private Integer nota;

    private String comentario;

    //Metodo
    public AvaliacaoDTO(){}

    //Metodos especiais
    public void setNota(Integer nota){
        this.nota = nota;
    }

    public Integer getNota(){
        return this.nota;
    }

    public void setComentario(String comentario){
        this.comentario = comentario;
    }

    public String getComentario(){
        return this.comentario;
    }
}
