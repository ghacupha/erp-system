package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.*;
import io.github.erp.repository.ApplicationUserRepository;
import io.github.erp.repository.search.ApplicationUserSearchRepository;
import io.github.erp.service.ApplicationUserService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import io.github.erp.web.rest.TestUtil;
import io.github.erp.web.rest.UserResourceIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ApplicationUserResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final UUID DEFAULT_DESIGNATION = UUID.randomUUID();
    private static final UUID UPDATED_DESIGNATION = UUID.randomUUID();

    private static final String DEFAULT_APPLICATION_IDENTITY = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_IDENTITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/application-users";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Mock
    private ApplicationUserService applicationUserServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ApplicationUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicationUserSearchRepository mockApplicationUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .designation(DEFAULT_DESIGNATION)
            .applicationIdentity(DEFAULT_APPLICATION_IDENTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        applicationUser.setOrganization(dealer);
        // Add required entity
        applicationUser.setDepartment(dealer);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        applicationUser.setSecurityClearance(securityClearance);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicationUser.setSystemIdentity(user);
        // Add required entity
        applicationUser.setDealerIdentity(dealer);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .designation(UPDATED_DESIGNATION)
            .applicationIdentity(UPDATED_APPLICATION_IDENTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        applicationUser.setOrganization(dealer);
        // Add required entity
        applicationUser.setDepartment(dealer);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createUpdatedEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        applicationUser.setSecurityClearance(securityClearance);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicationUser.setSystemIdentity(user);
        // Add required entity
        applicationUser.setDealerIdentity(dealer);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testApplicationUser.getApplicationIdentity()).isEqualTo(DEFAULT_APPLICATION_IDENTITY);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).save(testApplicationUser);
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setDesignation(null);

        // Create the ApplicationUser, which fails.
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApplicationIdentityIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setApplicationIdentity(null);

        // Create the ApplicationUser, which fails.
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.applicationIdentity").value(DEFAULT_APPLICATION_IDENTITY));
    }

    @Test
    @Transactional
    void getApplicationUsersByIdFiltering() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        Long id = applicationUser.getId();

        defaultApplicationUserShouldBeFound("id.equals=" + id);
        defaultApplicationUserShouldNotBeFound("id.notEquals=" + id);

        defaultApplicationUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.greaterThan=" + id);

        defaultApplicationUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApplicationUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation equals to DEFAULT_DESIGNATION
        defaultApplicationUserShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the applicationUserList where designation equals to UPDATED_DESIGNATION
        defaultApplicationUserShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation not equals to DEFAULT_DESIGNATION
        defaultApplicationUserShouldNotBeFound("designation.notEquals=" + DEFAULT_DESIGNATION);

        // Get all the applicationUserList where designation not equals to UPDATED_DESIGNATION
        defaultApplicationUserShouldBeFound("designation.notEquals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultApplicationUserShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the applicationUserList where designation equals to UPDATED_DESIGNATION
        defaultApplicationUserShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where designation is not null
        defaultApplicationUserShouldBeFound("designation.specified=true");

        // Get all the applicationUserList where designation is null
        defaultApplicationUserShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity equals to DEFAULT_APPLICATION_IDENTITY
        defaultApplicationUserShouldBeFound("applicationIdentity.equals=" + DEFAULT_APPLICATION_IDENTITY);

        // Get all the applicationUserList where applicationIdentity equals to UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldNotBeFound("applicationIdentity.equals=" + UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity not equals to DEFAULT_APPLICATION_IDENTITY
        defaultApplicationUserShouldNotBeFound("applicationIdentity.notEquals=" + DEFAULT_APPLICATION_IDENTITY);

        // Get all the applicationUserList where applicationIdentity not equals to UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldBeFound("applicationIdentity.notEquals=" + UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsInShouldWork() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity in DEFAULT_APPLICATION_IDENTITY or UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldBeFound("applicationIdentity.in=" + DEFAULT_APPLICATION_IDENTITY + "," + UPDATED_APPLICATION_IDENTITY);

        // Get all the applicationUserList where applicationIdentity equals to UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldNotBeFound("applicationIdentity.in=" + UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityIsNullOrNotNull() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity is not null
        defaultApplicationUserShouldBeFound("applicationIdentity.specified=true");

        // Get all the applicationUserList where applicationIdentity is null
        defaultApplicationUserShouldNotBeFound("applicationIdentity.specified=false");
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity contains DEFAULT_APPLICATION_IDENTITY
        defaultApplicationUserShouldBeFound("applicationIdentity.contains=" + DEFAULT_APPLICATION_IDENTITY);

        // Get all the applicationUserList where applicationIdentity contains UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldNotBeFound("applicationIdentity.contains=" + UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByApplicationIdentityNotContainsSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList where applicationIdentity does not contain DEFAULT_APPLICATION_IDENTITY
        defaultApplicationUserShouldNotBeFound("applicationIdentity.doesNotContain=" + DEFAULT_APPLICATION_IDENTITY);

        // Get all the applicationUserList where applicationIdentity does not contain UPDATED_APPLICATION_IDENTITY
        defaultApplicationUserShouldBeFound("applicationIdentity.doesNotContain=" + UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void getAllApplicationUsersByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Dealer organization;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            organization = DealerResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(organization);
        em.flush();
        applicationUser.setOrganization(organization);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long organizationId = organization.getId();

        // Get all the applicationUserList where organization equals to organizationId
        defaultApplicationUserShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the applicationUserList where organization equals to (organizationId + 1)
        defaultApplicationUserShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Dealer department;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            department = DealerResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(department);
        em.flush();
        applicationUser.setDepartment(department);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long departmentId = department.getId();

        // Get all the applicationUserList where department equals to departmentId
        defaultApplicationUserShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the applicationUserList where department equals to (departmentId + 1)
        defaultApplicationUserShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        applicationUser.setSecurityClearance(securityClearance);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long securityClearanceId = securityClearance.getId();

        // Get all the applicationUserList where securityClearance equals to securityClearanceId
        defaultApplicationUserShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the applicationUserList where securityClearance equals to (securityClearanceId + 1)
        defaultApplicationUserShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersBySystemIdentityIsEqualToSomething() throws Exception {
        // Get already existing entity
        User systemIdentity = applicationUser.getSystemIdentity();
        applicationUserRepository.saveAndFlush(applicationUser);
        Long systemIdentityId = systemIdentity.getId();

        // Get all the applicationUserList where systemIdentity equals to systemIdentityId
        defaultApplicationUserShouldBeFound("systemIdentityId.equals=" + systemIdentityId);

        // Get all the applicationUserList where systemIdentity equals to (systemIdentityId + 1)
        defaultApplicationUserShouldNotBeFound("systemIdentityId.equals=" + (systemIdentityId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByUserPropertiesIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        UniversallyUniqueMapping userProperties;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            userProperties = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(userProperties);
            em.flush();
        } else {
            userProperties = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(userProperties);
        em.flush();
        applicationUser.addUserProperties(userProperties);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long userPropertiesId = userProperties.getId();

        // Get all the applicationUserList where userProperties equals to userPropertiesId
        defaultApplicationUserShouldBeFound("userPropertiesId.equals=" + userPropertiesId);

        // Get all the applicationUserList where userProperties equals to (userPropertiesId + 1)
        defaultApplicationUserShouldNotBeFound("userPropertiesId.equals=" + (userPropertiesId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByDealerIdentityIsEqualToSomething() throws Exception {
        // Get already existing entity
        Dealer dealerIdentity = applicationUser.getDealerIdentity();
        applicationUserRepository.saveAndFlush(applicationUser);
        Long dealerIdentityId = dealerIdentity.getId();

        // Get all the applicationUserList where dealerIdentity equals to dealerIdentityId
        defaultApplicationUserShouldBeFound("dealerIdentityId.equals=" + dealerIdentityId);

        // Get all the applicationUserList where dealerIdentity equals to (dealerIdentityId + 1)
        defaultApplicationUserShouldNotBeFound("dealerIdentityId.equals=" + (dealerIdentityId + 1));
    }

    @Test
    @Transactional
    void getAllApplicationUsersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        applicationUser.addPlaceholder(placeholder);
        applicationUserRepository.saveAndFlush(applicationUser);
        Long placeholderId = placeholder.getId();

        // Get all the applicationUserList where placeholder equals to placeholderId
        defaultApplicationUserShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the applicationUserList where placeholder equals to (placeholderId + 1)
        defaultApplicationUserShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApplicationUserShouldBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));

        // Check, that the count call also returns 1
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApplicationUserShouldNotBeFound(String filter) throws Exception {
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser.designation(UPDATED_DESIGNATION).applicationIdentity(UPDATED_APPLICATION_IDENTITY);
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(updatedApplicationUser);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testApplicationUser.getApplicationIdentity()).isEqualTo(UPDATED_APPLICATION_IDENTITY);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository).save(testApplicationUser);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.applicationIdentity(UPDATED_APPLICATION_IDENTITY);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testApplicationUser.getApplicationIdentity()).isEqualTo(UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.designation(UPDATED_DESIGNATION).applicationIdentity(UPDATED_APPLICATION_IDENTITY);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testApplicationUser.getApplicationIdentity()).isEqualTo(UPDATED_APPLICATION_IDENTITY);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // Create the ApplicationUser
        ApplicationUserDTO applicationUserDTO = applicationUserMapper.toDto(applicationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(0)).save(applicationUser);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ApplicationUser in Elasticsearch
        verify(mockApplicationUserSearchRepository, times(1)).deleteById(applicationUser.getId());
    }

    @Test
    @Transactional
    void searchApplicationUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        when(mockApplicationUserSearchRepository.search("id:" + applicationUser.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(applicationUser), PageRequest.of(0, 1), 1));

        // Search the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].applicationIdentity").value(hasItem(DEFAULT_APPLICATION_IDENTITY)));
    }
}
