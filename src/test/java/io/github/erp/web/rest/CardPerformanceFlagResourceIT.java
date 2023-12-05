package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CardPerformanceFlag;
import io.github.erp.domain.enumeration.CardPerformanceFlags;
import io.github.erp.repository.CardPerformanceFlagRepository;
import io.github.erp.repository.search.CardPerformanceFlagSearchRepository;
import io.github.erp.service.criteria.CardPerformanceFlagCriteria;
import io.github.erp.service.dto.CardPerformanceFlagDTO;
import io.github.erp.service.mapper.CardPerformanceFlagMapper;
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
 * Integration tests for the {@link CardPerformanceFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardPerformanceFlagResourceIT {

    private static final CardPerformanceFlags DEFAULT_CARD_PERFORMANCE_FLAG = CardPerformanceFlags.Y;
    private static final CardPerformanceFlags UPDATED_CARD_PERFORMANCE_FLAG = CardPerformanceFlags.N;

    private static final String DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_PERFORMANCE_FLAG_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-performance-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-performance-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardPerformanceFlagRepository cardPerformanceFlagRepository;

    @Autowired
    private CardPerformanceFlagMapper cardPerformanceFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardPerformanceFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardPerformanceFlagSearchRepository mockCardPerformanceFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardPerformanceFlagMockMvc;

    private CardPerformanceFlag cardPerformanceFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardPerformanceFlag createEntity(EntityManager em) {
        CardPerformanceFlag cardPerformanceFlag = new CardPerformanceFlag()
            .cardPerformanceFlag(DEFAULT_CARD_PERFORMANCE_FLAG)
            .cardPerformanceFlagDescription(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION)
            .cardPerformanceFlagDetails(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS);
        return cardPerformanceFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardPerformanceFlag createUpdatedEntity(EntityManager em) {
        CardPerformanceFlag cardPerformanceFlag = new CardPerformanceFlag()
            .cardPerformanceFlag(UPDATED_CARD_PERFORMANCE_FLAG)
            .cardPerformanceFlagDescription(UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION)
            .cardPerformanceFlagDetails(UPDATED_CARD_PERFORMANCE_FLAG_DETAILS);
        return cardPerformanceFlag;
    }

    @BeforeEach
    public void initTest() {
        cardPerformanceFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeCreate = cardPerformanceFlagRepository.findAll().size();
        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);
        restCardPerformanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeCreate + 1);
        CardPerformanceFlag testCardPerformanceFlag = cardPerformanceFlagList.get(cardPerformanceFlagList.size() - 1);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlag()).isEqualTo(DEFAULT_CARD_PERFORMANCE_FLAG);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDescription()).isEqualTo(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDetails()).isEqualTo(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(1)).save(testCardPerformanceFlag);
    }

    @Test
    @Transactional
    void createCardPerformanceFlagWithExistingId() throws Exception {
        // Create the CardPerformanceFlag with an existing ID
        cardPerformanceFlag.setId(1L);
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        int databaseSizeBeforeCreate = cardPerformanceFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardPerformanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void checkCardPerformanceFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardPerformanceFlagRepository.findAll().size();
        // set the field null
        cardPerformanceFlag.setCardPerformanceFlag(null);

        // Create the CardPerformanceFlag, which fails.
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        restCardPerformanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardPerformanceFlagDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardPerformanceFlagRepository.findAll().size();
        // set the field null
        cardPerformanceFlag.setCardPerformanceFlagDescription(null);

        // Create the CardPerformanceFlag, which fails.
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        restCardPerformanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlags() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardPerformanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlag").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDescription").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDetails").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardPerformanceFlag() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get the cardPerformanceFlag
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, cardPerformanceFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardPerformanceFlag.getId().intValue()))
            .andExpect(jsonPath("$.cardPerformanceFlag").value(DEFAULT_CARD_PERFORMANCE_FLAG.toString()))
            .andExpect(jsonPath("$.cardPerformanceFlagDescription").value(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION))
            .andExpect(jsonPath("$.cardPerformanceFlagDetails").value(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardPerformanceFlagsByIdFiltering() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        Long id = cardPerformanceFlag.getId();

        defaultCardPerformanceFlagShouldBeFound("id.equals=" + id);
        defaultCardPerformanceFlagShouldNotBeFound("id.notEquals=" + id);

        defaultCardPerformanceFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardPerformanceFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultCardPerformanceFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardPerformanceFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag equals to DEFAULT_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlag.equals=" + DEFAULT_CARD_PERFORMANCE_FLAG);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag equals to UPDATED_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlag.equals=" + UPDATED_CARD_PERFORMANCE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag not equals to DEFAULT_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlag.notEquals=" + DEFAULT_CARD_PERFORMANCE_FLAG);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag not equals to UPDATED_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlag.notEquals=" + UPDATED_CARD_PERFORMANCE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagIsInShouldWork() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag in DEFAULT_CARD_PERFORMANCE_FLAG or UPDATED_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldBeFound(
            "cardPerformanceFlag.in=" + DEFAULT_CARD_PERFORMANCE_FLAG + "," + UPDATED_CARD_PERFORMANCE_FLAG
        );

        // Get all the cardPerformanceFlagList where cardPerformanceFlag equals to UPDATED_CARD_PERFORMANCE_FLAG
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlag.in=" + UPDATED_CARD_PERFORMANCE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlag is not null
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlag.specified=true");

        // Get all the cardPerformanceFlagList where cardPerformanceFlag is null
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription equals to DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlagDescription.equals=" + DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription equals to UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlagDescription.equals=" + UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription not equals to DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlagDescription.notEquals=" + DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription not equals to UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlagDescription.notEquals=" + UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription in DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION or UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldBeFound(
            "cardPerformanceFlagDescription.in=" +
            DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION +
            "," +
            UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        );

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription equals to UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlagDescription.in=" + UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription is not null
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlagDescription.specified=true");

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription is null
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlagDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription contains DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldBeFound("cardPerformanceFlagDescription.contains=" + DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription contains UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldNotBeFound("cardPerformanceFlagDescription.contains=" + UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardPerformanceFlagsByCardPerformanceFlagDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription does not contain DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldNotBeFound(
            "cardPerformanceFlagDescription.doesNotContain=" + DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION
        );

        // Get all the cardPerformanceFlagList where cardPerformanceFlagDescription does not contain UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        defaultCardPerformanceFlagShouldBeFound(
            "cardPerformanceFlagDescription.doesNotContain=" + UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardPerformanceFlagShouldBeFound(String filter) throws Exception {
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardPerformanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlag").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDescription").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDetails").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardPerformanceFlagShouldNotBeFound(String filter) throws Exception {
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardPerformanceFlag() throws Exception {
        // Get the cardPerformanceFlag
        restCardPerformanceFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardPerformanceFlag() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();

        // Update the cardPerformanceFlag
        CardPerformanceFlag updatedCardPerformanceFlag = cardPerformanceFlagRepository.findById(cardPerformanceFlag.getId()).get();
        // Disconnect from session so that the updates on updatedCardPerformanceFlag are not directly saved in db
        em.detach(updatedCardPerformanceFlag);
        updatedCardPerformanceFlag
            .cardPerformanceFlag(UPDATED_CARD_PERFORMANCE_FLAG)
            .cardPerformanceFlagDescription(UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION)
            .cardPerformanceFlagDetails(UPDATED_CARD_PERFORMANCE_FLAG_DETAILS);
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(updatedCardPerformanceFlag);

        restCardPerformanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardPerformanceFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);
        CardPerformanceFlag testCardPerformanceFlag = cardPerformanceFlagList.get(cardPerformanceFlagList.size() - 1);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlag()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDescription()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDetails()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG_DETAILS);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository).save(testCardPerformanceFlag);
    }

    @Test
    @Transactional
    void putNonExistingCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardPerformanceFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void partialUpdateCardPerformanceFlagWithPatch() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();

        // Update the cardPerformanceFlag using partial update
        CardPerformanceFlag partialUpdatedCardPerformanceFlag = new CardPerformanceFlag();
        partialUpdatedCardPerformanceFlag.setId(cardPerformanceFlag.getId());

        partialUpdatedCardPerformanceFlag.cardPerformanceFlag(UPDATED_CARD_PERFORMANCE_FLAG);

        restCardPerformanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardPerformanceFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardPerformanceFlag))
            )
            .andExpect(status().isOk());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);
        CardPerformanceFlag testCardPerformanceFlag = cardPerformanceFlagList.get(cardPerformanceFlagList.size() - 1);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlag()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDescription()).isEqualTo(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDetails()).isEqualTo(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardPerformanceFlagWithPatch() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();

        // Update the cardPerformanceFlag using partial update
        CardPerformanceFlag partialUpdatedCardPerformanceFlag = new CardPerformanceFlag();
        partialUpdatedCardPerformanceFlag.setId(cardPerformanceFlag.getId());

        partialUpdatedCardPerformanceFlag
            .cardPerformanceFlag(UPDATED_CARD_PERFORMANCE_FLAG)
            .cardPerformanceFlagDescription(UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION)
            .cardPerformanceFlagDetails(UPDATED_CARD_PERFORMANCE_FLAG_DETAILS);

        restCardPerformanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardPerformanceFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardPerformanceFlag))
            )
            .andExpect(status().isOk());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);
        CardPerformanceFlag testCardPerformanceFlag = cardPerformanceFlagList.get(cardPerformanceFlagList.size() - 1);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlag()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDescription()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG_DESCRIPTION);
        assertThat(testCardPerformanceFlag.getCardPerformanceFlagDetails()).isEqualTo(UPDATED_CARD_PERFORMANCE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardPerformanceFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardPerformanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardPerformanceFlagRepository.findAll().size();
        cardPerformanceFlag.setId(count.incrementAndGet());

        // Create the CardPerformanceFlag
        CardPerformanceFlagDTO cardPerformanceFlagDTO = cardPerformanceFlagMapper.toDto(cardPerformanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardPerformanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardPerformanceFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardPerformanceFlag in the database
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(0)).save(cardPerformanceFlag);
    }

    @Test
    @Transactional
    void deleteCardPerformanceFlag() throws Exception {
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);

        int databaseSizeBeforeDelete = cardPerformanceFlagRepository.findAll().size();

        // Delete the cardPerformanceFlag
        restCardPerformanceFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardPerformanceFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardPerformanceFlag> cardPerformanceFlagList = cardPerformanceFlagRepository.findAll();
        assertThat(cardPerformanceFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardPerformanceFlag in Elasticsearch
        verify(mockCardPerformanceFlagSearchRepository, times(1)).deleteById(cardPerformanceFlag.getId());
    }

    @Test
    @Transactional
    void searchCardPerformanceFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardPerformanceFlagRepository.saveAndFlush(cardPerformanceFlag);
        when(mockCardPerformanceFlagSearchRepository.search("id:" + cardPerformanceFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardPerformanceFlag), PageRequest.of(0, 1), 1));

        // Search the cardPerformanceFlag
        restCardPerformanceFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardPerformanceFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardPerformanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlag").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDescription").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardPerformanceFlagDetails").value(hasItem(DEFAULT_CARD_PERFORMANCE_FLAG_DETAILS.toString())));
    }
}
