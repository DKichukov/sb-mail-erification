package emailVerification.events.listener;

import emailVerification.events.RegistrationCompleteEvent;
import emailVerification.models.User;
import emailVerification.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteLEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //-----steps to follow up -----
        //1. get the newly registered user
        User theUser = event.getUser();
        //2. create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. save the verification token for the user
        userService.saveUserVerificationToken(theUser,verificationToken);
        //4. build the verification URL to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token=" + verificationToken;
        //5. sent the email
        log.info("Click the link to verify your registration: {}", url);

    }
}
