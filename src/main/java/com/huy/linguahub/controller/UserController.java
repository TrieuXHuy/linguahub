package com.huy.linguahub.controller;

import com.huy.linguahub.controller.error.ResourceAlreadyExistsException;
import com.huy.linguahub.controller.error.ResourceNotFoundException;
import com.huy.linguahub.domain.User;
import com.huy.linguahub.repository.UserRepository;
import com.huy.linguahub.service.UserService;
import com.huy.linguahub.service.dto.response.filter.ResultPaginationDTO;
import com.huy.linguahub.service.dto.response.user.ResCreateUserDTO;
import com.huy.linguahub.service.dto.response.user.ResGetUserDTO;
import com.huy.linguahub.service.dto.response.user.ResUpdateUserDTO;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User reqUser) throws ResourceAlreadyExistsException {
        String email = reqUser.getEmail();
        if(this.userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(reqUser));
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws ResourceNotFoundException {
        User userDB = userRepository.findById(reqUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(userDB.isDeleted()) {
            throw new ResourceNotFoundException("User not found!");
        }
        return ResponseEntity.ok(this.userService.updateUser(userDB, reqUser));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> specUser, Pageable pageable) {
        Specification<User> spec = specUser.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResGetUserDTO> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(userDB.isDeleted()) {
            throw new ResourceNotFoundException("User not found!");
        }
        return ResponseEntity.ok(this.userService.toGetUserDTO(userDB));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws ResourceNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(userDB.isDeleted()) {
            throw new ResourceNotFoundException("User not found!");
        }
        this.userService.deleteUserById(userDB);
        return ResponseEntity.ok().build();
    }
}
