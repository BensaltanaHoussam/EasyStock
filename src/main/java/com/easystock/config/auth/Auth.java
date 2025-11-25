package com.easystock.config.auth;

import com.easystock.entity.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to secure controller methods.
 * Use it on methods that require authentication and optionally authorization.
 */
@Target(ElementType.METHOD) // This annotation can only be used on methods
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime for reflection
public @interface Auth {
    /**
     * An array of roles that are permitted to access the method.
     * If empty, only authentication is required (any logged-in user can access).
     * @return an array of UserRole enums
     */
    UserRole[] allowedRoles() default {};
}