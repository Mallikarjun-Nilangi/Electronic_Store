package com.lcwd.elestronic.store.Dto;

public class JwtResponse {

	private String jwtTocken;
	
	private UserDto user;

	public String getJwtTocken() {
		return jwtTocken;
	}

	public void setJwtTocken(String jwtTocken) {
		this.jwtTocken = jwtTocken;
	}

	public UserDto getUserDto() {
		return user;
	}

	public void setUserDto(UserDto user) {
		this.user = user;
	}

	public JwtResponse(String jwtTocken, UserDto user) {
		super();
		this.jwtTocken = jwtTocken;
		this.user = user;
	}

	public JwtResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
