package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.PostDto;
import com.blog.payloads.UserDto;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PostServiceImplTest {

    @Mock
    private PostRepo postRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CategoryRepo categoryRepo;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PostServiceImpl postService;

    PostDto postDto;
    Post post;
    CategoryDto categoryDto;
    Category category;
    UserDto userDto;
    User user;

    int categoryId;
    int userId;
    int postId;

    List<Post> posts;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        categoryId = 1;
        categoryDto = new CategoryDto();
        categoryDto.setCategoryId(categoryId);
        categoryDto.setCategoryTitle("Category1");
        categoryDto.setCategoryDescription("Category1 description");

        category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        userId = 2;
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("John");
        userDto.setEmail("john@gmail.com");
        userDto.setPassword("pass");
        userDto.setAbout("About John");

        user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        postId = 1;
        postDto = new PostDto();
        postDto.setPostId(postId);
        postDto.setTitle("Post Title");
        postDto.setContent("Post Content");
        postDto.setImageName("default.png");
        postDto.setAddedDate(null);
        postDto.setCategory(categoryDto);
        postDto.setUser(userDto);

        post = new Post();
        post.setPostId(postDto.getPostId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
        post.setAddedDate(postDto.getAddedDate());
        post.setCategory(category);
        post.setUser(user);

        posts = new ArrayList<>();
        posts.add(post);
    }

    @Test
    void createPost() {

        when(postService.postToDto(post)).thenReturn(postDto);
        when(postService.dtoToPost(postDto)).thenReturn(post);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        when(postRepo.save(post)).thenReturn(post);

        PostDto resUltPost = postService.createPost(postDto, userId, categoryId);

        assertEquals(postDto.getCategory(), resUltPost.getCategory());

    }

    @Test
    void updatePost() {

        when(postService.postToDto(post)).thenReturn(postDto);
        when(postService.dtoToPost(postDto)).thenReturn(post);

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));
        when(postRepo.save(post)).thenReturn(post);

        PostDto resultPost = postService.updatePost(postDto, postId);

        assertEquals(postDto.getCategory(), resultPost.getCategory());
    }

    @Test
    void deletePost() {
    }

    @Test
    void getAllPost() {
        when(postRepo.findAll()).thenReturn(posts);

        List<PostDto> response = postService.getAllPost();

        assertEquals(posts.get(0).getCategory(), response.get(0).getCategory());
    }

    @Test
    void getPostById() {

        when(postService.postToDto(post)).thenReturn(postDto);
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));

        PostDto resultPost = postService.getPostById(postId);

        assertEquals(post.getPostId(), resultPost.getPostId());
    }

    @Test
    void getPostsByCategory() {

        categoryId = 1;

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(postRepo.findByCategory(category)).thenReturn(posts);

        List<PostDto> response = postService.getPostsByCategory(categoryId);

        assertEquals(posts.get(0).getTitle(), response.get(0).getTitle());
    }

    @Test
    void getPostsByUser() {
        userId = 2;

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(postRepo.findByUser(user)).thenReturn(posts);

        List<PostDto> response = postService.getPostsByUser(userId);

        assertEquals(posts.get(0).getTitle(), response.get(0).getTitle());
    }
}