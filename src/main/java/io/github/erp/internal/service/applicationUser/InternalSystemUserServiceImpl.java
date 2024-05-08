package io.github.erp.internal.service.applicationUser;

import io.github.erp.domain.User;
import io.github.erp.repository.UserRepository;
import io.github.erp.repository.search.UserSearchRepository;
import io.github.erp.service.dto.UserDTO;
import io.github.erp.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InternalSystemUserServiceImpl implements InternalSystemUserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    public InternalSystemUserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    /**
     * Search for the User corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    public List<UserDTO> search(String query) {
        return StreamSupport.stream(userSearchRepository.search(query).spliterator(), false).map(UserDTO::new).collect(Collectors.toList());
    }

    public Optional<List<User>> findAll() {

        return Optional.of(userRepository.findAll());

    }
}
