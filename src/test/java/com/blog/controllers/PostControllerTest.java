package com.blog.controllers;

import com.blog.exceptions.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;

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
    void createPost_notCreated(){

        postDto.setCategory(null);

        when(postService.createPost(postDto, userId, categoryId)).
                thenThrow(new NullPointerException("Category is null"));

        assertThrows(NullPointerException.class, () ->
                postController.createPost(postDto, userId, categoryId));
    }

    @Test
    void createPost_notFound(){
        userId = 4;

        when(postService.createPost(postDto, userId, categoryId)).
                thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () ->
                postController.createPost(postDto,userId, categoryId));
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
    void getPostsByUser_notFound(){
        userId = 4;

        when(postService.getPostsByUser(userId)).
                thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () ->
                postController.getPostsByUser(userId));
    }

    @Test
    void getPostsByCategory() {

        when(postService.getPostsByCategory(categoryId)).thenReturn(posts);

        ResponseEntity<List<PostDto>> response = postController.getPostsByCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts.get(0).getCategory().getCategoryTitle(), response.getBody()
                .get(0).getCategory().getCategoryTitle());

    }

    @Test
    void getAllPost() {
        when(postService.getAllPost()).thenReturn(posts);

        ResponseEntity<List<PostDto>> response = postController.getAllPost();

        assertEquals(posts.get(0).getTitle(), response.getBody().get(0).getTitle());

    }

    @Test
    void getAllPost_notReturned(){

        when(postService.getAllPost()).thenReturn(new ArrayList<>());

        ResponseEntity<List<PostDto>> response = postController.getAllPost();

        assertEquals(0, response.getBody().size());

    }

    @Test
    void getPostById() {
        postId = 1;
        when(postService.getPostById(postId)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.getPostById(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDto.getTitle(), response.getBody().getTitle());
    }

    @Test
    void getPostById_notFound(){
        postId = 3;

        when(postService.getPostById(postId)).
                thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () ->
                postController.getPostById(postId));
    }

    @Test
    void deletePost() {
        postId = 1;
        doNothing().when(postService).deletePost(postId);

        ApiResponse response = postController.deletePost(postId);

        assertEquals("Post is successfully deleted !!", response.getMessage());
    }

    @Test
    void deletePost_notDeleted(){

        doThrow(new ResourceNotFoundException()).
                when(postService).deletePost(postId);

        assertThrows(ResourceNotFoundException.class, () ->
                postController.deletePost(postId));
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

    @Test
    void searchPostByTitle(){
        String keyword = "Post";

        when(postService.searchPosts(keyword)).thenReturn(posts);

        ResponseEntity<List<PostDto>> response =
                postController.searchPostByTitle(keyword);

        assertEquals(posts.size(), response.getBody().size());

    }

    @Test
    void searchPostByTitle_notFound(){
        String keyword = "Art";

        when(postService.searchPosts(keyword)).thenReturn(new ArrayList<>());

        ResponseEntity<List<PostDto>> response =
                postController.searchPostByTitle(keyword);

        assertEquals(0, response.getBody().size());

    }
}