/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

//import com.viettel.database.DAO.BaseDAOAction;
import com.viettel.utility.PropertiesUtils;;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.Logger;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.Logger;

/**
 *
 * @author trungdh3_s
 */
//public class SentEmail extends BaseDAOAction {
public class SentEmail {
//    public static Logger logger;
    public static int SendMail(String to, String pass, String account, String seconds, Logger logger) {
        try {
            if (to == null || to.trim().equals("")) {
                return 0;
            }
            String smtpServer = "";
            String from = "";
            String subject = "";
            String psw = "";
            String body = "";
            
            PropertiesUtils pros = new PropertiesUtils();
            pros.loadProperties("../etc/common.cfg", false);
            
            try {
                smtpServer = pros.getProperty("smtpServer");
                from = pros.getProperty("from");
                psw = pros.getProperty("psw");
                subject = pros.getProperty("subject");
                body = pros.getProperty("body");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.smtp.socketFactory.port", "25");
            props.put("mail.smtp.socketFactory.class", "");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "false");
            final String login = from;//"nth001@gmail.com";//usermail
            final String pwd = psw;//"password cua ban o day";
            Authenticator pa = null; //default: no authentication
            if (login != null && pwd != null) { //authentication required?
                props.put("mail.smtp.auth", "true");
                pa = new Authenticator() {

                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, pwd);
                    }
                };
            }
            //chỗ này có thể cần thay đổi trung dh3
            Session session1 = Session.getInstance(props, pa);
            Message msg = new MimeMessage(session1);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
                    to, false));

            msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            MimeBodyPart messagePart = new MimeBodyPart();
            MimeMultipart multipart = new MimeMultipart();
            body = body.replace("%sec%", seconds);
            body = body.replace("%acc%", account);
            body = body.replace("%pwd%", pass);
            messagePart.setText(body);
//            messagePart.setHeader("Content-Type", "text/plain;  charset=\"utf-8\"");
//            msg.setHeader("Content-Type", "text/plain; charset=utf-8");
////            text/html;; x-java-content-handler=com.sun.mail.handlers.text_html
//            messagePart.setHeader("Content-Transfer-Encoding", "Base64");
            multipart.addBodyPart(messagePart);
            msg.setContent(multipart);
            msg.setHeader("X-Mailer", "LOTONtechEmail");
            msg.setSentDate(new Date());
            msg.saveChanges();
            Transport.send(msg);
            System.out.println("Message sent OK.");
        } catch (Exception ex) {
            logger.error("SentEmail: ex\n" + ex);
            return -1;
        }

        return 1;
    }
}
