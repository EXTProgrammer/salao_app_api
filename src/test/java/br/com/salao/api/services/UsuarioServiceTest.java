package br.com.salao.api.services;
import br.com.salao.api.dto.AdminCreateUserDTO;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void criptografarSenhaEDefinirRole(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setNome("Maria");
        usuarioEntrada.setEmail("maria@gmail.com");
        usuarioEntrada.setSenha("12121212");

        when(passwordEncoder.encode(usuarioEntrada.getSenha())).thenReturn("$2a$10$HASH_CRIPTOGRAFADO");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(50L);
            return u;
        });

        Usuario resultado = usuarioService.registrar(usuarioEntrada);

        assertEquals("$2a$10$HASH_CRIPTOGRAFADO", resultado.getSenha());
        assertEquals("ROLE_CLIENTE", resultado.getRole());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void criarClientePorAdminSemEmail(){
        AdminCreateUserDTO dto = new AdminCreateUserDTO();
        dto.setNome("Cliente Telefone");
        dto.setTelefone("11999998888");
        dto.setEmail(null);

        when(usuarioRepository.findByTelefone(dto.getTelefone())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("HASH_SENHA_ALEATORIA");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.AdminCriaCliente(dto);

        assertEquals("11999998888@email.salao", resultado.getEmail());
        assertEquals("ROLE_CLIENTE", resultado.getRole());
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
