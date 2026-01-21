package br.com.salao.api.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordDTO {

    @Email
    @NotBlank(message = "O email deve ser incluído!")
    private String email;

    //métodos
    public ForgotPasswordDTO(){}

    //métodos especiais
    public void setEmail(String email){this.email = email;}

    public String getEmail(){return this.email;}
}
