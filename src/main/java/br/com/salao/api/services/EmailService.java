package br.com.salao.api.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviarEmailRec(String destinatario, String codigo){
        try{
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject("Recuperação de Senha - Maria Cabelereira");
            mensagem.setText("Olá!\n\n" +
                    "Recebemos um pedido para redefinir sua senha.\n" +
                    "Seu código de recuperação é: " + codigo + "\n\n" +
                    "Se você não solicitou isso, ignore este e-mail.");

            javaMailSender.send(mensagem);

            System.out.println("E-mail enviado com sucesso para " + destinatario);
        } catch (Exception e){
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
            throw new RuntimeException("Erro ao enviar e-mail de recuperação.");
        }

    }
}
