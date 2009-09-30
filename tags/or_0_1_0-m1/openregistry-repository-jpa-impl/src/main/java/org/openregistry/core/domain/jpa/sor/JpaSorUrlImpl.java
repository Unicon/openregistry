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
package org.openregistry.core.domain.jpa.sor;

import org.hibernate.envers.Audited;
import org.openregistry.core.domain.internal.Entity;
import org.openregistry.core.domain.Url;
import org.openregistry.core.domain.Type;
import org.openregistry.core.domain.jpa.JpaTypeImpl;
import org.openregistry.core.domain.jpa.sor.JpaSorRoleImpl;
import org.javalid.annotations.validation.NotNull;
import org.javalid.annotations.core.ValidateDefinition;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Nancy Mond
 * Date: Apr 7, 2009
 * Time: 2:45:38 PM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Entity(name="sorUrl")
@Table(name="prs_urls")
@Audited
@ValidateDefinition
public final class JpaSorUrlImpl extends Entity implements Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prs_urls_seq")
    @SequenceGenerator(name="prs_urls_seq",sequenceName="prs_urls_seq",initialValue=1,allocationSize=50)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="address_t")
    @NotNull
    private JpaTypeImpl type;

    @Column(name="url",length=500,nullable = false)
    @NotNull
    private URL url;

    @ManyToOne(optional=false)
    @JoinColumn(name="role_record_id")
    private JpaSorRoleImpl sorRole;

    public JpaSorUrlImpl() {
        // nothing to do
    }

    public JpaSorUrlImpl(final JpaSorRoleImpl sorRole) {
        this.sorRole = sorRole;
    }

    public Type getType() {
        return this.type;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setType(final Type type) {
        Assert.isInstanceOf(JpaTypeImpl.class, type);
        this.type = (JpaTypeImpl) type;
    }

    public void setUrl(final URL url) {
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }
}