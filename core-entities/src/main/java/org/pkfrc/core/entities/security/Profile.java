package org.pkfrc.core.entities.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.pkfrc.core.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Audited
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PKF_PROFILE")
@SequenceGenerator(name = "RC_PROFILE_SEQ", sequenceName = "RC_PROFILE_SEQ", allocationSize = 1, initialValue = 1)
public class Profile implements BaseEntity<Long> {

	@Id
	@Column(name = "PROFIL_ID", unique = true, updatable = false)
	@GeneratedValue(generator = "PKF_SE_PROFILE_SEQ", strategy = GenerationType.SEQUENCE)
	Long id;


	private String code;

	private String intitule;

	private static final long serialVersionUID = 1L;

	// unlimited if null
	private Integer maxSessions;
	// unlimited if null
	private Integer maxConnectAttempts;
	private String defaultPwd;
	// unlimited if null
	private Integer pwdDuration;
	// unlimited if null
	private Integer unameDuration;

	// validate automattically at creation
	private Boolean autoValidated = Boolean.FALSE;
	// can be for exemple created online
	private Boolean selfCreated = Boolean.FALSE;
	private Boolean sendMail = Boolean.FALSE;
	private Boolean validateByMail = Boolean.FALSE;

	private Integer pwdMinLength = 0;
	private Integer pwdMinNbNumeric = 0;
	private Integer pwdMinNbSpecialChar = 0;
	private Integer pwdMinNbUpperChar = 0;

	protected String userCreate;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date dateCreate;

	protected String userUpdate;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date dateUpdate;

	// will be defined by the using app;
	private String type;

	@ManyToMany(fetch = FetchType.LAZY  )
	@JoinTable(name = "PKF_PROFILE_FNLTE", joinColumns = { @JoinColumn(name = "PROFIL_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "FONCTE_ID") })
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public Set<Fonctionalite> fonctionalites = new HashSet<>(0);


	@Override
	public Long getVersion() {
		return getVersion();
	}

	@Override
	public void setVersion(Long version) {

	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fonctionalites == null) ? 0 : fonctionalites.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (fonctionalites == null) {
			if (other.fonctionalites != null)
				return false;
		} else if (!fonctionalites.equals(other.fonctionalites))
			return false;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;

		return true;
	}






}


