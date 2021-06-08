/*
 * UserRefreshTokenPersistence.java
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

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class UserRefreshTokenPersistence implements RefreshTokenPersistence {

    // Note: old refresh tokens are never removed!
    private static final Map<String, RefreshToken> IN_MEMORY_REFRESH_TOKEN_STORE = new HashMap<>();

    @Override
    @EventListener
    public void persistToken(final RefreshTokenGeneratedEvent event) {
        IN_MEMORY_REFRESH_TOKEN_STORE.keySet()
                .forEach(key -> System.out.println(key));

        if (event != null
                && event.getRefreshToken() != null
                && event.getUserDetails() != null
                && event.getUserDetails().getUsername() != null) {

            IN_MEMORY_REFRESH_TOKEN_STORE.put(
                    event.getRefreshToken(),
                    new RefreshToken(
                            event.getRefreshToken(),
                            false,
                            event.getUserDetails().getUsername(),
                            event.getUserDetails().getRoles() == null
                                    ? Collections.emptyList()
                                    : List.copyOf(event.getUserDetails().getRoles())
                    )
            );
        }
    }

    @Override
    public Publisher<UserDetails> getUserDetails(final String refreshToken) {
        return Flowable.create(emitter -> {

            final RefreshToken existingRefreshToken = IN_MEMORY_REFRESH_TOKEN_STORE.get(refreshToken);

            if (existingRefreshToken != null) {
                if (existingRefreshToken.isRevoked()) {
                    emitter.onError(new OauthErrorResponseException(
                            IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "refresh token revoked",
                            null)
                    );
                } else {
                    emitter.onNext(new UserDetails(
                            existingRefreshToken.getUsername(),
                            existingRefreshToken.getRoles())
                    );
                    emitter.onComplete();
                }
            } else {
                emitter.onError(new OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token not found",
                        null)
                );
            }
        }, BackpressureStrategy.ERROR);
    }
}
