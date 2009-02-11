package org.openregistry.core.domain.jpa;

import org.openregistry.core.domain.internal.Entity;
import org.openregistry.core.domain.*;

import javax.persistence.*;
import java.util.*;

import org.javalid.annotations.core.JvGroup;
import org.javalid.annotations.core.ValidateDefinition;
import org.javalid.annotations.validation.NotEmpty;
import org.javalid.annotations.validation.NotNull;

/**
 * Role entity mapped to a persistence store with JPA annotations.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 1.0.0
 */
@javax.persistence.Entity(name="role")
@Table(name="prc_role_records")
@ValidateDefinition
public class JpaRoleImpl extends Entity implements Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prs_sor_role_record_seq")
    @SequenceGenerator(name="prs_sor_role_record_seq",sequenceName="prs_sor_role_record_seq",initialValue=1,allocationSize=50)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="role",fetch = FetchType.EAGER)
    private Set<JpaUrlImpl> urls = new HashSet<JpaUrlImpl>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy="role",fetch = FetchType.EAGER)
    private Set<JpaEmailAddressImpl> emailAddresses = new HashSet<JpaEmailAddressImpl>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy="role",fetch = FetchType.EAGER)
    private Set<JpaPhoneImpl> phones = new HashSet<JpaPhoneImpl>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy="role",fetch = FetchType.EAGER)
    private Set<JpaAddressImpl> addresses = new HashSet<JpaAddressImpl>();

    @ManyToOne(optional = false)
    @JoinColumn(name="sponsor_id")
    private JpaPersonImpl sponsor;

    @Column(name="percent_time",nullable=false)
    private int percentage;

    @Column(name="code", nullable = true, updatable = false, insertable = false)
    private String localCode;

    @ManyToOne(optional = false)
    @JoinColumn(name="status_t")
    private JpaTypeImpl personStatus;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="role",fetch=FetchType.EAGER)
    private Set<JpaLeaveImpl> leaves = new HashSet<JpaLeaveImpl>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_info_id")
    private JpaRoleInfoImpl roleInfo;

    @ManyToOne(optional = false)
    @JoinColumn(name="person_id", nullable=false)
    private JpaPersonImpl person;

    @Column(name="leave_start_date")
    @Temporal(TemporalType.DATE)
    private Date start;

    @Column(name="leave_stop_date")
    @Temporal(TemporalType.DATE)
    private Date end;

    @ManyToOne(optional = false)
    @JoinColumn(name="termination_t")
    private JpaTypeImpl terminationReason;

    public JpaRoleImpl() {

    }

    public JpaRoleImpl(JpaRoleInfoImpl roleInfo, JpaPersonImpl person) {
        this.roleInfo = roleInfo;
        this.person = person;
    }

    protected Long getId() {
        return this.id;
    }

    public Set<? extends Address> getAddresses() {
        return this.addresses;
    }

    public Set<? extends Phone> getPhones() {
        return this.phones;
    }

    public Set<? extends EmailAddress> getEmailAddresses() {
        return this.emailAddresses;
    }

    public Set<? extends Url> getUrls() {
        return this.urls;
    }

    public Address addAddress() {
        final JpaAddressImpl jpaAddress = new JpaAddressImpl(this);
        this.addresses.add(jpaAddress);
        return jpaAddress;
    }

    public Url addUrl() {
        final JpaUrlImpl url = new JpaUrlImpl(this);
        this.urls.add(url);
        return url;
    }

    public EmailAddress addEmailAddress() {
        final JpaEmailAddressImpl jpaEmailAddress = new JpaEmailAddressImpl(this);
        this.emailAddresses.add(jpaEmailAddress);
        return jpaEmailAddress;
    }

    public Phone addPhone() {
        final JpaPhoneImpl jpaPhone = new JpaPhoneImpl(this);
        this.phones.add(jpaPhone);
        return jpaPhone;
    }

    public String getTitle() {
        return this.roleInfo.getTitle();
    }

    public Type getAffiliationType() {
        return this.roleInfo.getAffiliationType();
    }

    public void setSponsor(final Person sponsor) {
        if (!(sponsor instanceof JpaPersonImpl)) {
            throw new IllegalArgumentException("sponsor must be of type JpaPersonImpl.");
        }
        this.sponsor = (JpaPersonImpl) sponsor;
    }

    @JvGroup
    @NotNull (customCode="sponsorRequiredMsg")
    public Person getSponsor() {
        return this.sponsor;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public void setPercentage(final int percentage) {
        this.percentage = percentage;
    }

    public Type getPersonStatus() {
        return this.personStatus;
    }

    public void setPersonStatus(final Type personStatus) {
        if (!(personStatus instanceof JpaTypeImpl)) {
            throw new IllegalArgumentException("Requires type JpaTypeImpl");
        }

        this.personStatus = (JpaTypeImpl) personStatus;
    }

    public Set<? extends Leave> getLeaves() {
        return this.leaves;
    }

    public Department getDepartment() {
        return this.roleInfo.getDepartment();
    }

    public Campus getCampus() {
        return this.roleInfo.getCampus();
    }

    public String getLocalCode() {
        return this.localCode;
    }

    public Type getTerminationReason() {
        return this.terminationReason;
    }

    public void setTerminationReason(final Type reason) {
        if (!(reason instanceof JpaTypeImpl)) {
            throw new IllegalArgumentException("Requires type JpaTypeImpl");
        }
        this.terminationReason = (JpaTypeImpl) reason;
    }

    @JvGroup
    @NotNull (customCode="startDateRequiredMsg")
    public Date getStart() {
        return this.start;
    }

    @JvGroup
    @NotNull (customCode="endDateRequiredMsg")
    public Date getEnd() {
        return this.end;
    }

    public void setStart(final Date date) {
        this.start = date;
    }

    public void setEnd(final Date date) {
        this.end = date;
    }
}
