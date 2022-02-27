package org.pkfrc.core.services.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.repo.security.UserRepository;
import org.pkfrc.core.services.base.BaseServiceImpl;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.core.services.base.Validation;
import org.pkfrc.core.services.enums.EServiceDataType;
import org.pkfrc.core.utilities.enumerations.ETransactionalOperation;
import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.pkfrc.core.utilities.helper.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements IUserService {

	public static final String AUTH_INVALID_ACCOUNT_STATUS = "InvalidAccountStatus";
	public static final String AUTH_ACCOUNT_LOCKED = "AccountLocked";
	public static final String AUTH_INVALID_CREDENTIALS = "InvalidCredentials";
	public static final String AUTH_SUCCESS = "LoginSuccess";

	@Autowired
	UserRepository repo;

	@Autowired
	JwtService jwtService;

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected Class<User> getClazz() {
		return User.class;
	}

	@Override
	protected List<Validation> validateRecord(User record, ETransactionalOperation operation) {
		List<Validation> validations = new ArrayList<>();
		return validations;
	}

	@Override
	protected List<Validation> validateRecords(Collection<User> record, ETransactionalOperation operation) {
		return null;
	}

	@Override
	public ServiceData<User> create(User user, User record, boolean validate) throws Exception {
		List<Validation> validations = validateCreate(user, record);
		user = repo.findByUserNameIgnoreCase("SYSTEM");
		if (!validations.isEmpty()) {
			return getInvalidResult(validations);
		}
		record.setDefaultPwd(true);
		record.setStatus(EUserStatus.Active);
		record.setPassword(BcryptEncryption.encode(record.getPassword()));
		ServiceData<User> sData = super.create(user, record, false);
		return sData;
	}

	
	@Override
	public ServiceData<User> doDeposit(User user, Integer coin) throws Exception {
		List<Validation> result = new ArrayList<>(0);
		List<Integer> integers = Arrays.asList(5, 10, 20, 50, 100);
		boolean integerExists = integers.contains(coin);
		if (integerExists) {
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_coin", "Invalid_coin"));
			return getInvalidResult(result);
		}

		user.setDeposit(user.getDeposit()+coin.doubleValue());
		return super.update(user, user);
	}

	protected List<Validation> validateCreate(User userCreating, User record) {
		List<Validation> validations = new ArrayList<>();
		checkUserCreating(userCreating);
		return validations;
	}


	private void checkUserCreating(User userCreating) {
		if (userCreating == null) {
			throw new SmartTechException("UserCreating.Is.Null");
		}

	}

	public List<Validation> validatePassword(String text) {
		List<Validation> validations = new ArrayList<>();
		if (text == null || text.length() < 6) {
			validations.add(new Validation("Password.Must.Be.At.Least.6.Characters"));
		}
		return validations;
	}

	public List<Validation> validateEmail(String email) {
		List<Validation> validations = new ArrayList<>();
		if (!StringHelper.isValidEmail(email)) {
			validations.add(new Validation("Email.Invalid"));
		} else {
			if (repo.findByUserName(email).isPresent()) {
				validations.add(new Validation("Email.Exist"));
			}
		}
		return validations;
	}

	public List<Validation> validateLogin(String login) {
		List<Validation> validations = new ArrayList<>();
		if (login == null || login.length() < 4) {
			validations.add(new Validation("Login.Must.Be.At.Least.4.Characters"));
		} else if (repo.findByUserName(login).isPresent()) {
			validations.add(new Validation("Login.Exist"));
		}
		return validations;
	}

	@Override
	public ServiceData<User> findByLogin(String login) {
		ServiceData<User> sData = createServiceData();
		try {
			return setServiceData(sData, repo.findByUserName(login));
		} catch (Exception e) {
			throwException("FindEntityById.Exception", e);
		}
		return sData;
	}

	@Override
	public ServiceData<User> findByEmail(String email) {
		ServiceData<User> sData = createServiceData();
		try {
			return setServiceData(sData, repo.findByUserName(email));
		} catch (Exception e) {
			throwException("FindEntityById.Exception", e);
		}
		return sData;
	}

	@Override
	public AuthResponse auth(String username, String password) {
		Optional<User> optional = repo.findByUserName(username);
		AuthResponse response = new AuthResponse();
		String msg;
		if (optional.isPresent()) {
			User user = optional.get();
			EUserStatus status = user.getStatus();
			msg = status == null ? AUTH_INVALID_ACCOUNT_STATUS
					: status.equals(EUserStatus.Locked) ? AUTH_ACCOUNT_LOCKED : "";
			if (msg.isEmpty()) {
				if (BcryptEncryption.matches(password, user.getPassword())) {
					response.setSuccess(true);
					response.setMessage(AUTH_SUCCESS);
					response.setUser(new UserWithToken(user, jwtService.toToken(user)));
				} else {
					response.setMessage(AUTH_INVALID_CREDENTIALS);
				}
			} else {
				response.setMessage(msg);
			}
		} else {
			response.setMessage(AUTH_INVALID_CREDENTIALS);
		}
		return response;
	}

	@Override
	public UserWithToken getConnectedUserDetails() {
		UserWithToken userWithToken = null;
		try {
			ServiceData<User> user = getConnectedUser();
			if (user.isValid()) {
				userWithToken = new UserWithToken(user.getRecord(), jwtService.toToken(user.getRecord()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return userWithToken;
	}

	@Override
	public ServiceData<User> changeUserPassword(Long id, String currentPassword, String newPassword) throws Exception {
		List<Validation> validations = new ArrayList<>();
		if (StringHelper.isEmpty(currentPassword) || StringHelper.isEmpty(newPassword)) {
			validations.add(new Validation("Password.Empty"));
		} else if (currentPassword.contentEquals(newPassword)) {
			validations.add(new Validation("Passwords.Must.Be.Different"));
		}
		validations.addAll(validatePassword(newPassword));
		User user = getConnectedUser().getRecord();
		if (user == null || !user.getId().equals(id)) {
			validations.add(new Validation("User.Must.Be.Connected"));
		}
		if (validations.isEmpty()) {
			if (BcryptEncryption.matches(currentPassword, user.getPassword())) {
				user.setPassword(BcryptEncryption.encode(newPassword));
				return this.update(user, user, false);
			} else {
				validations.add(new Validation("CurrentPassword.Does.Not.Matche"));
			}
		}
		return getInvalidResult(validations);
	}

	@Override
	public ServiceData<User> changePassword(Long id, String password) throws Exception {
		List<Validation> validations = new ArrayList<>();
		validations.addAll(validatePassword(password));
		User user = findById(id).getRecord();
		if (user == null) {
			validations.add(new Validation("User.Not.Found"));
		}
		User connectedUser = this.getConnectedUser().getRecord();
		if (connectedUser == null)
			validations.add(new Validation("User.Not.Connected"));
		if (validations.isEmpty()) {
			user.setPassword(BcryptEncryption.encode(password));
			return this.update(user, user, false);
		}
		return getInvalidResult(validations);
	}

	@Override
	public ServiceData<User> getConnectedUser() throws Exception {
		ServiceData<User> sData = new ServiceData<>();
		String username;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else if (principal instanceof User) {
				return super.setServiceData(sData, repo.findByUserName(((User) principal).getUserName()));
			} else {
				username = principal.toString();
			}
			return super.setServiceData(sData, repo.findByUserName(username));
		}
		sData.setMessage("User.Not.Found");
		sData.setType(EServiceDataType.Record);
		return sData;
	}

}