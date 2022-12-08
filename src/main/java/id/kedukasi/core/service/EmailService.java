package id.kedukasi.core.service;

import id.kedukasi.core.models.EmailDetails;
import id.kedukasi.core.models.Peserta;
import id.kedukasi.core.serviceImpl.PesertaServiceImpl;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;


public interface EmailService {

  String sendSimpleMail(EmailDetails details);

  String sendMailWithAttachment(EmailDetails details);

  boolean sendRegisterMail(Map<String, String> filesUpload, PesertaServiceImpl.SetPenambahanData setPenambahanData, Peserta pesertabaru, String pathfile) throws IOException, MessagingException;
}
