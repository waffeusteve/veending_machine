
package org.pkfrc.projurise.ws.config;

import javax.annotation.PostConstruct;

import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.Role;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.repo.security.RoleRepository;
import org.pkfrc.core.repo.security.UserRepository;
import org.pkfrc.core.services.security.BcryptEncryption;
import org.pkfrc.core.services.security.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit {

	@Autowired
	IUserService userService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepository;

	

	@PostConstruct
	public void ini() throws Exception {

		User system = userRepo.findByUserNameIgnoreCase("SYSTEM");
		if (system == null) {
			system = new User();
			system.setUserName("SYSTEM");
			system.setStatus(EUserStatus.Active);			
			system.setPassword(BcryptEncryption.encode("123456"));
			for (EUserType type : EUserType.values()) {
				Role role = new Role(type.name());
				roleRepository.save(role);
				system.getRoles().add(role);
			}
			userRepo.save(system);
		}

	}
}
