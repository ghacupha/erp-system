package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.BusinessStamp;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.BusinessStampRepository;
import io.github.erp.repository.search.BusinessStampSearchRepository;
import io.github.erp.service.BusinessStampService;
import io.github.erp.service.dto.BusinessStampDTO;
import io.github.erp.service.mapper.BusinessStampMapper;
import io.github.erp.web.rest.TestUtil;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the BusinessStampResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PAYMENTS_USER", "FIXED_ASSETS_USER"})
class BusinessStampResourceIT {

    private static final LocalDate DEFAULT_STAMP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STAMP_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STAMP_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments/business-stamps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search/business-stamps";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessStampRepository businessStampRepository;

    @Mock
    private BusinessStampRepository businessStampRepositoryMock;

    @Autowired
    private BusinessStampMapper businessStampMapper;

    @Mock
    private BusinessStampService businessStampServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BusinessStampSearchRepositoryMockConfiguration
     */
    @Autowired
    private BusinessStampSearchRepository mockBusinessStampSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessStampMockMvc;

    private BusinessStamp businessStamp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessStamp createEntity(EntityManager em) {
        BusinessStamp businessStamp = new BusinessStamp().stampDate(DEFAULT_STAMP_DATE).purpose(DEFAULT_PURPOSE).details(DEFAULT_DETAILS);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        businessStamp.setStampHolder(dealer);
        return businessStamp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessStamp createUpdatedEntity(EntityManager em) {
        BusinessStamp businessStamp = new BusinessStamp().stampDate(UPDATED_STAMP_DATE).purpose(UPDATED_PURPOSE).details(UPDATED_DETAILS);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        businessStamp.setStampHolder(dealer);
        return businessStamp;
    }

    @BeforeEach
    public void initTest() {
        businessStamp = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessStamp() throws Exception {
        int databaseSizeBeforeCreate = businessStampRepository.findAll().size();
        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);
        restBusinessStampMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessStamp testBusinessStamp = businessStampList.get(businessStampList.size() - 1);
        assertThat(testBusinessStamp.getStampDate()).isEqualTo(DEFAULT_STAMP_DATE);
        assertThat(testBusinessStamp.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testBusinessStamp.getDetails()).isEqualTo(DEFAULT_DETAILS);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(1)).save(testBusinessStamp);
    }

    @Test
    @Transactional
    void createBusinessStampWithExistingId() throws Exception {
        // Create the BusinessStamp with an existing ID
        businessStamp.setId(1L);
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        int databaseSizeBeforeCreate = businessStampRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessStampMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeCreate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    @Test
    @Transactional
    void getAllBusinessStamps() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessStamp.getId().intValue())))
            .andExpect(jsonPath("$.[*].stampDate").value(hasItem(DEFAULT_STAMP_DATE.toString())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessStampsWithEagerRelationshipsIsEnabled() throws Exception {
        when(businessStampServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessStampMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessStampServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessStampsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(businessStampServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessStampMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessStampServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBusinessStamp() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get the businessStamp
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL_ID, businessStamp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessStamp.getId().intValue()))
            .andExpect(jsonPath("$.stampDate").value(DEFAULT_STAMP_DATE.toString()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS));
    }

    @Test
    @Transactional
    void getBusinessStampsByIdFiltering() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        Long id = businessStamp.getId();

        defaultBusinessStampShouldBeFound("id.equals=" + id);
        defaultBusinessStampShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessStampShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessStampShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessStampShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessStampShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate equals to DEFAULT_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.equals=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate equals to UPDATED_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.equals=" + UPDATED_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate not equals to DEFAULT_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.notEquals=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate not equals to UPDATED_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.notEquals=" + UPDATED_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsInShouldWork() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate in DEFAULT_STAMP_DATE or UPDATED_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.in=" + DEFAULT_STAMP_DATE + "," + UPDATED_STAMP_DATE);

        // Get all the businessStampList where stampDate equals to UPDATED_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.in=" + UPDATED_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate is not null
        defaultBusinessStampShouldBeFound("stampDate.specified=true");

        // Get all the businessStampList where stampDate is null
        defaultBusinessStampShouldNotBeFound("stampDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate is greater than or equal to DEFAULT_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.greaterThanOrEqual=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate is greater than or equal to UPDATED_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.greaterThanOrEqual=" + UPDATED_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate is less than or equal to DEFAULT_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.lessThanOrEqual=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate is less than or equal to SMALLER_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.lessThanOrEqual=" + SMALLER_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsLessThanSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate is less than DEFAULT_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.lessThan=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate is less than UPDATED_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.lessThan=" + UPDATED_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where stampDate is greater than DEFAULT_STAMP_DATE
        defaultBusinessStampShouldNotBeFound("stampDate.greaterThan=" + DEFAULT_STAMP_DATE);

        // Get all the businessStampList where stampDate is greater than SMALLER_STAMP_DATE
        defaultBusinessStampShouldBeFound("stampDate.greaterThan=" + SMALLER_STAMP_DATE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose equals to DEFAULT_PURPOSE
        defaultBusinessStampShouldBeFound("purpose.equals=" + DEFAULT_PURPOSE);

        // Get all the businessStampList where purpose equals to UPDATED_PURPOSE
        defaultBusinessStampShouldNotBeFound("purpose.equals=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose not equals to DEFAULT_PURPOSE
        defaultBusinessStampShouldNotBeFound("purpose.notEquals=" + DEFAULT_PURPOSE);

        // Get all the businessStampList where purpose not equals to UPDATED_PURPOSE
        defaultBusinessStampShouldBeFound("purpose.notEquals=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeIsInShouldWork() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose in DEFAULT_PURPOSE or UPDATED_PURPOSE
        defaultBusinessStampShouldBeFound("purpose.in=" + DEFAULT_PURPOSE + "," + UPDATED_PURPOSE);

        // Get all the businessStampList where purpose equals to UPDATED_PURPOSE
        defaultBusinessStampShouldNotBeFound("purpose.in=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose is not null
        defaultBusinessStampShouldBeFound("purpose.specified=true");

        // Get all the businessStampList where purpose is null
        defaultBusinessStampShouldNotBeFound("purpose.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeContainsSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose contains DEFAULT_PURPOSE
        defaultBusinessStampShouldBeFound("purpose.contains=" + DEFAULT_PURPOSE);

        // Get all the businessStampList where purpose contains UPDATED_PURPOSE
        defaultBusinessStampShouldNotBeFound("purpose.contains=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPurposeNotContainsSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where purpose does not contain DEFAULT_PURPOSE
        defaultBusinessStampShouldNotBeFound("purpose.doesNotContain=" + DEFAULT_PURPOSE);

        // Get all the businessStampList where purpose does not contain UPDATED_PURPOSE
        defaultBusinessStampShouldBeFound("purpose.doesNotContain=" + UPDATED_PURPOSE);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details equals to DEFAULT_DETAILS
        defaultBusinessStampShouldBeFound("details.equals=" + DEFAULT_DETAILS);

        // Get all the businessStampList where details equals to UPDATED_DETAILS
        defaultBusinessStampShouldNotBeFound("details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details not equals to DEFAULT_DETAILS
        defaultBusinessStampShouldNotBeFound("details.notEquals=" + DEFAULT_DETAILS);

        // Get all the businessStampList where details not equals to UPDATED_DETAILS
        defaultBusinessStampShouldBeFound("details.notEquals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details in DEFAULT_DETAILS or UPDATED_DETAILS
        defaultBusinessStampShouldBeFound("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS);

        // Get all the businessStampList where details equals to UPDATED_DETAILS
        defaultBusinessStampShouldNotBeFound("details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details is not null
        defaultBusinessStampShouldBeFound("details.specified=true");

        // Get all the businessStampList where details is null
        defaultBusinessStampShouldNotBeFound("details.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details contains DEFAULT_DETAILS
        defaultBusinessStampShouldBeFound("details.contains=" + DEFAULT_DETAILS);

        // Get all the businessStampList where details contains UPDATED_DETAILS
        defaultBusinessStampShouldNotBeFound("details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        // Get all the businessStampList where details does not contain DEFAULT_DETAILS
        defaultBusinessStampShouldNotBeFound("details.doesNotContain=" + DEFAULT_DETAILS);

        // Get all the businessStampList where details does not contain UPDATED_DETAILS
        defaultBusinessStampShouldBeFound("details.doesNotContain=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllBusinessStampsByStampHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);
        Dealer stampHolder;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            stampHolder = DealerResourceIT.createEntity(em);
            em.persist(stampHolder);
            em.flush();
        } else {
            stampHolder = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(stampHolder);
        em.flush();
        businessStamp.setStampHolder(stampHolder);
        businessStampRepository.saveAndFlush(businessStamp);
        Long stampHolderId = stampHolder.getId();

        // Get all the businessStampList where stampHolder equals to stampHolderId
        defaultBusinessStampShouldBeFound("stampHolderId.equals=" + stampHolderId);

        // Get all the businessStampList where stampHolder equals to (stampHolderId + 1)
        defaultBusinessStampShouldNotBeFound("stampHolderId.equals=" + (stampHolderId + 1));
    }

    @Test
    @Transactional
    void getAllBusinessStampsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);
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
        businessStamp.addPlaceholder(placeholder);
        businessStampRepository.saveAndFlush(businessStamp);
        Long placeholderId = placeholder.getId();

        // Get all the businessStampList where placeholder equals to placeholderId
        defaultBusinessStampShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the businessStampList where placeholder equals to (placeholderId + 1)
        defaultBusinessStampShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessStampShouldBeFound(String filter) throws Exception {
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessStamp.getId().intValue())))
            .andExpect(jsonPath("$.[*].stampDate").value(hasItem(DEFAULT_STAMP_DATE.toString())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));

        // Check, that the count call also returns 1
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessStampShouldNotBeFound(String filter) throws Exception {
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessStampMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusinessStamp() throws Exception {
        // Get the businessStamp
        restBusinessStampMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessStamp() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();

        // Update the businessStamp
        BusinessStamp updatedBusinessStamp = businessStampRepository.findById(businessStamp.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessStamp are not directly saved in db
        em.detach(updatedBusinessStamp);
        updatedBusinessStamp.stampDate(UPDATED_STAMP_DATE).purpose(UPDATED_PURPOSE).details(UPDATED_DETAILS);
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(updatedBusinessStamp);

        restBusinessStampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessStampDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);
        BusinessStamp testBusinessStamp = businessStampList.get(businessStampList.size() - 1);
        assertThat(testBusinessStamp.getStampDate()).isEqualTo(UPDATED_STAMP_DATE);
        assertThat(testBusinessStamp.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testBusinessStamp.getDetails()).isEqualTo(UPDATED_DETAILS);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository).save(testBusinessStamp);
    }

    @Test
    @Transactional
    void putNonExistingBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessStampDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    // @Test
    @Transactional
    void putWithMissingIdPathParamBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    @Test
    @Transactional
    void partialUpdateBusinessStampWithPatch() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();

        // Update the businessStamp using partial update
        BusinessStamp partialUpdatedBusinessStamp = new BusinessStamp();
        partialUpdatedBusinessStamp.setId(businessStamp.getId());

        partialUpdatedBusinessStamp.stampDate(UPDATED_STAMP_DATE).purpose(UPDATED_PURPOSE).details(UPDATED_DETAILS);

        restBusinessStampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessStamp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessStamp))
            )
            .andExpect(status().isOk());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);
        BusinessStamp testBusinessStamp = businessStampList.get(businessStampList.size() - 1);
        assertThat(testBusinessStamp.getStampDate()).isEqualTo(UPDATED_STAMP_DATE);
        assertThat(testBusinessStamp.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testBusinessStamp.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateBusinessStampWithPatch() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();

        // Update the businessStamp using partial update
        BusinessStamp partialUpdatedBusinessStamp = new BusinessStamp();
        partialUpdatedBusinessStamp.setId(businessStamp.getId());

        partialUpdatedBusinessStamp.stampDate(UPDATED_STAMP_DATE).purpose(UPDATED_PURPOSE).details(UPDATED_DETAILS);

        restBusinessStampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessStamp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessStamp))
            )
            .andExpect(status().isOk());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);
        BusinessStamp testBusinessStamp = businessStampList.get(businessStampList.size() - 1);
        assertThat(testBusinessStamp.getStampDate()).isEqualTo(UPDATED_STAMP_DATE);
        assertThat(testBusinessStamp.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testBusinessStamp.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessStampDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    // @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessStamp() throws Exception {
        int databaseSizeBeforeUpdate = businessStampRepository.findAll().size();
        businessStamp.setId(count.incrementAndGet());

        // Create the BusinessStamp
        BusinessStampDTO businessStampDTO = businessStampMapper.toDto(businessStamp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessStampMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessStampDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessStamp in the database
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(0)).save(businessStamp);
    }

    @Test
    @Transactional
    void deleteBusinessStamp() throws Exception {
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);

        int databaseSizeBeforeDelete = businessStampRepository.findAll().size();

        // Delete the businessStamp
        restBusinessStampMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessStamp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessStamp> businessStampList = businessStampRepository.findAll();
        assertThat(businessStampList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BusinessStamp in Elasticsearch
        verify(mockBusinessStampSearchRepository, times(1)).deleteById(businessStamp.getId());
    }

    @Test
    @Transactional
    void searchBusinessStamp() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        businessStampRepository.saveAndFlush(businessStamp);
        when(mockBusinessStampSearchRepository.search("id:" + businessStamp.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(businessStamp), PageRequest.of(0, 1), 1));

        // Search the businessStamp
        restBusinessStampMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + businessStamp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessStamp.getId().intValue())))
            .andExpect(jsonPath("$.[*].stampDate").value(hasItem(DEFAULT_STAMP_DATE.toString())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }
}
