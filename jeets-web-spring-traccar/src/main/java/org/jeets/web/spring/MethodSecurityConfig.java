package org.jeets.web.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/*
 * you could also use annotations in the web configuration
 * 		@EnableGlobalMethodSecurity(prePostEnabled = true)
 * 		@EnableWebSecurity
 * of your Application instead of using this class
 */

@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true
    //    , securedEnabled = true
    //    ,  jsr250Enabled = true
    )
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {}
