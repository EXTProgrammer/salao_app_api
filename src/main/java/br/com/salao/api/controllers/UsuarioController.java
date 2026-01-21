package br.com.salao.api.controllers;
import br.com.salao.api.dto.AdminCreateUserDTO;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import br.com.salao.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Usuario> criarRegistro(@Valid @RequestBody Usuario usuario){
        Usuario newUser = usuarioService.registrar(usuario);
        newUser.setSenha(null);

        return ResponseEntity.ok(newUser);
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
}
