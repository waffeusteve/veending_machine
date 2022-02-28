package org.pkfrc.core.services.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.security.LocalJwt;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.repo.security.ILocalJwtRepository;
import org.pkfrc.core.repo.security.RoleRepository;
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
	public static final String AUTH_OTHER_SESSIONS_OPENED = "There is already an active session using your account";

	@Autowired
	UserRepository repo;

	@Autowired
	JwtService jwtService;

	@Autowired
	ILocalJwtRepository localJwtRepo;

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Autowired
	RoleRepository roleRepository;

	@Override
	protected Class<User> getClazz() {
		return User.class;
	}

	@Override
	protected List<Validation> validateRecord(User record, ETransactionalOperation operation) {
		List<Validation> validations = new ArrayList<>();
		if (record.getDeposit() == null || record.getDeposit().intValue() % 5 != 0)
			validations.add(new Validation(getClazz().getSimpleName(), "deposit_should_be_in_multiples_of_5",
					"deposit_should_be_in_multiples_of_5"));
		if (operation.equals(ETransactionalOperation.Create)
				&& repo.findByUserNameIgnoreCase(record.getUserName()) != null)
			validations.add(
					new Validation(getClazz().getSimpleName(), "User_Name_Already_Used", "User_Name_Already_Used"));
		if (record.getType()==null || record.getUserName()==null || record.getPassword()==null || record.getDeposit()==null )
			validations.add(
					new Validation(getClazz().getSimpleName(), "Invalid_data_for_registration", "Invalid_data_for_registration"));

		return validations;
	}

	@Override
	protected List<Validation> validateRecords(Collection<User> record, ETransactionalOperation operation) {
		return null;
	}

	@Override
	public ServiceData<User> create(User user, User record, boolean validate) throws Exception {
		user = repo.findByUserNameIgnoreCase("SYSTEM");
		List<Validation> validations = validateRecord(record, ETransactionalOperation.Create);
		if (!validations.isEmpty())
			return getInvalidResult(validations);
		if (record.getType() != null)
			record.getRoles().add(roleRepository.findByName(record.getType().name()));

		record.setDefaultPwd(true);
		record.setStatus(EUserStatus.Active);
		record.setPassword(BcryptEncryption.encode(record.getPassword()));
		return super.create(user, record, false);
	}

	@Override
	public ServiceData<User> doDeposit(User user, Integer coin) throws Exception {
		List<Validation> result = new ArrayList<>(0);
		List<Integer> integers = Arrays.asList(5, 10, 20, 50, 100);
		boolean integerExists = integers.contains(coin);
		if (!integerExists) {
			result.add(new Validation(getClazz().getSimpleName(), "Invalid_coin", "Invalid_coin"));
			return getInvalidResult(result);
		}

		user.setDeposit(user.getDeposit() + coin.doubleValue());
		return super.update(user, user, false);
	}

	@Override
	public ServiceData<User> doResetDeposit(User user) throws Exception {
		List<Validation> val = new ArrayList<>(0);
		if (user.getDeposit().intValue() == 0) {
			val.add(new Validation(getClazz().getSimpleName(), "Account_already_reset", "Account_already_reset"));
			return getInvalidResult(val);
		}
		user.setDeposit(0.00);
		return super.update(user, user, false);
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
					UserWithToken usr = new UserWithToken(user, jwtService.toToken(user));
					response.setUser(usr);

					// Check open sessions
					List<LocalJwt> sessionsOpened = localJwtRepo.findByUserId(user.getId());
					response.setMessage(!sessionsOpened.isEmpty() ? AUTH_OTHER_SESSIONS_OPENED : AUTH_SUCCESS);

					// store token in db
					LocalJwt storeJwt = new LocalJwt();
					storeJwt.setToken(usr.getToken());
					storeJwt.setUserId(user.getId());
					localJwtRepo.save(storeJwt);
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