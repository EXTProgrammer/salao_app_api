package br.com.salao.api.services;
import br.com.salao.api.dto.AgendamentoRequestDTO;
import br.com.salao.api.models.Agendamento;
import br.com.salao.api.models.Profissional;
import br.com.salao.api.models.Servico;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.AgendamentoRepository;
import br.com.salao.api.repositories.ProfissionalRepository;
import br.com.salao.api.repositories.ServicoRepository;
import br.com.salao.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendaProfissional(String email, LocalDate data){
        Usuario userPr = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(LocalTime.MAX);

        if ("ROLE_ADMIN".equals(userPr.getRole()))
            return agendamentoRepository.findByDataInicioBetweenOrderByDataInicioAsc(inicioDia, fimDia);

        Optional<Profissional> profissional = profissionalRepository.findByUsuarioId(userPr.getId());
        if (profissional.isEmpty())
            return new ArrayList<>();

        return agendamentoRepository.findByProfissionalIdAndDataInicioBetweenOrderByDataInicioAsc(profissional.get().getId(), inicioDia, fimDia);
    }

    @Transactional
    public void alterarStatus(Long id, String newStatus, String loggedEmail){
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        boolean isAdmin = "ROLE_ADMIN".equals(loggedUser.getRole());
        boolean isProfissional = "ROLE_PROFESSIONAL".equals(loggedUser.getRole()) &&
                agendamento.getProfissional().getUsuario().getId().equals(loggedUser.getId());

        if (!isAdmin && !isProfissional)
            throw new RuntimeException("Você não tem permissão para alterar o status de agendamento.");

        if ("CANCELADO".equalsIgnoreCase(agendamento.getStatus()))
            throw new RuntimeException("Não é possível alterar um atendimento cancelado.");

        agendamento.setStatus(newStatus);
        agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listAllMyAgendamentos(String loggedEmail){
        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return agendamentoRepository.findByClienteIdOrderByDataInicioAsc(loggedUser.getId());
    }

    @Transactional
    public void cancelarAgendamentoCliente(Long id, String loggedEmail){
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        boolean isCliente = agendamento.getCliente().getId().equals(loggedUser.getId());
        boolean isAdmin = "ROLE_ADMIN".equals(loggedUser.getRole());

        boolean isProfissional = "ROLE_PROFESSIONAL".equals(loggedUser.getRole()) &&
                agendamento.getProfissional().getUsuario().getId().equals(loggedUser.getId());

        if (!isCliente && !isAdmin && !isProfissional)
            throw new RuntimeException("Você não tem permissão para cancelar este agendamento.");

        if ("CONFIRMADO".equalsIgnoreCase(agendamento.getStatus()))
            throw new RuntimeException("Este horário já foi confirmado pelo salão. " +
                    "Por favor, entre em contato via telefone/WhatsApp para cancelar.");

        if ("CANCELADO".equalsIgnoreCase(agendamento.getStatus()) || "CONCLUIDO".equalsIgnoreCase(agendamento.getStatus()))
            throw new RuntimeException("Este agendamento já foi encerrado.");

        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }

    public List<String> consultarHorarios(LocalDate data, Long profissionalId, Integer duracao){
        LocalTime abertura = LocalTime.of(8, 0);
        LocalTime fechamento = LocalTime.of(20, 0);

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);
        List<Agendamento> agendamentosMarcados =
                agendamentoRepository.findByProfissionalIdAndDataInicioBetweenOrderByDataInicioAsc(profissionalId, inicioDia, fimDia);

        List<String> horariosLivres = new ArrayList<>();
        LocalTime horarioAnalise = abertura;

        while(horarioAnalise.plusMinutes(duracao).isBefore(fechamento.plusMinutes(1))){
            LocalDateTime inicioTentativa = data.atTime(horarioAnalise);
            LocalDateTime fimTentativa = inicioTentativa.plusMinutes(duracao);

            if (data.equals(LocalDate.now()) && inicioTentativa.isBefore(LocalDateTime.now())){
                horarioAnalise = horarioAnalise.plusMinutes(15);
                continue;
            }

            boolean conflito = false;
            for (Agendamento existente : agendamentosMarcados){
                if ("CANCELADO".equalsIgnoreCase(existente.getStatus())) continue;

                LocalDateTime inicioExistente = existente.getDataInicio();
                LocalDateTime fimExistente = inicioExistente.plusMinutes(existente.getServico().getDuracaoMin());

                if (inicioTentativa.isBefore(fimExistente) && fimTentativa.isAfter(inicioExistente)){
                    conflito = true;
                    break;
                }
            }

            if (!conflito)
                horariosLivres.add(horarioAnalise.toString());

            horarioAnalise = horarioAnalise.plusMinutes(15);
        }

        return horariosLivres;
    }

    public List<Agendamento> buscarTodosAgendamentos(String loggedMail){
        Usuario usuario = usuarioRepository.findByEmail(loggedMail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if ("ROLE_ADMIN".equals(usuario.getRole()))
            return agendamentoRepository.findByDataInicioAfterOrderByDataInicioAsc(LocalDateTime.now().with(LocalTime.MIN));

        if("ROLE_PROFESSIONAL".equals(usuario.getRole()))
            return agendamentoRepository.findByProfissionalIdAndDataInicioAfterOrderByDataInicioAsc(usuario.getId(), LocalDateTime.now().with(LocalTime.MIN));

        throw new RuntimeException("Acesso negado. Você não tem permissão para executar esta ação.");
    }

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequestDTO dto, String loggedEmail){
        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Sessão Inválida."));

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        Profissional profissional = profissionalRepository.findById(dto.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

        LocalDateTime dataInicio = dto.getDataInicio();
        LocalDateTime dataFim = dataInicio.plusMinutes(servico.getDuracaoMin());
        validarRegrasAgendamento(dto.getProfissionalId(), dto.getDataInicio(), dataFim);

        Agendamento newSchedule = new Agendamento();
        newSchedule.setStatus("PENDENTE");
        newSchedule.setProfissional(profissional);
        newSchedule.setServico(servico);
        newSchedule.setDataInicio(dataInicio);
        newSchedule.setDataFim(dataFim);
        definirCliente(newSchedule, dto, loggedUser);

        return agendamentoRepository.save(newSchedule);
    }

    private void validarRegrasAgendamento(Long professionalId, LocalDateTime dataInicio, LocalDateTime dataFim){
        if (dataInicio.isAfter(LocalDateTime.now().plusYears(1)))
            throw new RuntimeException("Não é permitido fazer agendamentos com mais de 1 ano de antecedência.");

        if (agendamentoRepository.existeConflito(professionalId, dataInicio, dataFim))
            throw new RuntimeException("Já existe um agendamento para este profissional neste horário.");
    }

    private void definirCliente(Agendamento agendamento, AgendamentoRequestDTO dto, Usuario loggedUser){
        if ("ROLE_CLIENTE".equals(loggedUser.getRole())){
            if (dto.getClienteId() != null && !loggedUser.getId().equals(dto.getClienteId()))
                throw new RuntimeException("Você não tem permissão para agendar para outros clientes.");

            agendamento.setCliente(loggedUser);
        } else {
            if (dto.getClienteId() != null){
                Usuario clienteCadastrado = usuarioRepository.findById(dto.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Usuário informado não foi encontrado."));
                agendamento.setCliente(clienteCadastrado);
            } else if (dto.getNomeClienteAvulso() != null && !dto.getNomeClienteAvulso().trim().isEmpty()) {
                agendamento.setCliente(null);
                agendamento.setNomeClienteAvulso(dto.getNomeClienteAvulso());
                agendamento.setTelefoneClienteAvulso(dto.getTelefoneClienteAvulso());
            } else {
                agendamento.setCliente(loggedUser);
            }
        }

    }
}
