package com.blog.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.blog.payloads.CategoryDto;
import com.blog.payloads.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public Post dtoToPost(PostDto postDto) {
        Post post = this.modelMapper.map(postDto, Post.class);
        return post;
    }

    public PostDto postToDto(Post post) {
        PostDto postDto = this.modelMapper.map(post, PostDto.class);
        return postDto;
    }


    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

        log.info("Creating post with userId {} and categoryId {}", userId, categoryId);
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> {
                    String errMsg = String.format("User not found with id %d", userId);
                    log.error(errMsg);
                    return new ResourceNotFoundException("User ", "User id", userId);
                }
                );

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                            String errMsg = String.format("Category not found with id %d",
                                    categoryId);
                            log.error(errMsg);
                            return new ResourceNotFoundException
                                    ("Category", "category id ", categoryId);
                        }
                );

        Post post = this.dtoToPost(postDto);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepo.save(post);

        return this.postToDto(newPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        log.info("Updating post with ID: {}", postId);

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> {
                    String errMsg = String.format("Post not found with id %d", postId);
                    log.error(errMsg);
                    return new ResourceNotFoundException("Post ", "post id", postId);
                });

        Category category = this.categoryRepo.findById(postDto.getCategory().getCategoryId()).get();

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
        post.setCategory(category);


        Post updatedPost = this.postRepo.save(post);
        return this.postToDto(updatedPost);
    }

    @Override
    public void deletePost(Integer postId) {

        log.info("Deleting post with id {}", postId);
        Post deletedPost = this.postRepo.findById(postId)
                .orElseThrow(() -> {
                    String errMsg = String.format("Post not found with id %d", postId);
                    log.error(errMsg);
                    return new ResourceNotFoundException("Post ", "post id", postId);
                });

        this.postRepo.delete(deletedPost);

    }


    @Override
    public List<PostDto> getAllPost(){
        log.info("Retrieving all posts");

        List<Post> posts = this.postRepo.findAll();

        List<PostDto> postDtos = new ArrayList<>();
        for(Post post: posts){
            PostDto postDto = new PostDto(post.getPostId(),
                    post.getTitle(), post.getContent(), post.getImageName(),
                    post.getAddedDate(), this.modelMapper.map(post.getCategory(), CategoryDto
                    .class), this.modelMapper.map(post.getUser(), UserDto.class));
            postDtos.add(postDto);
        }
        return postDtos;
    }


    @Override
    public PostDto getPostById(Integer postId) {

        log.info("Retrieving post by ID: {}", postId);

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> {
                    String errMsg = String.format("Post with id %d not found", postId);
                    log.error(errMsg);
                   return new ResourceNotFoundException("Post", "post id", postId);
                });
        return this.postToDto(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {

        log.info("Getting posts of category with id {}", categoryId);

        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> {
                    String errMsg = String.format("Category not found with id %d", categoryId);
                    log.error(errMsg);
                    return new ResourceNotFoundException("Category", "category id",
                            categoryId);
                });
        List<Post> posts = this.postRepo.findByCategory(cat);

//        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());

        List<PostDto> postDtos = new ArrayList<>();
        for(Post post: posts){
            PostDto postDto = new PostDto(post.getPostId(),
                    post.getTitle(), post.getContent(), post.getImageName(),
                    post.getAddedDate(), this.modelMapper.map(post.getCategory(), CategoryDto
                    .class), this.modelMapper.map(post.getUser(), UserDto.class));
            postDtos.add(postDto);
        }

        return postDtos;


    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {

        log.info("Getting posts of user with id {}", userId);
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> {
                    String errMsg = String.format("User not found with id %d", userId);
                    log.error(errMsg);
                    return new ResourceNotFoundException("User ", "userId ", userId);
                });
        List<Post> posts = this.postRepo.findByUser(user);

//        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());

        List<PostDto> postDtos = new ArrayList<>();
        for(Post post: posts){
            PostDto postDto = new PostDto(post.getPostId(),
                    post.getTitle(), post.getContent(), post.getImageName(),
                    post.getAddedDate(), this.modelMapper.map(post.getCategory(), CategoryDto
                    .class), this.modelMapper.map(post.getUser(), UserDto.class));
            postDtos.add(postDto);
        }
        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepo.searchByTitle("%" + keyword + "%");
        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

}
