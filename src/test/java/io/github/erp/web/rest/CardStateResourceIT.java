package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.domain.CardState;
import io.github.erp.domain.enumeration.CardStateFlagTypes;
import io.github.erp.repository.CardStateRepository;
import io.github.erp.repository.search.CardStateSearchRepository;
import io.github.erp.service.criteria.CardStateCriteria;
import io.github.erp.service.dto.CardStateDTO;
import io.github.erp.service.mapper.CardStateMapper;
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

/**
 * Integration tests for the {@link CardStateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardStateResourceIT {

    private static final CardStateFlagTypes DEFAULT_CARD_STATE_FLAG = CardStateFlagTypes.P;
    private static final CardStateFlagTypes UPDATED_CARD_STATE_FLAG = CardStateFlagTypes.V;

    private static final String DEFAULT_CARD_STATE_FLAG_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_STATE_FLAG_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_STATE_FLAG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_STATE_FLAG_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-states";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardStateRepository cardStateRepository;

    @Autowired
    private CardStateMapper cardStateMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardStateSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardStateSearchRepository mockCardStateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardStateMockMvc;

    private CardState cardState;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardState createEntity(EntityManager em) {
        CardState cardState = new CardState()
            .cardStateFlag(DEFAULT_CARD_STATE_FLAG)
            .cardStateFlagDetails(DEFAULT_CARD_STATE_FLAG_DETAILS)
            .cardStateFlagDescription(DEFAULT_CARD_STATE_FLAG_DESCRIPTION);
        return cardState;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardState createUpdatedEntity(EntityManager em) {
        CardState cardState = new CardState()
            .cardStateFlag(UPDATED_CARD_STATE_FLAG)
            .cardStateFlagDetails(UPDATED_CARD_STATE_FLAG_DETAILS)
            .cardStateFlagDescription(UPDATED_CARD_STATE_FLAG_DESCRIPTION);
        return cardState;
    }

    @BeforeEach
    public void initTest() {
        cardState = createEntity(em);
    }

    @Test
    @Transactional
    void createCardState() throws Exception {
        int databaseSizeBeforeCreate = cardStateRepository.findAll().size();
        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);
        restCardStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStateDTO)))
            .andExpect(status().isCreated());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeCreate + 1);
        CardState testCardState = cardStateList.get(cardStateList.size() - 1);
        assertThat(testCardState.getCardStateFlag()).isEqualTo(DEFAULT_CARD_STATE_FLAG);
        assertThat(testCardState.getCardStateFlagDetails()).isEqualTo(DEFAULT_CARD_STATE_FLAG_DETAILS);
        assertThat(testCardState.getCardStateFlagDescription()).isEqualTo(DEFAULT_CARD_STATE_FLAG_DESCRIPTION);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(1)).save(testCardState);
    }

    @Test
    @Transactional
    void createCardStateWithExistingId() throws Exception {
        // Create the CardState with an existing ID
        cardState.setId(1L);
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        int databaseSizeBeforeCreate = cardStateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void checkCardStateFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardStateRepository.findAll().size();
        // set the field null
        cardState.setCardStateFlag(null);

        // Create the CardState, which fails.
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        restCardStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStateDTO)))
            .andExpect(status().isBadRequest());

        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardStateFlagDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardStateRepository.findAll().size();
        // set the field null
        cardState.setCardStateFlagDetails(null);

        // Create the CardState, which fails.
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        restCardStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStateDTO)))
            .andExpect(status().isBadRequest());

        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardStates() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardState.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStateFlag").value(hasItem(DEFAULT_CARD_STATE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStateFlagDetails").value(hasItem(DEFAULT_CARD_STATE_FLAG_DETAILS)))
            .andExpect(jsonPath("$.[*].cardStateFlagDescription").value(hasItem(DEFAULT_CARD_STATE_FLAG_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCardState() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get the cardState
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL_ID, cardState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardState.getId().intValue()))
            .andExpect(jsonPath("$.cardStateFlag").value(DEFAULT_CARD_STATE_FLAG.toString()))
            .andExpect(jsonPath("$.cardStateFlagDetails").value(DEFAULT_CARD_STATE_FLAG_DETAILS))
            .andExpect(jsonPath("$.cardStateFlagDescription").value(DEFAULT_CARD_STATE_FLAG_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCardStatesByIdFiltering() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        Long id = cardState.getId();

        defaultCardStateShouldBeFound("id.equals=" + id);
        defaultCardStateShouldNotBeFound("id.notEquals=" + id);

        defaultCardStateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardStateShouldNotBeFound("id.greaterThan=" + id);

        defaultCardStateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardStateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlag equals to DEFAULT_CARD_STATE_FLAG
        defaultCardStateShouldBeFound("cardStateFlag.equals=" + DEFAULT_CARD_STATE_FLAG);

        // Get all the cardStateList where cardStateFlag equals to UPDATED_CARD_STATE_FLAG
        defaultCardStateShouldNotBeFound("cardStateFlag.equals=" + UPDATED_CARD_STATE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlag not equals to DEFAULT_CARD_STATE_FLAG
        defaultCardStateShouldNotBeFound("cardStateFlag.notEquals=" + DEFAULT_CARD_STATE_FLAG);

        // Get all the cardStateList where cardStateFlag not equals to UPDATED_CARD_STATE_FLAG
        defaultCardStateShouldBeFound("cardStateFlag.notEquals=" + UPDATED_CARD_STATE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagIsInShouldWork() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlag in DEFAULT_CARD_STATE_FLAG or UPDATED_CARD_STATE_FLAG
        defaultCardStateShouldBeFound("cardStateFlag.in=" + DEFAULT_CARD_STATE_FLAG + "," + UPDATED_CARD_STATE_FLAG);

        // Get all the cardStateList where cardStateFlag equals to UPDATED_CARD_STATE_FLAG
        defaultCardStateShouldNotBeFound("cardStateFlag.in=" + UPDATED_CARD_STATE_FLAG);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlag is not null
        defaultCardStateShouldBeFound("cardStateFlag.specified=true");

        // Get all the cardStateList where cardStateFlag is null
        defaultCardStateShouldNotBeFound("cardStateFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails equals to DEFAULT_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldBeFound("cardStateFlagDetails.equals=" + DEFAULT_CARD_STATE_FLAG_DETAILS);

        // Get all the cardStateList where cardStateFlagDetails equals to UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.equals=" + UPDATED_CARD_STATE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails not equals to DEFAULT_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.notEquals=" + DEFAULT_CARD_STATE_FLAG_DETAILS);

        // Get all the cardStateList where cardStateFlagDetails not equals to UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldBeFound("cardStateFlagDetails.notEquals=" + UPDATED_CARD_STATE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails in DEFAULT_CARD_STATE_FLAG_DETAILS or UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldBeFound("cardStateFlagDetails.in=" + DEFAULT_CARD_STATE_FLAG_DETAILS + "," + UPDATED_CARD_STATE_FLAG_DETAILS);

        // Get all the cardStateList where cardStateFlagDetails equals to UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.in=" + UPDATED_CARD_STATE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails is not null
        defaultCardStateShouldBeFound("cardStateFlagDetails.specified=true");

        // Get all the cardStateList where cardStateFlagDetails is null
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsContainsSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails contains DEFAULT_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldBeFound("cardStateFlagDetails.contains=" + DEFAULT_CARD_STATE_FLAG_DETAILS);

        // Get all the cardStateList where cardStateFlagDetails contains UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.contains=" + UPDATED_CARD_STATE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDetails does not contain DEFAULT_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldNotBeFound("cardStateFlagDetails.doesNotContain=" + DEFAULT_CARD_STATE_FLAG_DETAILS);

        // Get all the cardStateList where cardStateFlagDetails does not contain UPDATED_CARD_STATE_FLAG_DETAILS
        defaultCardStateShouldBeFound("cardStateFlagDetails.doesNotContain=" + UPDATED_CARD_STATE_FLAG_DETAILS);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription equals to DEFAULT_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldBeFound("cardStateFlagDescription.equals=" + DEFAULT_CARD_STATE_FLAG_DESCRIPTION);

        // Get all the cardStateList where cardStateFlagDescription equals to UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.equals=" + UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription not equals to DEFAULT_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.notEquals=" + DEFAULT_CARD_STATE_FLAG_DESCRIPTION);

        // Get all the cardStateList where cardStateFlagDescription not equals to UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldBeFound("cardStateFlagDescription.notEquals=" + UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription in DEFAULT_CARD_STATE_FLAG_DESCRIPTION or UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldBeFound(
            "cardStateFlagDescription.in=" + DEFAULT_CARD_STATE_FLAG_DESCRIPTION + "," + UPDATED_CARD_STATE_FLAG_DESCRIPTION
        );

        // Get all the cardStateList where cardStateFlagDescription equals to UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.in=" + UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription is not null
        defaultCardStateShouldBeFound("cardStateFlagDescription.specified=true");

        // Get all the cardStateList where cardStateFlagDescription is null
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription contains DEFAULT_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldBeFound("cardStateFlagDescription.contains=" + DEFAULT_CARD_STATE_FLAG_DESCRIPTION);

        // Get all the cardStateList where cardStateFlagDescription contains UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.contains=" + UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardStatesByCardStateFlagDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        // Get all the cardStateList where cardStateFlagDescription does not contain DEFAULT_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldNotBeFound("cardStateFlagDescription.doesNotContain=" + DEFAULT_CARD_STATE_FLAG_DESCRIPTION);

        // Get all the cardStateList where cardStateFlagDescription does not contain UPDATED_CARD_STATE_FLAG_DESCRIPTION
        defaultCardStateShouldBeFound("cardStateFlagDescription.doesNotContain=" + UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardStateShouldBeFound(String filter) throws Exception {
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardState.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStateFlag").value(hasItem(DEFAULT_CARD_STATE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStateFlagDetails").value(hasItem(DEFAULT_CARD_STATE_FLAG_DETAILS)))
            .andExpect(jsonPath("$.[*].cardStateFlagDescription").value(hasItem(DEFAULT_CARD_STATE_FLAG_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardStateShouldNotBeFound(String filter) throws Exception {
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardStateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardState() throws Exception {
        // Get the cardState
        restCardStateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardState() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();

        // Update the cardState
        CardState updatedCardState = cardStateRepository.findById(cardState.getId()).get();
        // Disconnect from session so that the updates on updatedCardState are not directly saved in db
        em.detach(updatedCardState);
        updatedCardState
            .cardStateFlag(UPDATED_CARD_STATE_FLAG)
            .cardStateFlagDetails(UPDATED_CARD_STATE_FLAG_DETAILS)
            .cardStateFlagDescription(UPDATED_CARD_STATE_FLAG_DESCRIPTION);
        CardStateDTO cardStateDTO = cardStateMapper.toDto(updatedCardState);

        restCardStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardStateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);
        CardState testCardState = cardStateList.get(cardStateList.size() - 1);
        assertThat(testCardState.getCardStateFlag()).isEqualTo(UPDATED_CARD_STATE_FLAG);
        assertThat(testCardState.getCardStateFlagDetails()).isEqualTo(UPDATED_CARD_STATE_FLAG_DETAILS);
        assertThat(testCardState.getCardStateFlagDescription()).isEqualTo(UPDATED_CARD_STATE_FLAG_DESCRIPTION);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository).save(testCardState);
    }

    @Test
    @Transactional
    void putNonExistingCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardStateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardStateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void partialUpdateCardStateWithPatch() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();

        // Update the cardState using partial update
        CardState partialUpdatedCardState = new CardState();
        partialUpdatedCardState.setId(cardState.getId());

        partialUpdatedCardState.cardStateFlag(UPDATED_CARD_STATE_FLAG).cardStateFlagDetails(UPDATED_CARD_STATE_FLAG_DETAILS);

        restCardStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardState))
            )
            .andExpect(status().isOk());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);
        CardState testCardState = cardStateList.get(cardStateList.size() - 1);
        assertThat(testCardState.getCardStateFlag()).isEqualTo(UPDATED_CARD_STATE_FLAG);
        assertThat(testCardState.getCardStateFlagDetails()).isEqualTo(UPDATED_CARD_STATE_FLAG_DETAILS);
        assertThat(testCardState.getCardStateFlagDescription()).isEqualTo(DEFAULT_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCardStateWithPatch() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();

        // Update the cardState using partial update
        CardState partialUpdatedCardState = new CardState();
        partialUpdatedCardState.setId(cardState.getId());

        partialUpdatedCardState
            .cardStateFlag(UPDATED_CARD_STATE_FLAG)
            .cardStateFlagDetails(UPDATED_CARD_STATE_FLAG_DETAILS)
            .cardStateFlagDescription(UPDATED_CARD_STATE_FLAG_DESCRIPTION);

        restCardStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardState))
            )
            .andExpect(status().isOk());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);
        CardState testCardState = cardStateList.get(cardStateList.size() - 1);
        assertThat(testCardState.getCardStateFlag()).isEqualTo(UPDATED_CARD_STATE_FLAG);
        assertThat(testCardState.getCardStateFlagDetails()).isEqualTo(UPDATED_CARD_STATE_FLAG_DETAILS);
        assertThat(testCardState.getCardStateFlagDescription()).isEqualTo(UPDATED_CARD_STATE_FLAG_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardStateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardState() throws Exception {
        int databaseSizeBeforeUpdate = cardStateRepository.findAll().size();
        cardState.setId(count.incrementAndGet());

        // Create the CardState
        CardStateDTO cardStateDTO = cardStateMapper.toDto(cardState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardStateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardStateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardState in the database
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(0)).save(cardState);
    }

    @Test
    @Transactional
    void deleteCardState() throws Exception {
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);

        int databaseSizeBeforeDelete = cardStateRepository.findAll().size();

        // Delete the cardState
        restCardStateMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardState.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardState> cardStateList = cardStateRepository.findAll();
        assertThat(cardStateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardState in Elasticsearch
        verify(mockCardStateSearchRepository, times(1)).deleteById(cardState.getId());
    }

    @Test
    @Transactional
    void searchCardState() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardStateRepository.saveAndFlush(cardState);
        when(mockCardStateSearchRepository.search("id:" + cardState.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardState), PageRequest.of(0, 1), 1));

        // Search the cardState
        restCardStateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardState.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardStateFlag").value(hasItem(DEFAULT_CARD_STATE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardStateFlagDetails").value(hasItem(DEFAULT_CARD_STATE_FLAG_DETAILS)))
            .andExpect(jsonPath("$.[*].cardStateFlagDescription").value(hasItem(DEFAULT_CARD_STATE_FLAG_DESCRIPTION)));
    }
}
