/*
 * HelloController.java
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

package org.nerdcoding.example.micronaut.jwt.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;

import java.security.Principal;

@Controller("${micronaut.controller.base-path}")
@Secured({"ROLE_USER"})
public class HelloController {

    @Get("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<String> hello(final Principal principal) {
        return HttpResponse.status(HttpStatus.OK).body("hello");
    }

    @Get("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({"ROLE_ADMIN"})
    public HttpResponse<String> admin() {
        return HttpResponse.status(HttpStatus.OK).body("admin");
    }

}
