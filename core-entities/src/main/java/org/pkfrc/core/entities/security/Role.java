package org.pkfrc.core.entities.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.pkfrc.core.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing all the roles that a user can posses. Each role have a list of privileges or authority
 */
@Audited
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RC_ROLE")
@SequenceGenerator(name = "RC_ROLE_SEQ", sequenceName = "RC_ROLE_SEQ", allocationSize = 1, initialValue = 1)
@EqualsAndHashCode(of = {"name"})
public class Role implements BaseEntity<Long> {

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