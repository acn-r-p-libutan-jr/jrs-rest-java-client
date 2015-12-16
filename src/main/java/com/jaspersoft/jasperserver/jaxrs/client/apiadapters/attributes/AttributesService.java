/*
 * Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights  reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.attributes;

import com.jaspersoft.jasperserver.dto.authority.ClientTenant;
import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.SessionStorage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import static java.util.Arrays.asList;

/**
 * @author Alex Krasnyanskiy
 * @since 6.0.1-ALPHA
 */
public class AttributesService extends AbstractAdapter {
    private List<String> holderUri = new LinkedList<String>();

    public AttributesService(SessionStorage sessionStorage) {
        super(sessionStorage);
        holderUri.add("/");
    }

    public AttributesService forOrganization(String organizationName) {
        return this.forOrganization(new ClientTenant().setId(organizationName));
    }

    public AttributesService forOrganization(ClientTenant organization) {
        if (organization == null || organization.getId() == null || organization.getId().equals("")) {
            throw new IllegalArgumentException("Organization is not valid.");
        }
        this.holderUri.addAll(asList("organizations/", organization.getId(), "/"));
        return this;
    }

    public AttributesService forUser(ClientUser user) {
        if (user == null || user.getUsername() == null|| user.getUsername().equals("")) {
            throw new IllegalArgumentException("User is not valid.");
        }
        this.holderUri.addAll(asList("users/", user.getUsername(), "/"));
        return this;
    }


    public AttributesService forUser(String userName) {
        return this.forUser(new ClientUser().setUsername(userName));
    }

    public SingleAttributeAdapter attribute(String attributeName) {
        if (attributeName == null || attributeName.equals("")) {
            throw new IllegalArgumentException("Attribute name is not valid.");
        }
        return new SingleAttributeAdapter(StringUtils.join(holderUri, null), sessionStorage, attributeName);
    }

    public BatchAttributeAdapter allAttributes() {
        return new BatchAttributeAdapter(StringUtils.join(holderUri, null), sessionStorage);
    }

    public BatchAttributeAdapter attributes(Collection<String> attributesNames) {
        if (attributesNames == null || attributesNames.size() == 0) {
            throw new IllegalArgumentException("List of attributes is not valid.");
        }
        return new BatchAttributeAdapter(StringUtils.join(holderUri, null), sessionStorage, attributesNames);
    }

    public BatchAttributeAdapter attributes(String... attributesNames) {
        return this.attributes(asList(attributesNames));
    }
}
