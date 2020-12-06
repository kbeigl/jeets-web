package org.jeets.web.spring.annotations;

import org.jeets.web.spring.traccar.TraccarAuthentication;
import org.springframework.security.access.prepost.PostAuthorize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("hasRole('" + TraccarAuthentication.traccarManager + "')")
public @interface IsTraccarManager {
}
