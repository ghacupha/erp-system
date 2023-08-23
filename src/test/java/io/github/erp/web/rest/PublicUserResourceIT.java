
/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.User;
import io.github.erp.repository.UserRepository;
import io.github.erp.repository.search.UserSearchRepository;
import io.github.erp.security.AuthoritiesConstants;
import io.github.erp.web.rest.userManagement.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@DirtiesContext
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class PublicUserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    @Autowired
    private UserRepository userRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.UserSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserSearchRepository mockUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).clear();
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).clear();
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.initTestUser(userRepository, em);
    }

    @Test
    @Transactional
    void getAllPublicUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the users
        restUserMockMvc
            .perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].email").doesNotExist())
            .andExpect(jsonPath("$.[*].imageUrl").doesNotExist())
            .andExpect(jsonPath("$.[*].langKey").doesNotExist());
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        restUserMockMvc
            .perform(get("/api/authorities").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasItems(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)));
    }

    @Test
    @Transactional
    void getAllUsersSortedByParameters() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        restUserMockMvc.perform(get("/api/users?sort=resetKey,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=password,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        restUserMockMvc
            .perform(get("/api/users?sort=resetKey,id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        restUserMockMvc.perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
