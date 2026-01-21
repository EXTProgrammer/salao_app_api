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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Test
    void criarQuandoNaoHaConflito(){
        String email = "cliente@gmail.com";
        LocalDateTime data = LocalDateTime.now().plusDays(1);

        AgendamentoRequestDTO dto = new AgendamentoRequestDTO();
        dto.setProfissionalId(6L);
        dto.setServicoId(5L);
        dto.setDataInicio(data);

        Usuario cliente = new Usuario();
        cliente.setId(3L);
        cliente.setRole("ROLE_CLIENTE");

        Profissional pro = new Profissional();
        pro.setId(6L);

        Servico servico = new Servico();
        servico.setId(5L);
        servico.setDuracaoMin(60);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(cliente));
        when(profissionalRepository.findById(pro.getId())).thenReturn(Optional.of(pro));
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));

        when(agendamentoRepository.existeConflito(any(), any(), any())).thenReturn(false);

        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(i -> {
            Agendamento a = i.getArgument(0);
            a.setId(999L);
            return a;
        });

        Agendamento resultado = agendamentoService.criarAgendamento(dto, email);

        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
        assertEquals("PENDENTE", resultado.getStatus());
        assertEquals(data.plusMinutes(60), resultado.getDataFim());

        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    void impedirCasoConflito(){
        String email = "cliente@teste.com";
        LocalDateTime data = LocalDateTime.now();

        AgendamentoRequestDTO dto = new AgendamentoRequestDTO();
        dto.setProfissionalId(10L);
        dto.setDataInicio(data);
        dto.setServicoId(20L);

        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setRole("ROLE_CLIENTE");

        Profissional pro = new Profissional();
        pro.setId(10L);

        Servico servico = new Servico();
        servico.setId(20L);
        servico.setDuracaoMin(60);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(cliente));
        when(profissionalRepository.findById(pro.getId())).thenReturn(Optional.of(pro));
        when(servicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));

        when(agendamentoRepository.existeConflito(any(), any(), any())).thenReturn(true);

        RuntimeException error = assertThrows(RuntimeException.class, () -> {
            agendamentoService.criarAgendamento(dto, email);
        });

        assertEquals("Já existe um agendamento para este profissional neste horário.", error.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }
}
