package emailVerification.services;

import emailVerification.models.User;
import emailVerification.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> finfByEmail(String email);
}
