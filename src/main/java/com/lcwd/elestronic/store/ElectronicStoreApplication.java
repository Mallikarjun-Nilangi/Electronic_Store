package com.lcwd.elestronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lcwd.elestronic.store.Entities.Role;
import com.lcwd.elestronic.store.Repository.RoleRepository;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	
	@Autowired
	private PasswordEncoder encoder; 
	
	@Autowired
	private RoleRepository repository;
	
	@Value("${normal.role.id}")
	private String role_normal_id;
	
	@Value("${admin.role.id}")
	private String role_admin_id;

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println();
		
		
		try {
			
			Role roleAdmin = new Role();
			roleAdmin.setRoleId(role_admin_id);
			roleAdmin.setRoleName("ROLE_ADMIN");
			Role roleNormal = new Role();
			roleNormal.setRoleId(role_normal_id);
			roleNormal.setRoleName("ROLE_NORMAL");
			
			repository.save(roleAdmin);
			repository.save(roleNormal);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
