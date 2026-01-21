package br.com.salao.api.controllers;
import br.com.salao.api.dto.ProfissionalDTO;
import br.com.salao.api.models.Profissional;
import br.com.salao.api.repositories.ProfissionalRepository;
import br.com.salao.api.services.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {
    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @GetMapping
    public ResponseEntity<List<Profissional>> listarTodos(){
        return ResponseEntity.ok(profissionalService.listarTodosProfissionais());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Profissional> criar(@Valid @RequestBody ProfissionalDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.criarOuAtualizar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> buscarProfissionalPorId(@PathVariable Long id){
        return profissionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
