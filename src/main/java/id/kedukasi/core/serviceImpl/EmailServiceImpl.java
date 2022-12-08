package id.kedukasi.core.serviceImpl;

import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.service.EmailService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  TemplateEngine templateEngine;

  @Override
  public String sendSimpleMail(EmailDetails details) {
    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      javaMailSender.send(mailMessage);
      return "Mail Sent Successfully...";
    } catch (MailException e) {
      return "Error while Sending Mail";
    }
  }

  @Override
  public String sendMailWithAttachment(EmailDetails details) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper;
    try {
      mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(sender);
      mimeMessageHelper.setTo(details.getRecipient());
      mimeMessageHelper.setText(details.getMsgBody(), true);
      mimeMessageHelper.setSubject(details.getSubject());
      if (details.getAttachment() != null) {
        FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
        mimeMessageHelper.addAttachment(file.getFilename(), file);
      }

      javaMailSender.send(mimeMessage);
      return "Mail sent Successfully";
    } catch (MessagingException e) {
      return "Error while sending mail!!!";
    }
  }

  @Override
  public boolean sendRegisterMail(Map<String, String> filesUpload, PesertaServiceImpl.SetPenambahanData setPenambahanData, Peserta pesertabaru, String pathfile) throws IOException, MessagingException {
    InputStream imageIs = null;
    Path currentPath = Paths.get(".");
    Path absolutePath = currentPath.toAbsolutePath();
    String setPath = absolutePath + pathfile;
    Map<String,String> dataFile = new HashMap<>();
    Context context = new Context();
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

    context.setVariable("peserta", pesertabaru);
    context.setVariable("keteranganLain", setPenambahanData);

    for(Map.Entry<String, String> data : filesUpload.entrySet()){
//      logger.info("templates/" + filesUpload.get(data.getKey()));
//      File file = new File(setPath + filesUpload.get(data.getKey()));
//      Tika tika = new Tika();
//      String fileType = tika.detect(file);
//      dataFile.put(data.getValue(),fileType);
      context.setVariable("file" + data.getKey(),"https://0b7d-36-69-111-142.ap.ngrok.io/peserta/image-response-entity/" + data.getValue());
//      imageIs = this.getClass().getClassLoader().getResourceAsStream("templates/" + filesUpload.get(data.getKey()));
//      byte[] imageByteArray = IOUtils.toByteArray(imageIs);
//      final InputStreamSource imageSource = new ByteArrayResource(imageByteArray);
//      helper.addInline(data.getValue(), imageSource, fileType);
    }

    String process = templateEngine.process("register", context);
    helper.setSubject("Welcome " + pesertabaru.getNamaPeserta());
    helper.setText(process, true);
    helper.setTo(pesertabaru.getEmail());
    javaMailSender.send(mimeMessage);


    logger.info(dataFile.toString());
    return true;
  }
}
