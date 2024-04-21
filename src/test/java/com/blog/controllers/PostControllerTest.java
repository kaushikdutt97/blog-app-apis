package com.blog.controllers;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.PostDto;
import com.blog.payloads.UserDto;
import com.blog.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PostControllerTest {

    @Mock
    private PostService postService;
    @InjectMocks
    private PostController postController;
    PostDto postDto;
    PostDto postDto1;
    UserDto userDto;
    CategoryDto categoryDto;
    int categoryId;
    int userId;
    int postId;
    List<PostDto> posts;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryId = 1;
        categoryDto = new CategoryDto();
        categoryDto.setCategoryId(categoryId);
        categoryDto.setCategoryTitle("Category1");
        categoryDto.setCategoryDescription("Category1 description");

        userId = 2;
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        postId = 1;
        postDto = new PostDto();
        postDto.setPostId(postId);
        postDto.setTitle("Post Title");
        postDto.setContent("Post Content");
        postDto.setImageName("default.png");
        postDto.setAddedDate(null);
        postDto.setCategory(categoryDto);
        postDto.setUser(userDto);

        postId = 2;
        postDto1 = new PostDto();
        postDto1.setPostId(postId);
        postDto1.setTitle("Post1 Title");
        postDto1.setContent("Post1 Content");
        postDto1.setImageName("default1.png");
        postDto1.setAddedDate(null);
        postDto1.setCategory(categoryDto);
        postDto1.setUser(userDto);

        posts = new ArrayList<>();
        posts.add(postDto);
        posts.add(postDto1);
    }

    @Test
    void createPost() {

        when(postService.createPost(postDto, userId, categoryId)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.
                createPost(postDto, userId, categoryId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Post Title", response.getBody().getTitle());

    }

    @Test
    void getPostsByUser() {

        when(postService.getPostsByUser(userId)).thenReturn(posts);

        ResponseEntity<List<PostDto>> response = postController.getPostsByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts.get(0).getUser().getName(), response.getBody()
                .get(0).getUser().getName());

    }

    @Test
    void getPostsByCategory() {

        when(postService.getPostsByCategory(categoryId)).thenReturn(posts);

        ResponseEntity<List<PostDto>> response = postController.getPostsByCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts.get(0).getCategory().getCategoryTitle(), response.getBody()
                .get(0).getCategory().getCategoryTitle());

    }

//    @Test
//    void getAllPost() {
//        when(postService.getAllPost()).thenReturn(posts);
//
//    }

    @Test
    void getPostById() {
        postId = 1;
        when(postService.getPostById(postId)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.getPostById(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDto.getTitle(), response.getBody().getTitle());
    }

    @Test
    void deletePost() {
        postId = 1;
        doNothing().when(postService).deletePost(postId);

        ApiResponse response = postController.deletePost(postId);

        assertEquals("Post is successfully deleted !!", response.getMessage());
    }

    @Test
    void updatePost() {
        postId = 1;
        when(postService.updatePost(postDto, postId)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.
                updatePost(postDto, postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post Title", response.getBody().getTitle());
    }
}