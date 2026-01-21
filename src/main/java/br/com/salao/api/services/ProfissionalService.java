package br.com.salao.api.services;
import br.com.salao.api.dto.ProfissionalDTO;
import br.com.salao.api.models.Profissional;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.ProfissionalRepository;
import br.com.salao.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {
    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Profissional> listarTodosProfissionais(){
        return profissionalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Profissional> buscarProfissionalPorId(Long id){
        return profissionalRepository.findById(id);
    }

    @Transactional
    public Profissional criarOuAtualizar(ProfissionalDTO dto){
        Usuario userF = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow( () -> new RuntimeException("Usuário não encontrado com o ID: " + dto.getUsuarioId()));

        if (!userF.getRole().equals("ROLE_PROFISSIONAL")){
            userF.setRole("ROLE_PROFISSIONAL");
            usuarioRepository.save(userF);
        }

        Optional<Profissional> profF = profissionalRepository.findByUsuarioId(userF.getId());

        Profissional profissional;
        if (profF.isPresent())
            profissional = profF.get();
        else {
            profissional = new Profissional();
            profissional.setUsuario(userF);
        }
        profissional.setEspecialidades(dto.getEspecialidades());

        return profissionalRepository.save(profissional);
    }
}
