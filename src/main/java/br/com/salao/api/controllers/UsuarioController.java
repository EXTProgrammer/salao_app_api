package br.com.salao.api.controllers;
import br.com.salao.api.dto.AdminCreateUserDTO;
import br.com.salao.api.dto.ProfileUpdateDTO;
import br.com.salao.api.dto.RegisterRequestDTO;
import br.com.salao.api.dto.RegisterResponseDTO;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import br.com.salao.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<RegisterResponseDTO> registrar(@RequestBody RegisterRequestDTO registerDto){
        Usuario usuario = usuarioService.registrar(registerDto);

        RegisterResponseDTO response = new RegisterResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/criar-cliente")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<Usuario> adminCriaCliente(@Valid @RequestBody AdminCreateUserDTO dto){
        Usuario aUser = usuarioService.AdminCriaCliente(dto);
        aUser.setSenha(null);

        return ResponseEntity.ok(aUser);
    }

    @GetMapping("/pesquisar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<List<Usuario>> pesquisar(@RequestParam String termo){
        List<Usuario> resultados = usuarioRepository.findByNomeContainingOrTelefoneContaining(termo, termo);
        resultados.forEach(usuario -> usuario.setSenha(null));

        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/eu")
    public ResponseEntity<Usuario> meuPerfil(Authentication auth){
        Usuario usuario = usuarioRepository.findByEmail(auth.getName()).get();
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/buscar-por-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email){
        Usuario resultado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado com este e-mail."));

        resultado.setSenha(null);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Usuario> atualizacaoPerfil(@Valid @RequestBody ProfileUpdateDTO dto, Authentication authentication){
        String loggedMail = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(loggedMail)
                .orElseThrow(() -> new RuntimeException("Sessão Inválida."));

        Usuario atualizado = usuarioService.atualizarPerfil(dto, usuario.getId());
        atualizado.setSenha(null);
        return ResponseEntity.ok(atualizado);
    }
}
