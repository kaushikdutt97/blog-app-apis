package com.blog.controllers;

import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.services.UserService;
import net.bytebuddy.agent.VirtualMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    UserDto userDto;
    UserDto userDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto =new UserDto();
        int userId = 1;
        userDto.setId(userId);
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        userDto2 = new UserDto();
        userDto2.setId(3);
        userDto2.setName("");
        userDto2.setEmail("john@gmail.com");
        userDto2.setPassword("pass");
        userDto2.setAbout("About John");

    }

    @Test
    void createUser() {

        when(userService.createUser(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John", response.getBody().getName());
    }

    @Test
    void createUser_notCreated(){
        when(userService.createUser(userDto2)).
                thenThrow(new NullPointerException("Name is null"));

        ResponseEntity<UserDto> response;
        try {
            response = userController.createUser(userDto2);
        } catch (NullPointerException ex) {
            assertEquals("Name is null", ex.getMessage());
        }
    }


    @Test
    void updateUser() {

        int userId = 1;
        when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(userDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getName());

    }

    @Test
    void updateUser_notFound(){
        Integer userId = 4;
        UserDto userDto3 = new UserDto();
        userDto3.setName("Jimmy");
        userDto3.setEmail("jimmy@example.com");
        userDto3.setPassword("password");
        userDto3.setAbout("About Jimmy");

        assertThrows(ResourceNotFoundException.class, () -> {
            userController.updateUser(userDto2, userId);
        });
    }

    @Test
    void deleteUser() {
        int userId = 1;

        doNothing().when(userService).deleteUser(userId);
        ResponseEntity<ApiResponse> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted Successfully", response.getBody().getMessage());
        assertEquals(true, response.getBody().isSuccess());

    }

    @Test
    void getAllUsers() {

        UserDto userDto1 =new UserDto();
        int userId = 2;
        userDto.setId(userId);
        userDto.setName("Jake");
        userDto.setEmail("jake@gmail.com");
        userDto.setPassword("pass1");
        userDto.setAbout("About Jake");

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);
        userDtos.add(userDto1);

        when(userService.getAllUsers()).thenReturn(userDtos);

        ResponseEntity<List<UserDto>> responseUsers = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseUsers.getStatusCode());
        assertEquals(userDtos.get(0).getName(), responseUsers.getBody().get(0).getName());

    }

    @Test
    void getSingleUser() {
        int userId = 1;

        when(userService.getUserById(userId)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getSingleUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getName());
    }
}