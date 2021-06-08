/*
 * UserPasswordAuthenticationProvider.java
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

import org.reactivestreams.Publisher;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class UserPasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Map<String, User> IN_MEMORY_USERS = Map.of(
            "bob", new User("bob", "bob123", List.of("ROLE_USER")),
            "cindy", new User("cindy", "cindy123", List.of("ROLE_USER")),
            "admin", new User("admin", "admin123", List.of("ROLE_USER", "ROLE_ADMIN"))
    );

    @Override
    public Publisher<AuthenticationResponse> authenticate(
            final HttpRequest<?> httpRequest,
            final AuthenticationRequest<?, ?> authenticationRequest) {

        return Flowable.create(
                flowableEmitter -> authenticate(authenticationRequest)
                        .ifPresentOrElse(
                                userDetails -> { flowableEmitter.onNext(userDetails); flowableEmitter.onComplete(); },
                                () -> flowableEmitter.onError(new AuthenticationException(new AuthenticationFailed()))
                        ),
                BackpressureStrategy.ERROR
        );
    }

    private Optional<UserDetails> authenticate(final AuthenticationRequest<?, ?> authenticationRequest) {
        return findUserByUsername(authenticationRequest.getIdentity())
                .filter(user -> checkPassword(user.getCedentials(), authenticationRequest.getSecret()))
                .map(user -> new UserDetails(user.getUsername(), user.getRoles()));
    }

    private <T> Optional<User> findUserByUsername(final T username) {
        return Optional.ofNullable(IN_MEMORY_USERS.get(username));
    }

    private <T> boolean checkPassword(final String encodedPassword, final T rawPassword) {
        return encodedPassword.equals(rawPassword);
    }
}
