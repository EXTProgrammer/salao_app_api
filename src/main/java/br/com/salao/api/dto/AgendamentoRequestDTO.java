package br.com.salao.api.dto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "Você deve informar o cliente.")
    private Long clienteId;

    @NotNull(message = "Você deve informar o profissional.")
    private Long profissionalId;

    @NotNull(message = "Você deve informar o serviço.")
    private Long servicoId;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    private LocalDateTime dataInicio;

    //Métodos
    public AgendamentoRequestDTO(){}

    //Métodos Especiais
    public void setClienteId(Long clienteId){
        this.clienteId = clienteId;
    }

    public Long getClienteId(){
        return this.clienteId;
    }

    public void setProfissionalId(Long profissionalId){
        this.profissionalId = profissionalId;
    }

    public Long getProfissionalId(){
        return this.profissionalId;
    }

    public void setServicoId(Long servicoId){
        this.servicoId = servicoId;
    }

    public Long getServicoId(){
        return this.servicoId;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataInicio(){
        return this.dataInicio;
    }
}
