package br.com.salao.api.repositories;
import br.com.salao.api.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByUsuarioId(Long id);
}
