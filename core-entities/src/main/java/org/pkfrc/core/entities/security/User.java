package org.pkfrc.core.entities.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.pkfrc.core.entities.base.BaseEntity;
import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.enums.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a user of the application.
 */

@ToString(exclude = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "RC_USER")
@SequenceGenerator(name = "RC_USER_SEQ", sequenceName = "RC_USER_SEQ", allocationSize = 1, initialValue = 1)
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(generator = "APP_USR_SEQ", strategy = GenerationType.SEQUENCE)
    Long id;

   
    @Column(name = "USERNAME", length = 64)
    String userName;

    @Column(name = "PASSWORD", length = 64)
    String password;
    
    @Column(name = "SESSION_UP")
    private Boolean sessionUp = Boolean.FALSE;
    
    @Column(name = "DEFAULT_PWD")
    private Boolean defaultPwd = Boolean.FALSE;
    
    @Column(name = "TYPE")
    private EUserType type;
    
    
    @Column(name = "STATUS")
    private EUserStatus status;   
    
    
	@Column(name = "DEPOSIT")
    private Double deposit;

   
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "APP_USRS_ROLES",
            joinColumns = @JoinColumn(
                    name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "ROLE_ID", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();


    @Version
    private Long version;
    
    
}
