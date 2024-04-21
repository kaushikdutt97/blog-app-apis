package com.blog.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CommentDto;
import com.blog.repositories.CommentRepo;
import com.blog.repositories.PostRepo;
import com.blog.services.CommentService;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		log.info("Creating comment for post with ID {}", postId);
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> {
					String errorMsg = String.format("Post with ID %d not found", postId);
					log.error(errorMsg);
					return new ResourceNotFoundException("Post", "post id ", postId);
				});

		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);

		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		log.info("Deleting comment with ID {}", commentId);
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> {
					String errorMsg = String.format("Comment with ID %d not found", commentId);
					log.error(errorMsg);
					return new ResourceNotFoundException("Comment", "CommentId", commentId);
				});
		this.commentRepo.delete(comment);
	}

}