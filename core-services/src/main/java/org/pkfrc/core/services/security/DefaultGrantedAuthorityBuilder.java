package org.pkfrc.core.services.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pkfrc.core.entities.security.Privilege;
import org.pkfrc.core.entities.security.Role;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.utilities.helper.SpringBeanHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


/**
 * Class implementing the default getAuthorities from a user if a
 */

@Component
public class DefaultGrantedAuthorityBuilder {

    public static final String SCANNED_BASE_PACKAGE = "org.pkfrc";

    Logger logger = LoggerFactory.getLogger(DefaultGrantedAuthorityBuilder.class);

    @Autowired
    SpringBeanHelper beanHelper;

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        try{
            GrantedAuthorityBuilder builder = beanHelper.getBean(GrantedAuthorityBuilder.class);
            if(builder != null){
                return builder.getAuthorities(user);
            }else {
                return buildAuthorities(user);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() +":"+ ex.getStackTrace());
        }
        return new ArrayList<>();
    }


    private  Collection<? extends GrantedAuthority> buildAuthorities(User user) {
            Set<String> userRolesPrivileges = getRolePrivileges(user.getRoles());
            return getGrantedAuthorities(userRolesPrivileges);
    }



    public static Set<String> getRolePrivileges(Collection<Role> roles) {
        Set<String> privileges = new HashSet<>();
        for (Role role : roles) {
            privileges.add("ROLE_"+role.getName().toUpperCase());
//            privileges.addAll(getPrivileges(role.getPrivileges()));
        }
        return privileges;
    }

    public static Set<String> getPrivileges(Collection<Privilege> privileges) {
        Set<String> strPrivileges = new HashSet<>();
        for (Privilege item : privileges) {
            strPrivileges.add(item.getName());
        }
        return strPrivileges;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(Set<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
