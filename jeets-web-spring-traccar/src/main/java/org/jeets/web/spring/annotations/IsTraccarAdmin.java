package org.jeets.web.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jeets.web.spring.traccar.TraccarAuthentication;
import org.springframework.security.access.prepost.PostAuthorize;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("hasRole('" + TraccarAuthentication.traccarAdmin + "')")
public @interface IsTraccarAdmin {}
