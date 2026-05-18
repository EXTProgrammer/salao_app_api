package br.com.salao.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileUpdateDTO {
    @NotBlank(message = "O nome não pode estar vazio.")
    private String nome;

    @NotBlank(message = "O telefone não pode estar vazio.")
    @Size(min = 11, max = 15, message = "Telefone inválido.")
    private String telefone;

    public ProfileUpdateDTO(){}

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getTelefone(){
        return this.telefone;
    }
}
