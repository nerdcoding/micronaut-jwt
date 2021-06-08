/*
 * RefreshToken.java
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

public class RefreshToken {

    private final String refreshToken;
    private final boolean revoked;

    private final String username;
    private final List<String> roles;

    public RefreshToken(
            final String refreshToken,
            final boolean revoked,
            final String username,
            final List<String> roles) {
        this.refreshToken = refreshToken;
        this.revoked = revoked;
        this.username = username;
        this.roles = roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
