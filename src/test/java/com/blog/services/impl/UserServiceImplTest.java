package com.blog.services.impl;

import com.blog.entities.User;
import com.blog.payloads.UserDto;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;
    UserDto userDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateUser() {
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        when(userRepo.save(user)).thenReturn(user);
        when(userService.dtoToUser(userDto)).thenReturn(user);
        when(userService.userToDto(user)).thenReturn(userDto);
        UserDto result = userService.createUser(userDto);

        assertEquals(userDto.getId(), result.getId());
    }

    @Test
    void testUpdateUser() {
        userDto = new UserDto();
//        userDto.setId(1);
        int userId = 1;
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        User user = new User();
//        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(user);

        when(userService.dtoToUser(userDto)).thenReturn(user);
        when(userService.userToDto(user)).thenReturn(userDto);

        UserDto resultUser = userService.updateUser(userDto, userId);

        assertEquals(userDto.getName(), resultUser.getName());
    }

    @Test
    void getUserById() {

        userDto = new UserDto();
//        userDto.setId(1);
        int userId = 1;
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        User user = new User();
//        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        when(userService.dtoToUser(userDto)).thenReturn(user);
        when(userService.userToDto(user)).thenReturn(userDto);

        UserDto resultUser = userService.getUserById(userId);


    }

    @Test
    void getAllUsers() {

        UserDto userDto1 = new UserDto();
        userDto1.setId(1);
        userDto1.setName("John");
        userDto1.setEmail("john@gmail.com");
        userDto1.setPassword("pass");
        userDto1.setAbout("About John");

        User user1 = new User();
        user1.setId(userDto1.getId());
        user1.setName(userDto1.getName());
        user1.setEmail(userDto1.getEmail());
        user1.setPassword(userDto1.getPassword());
        user1.setAbout(userDto1.getAbout());

        UserDto userDto2 = new UserDto();
        userDto2.setId(2);
        userDto2.setName("Jake");
        userDto2.setEmail("jake@gmail.com");
        userDto2.setPassword("pass1");
        userDto2.setAbout("About Jake");

        User user2 = new User();
        user2.setId(userDto2.getId());
        user2.setName(userDto2.getName());
        user2.setEmail(userDto2.getEmail());
        user2.setPassword(userDto2.getPassword());
        user2.setAbout(userDto2.getAbout());

        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        when(userRepo.findAll()).thenReturn(users);
        List<UserDto> resultUserDtos = userService.getAllUsers();


        assertEquals(user1.getId(), resultUserDtos.get(0).getId());
        assertEquals(user2.getId(), resultUserDtos.get(1).getId());
    }

    @Test
    void deleteUser() {

        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setAbout("About John");

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepo, times(1)).delete(user);
    }

    @Test
    void registerNewUser() {
    }
//    @Test
//    public void testUpdateUser_NotFound() {
//        Integer userId = 1;
//        UserDto userDto = new UserDto();
//        userDto.setName("John");
//        userDto.setEmail("john@example.com");
//        userDto.setPassword("password");
//        userDto.setAbout("About John");
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            userService.updateUser(userDto, userId);
//        });
//    }

//    @Test
//    public void testGetUserById_NotFound() {
//        Integer userId = 1;
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            userService.getUserById(userId);
//        });
//    }
//
//    @Test
//    public void testDeleteUser_NotFound() {
//        Integer userId = 1;
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            userService.deleteUser(userId);
//        });
//    }
}