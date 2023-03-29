package emailVerification.services;

import emailVerification.exceptions.UserAlreadyExistsException;
import emailVerification.models.User;
import emailVerification.registration.RegistrationRequest;
import emailVerification.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
        newUser.setFirstName(request.fistName());
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
}