package br.com.salao.api.dto;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProfissionalDTO {

    @NotNull(message = "Você deve selecionar um usuário para promover.")
    private Long usuarioId;

    @Size(min = 1, message = "O profissional deve ter ao menos uma especialidade.")
    private List<String> especialidades;

    //Métodos
    public ProfissionalDTO(){}

    //Métodos Especiais
    public void setUsuarioId(Long usuarioId){
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId(){
        return this.usuarioId;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public List<String> getEspecialidades(){
        return this.especialidades;
    }
}
