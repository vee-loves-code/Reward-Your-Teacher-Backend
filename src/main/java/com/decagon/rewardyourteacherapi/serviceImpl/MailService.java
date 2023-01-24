package com.decagon.rewardyourteacherapi.serviceImpl;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Data
//@AllArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String email, String mailSubject, String emailBody) throws MessagingException {

        MimeMessage mailMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true);


        mimeMessageHelper.setFrom("rewardyourteacher@gmail.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(mailSubject);
        mimeMessageHelper.setText(emailBody);

        FileDataSource fileDataSource = new FileDataSource("src/main/resources/static/email.png");
        mimeMessageHelper.addAttachment("email.png", fileDataSource);


        javaMailSender.send(mailMessage);

    }

//    public void sendEmail(UserDTO userDTO) throws MessagingException {
//    Properties mailProperties = new Properties();
//        mailProperties.put("mail.smtp.auth", true);
//        mailProperties.put("mail.smtp.starttls.enable", "true");
//        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
//        mailProperties.put("mail.smtp.port", "587");
//        String emailID = "my task manager";
//
//        Authenticator auth = new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("developernoreplies@gmail.com", "lltwmmnlqqzrqqvs");
//            }
//        };
//        Session session = Session.getDefaultInstance(mailProperties, auth);
//
//        createEmail(userDTO, session);
//
//    }
//
//
//
//
//    public void createEmail(UserDTO userDTO, Session session) throws MessagingException {
//        String mailSubject = "Welcome to RewardYourTeacher";
//        System.out.println("before get name");
//        String emailBody = "Hello " + userDTO.getName() + "\n"
//                + "You have successfully registered on my RewardYourTeacher Application, where you get to reward your teacher by sending funds to their wallet.";
//
//
//        MimeMessage mimeMessage = new MimeMessage(session);
//        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
//        mimeMessage.addHeader("format", "flowed");
//        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
//
//        mimeMessage.setFrom(new InternetAddress("developernoreplies@gmail.com"));
//        mimeMessage.setReplyTo(InternetAddress.parse("developernoreplies@gmail.com"));
//        mimeMessage.setSubject(mailSubject, "UTF-8");
//        mimeMessage.setText(emailBody, "UTF-8");
//        mimeMessage.setSentDate(new Date());
//        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(userDTO.getEmail(), true));
//
//        // Create the message body part
//        BodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setText(emailBody);
//
//        // Create a multipart message for attachment
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//
//        // Second part is image attachment
//        messageBodyPart = new MimeBodyPart();
//        String filename = "src/main/resources/static/email.png";
//        DataSource source = new FileDataSource(new File(filename));
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName(filename);
//
//        //Trick is to add the content-id header here
//        messageBodyPart.setHeader("Content-ID", "image_id");
//        multipart.addBodyPart(messageBodyPart);
//
//        //third part for displaying image in the email body
//        messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent("<img src='cid:image_id'>", "text/html");
//        multipart.addBodyPart(messageBodyPart);
//
//        //Set the multipart message to the email message
//        mimeMessage.setContent(multipart);
//
//        Transport.send(mimeMessage);
//
//    }





}
