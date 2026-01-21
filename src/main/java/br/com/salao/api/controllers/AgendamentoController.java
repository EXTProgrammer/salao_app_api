package br.com.salao.api.controllers;
import br.com.salao.api.dto.AgendamentoRequestDTO;
import br.com.salao.api.models.Agendamento;
import br.com.salao.api.services.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {
    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> criarAgendamento(@Valid @RequestBody AgendamentoRequestDTO dto, Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criarAgendamento(dto, authentication.getName()));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<Agendamento>> meusAgendamentos(Authentication authentication){
        List<Agendamento> lista = agendamentoService.listAllMyAgendamentos(authentication.getName());
        lista.forEach(agendamento -> {
            if (agendamento.getCliente() != null) agendamento.getCliente().setSenha(null);
            if (agendamento.getProfissional() != null && agendamento.getProfissional().getUsuario() != null){
                agendamento.getProfissional().getUsuario().setSenha(null);
            }
        });

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/agenda-profissional")
    public ResponseEntity<List<Agendamento>> profissionalAgenda(Authentication authentication, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate){
        List<Agendamento> listaPro = agendamentoService.listarAgendaProfissional(authentication.getName(), localDate);
        listaPro.forEach(agendamento -> {
            if (agendamento.getCliente() != null) agendamento.getCliente().setSenha(null);
            if (agendamento.getProfissional() != null && agendamento.getProfissional().getUsuario() != null){
                agendamento.getProfissional().getUsuario().setSenha(null);
            }
        });

        return ResponseEntity.ok(listaPro);
    }
}
