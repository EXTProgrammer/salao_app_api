package br.com.salao.api.repositories;
import br.com.salao.api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefone(String telefone);
    List<Usuario> findByNomeContainingOrTelefoneContaining(String nome, String telefone);
    Optional<Usuario> findByResetToken(String resetToken);
}
