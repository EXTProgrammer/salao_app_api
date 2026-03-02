package br.com.salao.api.services;
import br.com.salao.api.dto.AdminCreateUserDTO;
import br.com.salao.api.dto.RegisterRequestDTO;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    //Métodos

    @Transactional
    public Usuario registrar(RegisterRequestDTO dto){
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent())
            throw new RuntimeException("Este email já está cadastrado.");

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setRole("ROLE_CLIENTE");
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario AdminCriaCliente(AdminCreateUserDTO dto){
        if (usuarioRepository.findByTelefone(dto.getTelefone()).isPresent())
            throw new RuntimeException("Este telefone já está cadastrado para outro cliente.");

        Usuario newUser = new Usuario();
        newUser.setNome(dto.getNome());
        newUser.setTelefone(dto.getTelefone());

        if (dto.getEmail() != null && !dto.getEmail().isBlank()){
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent())
               throw new RuntimeException("Este email já está cadastrado.");
            else
                newUser.setEmail(dto.getEmail());
        } else {
            String genMail = dto.getTelefone() + "@email.salao";
            newUser.setEmail(genMail);
        }

        String randomPass = UUID.randomUUID().toString();
        newUser.setSenha(passwordEncoder.encode(randomPass));

        newUser.setRole("ROLE_CLIENTE");

        return usuarioRepository.save(newUser);
    }

    @Transactional
    public void desativarUsuario(Long id){
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setAtivo(false);
        usuarioRepository.save(user);
    }

    @Transactional
    public void solicitarRecupSenha(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("E-mail não encontrado."));

        String codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        usuario.setResetToken(codigo);
        usuario.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        emailService.enviarEmailRec(email, codigo);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha){
        Usuario userToken = usuarioRepository.findByResetToken(token)
                .orElseThrow(()-> new RuntimeException("Token inválido."));
        if (userToken.getResetTokenExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expirado.");

        userToken.setSenha(passwordEncoder.encode(novaSenha));

        userToken.setResetToken(null);
        userToken.setResetTokenExpiryDate(null);
        usuarioRepository.save(userToken);
    }
}
