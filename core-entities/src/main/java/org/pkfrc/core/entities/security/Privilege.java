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

@Audited
@Data
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RC_ROLE")
@SequenceGenerator(name = "RC_ROLE_SEQ", sequenceName = "RC_ROLE_SEQ", allocationSize = 1, initialValue = 1)
public class Privilege  implements BaseEntity<Long> {

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(generator = "RC_ROLE_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME", unique = true, length = 64, nullable = false)
    private String name;

    @Column(name = "GROUPING", unique = true, length = 64)
    private String grouping;

    @Version
    private Long version;

//    @ManyToMany(mappedBy = "privileges",  fetch = FetchType.LAZY)
//    private Set<Role> roles= new HashSet<>();
//
//    @ManyToMany(mappedBy = "excludedPrivileges", fetch = FetchType.LAZY)
//    private Set<User> excludedUsers= new HashSet<>();
//
//    @ManyToMany(mappedBy = "includedPrivileges", fetch = FetchType.LAZY)
//    private Set<User> includedUsers= new HashSet<>();

    public Privilege(String name){
        this.name = name;
    }
}