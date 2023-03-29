package emailVerification.controllers;

import emailVerification.events.RegistrationCompleteEvent;
import emailVerification.models.User;
import emailVerification.registration.RegistrationRequest;
import emailVerification.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping
    public String registerUser(RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        //publish(with publisher) registration event
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        return " Success! Please check your email to complete your registration";

    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName() + " :" + request.getServerPort() + request.getContextPath();
    }
}
