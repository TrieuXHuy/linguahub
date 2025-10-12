package com.huy.linguahub.service;

import com.huy.linguahub.domain.User;
import com.huy.linguahub.repository.UserRepository;
import com.huy.linguahub.service.dto.response.filter.Pagination;
import com.huy.linguahub.service.dto.response.filter.ResultPaginationDTO;
import com.huy.linguahub.service.dto.response.user.CreateUserDTO;
import com.huy.linguahub.service.dto.response.user.GetUserDTO;
import com.huy.linguahub.service.dto.response.user.UpdateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreateUserDTO createUser(User reqUser) {
        User savedUser = this.userRepository.save(reqUser);

        return CreateUserDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(savedUser.getGender())
                .age(savedUser.getAge())
                .address(savedUser.getAddress())
                .imageUrl(savedUser.getImageUrl())
                .createdBy(savedUser.getCreatedBy())
                .createdDate(savedUser.getCreatedDate())
                .build();
    }

    public UpdateUserDTO updateUser(User userDB, User reqUser) {

        userDB.setFirstName(reqUser.getFirstName());
        userDB.setLastName(reqUser.getLastName());
        userDB.setGender(reqUser.getGender());
        userDB.setAge(reqUser.getAge());
        userDB.setAddress(reqUser.getAddress());
        userDB.setImageUrl(reqUser.getImageUrl());
        User savedUser = this.userRepository.save(userDB);

        return UpdateUserDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(savedUser.getGender())
                .age(savedUser.getAge())
                .address(savedUser.getAddress())
                .imageUrl(savedUser.getImageUrl())
                .lastModifiedBy(savedUser.getLastModifiedBy())
                .lastModifiedDate(savedUser.getLastModifiedDate())
                .build();
    }

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination();
        pagination.setPage(pageable.getPageNumber() + 1);
        pagination.setSize(pageable.getPageSize());
        pagination.setTotalPages(users.getTotalPages());
        pagination.setTotalElements(users.getTotalElements());

        List<GetUserDTO> usersDTO = users.getContent().stream()
                .map(this::toGetUserDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setPagination(pagination);
        resultPaginationDTO.setResult(usersDTO);

        return resultPaginationDTO;
    }

    public GetUserDTO getUserById(Long id) {
        User userDB = this.userRepository.findById(id).orElseThrow();

        return toGetUserDTO(userDB);
    }

    public GetUserDTO toGetUserDTO(User user) {
        return GetUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .age(user.getAge())
                .address(user.getAddress())
                .imageUrl(user.getImageUrl())
                .createdBy(user.getCreatedBy())
                .createdDate(user.getCreatedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }

    public void deleteUserById(User userDB) {
        userDB.setDeleted(true);
        this.userRepository.save(userDB);
    }
}
