package com.huy.linguahub.web.rest;

import com.huy.linguahub.domain.User;
import com.huy.linguahub.repository.UserRepository;
import com.huy.linguahub.service.UserService;
import com.huy.linguahub.service.dto.response.filter.ResultPaginationDTO;
import com.huy.linguahub.service.dto.response.user.CreateUserDTO;
import com.huy.linguahub.service.dto.response.user.GetUserDTO;
import com.huy.linguahub.service.dto.response.user.UpdateUserDTO;
import com.huy.linguahub.web.rest.errors.ResourceAlreadyExistsException;
import com.huy.linguahub.web.rest.errors.ResourceNotFoundException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserResource {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<CreateUserDTO> createUser(@Valid @RequestBody User reqUser) throws ResourceAlreadyExistsException {
        String email = reqUser.getEmail();
        if(email.isEmpty() && this.userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(reqUser));
    }

    @PutMapping("/users")
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody User reqUser) throws ResourceNotFoundException {
        User userDB = userRepository.findById(reqUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(userDB.isDeleted()) {
            throw new ResourceNotFoundException("User not found!");
        }
        return ResponseEntity.ok(this.userService.updateUser(userDB, reqUser));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<GetUserDTO> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        if(!this.userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found!");
        }

        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws ResourceNotFoundException {
        if(!this.userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found!");
        }
        this.userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
