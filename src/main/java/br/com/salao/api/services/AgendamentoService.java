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
import java.util.List;

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
    public List<Agendamento> listarAgendamentos(String loggedEmail){
        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return agendamentoRepository.findByClienteIdOrderByDataInicioAsc(loggedUser.getId());
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendaProfissional(String email, LocalDate data){
        Usuario userPr = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Profissional Prof = profissionalRepository.findByUsuarioId(userPr.getId())
                .orElseThrow(() -> new RuntimeException("O usuário não está cadastrado como um profissional."));

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByProfissionalIdAndDataInicioBetweenOrderByDataInicioAsc(Prof.getId(), inicioDia, fimDia);
    }

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequestDTO dto, String loggedEmail){
        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Usuario scheduledUser;
        if (dto.getClienteId() != null)
            scheduledUser = usuarioRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Usuário informado não foi encontrado."));
        else
            scheduledUser = loggedUser;

        if ("ROLE_CLIENTE".equals(loggedUser.getRole()))
            if (!loggedUser.getId().equals(scheduledUser.getId()))
                throw new RuntimeException("Você não tem permissão para agendar para outros clientes.");

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        Profissional profissional = profissionalRepository.findById(dto.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

        LocalDateTime dataInicio = dto.getDataInicio();
        LocalDateTime dataFim = dataInicio.plusMinutes(servico.getDuracaoMin());

        if (agendamentoRepository.existeConflito(profissional.getId(), dataInicio, dataFim))
            throw new RuntimeException("Já existe um agendamento para este profissional neste horário.");

        Agendamento newSchedule = new Agendamento();
        newSchedule.setCliente(scheduledUser);
        newSchedule.setProfissional(profissional);
        newSchedule.setServico(servico);
        newSchedule.setStatus("PENDENTE");
        newSchedule.setDataInicio(dataInicio);
        newSchedule.setDataFim(dataFim);

        return agendamentoRepository.save(newSchedule);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listAllMyAgendamentos(String loggedEmail){
        Usuario loggedUser = usuarioRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return agendamentoRepository.findByClienteIdOrderByDataInicioAsc(loggedUser.getId());
    }
}
