package br.com.salao.api.repositories;
import br.com.salao.api.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {}
