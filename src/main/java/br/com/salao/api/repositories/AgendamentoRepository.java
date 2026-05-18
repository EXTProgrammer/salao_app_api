package br.com.salao.api.repositories;
import br.com.salao.api.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
    List<Agendamento> findByClienteIdOrderByDataInicioAsc(Long clienteId);
    List<Agendamento> findByProfissionalIdAndDataInicioBetweenOrderByDataInicioAsc(Long profissionalId, LocalDateTime inicioDia, LocalDateTime fimDia);
    List<Agendamento> findByDataInicioBetweenOrderByDataInicioAsc(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByDataInicioAfterOrderByDataInicioAsc(LocalDateTime data);
    List<Agendamento> findByProfissionalIdAndDataInicioAfterOrderByDataInicioAsc(Long id, LocalDateTime data);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
           "WHERE a.profissional.id = :profissionalId " +
           "AND ((a.dataInicio < :fim AND a.dataFim > :inicio))")
    boolean existeConflito(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
            );
}
