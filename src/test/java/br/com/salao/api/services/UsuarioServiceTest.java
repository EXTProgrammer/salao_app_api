package br.com.salao.api.services;
import br.com.salao.api.dto.AdminCreateUserDTO;
import br.com.salao.api.dto.RegisterRequestDTO;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterRequestDTO registerRequestDTO;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp(){
       registerRequestDTO = new RegisterRequestDTO();
       registerRequestDTO.setNome("Fulano Silva");
       registerRequestDTO.setEmail("fulano.teste@gmail.com");
       registerRequestDTO.setTelefone("5099999999999");
       registerRequestDTO.setSenha("teste1234");

       usuarioMock = new Usuario();
       usuarioMock.setId(1L);
       usuarioMock.setNome("Fulano Silva");
       usuarioMock.setEmail("fulano.teste@gmail.com");
       usuarioMock.setTelefone("5099999999999");
       usuarioMock.setSenha("senhaCriptografada");
       usuarioMock.setRole("ROLE_CLIENTE");
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso a partir do DTO")
    void registrarUsuarioComSucesso(){
        when(usuarioRepository.findByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequestDTO.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        Usuario resultado = usuarioService.registrar(registerRequestDTO);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Fulano Silva", resultado.getNome());
        assertEquals("ROLE_CLIENTE", resultado.getRole());

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario usuarioCapturado = usuarioCaptor.getValue();

        assertEquals("Fulano Silva", usuarioCapturado.getNome());
        assertEquals("fulano.teste@gmail.com", usuarioCapturado.getEmail());
        assertEquals("5099999999999", usuarioCapturado.getTelefone());
        assertEquals("senhaCriptografada", usuarioCapturado.getSenha());
        assertEquals("ROLE_CLIENTE", usuarioCapturado.getRole());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar um e-mail já existente")
    void lancarExceptionQuandoEmailExiste(){
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail(registerRequestDTO.getEmail());

        when(usuarioRepository.findByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.of(usuarioExistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           usuarioService.registrar(registerRequestDTO);
        });

        assertEquals("Este email já está cadastrado.", exception.getMessage());

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
