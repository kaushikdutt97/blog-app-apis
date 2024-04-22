package com.blog.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CommentDto;
import com.blog.services.CommentService;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment, @PathVariable Integer postId) {
		log.info("Creating comment for post ID {}: {}", postId, comment);
		CommentDto createComment = this.commentService.createComment(comment, postId);
		log.info("Comment created successfully: {}", createComment);
		return new ResponseEntity<>(createComment, HttpStatus.CREATED);
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
		log.info("Deleting comment with ID: {}", commentId);
		this.commentService.deleteComment(commentId);
		log.info("Comment deleted successfully");
		return new ResponseEntity<>(new ApiResponse("Comment deleted successfully !!", true), HttpStatus.OK);
	}
}
