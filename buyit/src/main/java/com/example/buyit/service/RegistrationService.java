package com.example.buyit.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.buyit.email.EmailSender;
import com.example.buyit.model.AppReg;
import com.example.buyit.model.AppUser;
import com.example.buyit.model.ConfirmationToken;
import com.example.buyit.model.UserRole;
import com.example.buyit.repository.AppUserRepository;
import com.example.buyit.security.PasswordEncoderConfig;

import jakarta.transaction.Transactional;

@Service
public class RegistrationService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoderConfig passwordEncoder;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    EmailSender emailSender;

    public String register(AppReg appreg) {
        if (appUserRepository.findByUsername(appreg.getUsername()).isPresent()) {
            return "Username already exists";
        }
        if (appUserRepository.findByPhoneno(appreg.getPhoneno()).isPresent()) {
            return "Phone number already exists";
        }
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(appreg.getPassword());
        String token = java.util.UUID.randomUUID().toString();
        AppUser user = new AppUser(appreg.getUsername(), appreg.getPhoneno(), appreg.getEmail(), encodedPassword, UserRole.USER, token);
        appUserRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // ✅ Send email after both are safely persisted
        String link = "http://localhost:7070/users/register/confirm?token=" + token;
        emailSender.send(appreg.getEmail(), buildEmail(appreg.getUsername(), link));
        return "User registered successfully";
    }

    public String deleteUser(Long id) {
        if (appUserRepository.existsById(id)) {
            confirmationTokenService.deleteTokensByUserId(id);
            appUserRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }

    public String updateUser(Long id, AppReg appreg) {
        AppUser existingUser = appUserRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
        existingUser.setUsername(appreg.getUsername());
        existingUser.setPhoneno(appreg.getPhoneno());
        existingUser.setEmail(appreg.getEmail());
        appUserRepository.save(existingUser);
        return "User updated successfully";
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getById(Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow();
        return user;
    }

    public AppUser getByToken(String token) {
        AppUser user = appUserRepository.findByToken(token).orElseThrow();
        return user;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()
                        -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserRepository.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
                + "\n"
                + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n"
                + "\n"
                + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                + "    <tbody><tr>\n"
                + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n"
                + "        \n"
                + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                + "          <tbody><tr>\n"
                + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                + "                  <tbody><tr>\n"
                + "                    <td style=\"padding-left:10px\">\n"
                + "                  \n"
                + "                    </td>\n"
                + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
                + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                + "                    </td>\n"
                + "                  </tr>\n"
                + "                </tbody></table>\n"
                + "              </a>\n"
                + "            </td>\n"
                + "          </tr>\n"
                + "        </tbody></table>\n"
                + "        \n"
                + "      </td>\n"
                + "    </tr>\n"
                + "  </tbody></table>\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                + "    <tbody><tr>\n"
                + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                + "      <td>\n"
                + "        \n"
                + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                + "                  <tbody><tr>\n"
                + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                + "                  </tr>\n"
                + "                </tbody></table>\n"
                + "        \n"
                + "      </td>\n"
                + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
                + "    </tr>\n"
                + "  </tbody></table>\n"
                + "\n"
                + "\n"
                + "\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                + "    <tbody><tr>\n"
                + "      <td height=\"30\"><br></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
                + "        \n"
                + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>"
                + "        \n"
                + "      </td>\n"
                + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td height=\"30\"><br></td>\n"
                + "    </tr>\n"
                + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n"
                + "\n"
                + "</div></div>";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getUserRole().name())
                .build();
    }
}
