package br.com.salao.api.dto;

public class RegisterResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String role;

    //Metodos
    public RegisterResponseDTO(){}

    public RegisterResponseDTO(Long id, String nome, String email, String telefone, String role){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.role = role;
    }

    //Metodos Especiais
    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getTelefone(){
        return this.telefone;
    }

    public void setRole(){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }
}
