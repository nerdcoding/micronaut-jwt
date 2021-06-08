/*
 * User.java
 *
 * Copyright (c) 2020, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.micronaut.jwt.auth;


import java.util.List;

public class User {

    private final String username;
    private final String cedentials;
    private final List<String> roles;

    public User(final String username, final String cedentials, final List<String> roles) {
        this.username = username;
        this.cedentials = cedentials;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getCedentials() {
        return cedentials;
    }

    public List<String> getRoles() {
        return roles;
    }
}
