package br.com.salao.api.config;
import br.com.salao.api.models.Servico;
import br.com.salao.api.models.Usuario;
import br.com.salao.api.repositories.ServicoRepository;
import br.com.salao.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@Profile("dev")
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.initial-password:ADMIN_PASS}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@salao.com").isEmpty()){
            Usuario admin = new Usuario();
            admin.setNome("Comandante Supremo");
            admin.setEmail("admin@salao.com");
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.setRole("ROLE_ADMIN");
            admin.setTelefone("999999999");
            usuarioRepository.save(admin);
            System.out.println(">>> Admin criado: admin@salao.com / admin123");
        }

        if (usuarioRepository.findByEmail("clienteteste@salao.com").isEmpty()){
            Usuario cliente = new Usuario();
            cliente.setNome("Paciente 0");
            cliente.setEmail("clienteteste@salao.com");
            cliente.setSenha(passwordEncoder.encode("cliente456"));
            cliente.setRole("ROLE_CLIENTE");
            cliente.setTelefone("888888888");
            usuarioRepository.save(cliente);
            System.out.println(">>> cliente criado: clienteteste@salao.com / cliente456");
        }

        if (servicoRepository.count() == 0){
            Servico s1 = new Servico();
            s1.setNome_servico("Corte Feminino");
            s1.setDescricao("Especialização em cortes femininos, todos tipos de cabelos.");
            s1.setPreco(BigDecimal.valueOf(80.00));
            s1.setDuracaoMin(60);

            servicoRepository.save(s1);
            System.out.println(">>> Serviço inicial criado.");
        }
    }
}
