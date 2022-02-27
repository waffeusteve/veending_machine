//package org.pkfrc.core.dto.security;
//
//import org.pkfrc.core.dto.base.AbstractDTO;
//import org.pkfrc.core.dto.base.EntityDTO;
//import org.pkfrc.core.dto.base.LanguageDTO;
//import org.pkfrc.core.entities.base.Language;
//import org.pkfrc.core.entities.enums.EUserStatus;
//import org.pkfrc.core.entities.enums.EUserType;
//import org.pkfrc.core.entities.security.Profile;
//import org.pkfrc.core.entities.security.User;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@NoArgsConstructor
//@Data
//public class UserDTO extends AbstractDTO<User, Long> implements EntityDTO<User, Long> {
//
//
//    String name;
//    EUserType type;
//    String login;
//    EUserStatus status;
//    String email;
//    String phone;
//    String address;
//    String about;
//    LanguageDTO lang;
//    private ProfileDTO profile;
//
//    public UserDTO(User entity){
//        super(entity);
//        name = entity.getName();
//        type = entity.getType();
//        status = entity.getStatus();
//        email = entity.getEmail();
//        phone = entity.getPhone();
//        address = entity.getAddress();
//        about = entity.getAbout();
//        login = entity.getLogin();
//        if(entity.getLang() != null){
//            this.lang = new LanguageDTO(entity.getLang());
//        }
//        if(entity.getProfile() != null){
//            this.profile = new ProfileDTO(entity.getProfile());
//        }
//    }
//
//    @Override
//    public User toEntity(User entity) {
//        super.toEntity(entity);
//        entity.setName(name);
//        entity.setType(type);
//        entity.setStatus(status);
//        entity.setEmail(email);
//        entity.setPhone(phone);
//        entity.setAddress(address);
//        entity.setAbout(about);
//        entity.setLogin(login);
//        if(lang != null){
//            entity.setLang(lang.toEntity(new Language()));
//        }
//        if(profile != null){
//            entity.setProfile(profile.toEntity(new Profile()));
//        }
//        return entity;
//    }
//}
