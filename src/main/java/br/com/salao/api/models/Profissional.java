package br.com.salao.api.models;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "profissionais")
public class Profissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ElementCollection
            @CollectionTable(name = "profissional_especialidades", joinColumns = @JoinColumn(name = "profissional_id"))
            @Column(name = "especialidade")
    private List<String> especialidades = new ArrayList<>();

    //Métodos
    public Profissional(){}

    public Profissional(Usuario usuario, List<String> especialidades){
        this.usuario = usuario;
        this.especialidades = especialidades;
    }

    //Métodos Especiais
    public Long getId(){
        return this.id;
    }

    public void setId(Long id){this.id = id;}

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Usuario getUsuario(){
        return this.usuario;
    }

    public List<String> getEspecialidades(){
        return this.especialidades;
    }

    public void setEspecialidades(List<String> especialidades){
        this.especialidades = especialidades;
    }

    public void addEspecialidade(String especialidade){
        this.especialidades.add(especialidade);
    }

    public void removeEspecialidade(String especialidade){
        this.especialidades.remove(especialidade);
    }
}
