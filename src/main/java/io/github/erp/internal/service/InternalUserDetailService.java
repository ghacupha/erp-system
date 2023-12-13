package io.github.erp.internal.service;

import io.github.erp.domain.ApplicationUser;

import java.util.Optional;

public interface InternalUserDetailService {

    Optional<ApplicationUser> getCurrentApplicationUser();
}
