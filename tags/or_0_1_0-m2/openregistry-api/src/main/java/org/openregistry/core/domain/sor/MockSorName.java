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
package org.openregistry.core.domain.sor;

import org.openregistry.core.domain.MockType;
import org.openregistry.core.domain.Name;
import org.openregistry.core.domain.Type;

/**
 * Implementation of the Name domain object that conforms to the tables for the Systems of Record
 *
 *
 * @author Nancy Mond
 * @version $Revision$ $Date$
 * @since 1.0.0
 */

public final class MockSorName implements Name {

    private Long id;
    
    private Type type;

    private String prefix;

    private String given;

    private String middle;

    private String family;

    private String suffix;

    private boolean officialName;

    private boolean preferredName;


    public MockSorName() {
        // nothing to do
    }

    public Long getId() {
        return this.id;
    }
    
    public Type getType() {
        return this.type;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getGiven() {
        return this.given;
    }

    public String getMiddle() {
        return this.middle;
    }

    public String getFamily() {
        return this.family;
    }

    public String getSuffix() {
        return this.suffix;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public void setGiven(final String given) {
        this.given = given;
    }

    public void setMiddle(final String middle) {
        this.middle = middle;
    }

    public void setFamily(final String family) {
        this.family = family;
    }

    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }

    public String getFormattedName(){
        final StringBuilder builder = new StringBuilder();

        construct(builder, "", this.family, ",");
        construct(builder, "", this.given, "");

        return builder.toString();
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();

        construct(builder, "", this.prefix, " ");
        construct(builder, "", this.given, " ");
        construct(builder, "", this.middle, " ");
        construct(builder, "", this.family, "");
        construct(builder, ",", this.suffix, "");

        return builder.toString();
    }

    protected void construct(final StringBuilder builder, final String prefix, final String string, final String delimiter) {
        if (string != null) {
            builder.append(prefix);
            builder.append(string);
            builder.append(delimiter);
        }
    }

	public boolean isOfficialName() {
        return this.officialName;
	}

	public boolean isPreferredName() {
        return this.preferredName;
	}

	public void setOfficialName(final boolean officialName) {
        this.officialName = officialName;
	}

	public void setPreferredName(final boolean preferredName) {
        this.preferredName = preferredName;
	}

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MockSorName that = (MockSorName) o;

        if (officialName != that.officialName) return false;
        if (preferredName != that.preferredName) return false;
        if (family != null ? !family.equals(that.family) : that.family != null) return false;
        if (given != null ? !given.equals(that.given) : that.given != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (middle != null ? !middle.equals(that.middle) : that.middle != null) return false;
        if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) return false;
        if (suffix != null ? !suffix.equals(that.suffix) : that.suffix != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        result = 31 * result + (given != null ? given.hashCode() : 0);
        result = 31 * result + (middle != null ? middle.hashCode() : 0);
        result = 31 * result + (family != null ? family.hashCode() : 0);
        result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
        result = 31 * result + (officialName ? 1 : 0);
        result = 31 * result + (preferredName ? 1 : 0);
        return result;
    }
}