package br.com.salao.api.dto;

public class LoginResponseDTO {
    private String token;
    private Long usuarioId;
    private String nome;
    private String role;

    //Métodos
    public LoginResponseDTO(String token, Long usuarioId, String nome, String role){
        this.token = token;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.role = role;
    }

    //Métodos Especiais
    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setUsuarioId(Long usuarioId){
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId(){
        return this.usuarioId;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
