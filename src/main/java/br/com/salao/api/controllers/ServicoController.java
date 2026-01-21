package br.com.salao.api.controllers;
import br.com.salao.api.dto.ServicoDTO;
import br.com.salao.api.models.Servico;
import br.com.salao.api.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<Servico>> listar(){
        List<Servico> lista = servicoService.listarTodosServicos();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Servico> criarServico(@Valid @RequestBody ServicoDTO dto){
        Servico newServico = new Servico();
        newServico.setNome_servico(dto.getNome_servico());
        newServico.setDescricao(dto.getDescricao());
        newServico.setPreco(dto.getPreco());
        newServico.setDuracaoMin(dto.getDuracaoMin());

        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarServico(newServico));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @Valid @RequestBody ServicoDTO dto){
        Servico dadosParaAtualizar = new Servico();
        dadosParaAtualizar.setNome_servico(dto.getNome_servico());
        dadosParaAtualizar.setDescricao(dto.getDescricao());
        dadosParaAtualizar.setPreco(dto.getPreco());
        dadosParaAtualizar.setDuracaoMin(dto.getDuracaoMin());

        return ResponseEntity.ok(servicoService.atualizarServico(id, dadosParaAtualizar));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id){
        servicoService.deletarServico(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id){
        return servicoService.buscarServicoPorID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
