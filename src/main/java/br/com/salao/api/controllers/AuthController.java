package br.com.salao.api.controllers;
import br.com.salao.api.dto.*;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import br.com.salao.api.security.JwtTokenProvider;
import br.com.salao.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDto){
        UsernamePasswordAuthenticationToken logData = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
        Authentication auth = authenticationManager.authenticate(logData);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenProvider.gerarToken(auth);
        Usuario user = usuarioRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Erro inesperado: Usuário existe mas não foi encontrado no banco."));

        LoginResponseDTO response = new LoginResponseDTO(token, user.getId(), user.getNome(), user.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> esqueciSenha(@Valid @RequestBody ForgotPasswordDTO dto){
        usuarioService.solicitarRecupSenha(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@Valid @RequestBody ResetPasswordDTO dto){
        usuarioService.redefinirSenha(dto.getEmail(), dto.getToken(), dto.getNewPass());
        return ResponseEntity.ok().build();
    }
}
