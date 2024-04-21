package com.blog.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blog.entities.User;

public class UserRepoTest {
	

	UserRepo userRepo;
	
	User user;
	
	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1);
		user.setName("Abhinav");
		user.setEmail("abhinav@gmail.com");
		user.setAbout("for testing");
		user.setPassword("123");
		
		userRepo.save(user);
	}
	
	void tearDown() {
		user = null;
		userRepo.deleteAll();
	}
	
	@Test
	void testFindByEmail_Found() {
		Optional<User> responseUser = userRepo.findByEmail("abhinav@gmail.com"); 
//		assertThat(responseUser.equals(user));
		assertEquals(user, responseUser);
	}

}
