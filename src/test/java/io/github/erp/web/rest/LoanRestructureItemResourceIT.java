package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.LoanRestructureItem;
import io.github.erp.repository.LoanRestructureItemRepository;
import io.github.erp.repository.search.LoanRestructureItemSearchRepository;
import io.github.erp.service.criteria.LoanRestructureItemCriteria;
import io.github.erp.service.dto.LoanRestructureItemDTO;
import io.github.erp.service.mapper.LoanRestructureItemMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LoanRestructureItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoanRestructureItemResourceIT {

    private static final String DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_RESTRUCTURE_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loan-restructure-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/loan-restructure-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanRestructureItemRepository loanRestructureItemRepository;

    @Autowired
    private LoanRestructureItemMapper loanRestructureItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanRestructureItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanRestructureItemSearchRepository mockLoanRestructureItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanRestructureItemMockMvc;

    private LoanRestructureItem loanRestructureItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRestructureItem createEntity(EntityManager em) {
        LoanRestructureItem loanRestructureItem = new LoanRestructureItem()
            .loanRestructureItemCode(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE)
            .loanRestructureItemType(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE)
            .loanRestructureItemDetails(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS);
        return loanRestructureItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanRestructureItem createUpdatedEntity(EntityManager em) {
        LoanRestructureItem loanRestructureItem = new LoanRestructureItem()
            .loanRestructureItemCode(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE)
            .loanRestructureItemType(UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE)
            .loanRestructureItemDetails(UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS);
        return loanRestructureItem;
    }

    @BeforeEach
    public void initTest() {
        loanRestructureItem = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanRestructureItem() throws Exception {
        int databaseSizeBeforeCreate = loanRestructureItemRepository.findAll().size();
        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);
        restLoanRestructureItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeCreate + 1);
        LoanRestructureItem testLoanRestructureItem = loanRestructureItemList.get(loanRestructureItemList.size() - 1);
        assertThat(testLoanRestructureItem.getLoanRestructureItemCode()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemType()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemDetails()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(1)).save(testLoanRestructureItem);
    }

    @Test
    @Transactional
    void createLoanRestructureItemWithExistingId() throws Exception {
        // Create the LoanRestructureItem with an existing ID
        loanRestructureItem.setId(1L);
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        int databaseSizeBeforeCreate = loanRestructureItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanRestructureItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void checkLoanRestructureItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRestructureItemRepository.findAll().size();
        // set the field null
        loanRestructureItem.setLoanRestructureItemCode(null);

        // Create the LoanRestructureItem, which fails.
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        restLoanRestructureItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanRestructureItemTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRestructureItemRepository.findAll().size();
        // set the field null
        loanRestructureItem.setLoanRestructureItemType(null);

        // Create the LoanRestructureItem, which fails.
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        restLoanRestructureItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItems() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureItemCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanRestructureItem() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get the loanRestructureItem
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL_ID, loanRestructureItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanRestructureItem.getId().intValue()))
            .andExpect(jsonPath("$.loanRestructureItemCode").value(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE))
            .andExpect(jsonPath("$.loanRestructureItemType").value(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE))
            .andExpect(jsonPath("$.loanRestructureItemDetails").value(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanRestructureItemsByIdFiltering() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        Long id = loanRestructureItem.getId();

        defaultLoanRestructureItemShouldBeFound("id.equals=" + id);
        defaultLoanRestructureItemShouldNotBeFound("id.notEquals=" + id);

        defaultLoanRestructureItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanRestructureItemShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanRestructureItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanRestructureItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode equals to DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemCode.equals=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE);

        // Get all the loanRestructureItemList where loanRestructureItemCode equals to UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.equals=" + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode not equals to DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.notEquals=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE);

        // Get all the loanRestructureItemList where loanRestructureItemCode not equals to UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemCode.notEquals=" + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode in DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE or UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldBeFound(
            "loanRestructureItemCode.in=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE + "," + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        );

        // Get all the loanRestructureItemList where loanRestructureItemCode equals to UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.in=" + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode is not null
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemCode.specified=true");

        // Get all the loanRestructureItemList where loanRestructureItemCode is null
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode contains DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemCode.contains=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE);

        // Get all the loanRestructureItemList where loanRestructureItemCode contains UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.contains=" + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemCode does not contain DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemCode.doesNotContain=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE);

        // Get all the loanRestructureItemList where loanRestructureItemCode does not contain UPDATED_LOAN_RESTRUCTURE_ITEM_CODE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemCode.doesNotContain=" + UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType equals to DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemType.equals=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);

        // Get all the loanRestructureItemList where loanRestructureItemType equals to UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.equals=" + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType not equals to DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.notEquals=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);

        // Get all the loanRestructureItemList where loanRestructureItemType not equals to UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemType.notEquals=" + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType in DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE or UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldBeFound(
            "loanRestructureItemType.in=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE + "," + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        );

        // Get all the loanRestructureItemList where loanRestructureItemType equals to UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.in=" + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType is not null
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemType.specified=true");

        // Get all the loanRestructureItemList where loanRestructureItemType is null
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType contains DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemType.contains=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);

        // Get all the loanRestructureItemList where loanRestructureItemType contains UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.contains=" + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanRestructureItemsByLoanRestructureItemTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        // Get all the loanRestructureItemList where loanRestructureItemType does not contain DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldNotBeFound("loanRestructureItemType.doesNotContain=" + DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);

        // Get all the loanRestructureItemList where loanRestructureItemType does not contain UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE
        defaultLoanRestructureItemShouldBeFound("loanRestructureItemType.doesNotContain=" + UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanRestructureItemShouldBeFound(String filter) throws Exception {
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureItemCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanRestructureItemShouldNotBeFound(String filter) throws Exception {
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanRestructureItem() throws Exception {
        // Get the loanRestructureItem
        restLoanRestructureItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanRestructureItem() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();

        // Update the loanRestructureItem
        LoanRestructureItem updatedLoanRestructureItem = loanRestructureItemRepository.findById(loanRestructureItem.getId()).get();
        // Disconnect from session so that the updates on updatedLoanRestructureItem are not directly saved in db
        em.detach(updatedLoanRestructureItem);
        updatedLoanRestructureItem
            .loanRestructureItemCode(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE)
            .loanRestructureItemType(UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE)
            .loanRestructureItemDetails(UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS);
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(updatedLoanRestructureItem);

        restLoanRestructureItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRestructureItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureItem testLoanRestructureItem = loanRestructureItemList.get(loanRestructureItemList.size() - 1);
        assertThat(testLoanRestructureItem.getLoanRestructureItemCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemType()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemDetails()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository).save(testLoanRestructureItem);
    }

    @Test
    @Transactional
    void putNonExistingLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanRestructureItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void partialUpdateLoanRestructureItemWithPatch() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();

        // Update the loanRestructureItem using partial update
        LoanRestructureItem partialUpdatedLoanRestructureItem = new LoanRestructureItem();
        partialUpdatedLoanRestructureItem.setId(loanRestructureItem.getId());

        partialUpdatedLoanRestructureItem.loanRestructureItemCode(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);

        restLoanRestructureItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRestructureItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRestructureItem))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureItem testLoanRestructureItem = loanRestructureItemList.get(loanRestructureItemList.size() - 1);
        assertThat(testLoanRestructureItem.getLoanRestructureItemCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemType()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemDetails()).isEqualTo(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanRestructureItemWithPatch() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();

        // Update the loanRestructureItem using partial update
        LoanRestructureItem partialUpdatedLoanRestructureItem = new LoanRestructureItem();
        partialUpdatedLoanRestructureItem.setId(loanRestructureItem.getId());

        partialUpdatedLoanRestructureItem
            .loanRestructureItemCode(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE)
            .loanRestructureItemType(UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE)
            .loanRestructureItemDetails(UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS);

        restLoanRestructureItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanRestructureItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanRestructureItem))
            )
            .andExpect(status().isOk());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);
        LoanRestructureItem testLoanRestructureItem = loanRestructureItemList.get(loanRestructureItemList.size() - 1);
        assertThat(testLoanRestructureItem.getLoanRestructureItemCode()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_CODE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemType()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_TYPE);
        assertThat(testLoanRestructureItem.getLoanRestructureItemDetails()).isEqualTo(UPDATED_LOAN_RESTRUCTURE_ITEM_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanRestructureItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanRestructureItem() throws Exception {
        int databaseSizeBeforeUpdate = loanRestructureItemRepository.findAll().size();
        loanRestructureItem.setId(count.incrementAndGet());

        // Create the LoanRestructureItem
        LoanRestructureItemDTO loanRestructureItemDTO = loanRestructureItemMapper.toDto(loanRestructureItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanRestructureItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanRestructureItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanRestructureItem in the database
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(0)).save(loanRestructureItem);
    }

    @Test
    @Transactional
    void deleteLoanRestructureItem() throws Exception {
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);

        int databaseSizeBeforeDelete = loanRestructureItemRepository.findAll().size();

        // Delete the loanRestructureItem
        restLoanRestructureItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanRestructureItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanRestructureItem> loanRestructureItemList = loanRestructureItemRepository.findAll();
        assertThat(loanRestructureItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanRestructureItem in Elasticsearch
        verify(mockLoanRestructureItemSearchRepository, times(1)).deleteById(loanRestructureItem.getId());
    }

    @Test
    @Transactional
    void searchLoanRestructureItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanRestructureItemRepository.saveAndFlush(loanRestructureItem);
        when(mockLoanRestructureItemSearchRepository.search("id:" + loanRestructureItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanRestructureItem), PageRequest.of(0, 1), 1));

        // Search the loanRestructureItem
        restLoanRestructureItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanRestructureItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanRestructureItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanRestructureItemCode").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemType").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].loanRestructureItemDetails").value(hasItem(DEFAULT_LOAN_RESTRUCTURE_ITEM_DETAILS.toString())));
    }
}
