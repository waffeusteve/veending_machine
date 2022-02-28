package org.pkfrc.core.entities.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.pkfrc.core.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "JWT")
@SequenceGenerator(name = "JWT_SEQ", sequenceName = "JWT_SEQ", allocationSize = 1, initialValue = 1)
@Inheritance(strategy=InheritanceType.JOINED)
public class LocalJwt implements BaseEntity<Long> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(generator = "APP_JWT_SEQ", strategy = GenerationType.SEQUENCE)
    Long id;

   
    @Column(name = "TOKEN", length = 25000)
    String token;

    @Column(name = "APP_USER")
    Long userId;   
   
    
    @Version
    private Long version;
 
}
