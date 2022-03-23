package com.eti.unitTesting.controller;

import com.eti.unitTesting.entity.User;
import com.eti.unitTesting.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        return new ResponseEntity<User>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.update(user));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) throws Exception {
        userService.deleteById(id);
        return ResponseEntity.ok("Supression effectuée avec succès");
    }

    @GetMapping(path = "/find-one/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping(path = "/find-all")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(path = "/search-by-emailOrLogin/{emailOrLogin}")
    public ResponseEntity<User> findByEmailOrLogin(@PathVariable String emailOrLogin){
        return ResponseEntity.ok().body(userService.findByEmailOrLogin(emailOrLogin));
    }
}
