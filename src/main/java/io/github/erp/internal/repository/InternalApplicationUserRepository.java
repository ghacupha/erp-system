package io.github.erp.internal.repository;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.User;
import io.github.erp.repository.ApplicationUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InternalApplicationUserRepository
    extends
    ApplicationUserRepository,
    JpaRepository<ApplicationUser, Long>,
    JpaSpecificationExecutor<ApplicationUser>{

    Optional<ApplicationUser> findApplicationUserBySystemIdentity(User systemIdentity);
}
