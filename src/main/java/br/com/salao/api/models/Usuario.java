package br.com.salao.api.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiryDate;

    //Métodos
    public Usuario(){}

    public Usuario(String nome, String email, String telefone, String senha, String role, boolean ativo){
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.role = role;
        this.ativo = ativo;
    }

    //Métodos Especiais
    public Long getId(){
        return this.id;
    }

    public void setId(Long id){this.id = id;}

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
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

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getSenha(){
        return this.senha;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }

    public boolean isAtivo(){return this.ativo;}

    public void setAtivo(boolean ativo){this.ativo = ativo;}

    public void setResetToken(String resetToken){this.resetToken = resetToken;}

    public String getResetToken(){return this.resetToken;}

    public void setResetTokenExpiryDate(LocalDateTime resetTokenExpiryDate){
        this.resetTokenExpiryDate = resetTokenExpiryDate;
    }

    public LocalDateTime getResetTokenExpiryDate(){return this.resetTokenExpiryDate;}
}
