package io.github.erp.internal.service.applicationUser;

import io.github.erp.domain.ApplicationUser;

public class CurrentUserContext {

    private static final ThreadLocal<ApplicationUser> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(ApplicationUser user) {
        currentUser.set(user);
    }

    public static ApplicationUser getCurrentUser() {
        return currentUser.get();
    }

    public static void clearCurrentUser() {
        currentUser.remove();
    }
}

