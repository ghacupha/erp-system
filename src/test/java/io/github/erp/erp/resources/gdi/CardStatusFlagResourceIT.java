package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.domain.CardStatusFlag;
import io.github.erp.domain.enumeration.FlagCodes;
import io.github.erp.repository.CardStatusFlagRepository;
import io.github.erp.repository.search.CardStatusFlagSearchRepository;
import io.github.erp.service.dto.CardStatusFlagDTO;
import io.github.erp.service.mapper.CardStatusFlagMapper;
import io.github.erp.web.rest.CardStatusFlagResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Integration tests for the {@link CardStatusFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardStatusFlagResourceIT {

    private static final FlagCodes DEFAULT_CARD_STATUS_FLAG = FlagCodes.Y;
    private static final FlagCodes UPDATED_CARD_STATUS_FLAG = FlagCodes.N;

    private static final String DEFAULT_CARD_STATUS_FLAG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_STATUS_FLAG_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_STATUS_FLAG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_STATUS_FLAG_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/card-status-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-status-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardStatusFlagRepository cardStatusFlagRepository;

    @Autowired
    private CardStatusFlagMapper cardStatusFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardStatusFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardStatusFlagSearchRepository mockCardStatusFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardStatusFlagMockMvc;

    private CardStatusFlag cardStatusFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardStatusFlag createEntity(EntityManager em) {
        CardStatusFlag cardStatusFlag = new CardStatusFlag()
            .cardStatusFlag(DEFAULT_CARD_STATUS_FLAG)
            .cardStatusFlagDescription(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION)
            .cardStatusFlagDetails(DEFAULT_CARD_STATUS_FLAG_DETAILS);
        return cardStatusFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardStatusFlag createUpdatedEntity(EntityManager em) {
        CardStatusFlag cardStatusFlag = new CardStatusFlag()
            .cardStatusFlag(UPDATED_CARD_STATUS_FLAG)
            .cardStatusFlagDescription(UPDATED_CARD_STATUS_FLAG_DESCRIPTION)
            .cardStatusFlagDetails(UPDATED_CARD_STATUS_FLAG_DETAILS);
        return cardStatusFlag;
    }

    @BeforeEach
    public void initTest() {
        cardStatusFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createCardStatusFlag() throws Exception {
        int databaseSizeBeforeCreate = cardStatusFlagRepository.findAll().size();
        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);
        restCardStatusFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeCreate + 1);
        CardStatusFlag testCardStatusFlag = cardStatusFlagList.get(cardStatusFlagList.size() - 1);
        assertThat(testCardStatusFlag.getCardStatusFlag()).isEqualTo(DEFAULT_CARD_STATUS_FLAG);
        assertThat(testCardStatusFlag.getCardStatusFlagDescription()).isEqualTo(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);
        assertThat(testCardStatusFlag.getCardStatusFlagDetails()).isEqualTo(DEFAULT_CARD_STATUS_FLAG_DETAILS);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(1)).save(testCardStatusFlag);
    }

    @Test
    @Transactional
    void createCardStatusFlagWithExistingId() throws Exception {
        // Create the CardStatusFlag with an existing ID
        cardStatusFlag.setId(1L);
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        int databaseSizeBeforeCreate = cardStatusFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardStatusFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void checkCardStatusFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardStatusFlagRepository.findAll().size();
        // set the field null
        cardStatusFlag.setCardStatusFlag(null);

        // Create the CardStatusFlag, which fails.
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        restCardStatusFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardStatusFlagDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardStatusFlagRepository.findAll().size();
        // set the field null
        cardStatusFlag.setCardStatusFlagDescription(null);

        // Create the CardStatusFlag, which fails.
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        restCardStatusFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardStatusFlags() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardStatusFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStatusFlag").value(hasItem(DEFAULT_CARD_STATUS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStatusFlagDescription").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardStatusFlagDetails").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DETAILS)));
    }

    @Test
    @Transactional
    void getCardStatusFlag() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get the cardStatusFlag
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, cardStatusFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardStatusFlag.getId().intValue()))
            .andExpect(jsonPath("$.cardStatusFlag").value(DEFAULT_CARD_STATUS_FLAG.toString()))
            .andExpect(jsonPath("$.cardStatusFlagDescription").value(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION))
            .andExpect(jsonPath("$.cardStatusFlagDetails").value(DEFAULT_CARD_STATUS_FLAG_DETAILS));
    }

    @Test
    @Transactional
    void getCardStatusFlagsByIdFiltering() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        Long id = cardStatusFlag.getId();

        defaultCardStatusFlagShouldBeFound("id.equals=" + id);
        defaultCardStatusFlagShouldNotBeFound("id.notEquals=" + id);

        defaultCardStatusFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardStatusFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultCardStatusFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardStatusFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlag equals to DEFAULT_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldBeFound("cardStatusFlag.equals=" + DEFAULT_CARD_STATUS_FLAG);

        // Get all the cardStatusFlagList where cardStatusFlag equals to UPDATED_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlag.equals=" + UPDATED_CARD_STATUS_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlag not equals to DEFAULT_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlag.notEquals=" + DEFAULT_CARD_STATUS_FLAG);

        // Get all the cardStatusFlagList where cardStatusFlag not equals to UPDATED_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldBeFound("cardStatusFlag.notEquals=" + UPDATED_CARD_STATUS_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagIsInShouldWork() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlag in DEFAULT_CARD_STATUS_FLAG or UPDATED_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldBeFound("cardStatusFlag.in=" + DEFAULT_CARD_STATUS_FLAG + "," + UPDATED_CARD_STATUS_FLAG);

        // Get all the cardStatusFlagList where cardStatusFlag equals to UPDATED_CARD_STATUS_FLAG
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlag.in=" + UPDATED_CARD_STATUS_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlag is not null
        defaultCardStatusFlagShouldBeFound("cardStatusFlag.specified=true");

        // Get all the cardStatusFlagList where cardStatusFlag is null
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription equals to DEFAULT_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDescription.equals=" + DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);

        // Get all the cardStatusFlagList where cardStatusFlagDescription equals to UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.equals=" + UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription not equals to DEFAULT_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.notEquals=" + DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);

        // Get all the cardStatusFlagList where cardStatusFlagDescription not equals to UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDescription.notEquals=" + UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription in DEFAULT_CARD_STATUS_FLAG_DESCRIPTION or UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldBeFound(
            "cardStatusFlagDescription.in=" + DEFAULT_CARD_STATUS_FLAG_DESCRIPTION + "," + UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        );

        // Get all the cardStatusFlagList where cardStatusFlagDescription equals to UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.in=" + UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription is not null
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDescription.specified=true");

        // Get all the cardStatusFlagList where cardStatusFlagDescription is null
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription contains DEFAULT_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDescription.contains=" + DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);

        // Get all the cardStatusFlagList where cardStatusFlagDescription contains UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.contains=" + UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDescription does not contain DEFAULT_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDescription.doesNotContain=" + DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);

        // Get all the cardStatusFlagList where cardStatusFlagDescription does not contain UPDATED_CARD_STATUS_FLAG_DESCRIPTION
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDescription.doesNotContain=" + UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails equals to DEFAULT_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDetails.equals=" + DEFAULT_CARD_STATUS_FLAG_DETAILS);

        // Get all the cardStatusFlagList where cardStatusFlagDetails equals to UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.equals=" + UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails not equals to DEFAULT_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.notEquals=" + DEFAULT_CARD_STATUS_FLAG_DETAILS);

        // Get all the cardStatusFlagList where cardStatusFlagDetails not equals to UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDetails.notEquals=" + UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails in DEFAULT_CARD_STATUS_FLAG_DETAILS or UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldBeFound(
            "cardStatusFlagDetails.in=" + DEFAULT_CARD_STATUS_FLAG_DETAILS + "," + UPDATED_CARD_STATUS_FLAG_DETAILS
        );

        // Get all the cardStatusFlagList where cardStatusFlagDetails equals to UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.in=" + UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails is not null
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDetails.specified=true");

        // Get all the cardStatusFlagList where cardStatusFlagDetails is null
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsContainsSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails contains DEFAULT_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDetails.contains=" + DEFAULT_CARD_STATUS_FLAG_DETAILS);

        // Get all the cardStatusFlagList where cardStatusFlagDetails contains UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.contains=" + UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatusFlagsByCardStatusFlagDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        // Get all the cardStatusFlagList where cardStatusFlagDetails does not contain DEFAULT_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldNotBeFound("cardStatusFlagDetails.doesNotContain=" + DEFAULT_CARD_STATUS_FLAG_DETAILS);

        // Get all the cardStatusFlagList where cardStatusFlagDetails does not contain UPDATED_CARD_STATUS_FLAG_DETAILS
        defaultCardStatusFlagShouldBeFound("cardStatusFlagDetails.doesNotContain=" + UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardStatusFlagShouldBeFound(String filter) throws Exception {
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardStatusFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStatusFlag").value(hasItem(DEFAULT_CARD_STATUS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStatusFlagDescription").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardStatusFlagDetails").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DETAILS)));

        // Check, that the count call also returns 1
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardStatusFlagShouldNotBeFound(String filter) throws Exception {
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardStatusFlag() throws Exception {
        // Get the cardStatusFlag
        restCardStatusFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardStatusFlag() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();

        // Update the cardStatusFlag
        CardStatusFlag updatedCardStatusFlag = cardStatusFlagRepository.findById(cardStatusFlag.getId()).get();
        // Disconnect from session so that the updates on updatedCardStatusFlag are not directly saved in db
        em.detach(updatedCardStatusFlag);
        updatedCardStatusFlag
            .cardStatusFlag(UPDATED_CARD_STATUS_FLAG)
            .cardStatusFlagDescription(UPDATED_CARD_STATUS_FLAG_DESCRIPTION)
            .cardStatusFlagDetails(UPDATED_CARD_STATUS_FLAG_DETAILS);
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(updatedCardStatusFlag);

        restCardStatusFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardStatusFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);
        CardStatusFlag testCardStatusFlag = cardStatusFlagList.get(cardStatusFlagList.size() - 1);
        assertThat(testCardStatusFlag.getCardStatusFlag()).isEqualTo(UPDATED_CARD_STATUS_FLAG);
        assertThat(testCardStatusFlag.getCardStatusFlagDescription()).isEqualTo(UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
        assertThat(testCardStatusFlag.getCardStatusFlagDetails()).isEqualTo(UPDATED_CARD_STATUS_FLAG_DETAILS);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository).save(testCardStatusFlag);
    }

    @Test
    @Transactional
    void putNonExistingCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardStatusFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void partialUpdateCardStatusFlagWithPatch() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();

        // Update the cardStatusFlag using partial update
        CardStatusFlag partialUpdatedCardStatusFlag = new CardStatusFlag();
        partialUpdatedCardStatusFlag.setId(cardStatusFlag.getId());

        restCardStatusFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardStatusFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardStatusFlag))
            )
            .andExpect(status().isOk());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);
        CardStatusFlag testCardStatusFlag = cardStatusFlagList.get(cardStatusFlagList.size() - 1);
        assertThat(testCardStatusFlag.getCardStatusFlag()).isEqualTo(DEFAULT_CARD_STATUS_FLAG);
        assertThat(testCardStatusFlag.getCardStatusFlagDescription()).isEqualTo(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION);
        assertThat(testCardStatusFlag.getCardStatusFlagDetails()).isEqualTo(DEFAULT_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardStatusFlagWithPatch() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();

        // Update the cardStatusFlag using partial update
        CardStatusFlag partialUpdatedCardStatusFlag = new CardStatusFlag();
        partialUpdatedCardStatusFlag.setId(cardStatusFlag.getId());

        partialUpdatedCardStatusFlag
            .cardStatusFlag(UPDATED_CARD_STATUS_FLAG)
            .cardStatusFlagDescription(UPDATED_CARD_STATUS_FLAG_DESCRIPTION)
            .cardStatusFlagDetails(UPDATED_CARD_STATUS_FLAG_DETAILS);

        restCardStatusFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardStatusFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardStatusFlag))
            )
            .andExpect(status().isOk());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);
        CardStatusFlag testCardStatusFlag = cardStatusFlagList.get(cardStatusFlagList.size() - 1);
        assertThat(testCardStatusFlag.getCardStatusFlag()).isEqualTo(UPDATED_CARD_STATUS_FLAG);
        assertThat(testCardStatusFlag.getCardStatusFlagDescription()).isEqualTo(UPDATED_CARD_STATUS_FLAG_DESCRIPTION);
        assertThat(testCardStatusFlag.getCardStatusFlagDetails()).isEqualTo(UPDATED_CARD_STATUS_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardStatusFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardStatusFlag() throws Exception {
        int databaseSizeBeforeUpdate = cardStatusFlagRepository.findAll().size();
        cardStatusFlag.setId(count.incrementAndGet());

        // Create the CardStatusFlag
        CardStatusFlagDTO cardStatusFlagDTO = cardStatusFlagMapper.toDto(cardStatusFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStatusFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardStatusFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardStatusFlag in the database
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(0)).save(cardStatusFlag);
    }

    @Test
    @Transactional
    void deleteCardStatusFlag() throws Exception {
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);

        int databaseSizeBeforeDelete = cardStatusFlagRepository.findAll().size();

        // Delete the cardStatusFlag
        restCardStatusFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardStatusFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardStatusFlag> cardStatusFlagList = cardStatusFlagRepository.findAll();
        assertThat(cardStatusFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardStatusFlag in Elasticsearch
        verify(mockCardStatusFlagSearchRepository, times(1)).deleteById(cardStatusFlag.getId());
    }

    @Test
    @Transactional
    void searchCardStatusFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardStatusFlagRepository.saveAndFlush(cardStatusFlag);
        when(mockCardStatusFlagSearchRepository.search("id:" + cardStatusFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardStatusFlag), PageRequest.of(0, 1), 1));

        // Search the cardStatusFlag
        restCardStatusFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardStatusFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardStatusFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStatusFlag").value(hasItem(DEFAULT_CARD_STATUS_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStatusFlagDescription").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardStatusFlagDetails").value(hasItem(DEFAULT_CARD_STATUS_FLAG_DETAILS)));
    }
}
