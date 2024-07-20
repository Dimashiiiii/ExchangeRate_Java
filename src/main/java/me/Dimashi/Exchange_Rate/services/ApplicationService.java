package me.Dimashi.Exchange_Rate.services;

import lombok.AllArgsConstructor;
import me.Dimashi.Exchange_Rate.model.MyUser;
import me.Dimashi.Exchange_Rate.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
}
