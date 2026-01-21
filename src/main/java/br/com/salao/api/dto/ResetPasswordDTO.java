package br.com.salao.api.dto;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {

    @NotBlank
    private String token;

    @NotBlank(message = "Você deve redefinir a senha!")
    private String newPass;

    //métodos
    public ResetPasswordDTO(){}

    //métodos especiais
    public void setToken(String token){this.token = token;}

    public String getToken(){return this.token;}

    public void setNewPass(String newPass){this.newPass = newPass;}

    public String getNewPass(){return this.newPass;}
}
