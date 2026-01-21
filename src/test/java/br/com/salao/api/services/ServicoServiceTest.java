package br.com.salao.api.services;
import br.com.salao.api.models.Servico;
import br.com.salao.api.repositories.ServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicoServiceTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @Test
    void criarServicoComSucesso(){
        Servico servicoEntrada = new Servico();
        servicoEntrada.setNome_servico("Corte");
        servicoEntrada.setPreco(BigDecimal.valueOf(50.00));

        Servico servicoSaida = new Servico();
        servicoSaida.setId(1L);
        servicoSaida.setNome_servico("Corte");
        servicoSaida.setPreco(BigDecimal.valueOf(50.00));

        when(servicoRepository.save(any(Servico.class))).thenReturn(servicoSaida);

        Servico resultado = servicoService.criarServico(servicoEntrada);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Corte", resultado.getNome_servico());

        verify(servicoRepository).save(any(Servico.class));
    }

}
