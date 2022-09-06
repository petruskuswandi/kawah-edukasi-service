package id.kedukasi.core.service;

import id.kedukasi.core.models.EmailDetails;


public interface EmailService {

  String sendSimpleMail(EmailDetails details);

  String sendMailWithAttachment(EmailDetails details);
}
