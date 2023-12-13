package io.github.erp.internal.service;

import io.github.erp.domain.ApplicationUser;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class InternalUserDetailService {

    private final UserRepository userRepository;
    private final InternalApplicationUserRepository internalApplicationUserRepository;

    public InternalUserDetailService(UserRepository userRepository, InternalApplicationUserRepository internalApplicationUserRepository) {
        this.userRepository = userRepository;
        this.internalApplicationUserRepository = internalApplicationUserRepository;
    }

    public Optional<ApplicationUser> getCorrespondingApplicationUser() {

        return getCorrespondingApplicationUser(getCurrentUserDetails());
    }


    private Optional<ApplicationUser> getCorrespondingApplicationUser(UserDetails userDetails) {

        var ref = new Object() {
            Optional<ApplicationUser> reportUser = Optional.empty();
        };

        userRepository.findOneByLogin(userDetails.getUsername()).ifPresentOrElse(
            user -> internalApplicationUserRepository.findApplicationUserBySystemIdentity(user)
                .ifPresentOrElse(
                    applicationUser -> ref.reportUser = Optional.of(applicationUser),
                    () -> {throw new UsernameNotFoundException("We do not an application-user-id corresponding to user : " + user.getFirstName());}
                ),
            () -> {throw new UsernameNotFoundException("We could not retrieve user by username: " + userDetails.getUsername());}
        );

        return ref.reportUser;
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }else {
            // Handle case where user details are not found or not of the expected type
            throw new UsernameNotFoundException("Unfortunately we cannot account for the current user");
        }
    }

}
