package emailVerification.services;

import emailVerification.exceptions.UserAlreadyExistsException;
import emailVerification.models.User;
import emailVerification.registration.RegistrationRequest;
import emailVerification.registration.token.VerificationToken;
import emailVerification.registration.token.VerificationTokenRepository;
import emailVerification.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.finfByEmail(request.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with " + request.email() + " already exists");
        }
        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> finfByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token==null){
            return "Invalid verification token.";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            tokenRepository.delete(token);
            return "Token already expired.";
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return "Valid.";
    }
}
