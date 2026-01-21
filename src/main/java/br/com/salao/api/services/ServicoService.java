package br.com.salao.api.services;
import br.com.salao.api.models.Servico;
import br.com.salao.api.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    //Métodos
    @Transactional(readOnly = true)
    public List<Servico> listarTodosServicos(){
        return servicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Servico> buscarServicoPorID(Long id){
        return servicoRepository.findById(id);
    }

    @Transactional
    public Servico criarServico(Servico servico){
        return servicoRepository.save(servico);
    }

    @Transactional
    public Servico atualizarServico(Long id, Servico dadosAtualizados){
        Optional<Servico> servicoExistOpt = servicoRepository.findById(id);

        if (servicoExistOpt.isPresent()){
            Servico servicoExist = servicoExistOpt.get();

            servicoExist.setNome_servico(dadosAtualizados.getNome_servico());
            servicoExist.setDescricao(dadosAtualizados.getDescricao());
            servicoExist.setPreco(dadosAtualizados.getPreco());
            servicoExist.setDuracaoMin(dadosAtualizados.getDuracaoMin());

            return servicoRepository.save(servicoExist);
        } else
            throw new RuntimeException("Serviço não encontrado com o ID: " + id);
    }

    @Transactional
    public void deletarServico(Long id){
        if (servicoRepository.existsById(id)){
            servicoRepository.deleteById(id);
        } else
            throw new RuntimeException("Não foi possível deletar: Serviço não encontrado.");
    }
}
