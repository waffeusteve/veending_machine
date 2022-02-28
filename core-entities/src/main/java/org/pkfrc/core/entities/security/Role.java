package org.pkfrc.core.entities.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.pkfrc.core.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing all the roles that a user can posses. Each role have a list of privileges or authority
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RC_ROLE")
@SequenceGenerator(name = "RC_ROLE_SEQ", sequenceName = "RC_ROLE_SEQ", allocationSize = 1, initialValue = 1)
@EqualsAndHashCode(of = {"name"})
public class Role implements BaseEntity<Long> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(generator = "RC_ROLE_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME", unique = true, length = 64, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users= new HashSet<>();

    @Version
    Long version;
    
    public Role(String name){
        this.name = name;
    }
}