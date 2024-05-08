package io.github.erp.internal.service.applicationUser;

import io.github.erp.domain.User;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * This is a service to bring common implementations of entities to the system generated
 * User entity, among them and probably most important the search
 */
public interface InternalSystemUserService {

    /**
     * Search for the User corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<UserDTO> search(String query);

    /**
     *
     * @return List of users from the system generated User entity
     */
    Optional<List<User>> findAll();
}
