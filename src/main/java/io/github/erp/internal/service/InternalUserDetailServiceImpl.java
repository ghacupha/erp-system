package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
public class InternalUserDetailServiceImpl implements InternalUserDetailService {

    private final UserRepository userRepository;
    private final InternalApplicationUserRepository internalApplicationUserRepository;

    public InternalUserDetailServiceImpl(UserRepository userRepository, InternalApplicationUserRepository internalApplicationUserRepository) {
        this.userRepository = userRepository;
        this.internalApplicationUserRepository = internalApplicationUserRepository;
    }

    @Override
    public Optional<ApplicationUser> getCurrentApplicationUser() {

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

    private UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }else {
            // Handle case where user details are not found or not of the expected type
            throw new UsernameNotFoundException("Unfortunately we cannot account for the current user");
        }
    }

}
