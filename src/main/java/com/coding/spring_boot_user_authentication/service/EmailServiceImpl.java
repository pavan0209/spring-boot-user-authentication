package com.coding.spring_boot_user_authentication.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendSetPasswordEmail(String to, String name, String token) {
        sendEmail(
                to,
                name,
                token,
                "/set-password",
                "Complete your account setup",
                "Thanks for creating your account with",
                "You're just one step away from getting started. Click the button below to create your password and activate your account.",
                "Create Password",
                "This password setup link expires in",
                "Didn't create an account? You can safely ignore this email. No further action is required.",
                "Set Your Password"
        );
    }

    @Override
    public void sendResetPasswordEmail(String to, String name, String token) {
        sendEmail(
                to,
                name,
                token,
                "/reset-password",
                "Reset your password",
                "We received a request to reset the password for your",
                "Click the button below to choose a new password. If you didn't request this, you can safely ignore this email.",
                "Reset Password",
                "This password reset link expires in",
                "If you didn't request a password reset, you can safely ignore this email. Your password will remain unchanged.",
                "Reset Your Password"
        );
    }

    private void sendEmail(
            String to,
            String name,
            String token,
            String path,
            String heading,
            String intro,
            String description,
            String buttonText,
            String expiryText,
            String footerText,
            String subject) {

        String link = frontendUrl + path + "?token=" + token;

        String html = htmlTemplate(
                name,
                heading,
                intro,
                description,
                buttonText,
                expiryText,
                footerText,
                link
        );

        sendHtmlEmail(to, subject, html);
    }

    private String htmlTemplate(
            String name,
            String heading,
            String intro,
            String description,
            String buttonText,
            String expiryText,
            String footerText,
            String link) {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%2$s</title>
                </head>
                
                <body style="margin:0;padding:32px 16px;background:#f5f7fc;font-family:'Segoe UI',Arial,sans-serif;color:#374151;">
                
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="center">
                
                            <table role="presentation"
                                   width="560"
                                   cellpadding="0"
                                   cellspacing="0"
                                   style="background:#ffffff;border-radius:12px;overflow:hidden;border:1px solid #e5e7eb;">
                
                                <tr>
                                    <td align="center"
                                        style="background:#3f51b5;padding:18px 32px 28px;color:#ffffff;">
                
                                        <h1 style="margin:0 0 8px;font-size:30px;font-weight:700;color:#ffffff;">
                                            Hello, %1$s 👋
                                        </h1>
                
                                        <p style="margin:0;font-size:16px;opacity:.95;">
                                            %2$s
                                        </p>
                
                                    </td>
                                </tr>
                
                                <tr>
                                    <td style="padding:32px;">
                
                                        <p style="margin:0 0 18px;font-size:15px;line-height:1.8;color:#4b5563;">
                                            %3$s
                                            <strong>Spring Boot User Authentication</strong>.
                                        </p>
                
                                        <p style="margin:0 0 26px;font-size:15px;line-height:1.8;color:#4b5563;">
                                            %4$s
                                        </p>
                
                                        <table role="presentation" width="100%%">
                                            <tr>
                                                <td align="center">
                
                                                    <a href="%8$s"
                                                       style="display:inline-block;min-width:220px;background:#3f51b5;color:#ffffff;text-decoration:none;padding:15px 28px;border-radius:8px;font-size:15px;font-weight:600;">
                                                        %5$s
                                                    </a>
                
                                                </td>
                                            </tr>
                                        </table>
                
                                        <table role="presentation"
                                               width="100%%"
                                               style="margin-top:28px;background:#eef2ff;border-left:4px solid #3f51b5;border-radius:6px;">
                
                                            <tr>
                                                <td style="padding:16px 18px;font-size:14px;line-height:1.7;color:#4338ca;">
                                                    %6$s
                                                    <strong>30 minutes</strong>
                                                    and can only be used once.
                                                </td>
                                            </tr>
                
                                        </table>
                
                                        <p style="margin:26px 0 10px;font-size:14px;color:#6b7280;">
                                            If the button above doesn't work, copy and paste the following link into your browser:
                                        </p>
                
                                        <div style="background:#f9fafb;border:1px solid #e5e7eb;border-radius:6px;padding:12px;word-break:break-all;font-size:13px;color:#3f51b5;">
                                            %8$s
                                        </div>
                
                                        <hr style="border:none;border-top:1px solid #e5e7eb;margin:28px 0 18px;">
                
                                        <p style="margin:0;font-size:13px;line-height:1.8;color:#6b7280;">
                                            %7$s
                                        </p>
                
                                    </td>
                                </tr>
                
                                <tr>
                                    <td align="center"
                                        style="background:#fafafa;border-top:1px solid #eeeeee;padding:18px;">
                
                                        <p style="margin:0;color:#9ca3af;font-size:12px;">
                                            © 2026 Spring Boot User Authentication. All rights reserved.
                                        </p>
                
                                    </td>
                                </tr>
                
                            </table>
                
                        </td>
                    </tr>
                </table>
                
                </body>
                </html>
                """.formatted(
                name,
                heading,
                intro,
                description,
                buttonText,
                expiryText,
                footerText,
                link
        );
    }

    private void sendHtmlEmail(String to, String subject, String html) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email.", e);
        }
    }
}