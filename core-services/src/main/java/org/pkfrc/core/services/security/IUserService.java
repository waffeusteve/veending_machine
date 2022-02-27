package org.pkfrc.core.services.security;

import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.services.base.ServiceData;

public interface IUserService extends IBaseService<User, Long> {

    ServiceData<User> findByLogin(String login);

    ServiceData<User> findByEmail(String email);

    AuthResponse auth(String username, String password);

    UserWithToken getConnectedUserDetails();

    ServiceData<User> getConnectedUser() throws Exception;

    ServiceData<User> changeUserPassword(Long id, String currentPassword, String newPassword) throws Exception;

    ServiceData<User> changePassword(Long id, String password) throws Exception;

	ServiceData<User> doDeposit(User user, Integer coin) throws Exception;
}