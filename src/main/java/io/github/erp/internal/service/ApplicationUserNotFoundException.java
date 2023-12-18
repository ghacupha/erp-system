package io.github.erp.internal.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

/**
 * Thrown when we are unable to find matching application-user profile
 * for the current user account
 */
public class ApplicationUserNotFoundException extends UserPrincipalNotFoundException {

    public ApplicationUserNotFoundException(String name) {
        super(name);
    }
}
