package com.blog.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.entities.Post;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.services.FileService;
import com.blog.services.PostService;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;
//	create

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {

		log.info("Creating post for user ID {} and category ID {}: {}", userId, categoryId, postDto);

		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}

	// get by user

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {

		log.info("Fetching posts by user ID: {}", userId);

		List<PostDto> posts = this.postService.getPostsByUser(userId);

		log.info("Fetched {} posts for user ID {}: {}", posts.size(), userId, posts);

		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

	// get by category

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {

		log.info("Fetching posts by category ID: {}", categoryId);

		List<PostDto> posts = this.postService.getPostsByCategory(categoryId);

		log.info("Fetched {} posts for category ID {}: {}", posts.size(), categoryId,
				posts);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

	// get all posts

	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPost(){
		log.info("Getting all posts");
		List<PostDto> postResponse = this.postService.getAllPost();
		return new ResponseEntity<List<PostDto>>(postResponse, HttpStatus.OK);
	}

	// get post details by id

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {

		log.info("Fetching post by post ID: {}", postId);

		PostDto postDto = this.postService.getPostById(postId);


		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);

	}

	// delete post
	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePost(@PathVariable Integer postId) {
		log.info("Deleting post with ID {}", postId);
		this.postService.deletePost(postId);
		return new ApiResponse("Post is successfully deleted !!", true);
	}

	// update post

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,
											  @PathVariable Integer postId) {

		log.info("Updating post with ID {}: {}", postId, postDto);

		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

	}

	// search
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {

		log.info("Searching posts with keyword {}", keywords);

		List<PostDto> result = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}

	// post image upload

	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException {

		log.info("Uploading image:");
		PostDto postDto = this.postService.getPostById(postId);

		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

	}


    //method to serve files
    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream())   ;

    }

}
