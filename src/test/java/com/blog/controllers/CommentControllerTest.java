package com.blog.controllers;

import com.blog.entities.Comment;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.CommentDto;
import com.blog.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentController commentController;

    CommentDto commentDto;
    int postId;
    int commentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        commentId = 4;
        commentDto = new CommentDto();
        commentDto.setId(commentId);
        commentDto.setContent("This is first comment");

        postId = 1;
    }

    @Test
    void createComment() {

        when(commentService.createComment(commentDto, postId)).thenReturn(commentDto);

        ResponseEntity<CommentDto> response =
                commentController.createComment(commentDto, postId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(commentDto.getContent(), response.getBody().getContent());

    }

    @Test
    void createComment_notCreated() {
        commentDto.setContent("");

        when(commentService.createComment(commentDto, postId))
                .thenThrow(new NullPointerException("Content is null"));

        assertThrows(NullPointerException.class,
                () -> commentController.createComment(commentDto, postId));
    }

    @Test
    void deleteComment() {

        doNothing().when(commentService).deleteComment(commentId);

        ResponseEntity<ApiResponse> response = commentController.deleteComment(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully !!",
                response.getBody().getMessage());
    }

    @Test
    void deleteComment_notFound(){
        commentId=8;

        doThrow(ResourceNotFoundException.class).
                when(commentService).deleteComment(commentId);

        assertThrows(ResourceNotFoundException.class,
                () -> commentController.deleteComment(commentId));
    }
}