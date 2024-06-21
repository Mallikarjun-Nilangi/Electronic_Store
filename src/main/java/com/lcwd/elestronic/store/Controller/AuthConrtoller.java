package com.lcwd.elestronic.store.Controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.elestronic.store.Dto.JwtRequest;
import com.lcwd.elestronic.store.Dto.JwtResponse;
import com.lcwd.elestronic.store.Dto.UserDto;
import com.lcwd.elestronic.store.Entities.User;
import com.lcwd.elestronic.store.Exceptions.BadApiRequestException;
import com.lcwd.elestronic.store.Services.UserService;
import com.lcwd.elestronic.store.security.JwtHelper;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/auth")
//@CrossOrigin(
//		origins = "http://localhost:4200",
//		allowedHeaders = {"Authorization"},
//		methods = {RequestMethod.GET,RequestMethod.POST},
//		maxAge = 3600
//		)
@Tag(name = "AuthConrtoller",description = " APIs for Authentication !!")
public class AuthConrtoller {

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtHelper helper;
	
	@Value("${googleClientId")
	private String googleClientId;
	
	@Value("${newpassword")
	private String newpassword;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(AuthConrtoller.class);
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> Login(@RequestBody JwtRequest jwtRequest){
		this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
		
		String token = this.helper.generateToken(userDetails);
		
		UserDto userDto = mapper.map(userDetails, UserDto.class);
		
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setJwtTocken(token);
		jwtResponse.setUserDto(userDto);
		
		return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.OK);
	}
	
	
	private void doAuthenticate(String email, String password) {
		
		UsernamePasswordAuthenticationToken authenticationTocken = new UsernamePasswordAuthenticationToken(email, password);
		
		try {
			authenticationManager.authenticate(authenticationTocken);
			
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException("Invalid Username or Password !!");
		}
		
	}


	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal){
		String name = principal.getName();
		
		return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name), UserDto.class),HttpStatus.OK);
	}
	
	
	
	
	//Login with Google
	
	@PostMapping("/google")
	public ResponseEntity<JwtResponse> LoginWithGoogle(@RequestBody Map<String, Object> data) throws IOException{
		
		//get the idTocken from the request
		String idTocken = data.get("idTocken").toString();
		
	  NetHttpTransport nethttpTransport = new NetHttpTransport();
	  
	  JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
	  
	  GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(nethttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
	  
	  GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idTocken);
	  
	  Payload payload = googleIdToken.getPayload();
	  
	  logger.info("Payload : {}",payload);
	  
	  String email = payload.getEmail();
	  
	  User user = null;
	  
	   user = userService.findGoogleUserByEmail(email).orElse(null);
	  
	   if(user==null) {
		   //create new User
		   
		  user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
	   }
	   
	   JwtRequest jwtRequest = new JwtRequest();
	   jwtRequest.setEmail(user.getEmail());
	   jwtRequest.setPassword(newpassword);
	   ResponseEntity<JwtResponse> jwtResponseReponseEntity = this.Login(jwtRequest);
	   
	   return jwtResponseReponseEntity;
	}


	private User saveUser(String email, String name, String photoUrl) {
		
		UserDto userDto = new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		userDto.setPassword(newpassword);
		userDto.setImageName(photoUrl);
		userDto.setRoles(new HashSet<>());
		
		UserDto user = userService.createUser(userDto);
		
		return this.mapper.map(user, User.class);
	}
	
	
	
	
	
	
}
