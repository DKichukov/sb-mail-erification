package emailVerification.controllers;

import emailVerification.events.RegistrationCompleteEvent;
import emailVerification.models.User;
import emailVerification.registration.RegistrationRequest;
import emailVerification.registration.token.VerificationToken;
import emailVerification.registration.token.VerificationTokenRepository;
import emailVerification.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return " Success! Please check your email to complete your registration";
    }
    @GetMapping("verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().getIsEnabled()) {
            return "This account has already been verified, please, login,";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("Valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token.";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
