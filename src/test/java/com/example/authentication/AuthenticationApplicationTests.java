package com.example.authentication;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.security.jwt.TokenCreator;
import com.example.authentication.security.jwt.TokenVerifier;
import com.example.authentication.service.MessageService;
import com.example.authentication.service.UserService;
import com.example.authentication.util.MailSender;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@EnableConfigurationProperties
@ActiveProfiles(profiles = "dev")
class AuthenticationApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	TokenCreator tokenCreator;

	@Autowired
	TokenVerifier tokenVerifier;

	@Test
	void testCreator(){
		String email = "harryjung0330@gmail.com";
		String code = "15242";

		String token = tokenCreator.createSignUpSendMailToken(email, code);
		log.info("===============================================================");

		log.info(tokenVerifier.verifySignUpCheckCodeToken(token, email, code));
		token = tokenCreator.createSignUpCheckCodeToken("harryjung0330@gmail.com");
		log.info(tokenVerifier.verifySignUpAfterCodeToken(token, email));
		token = tokenCreator.createPasswordSendMailToken("harryjung0330@gmail.com", "15241");
		log.info(tokenVerifier.verifyPasswordCheckCodeToken(token, email, code));
		token = tokenCreator.createPasswordCheckCodeToken("harryjung0330@gmail.com");
		log.info(tokenVerifier.verifyPasswordAfterCodeToken(token, email));
	}

	@Autowired
	UserService userService;

	@Test
	void testChangeRole()
	{
		userService.changeUserRoleToUser(Long.valueOf(1));
	}

	@Autowired
	MessageService messageService;

	@Test
	void testMessage()
	{
		AppUser appUser = userService.findById(Long.valueOf(6)).orElseThrow(() -> new UsernameNotFoundException("user id with 1 not found!"));
		messageService.sendMessageSync(appUser);


	}




}
