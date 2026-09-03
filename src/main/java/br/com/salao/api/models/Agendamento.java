package br.com.salao.api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false)
    private String status;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(name = "nome_cliente_avulso")
    private String nomeClienteAvulso;

    @Column(name = "telefone_cliente_avulso")
    private String telefoneClienteAvulso;

    @Column(name = "nota_avaliacao")
    private Integer notaAvaliacao;

    @Column(name = "comentario_avaliacao")
    private String comentarioAvaliacao;

    //Métodos
    public Agendamento(){}

    public Agendamento(Usuario cliente, Profissional profissional, Servico servico, String status, LocalDateTime dataInicio, LocalDateTime dataFim){
        this.cliente = cliente;
        this.profissional = profissional;
        this.servico = servico;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    //Métodos Especiais
    public Long getId(){
        return this.id;
    }

    public void setId(Long id){this.id = id;}

    public void setCliente(Usuario cliente){
        this.cliente = cliente;
    }

    public Usuario getCliente(){
        return this.cliente;
    }

    public void setProfissional(Profissional profissional){
        this.profissional = profissional;
    }

    public Profissional getProfissional(){
        return this.profissional;
    }

    public void setServico(Servico servico){
        this.servico = servico;
    }

    public Servico getServico(){
        return this.servico;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }

    public void setDataInicio(LocalDateTime dataInicio){
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataInicio(){
        return this.dataInicio;
    }

    public void setDataFim(LocalDateTime dataFim){
        this.dataFim = dataFim;
    }

    public LocalDateTime getDataFim(){
        return this.dataFim;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getVersion(){
        return this.version;
    }

    public void setNomeClienteAvulso(String clienteAvulso){
        this.nomeClienteAvulso = clienteAvulso;
    }

    public String getNomeClienteAvulso(){
        return this.nomeClienteAvulso;
    }

    public void setTelefoneClienteAvulso(String telefoneAvulso){
        this.telefoneClienteAvulso = telefoneAvulso;
    }

    public String getTelefoneClienteAvulso(){
        return this.telefoneClienteAvulso;
    }

    public void setNotaAvaliacao(Integer notaAvaliacao){
        this.notaAvaliacao = notaAvaliacao;
    }

    public Integer getNotaAvaliacao(){
        return this.notaAvaliacao;
    }

    public void setComentarioAvaliacao(String comentarioAvaliacao){
        this.comentarioAvaliacao = comentarioAvaliacao;
    }

    public String getComentarioAvaliacao(){
        return this.comentarioAvaliacao;
    }
}
