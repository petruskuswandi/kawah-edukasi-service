package id.kedukasi.core.service;

import com.lowagie.text.DocumentException;
import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.models.Result;
import id.kedukasi.core.models.User;
import id.kedukasi.core.serviceImpl.PesertaServiceImpl;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;


public interface EmailService {
  String sendSimpleMail(EmailDetails details);

  String sendMailWithAttachment(EmailDetails details);
  ResponseEntity<Result> sendMailForgotPassword(EmailDetails details, User checkUserEmail, String urlendpoint);

  boolean sendRegisterMail(Map<String, String> filesUpload, PesertaServiceImpl.SetPenambahanData setPenambahanData, Peserta pesertabaru, String pathfile) throws IOException, MessagingException, DocumentException;
}
