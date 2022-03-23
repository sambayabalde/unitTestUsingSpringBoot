package com.eti.unitTesting.service.serviceImpl;

import com.eti.unitTesting.entity.User;
import com.eti.unitTesting.repository.UserRepository;
import com.eti.unitTesting.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new  RuntimeException("Cet Login est déjà utilisé");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new  RuntimeException("Cet Email est déjà utilisé");
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws RuntimeException {

        if (!userRepository.existsById(user.getId())){
            throw new  RuntimeException("Vous tentez de mettre à jour les infos d'un utilisateur qui n'existe pas");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) throws RuntimeException{
        if (!userRepository.existsById(id)){
            throw new  RuntimeException("Vous tentez de supprimer un utilisateur qui n'existe pas");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) throws Exception {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmailOrLogin(String searchString) {
        return userRepository.findAllByEmailOrLogin(searchString);
    }
}
