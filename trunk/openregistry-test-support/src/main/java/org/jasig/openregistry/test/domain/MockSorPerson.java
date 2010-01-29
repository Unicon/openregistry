/**
 * Copyright (C) 2009 Jasig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jasig.openregistry.test.domain;

import org.openregistry.core.domain.Name;
import org.openregistry.core.domain.RoleInfo;
import org.openregistry.core.domain.Type;
import org.openregistry.core.domain.internal.Entity;
import org.openregistry.core.domain.sor.SorPerson;
import org.openregistry.core.domain.sor.SorRole;

import java.util.*;

/**
 * Implementation of the SoR Person.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 1.0.0
 */

public class MockSorPerson extends Entity implements SorPerson {

    private Long id;

    private String sorId;

    private String sourceSorIdentifier;

    private Long personId;

    private Date dateOfBirth;

    private String gender;

    private List<Name> names = new ArrayList<Name>();

    private String ssn;

    private List<SorRole> roles = new ArrayList<SorRole>();

    public void setId(final Long id) {
        this.id = id;
    }

    public List<SorRole> getRoles(){
        return this.roles;
    }

    public String getSsn() {
        return this.ssn;
    }

    public void setSsn(final String ssn) {
        this.ssn = ssn;
    }

    public Long getId() {
        return this.id;
    }

    public String getSorId() {
        return this.sorId;
    }

    public String getSourceSor() {
        return this.sourceSorIdentifier;
    }

    public void setSourceSor(final String sorIdentifier) {
        this.sourceSorIdentifier = sorIdentifier;
    }

    public List<Name> getNames() {
        return this.names;
    }

    public void setSorId(final String id) {
        this.sorId = id;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(final Date date) {
        this.dateOfBirth = date;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public Name addName() {
        final MockName sorName = new MockName();
        this.names.add(sorName);
        return sorName;
    }

    public void addName(Name name) {
        this.names.add(name);
    }
    
    public Name addName(Type type) {
        final MockName sorName = new MockName();
        sorName.setType(type);
        this.names.add(sorName);
        return sorName;
    }

    public synchronized Name findNameByNameId(final Long id) {
        Name nameToFind = null;
        for (final Name name : this.names) {
            final Long nameId = name.getId();
            if (nameId != null && nameId.equals(id)) {
                nameToFind = name;
                break;
            }
        }
        return nameToFind;
    }

	public Long getPersonId() {
		return this.personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public SorRole addRole(final RoleInfo roleInfo) {
        final SorRole sorRole = new MockSorRole();
        this.roles.add(sorRole);
        return sorRole;
    }

    public void addRole(final SorRole role){
		roles.add(role);
    }

    public SorRole pickOutRole(String code) {
        return null;
    }

    public SorRole findSorRoleBySorRoleId(final String sorRoleId) {
        for (final SorRole sorRole : this.roles) {
            if (sorRole.getSorId().equals(sorRoleId)) {
                return sorRole;
            }
        }
        return null;
    }

    @Override
    public SorRole findSorRoleById(final Long roleId) {
        for (final SorRole sorRole : this.roles) {
            if (sorRole.getId().equals(roleId)) {
                return sorRole;
            }
        }
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MockSorPerson that = (MockSorPerson) o;

        if (dateOfBirth != null ? !dateOfBirth.equals(that.dateOfBirth) : that.dateOfBirth != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (names != null ? !names.equals(that.names) : that.names != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (roles != null ? !roles.equals(that.roles) : that.roles != null) return false;
        if (sorId != null ? !sorId.equals(that.sorId) : that.sorId != null) return false;
        if (sourceSorIdentifier != null ? !sourceSorIdentifier.equals(that.sourceSorIdentifier) : that.sourceSorIdentifier != null)
            return false;
        if (ssn != null ? !ssn.equals(that.ssn) : that.ssn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sorId != null ? sorId.hashCode() : 0);
        result = 31 * result + (sourceSorIdentifier != null ? sourceSorIdentifier.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (names != null ? names.hashCode() : 0);
        result = 31 * result + (ssn != null ? ssn.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}