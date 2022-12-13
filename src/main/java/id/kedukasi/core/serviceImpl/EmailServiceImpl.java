package id.kedukasi.core.serviceImpl;

import com.lowagie.text.DocumentException;
import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.User;
import id.kedukasi.core.service.EmailService;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sender;

  @Value("${spring.mail.email.admin}")
  private String emailadmin;

  @Value("${app.url.staging}")
  private String urlstaging;


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
      FileSystemResource logo = new FileSystemResource(new File("src/main/resources/static/logo-kawah-edukasi.png"));
      /*
        adding logo to html Template EmailDetails
       */
      if(logo.exists()){
        mimeMessageHelper.addInline("logo",logo);
      }

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
  public boolean sendRegisterMail(Map<String, String> filesUpload, PesertaServiceImpl.SetPenambahanData setPenambahanData, Peserta pesertabaru, String pathfile) throws IOException, MessagingException, DocumentException {
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
      context.setVariable("file" + data.getKey(),urlstaging + "/peserta/image-response-entity/" + data.getValue());
    }

    String process = templateEngine.process("register", context);
    helper.setSubject("Welcome " + pesertabaru.getNamaPeserta());
    helper.setText(process, true);
    helper.setTo(emailadmin);
    javaMailSender.send(mimeMessage);
    generatePdfFromHtml(process,pesertabaru.getId().toString(),"REGISTRASI_PESERTA");

    logger.info(dataFile.toString());
    return true;
  }


  public void generatePdfFromHtml(String html, String action, String id) throws IOException, DocumentException {
    //String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
    String outputFolder = "/src/main/resources/static/upload/documents/" + action + "_" + id + ".pdf";
    Path currentPath = Paths.get(".");
    Path absolutePath = currentPath.toAbsolutePath();
    String setPath = absolutePath + outputFolder;
    OutputStream outputStream = new FileOutputStream(setPath);

    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(html);
    renderer.layout();
    renderer.createPDF(outputStream);

    outputStream.close();
  }

  @Override
  public ResponseEntity<Result> sendMailForgotPassword(EmailDetails details, User checkUserEmail, String urlendpoint) {
    Result result = new Result();
    InputStream imageIs = null;
    String templateMailBodyImageKey = "imageResourceName";
    String templateMailBodyImageVal = "logo-kawah-edukasi.png";
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    SendMail testSendMail = new SendMail();
    testSendMail.setEndpoint(urlendpoint);


    try {
      Context context = new Context();
      context.setVariable("user", testSendMail);
      context.setVariable("userdetail", checkUserEmail);
      context.setVariable(templateMailBodyImageKey,templateMailBodyImageVal);
      String process = templateEngine.process("gantipassword", context);
      MimeMessageHelper helper = null;
      helper = new MimeMessageHelper(mimeMessage, true);
      helper.setSubject(details.getSubject());
      helper.setText(process, true);
      helper.setTo(details.getRecipient());

      imageIs = this.getClass().getClassLoader().getResourceAsStream("templates/" + templateMailBodyImageVal);
      byte[] imageByteArray = IOUtils.toByteArray(imageIs);
      final InputStreamSource imageSource = new ByteArrayResource(imageByteArray);
      helper.addInline(templateMailBodyImageVal, imageSource, "image/png");
      javaMailSender.send(mimeMessage);
      result.setMessage("file send successfully");
      return ResponseEntity.ok().body(result);
    } catch (MessagingException | IOException e) {
      result.setMessage(e.getMessage());
      return ResponseEntity.badRequest().body(result);
    }
  }

  public class SendMail {

    private String endpoint;
    private String email;
    private String username;
    private String name;

    public String getEndpoint() {
      return endpoint;
    }

    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
