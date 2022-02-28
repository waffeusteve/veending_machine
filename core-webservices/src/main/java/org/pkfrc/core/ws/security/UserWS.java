package org.pkfrc.core.ws.security;

import java.util.Set;

import org.pkfrc.core.dto.base.BaseRecordDTO;
import org.pkfrc.core.dto.base.EnumDTO;
import org.pkfrc.core.dto.security.AuthenticationRequest;
import org.pkfrc.core.dto.security.CreateUserDTO;
import org.pkfrc.core.dto.security.UserResume;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.services.security.AuthResponse;
import org.pkfrc.core.services.security.IUserService;
import org.pkfrc.core.ws.base.BaseEnumWS;
import org.pkfrc.core.ws.base.BaseWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/user")
public class UserWS extends BaseWS<User, Long> {

    Logger logger = LoggerFactory.getLogger(UserWS.class);

    @Autowired
    IUserService service;

    @Override
    public Logger getLOGGER() {
        return logger;
    }

    @Override
    public IBaseService<User, Long> getService() {
        return service;
    }

 
    
    @PostMapping(value = "/register")
    public ResponseEntity<BaseRecordDTO<UserResume>> register(
            @RequestBody CreateUserDTO dto,
            @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang ) {
        return super.create(dto, new User(), "admin", UserResume.class);
    }


    @PostMapping(value = "/deposit")
    public ResponseEntity<BaseRecordDTO<UserResume>> deposit(
    		@RequestParam(value = "coin", required = true) Integer coin) {
        try {
            BaseRecordDTO<UserResume> result = evaluateServiceData(service.doDeposit(getConnectedUser(), coin), UserResume.class);
            return new ResponseEntity<BaseRecordDTO<UserResume>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordDTO<UserResume>>(evaluateException(e), STATUS_OK);
        }
    }
    
    @GetMapping(value = "/reset")
    public ResponseEntity<BaseRecordDTO<UserResume>> reset() {
        try {
            BaseRecordDTO<UserResume> result = evaluateServiceData(service.doResetDeposit(getConnectedUser()), UserResume.class);
            return new ResponseEntity<BaseRecordDTO<UserResume>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordDTO<UserResume>>(evaluateException(e), STATUS_OK);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthResponse response = service.auth(request.getUsername(), request.getPassword());
            return new ResponseEntity<>(response, STATUS_OK);
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(e.getMessage() != null ? e.getMessage() : e.toString());
            return new ResponseEntity<>(response, STATUS_OK);
        }
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
	public ResponseEntity<Set<EnumDTO>> getType() {
		return new ResponseEntity<Set<EnumDTO>>(BaseEnumWS.buildEnum(EUserType.values()), HttpStatus.OK);
	}

}
