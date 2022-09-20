package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.1-SNAPSHOT
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.DeliveryNoteRepository;
import io.github.erp.repository.search.DeliveryNoteSearchRepository;
import io.github.erp.service.DeliveryNoteService;
import io.github.erp.service.dto.DeliveryNoteDTO;
import io.github.erp.service.mapper.DeliveryNoteMapper;
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
 * Integration tests for the DeliveryNoteResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class DeliveryNoteResourceIT {

    private static final String DEFAULT_DELIVERY_NOTE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_NOTE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOCUMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOCUMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOCUMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/dev/delivery-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/delivery-notes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Mock
    private DeliveryNoteRepository deliveryNoteRepositoryMock;

    @Autowired
    private DeliveryNoteMapper deliveryNoteMapper;

    @Mock
    private DeliveryNoteService deliveryNoteServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DeliveryNoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeliveryNoteSearchRepository mockDeliveryNoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryNoteMockMvc;

    private DeliveryNote deliveryNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .deliveryNoteNumber(DEFAULT_DELIVERY_NOTE_NUMBER)
            .documentDate(DEFAULT_DOCUMENT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        deliveryNote.setReceivedBy(dealer);
        // Add required entity
        deliveryNote.setSupplier(dealer);
        return deliveryNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryNote createUpdatedEntity(EntityManager em) {
        DeliveryNote deliveryNote = new DeliveryNote()
            .deliveryNoteNumber(UPDATED_DELIVERY_NOTE_NUMBER)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .quantity(UPDATED_QUANTITY);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        deliveryNote.setReceivedBy(dealer);
        // Add required entity
        deliveryNote.setSupplier(dealer);
        return deliveryNote;
    }

    @BeforeEach
    public void initTest() {
        deliveryNote = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryNote() throws Exception {
        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();
        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getDeliveryNoteNumber()).isEqualTo(DEFAULT_DELIVERY_NOTE_NUMBER);
        assertThat(testDeliveryNote.getDocumentDate()).isEqualTo(DEFAULT_DOCUMENT_DATE);
        assertThat(testDeliveryNote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeliveryNote.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDeliveryNote.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(1)).save(testDeliveryNote);
    }

    @Test
    @Transactional
    void createDeliveryNoteWithExistingId() throws Exception {
        // Create the DeliveryNote with an existing ID
        deliveryNote.setId(1L);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        int databaseSizeBeforeCreate = deliveryNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void checkDeliveryNoteNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteRepository.findAll().size();
        // set the field null
        deliveryNote.setDeliveryNoteNumber(null);

        // Create the DeliveryNote, which fails.
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryNoteRepository.findAll().size();
        // set the field null
        deliveryNote.setDocumentDate(null);

        // Create the DeliveryNote, which fails.
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        restDeliveryNoteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryNotes() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryNoteNumber").value(hasItem(DEFAULT_DELIVERY_NOTE_NUMBER)))
            .andExpect(jsonPath("$.[*].documentDate").value(hasItem(DEFAULT_DOCUMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeliveryNotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(deliveryNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeliveryNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deliveryNoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeliveryNotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deliveryNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeliveryNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deliveryNoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get the deliveryNote
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryNote.getId().intValue()))
            .andExpect(jsonPath("$.deliveryNoteNumber").value(DEFAULT_DELIVERY_NOTE_NUMBER))
            .andExpect(jsonPath("$.documentDate").value(DEFAULT_DOCUMENT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getDeliveryNotesByIdFiltering() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        Long id = deliveryNote.getId();

        defaultDeliveryNoteShouldBeFound("id.equals=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.notEquals=" + id);

        defaultDeliveryNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultDeliveryNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeliveryNoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber equals to DEFAULT_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.equals=" + DEFAULT_DELIVERY_NOTE_NUMBER);

        // Get all the deliveryNoteList where deliveryNoteNumber equals to UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.equals=" + UPDATED_DELIVERY_NOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber not equals to DEFAULT_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.notEquals=" + DEFAULT_DELIVERY_NOTE_NUMBER);

        // Get all the deliveryNoteList where deliveryNoteNumber not equals to UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.notEquals=" + UPDATED_DELIVERY_NOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber in DEFAULT_DELIVERY_NOTE_NUMBER or UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.in=" + DEFAULT_DELIVERY_NOTE_NUMBER + "," + UPDATED_DELIVERY_NOTE_NUMBER);

        // Get all the deliveryNoteList where deliveryNoteNumber equals to UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.in=" + UPDATED_DELIVERY_NOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber is not null
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.specified=true");

        // Get all the deliveryNoteList where deliveryNoteNumber is null
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber contains DEFAULT_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.contains=" + DEFAULT_DELIVERY_NOTE_NUMBER);

        // Get all the deliveryNoteList where deliveryNoteNumber contains UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.contains=" + UPDATED_DELIVERY_NOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryNoteNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where deliveryNoteNumber does not contain DEFAULT_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldNotBeFound("deliveryNoteNumber.doesNotContain=" + DEFAULT_DELIVERY_NOTE_NUMBER);

        // Get all the deliveryNoteList where deliveryNoteNumber does not contain UPDATED_DELIVERY_NOTE_NUMBER
        defaultDeliveryNoteShouldBeFound("deliveryNoteNumber.doesNotContain=" + UPDATED_DELIVERY_NOTE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate equals to DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.equals=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate equals to UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.equals=" + UPDATED_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate not equals to DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.notEquals=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate not equals to UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.notEquals=" + UPDATED_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate in DEFAULT_DOCUMENT_DATE or UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.in=" + DEFAULT_DOCUMENT_DATE + "," + UPDATED_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate equals to UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.in=" + UPDATED_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate is not null
        defaultDeliveryNoteShouldBeFound("documentDate.specified=true");

        // Get all the deliveryNoteList where documentDate is null
        defaultDeliveryNoteShouldNotBeFound("documentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate is greater than or equal to DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.greaterThanOrEqual=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate is greater than or equal to UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.greaterThanOrEqual=" + UPDATED_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate is less than or equal to DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.lessThanOrEqual=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate is less than or equal to SMALLER_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.lessThanOrEqual=" + SMALLER_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate is less than DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.lessThan=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate is less than UPDATED_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.lessThan=" + UPDATED_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDocumentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where documentDate is greater than DEFAULT_DOCUMENT_DATE
        defaultDeliveryNoteShouldNotBeFound("documentDate.greaterThan=" + DEFAULT_DOCUMENT_DATE);

        // Get all the deliveryNoteList where documentDate is greater than SMALLER_DOCUMENT_DATE
        defaultDeliveryNoteShouldBeFound("documentDate.greaterThan=" + SMALLER_DOCUMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description equals to DEFAULT_DESCRIPTION
        defaultDeliveryNoteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the deliveryNoteList where description equals to UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description not equals to DEFAULT_DESCRIPTION
        defaultDeliveryNoteShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the deliveryNoteList where description not equals to UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the deliveryNoteList where description equals to UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description is not null
        defaultDeliveryNoteShouldBeFound("description.specified=true");

        // Get all the deliveryNoteList where description is null
        defaultDeliveryNoteShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description contains DEFAULT_DESCRIPTION
        defaultDeliveryNoteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the deliveryNoteList where description contains UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where description does not contain DEFAULT_DESCRIPTION
        defaultDeliveryNoteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the deliveryNoteList where description does not contain UPDATED_DESCRIPTION
        defaultDeliveryNoteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultDeliveryNoteShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deliveryNoteList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultDeliveryNoteShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deliveryNoteList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the deliveryNoteList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber is not null
        defaultDeliveryNoteShouldBeFound("serialNumber.specified=true");

        // Get all the deliveryNoteList where serialNumber is null
        defaultDeliveryNoteShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultDeliveryNoteShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deliveryNoteList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultDeliveryNoteShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deliveryNoteList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultDeliveryNoteShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity equals to DEFAULT_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity equals to UPDATED_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity not equals to DEFAULT_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity not equals to UPDATED_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the deliveryNoteList where quantity equals to UPDATED_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity is not null
        defaultDeliveryNoteShouldBeFound("quantity.specified=true");

        // Get all the deliveryNoteList where quantity is null
        defaultDeliveryNoteShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity is less than or equal to SMALLER_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity is less than DEFAULT_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity is less than UPDATED_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        // Get all the deliveryNoteList where quantity is greater than DEFAULT_QUANTITY
        defaultDeliveryNoteShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the deliveryNoteList where quantity is greater than SMALLER_QUANTITY
        defaultDeliveryNoteShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
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
        deliveryNote.addPlaceholder(placeholder);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long placeholderId = placeholder.getId();

        // Get all the deliveryNoteList where placeholder equals to placeholderId
        defaultDeliveryNoteShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the deliveryNoteList where placeholder equals to (placeholderId + 1)
        defaultDeliveryNoteShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByReceivedByIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Dealer receivedBy;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            receivedBy = DealerResourceIT.createEntity(em);
            em.persist(receivedBy);
            em.flush();
        } else {
            receivedBy = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(receivedBy);
        em.flush();
        deliveryNote.setReceivedBy(receivedBy);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long receivedById = receivedBy.getId();

        // Get all the deliveryNoteList where receivedBy equals to receivedById
        defaultDeliveryNoteShouldBeFound("receivedById.equals=" + receivedById);

        // Get all the deliveryNoteList where receivedBy equals to (receivedById + 1)
        defaultDeliveryNoteShouldNotBeFound("receivedById.equals=" + (receivedById + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByDeliveryStampsIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        BusinessStamp deliveryStamps;
        if (TestUtil.findAll(em, BusinessStamp.class).isEmpty()) {
            deliveryStamps = BusinessStampResourceIT.createEntity(em);
            em.persist(deliveryStamps);
            em.flush();
        } else {
            deliveryStamps = TestUtil.findAll(em, BusinessStamp.class).get(0);
        }
        em.persist(deliveryStamps);
        em.flush();
        deliveryNote.addDeliveryStamps(deliveryStamps);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long deliveryStampsId = deliveryStamps.getId();

        // Get all the deliveryNoteList where deliveryStamps equals to deliveryStampsId
        defaultDeliveryNoteShouldBeFound("deliveryStampsId.equals=" + deliveryStampsId);

        // Get all the deliveryNoteList where deliveryStamps equals to (deliveryStampsId + 1)
        defaultDeliveryNoteShouldNotBeFound("deliveryStampsId.equals=" + (deliveryStampsId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        deliveryNote.setPurchaseOrder(purchaseOrder);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the deliveryNoteList where purchaseOrder equals to purchaseOrderId
        defaultDeliveryNoteShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the deliveryNoteList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultDeliveryNoteShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Dealer supplier;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            supplier = DealerResourceIT.createEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        deliveryNote.setSupplier(supplier);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long supplierId = supplier.getId();

        // Get all the deliveryNoteList where supplier equals to supplierId
        defaultDeliveryNoteShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the deliveryNoteList where supplier equals to (supplierId + 1)
        defaultDeliveryNoteShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveryNotesBySignatoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Dealer signatories;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            signatories = DealerResourceIT.createEntity(em);
            em.persist(signatories);
            em.flush();
        } else {
            signatories = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(signatories);
        em.flush();
        deliveryNote.addSignatories(signatories);
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        Long signatoriesId = signatories.getId();

        // Get all the deliveryNoteList where signatories equals to signatoriesId
        defaultDeliveryNoteShouldBeFound("signatoriesId.equals=" + signatoriesId);

        // Get all the deliveryNoteList where signatories equals to (signatoriesId + 1)
        defaultDeliveryNoteShouldNotBeFound("signatoriesId.equals=" + (signatoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeliveryNoteShouldBeFound(String filter) throws Exception {
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryNoteNumber").value(hasItem(DEFAULT_DELIVERY_NOTE_NUMBER)))
            .andExpect(jsonPath("$.[*].documentDate").value(hasItem(DEFAULT_DOCUMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeliveryNoteShouldNotBeFound(String filter) throws Exception {
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryNote() throws Exception {
        // Get the deliveryNote
        restDeliveryNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote
        DeliveryNote updatedDeliveryNote = deliveryNoteRepository.findById(deliveryNote.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryNote are not directly saved in db
        em.detach(updatedDeliveryNote);
        updatedDeliveryNote
            .deliveryNoteNumber(UPDATED_DELIVERY_NOTE_NUMBER)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .quantity(UPDATED_QUANTITY);
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(updatedDeliveryNote);

        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getDeliveryNoteNumber()).isEqualTo(UPDATED_DELIVERY_NOTE_NUMBER);
        assertThat(testDeliveryNote.getDocumentDate()).isEqualTo(UPDATED_DOCUMENT_DATE);
        assertThat(testDeliveryNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeliveryNote.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDeliveryNote.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository).save(testDeliveryNote);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote.documentDate(UPDATED_DOCUMENT_DATE).serialNumber(UPDATED_SERIAL_NUMBER);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getDeliveryNoteNumber()).isEqualTo(DEFAULT_DELIVERY_NOTE_NUMBER);
        assertThat(testDeliveryNote.getDocumentDate()).isEqualTo(UPDATED_DOCUMENT_DATE);
        assertThat(testDeliveryNote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeliveryNote.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDeliveryNote.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryNoteWithPatch() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();

        // Update the deliveryNote using partial update
        DeliveryNote partialUpdatedDeliveryNote = new DeliveryNote();
        partialUpdatedDeliveryNote.setId(deliveryNote.getId());

        partialUpdatedDeliveryNote
            .deliveryNoteNumber(UPDATED_DELIVERY_NOTE_NUMBER)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .quantity(UPDATED_QUANTITY);

        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryNote))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);
        DeliveryNote testDeliveryNote = deliveryNoteList.get(deliveryNoteList.size() - 1);
        assertThat(testDeliveryNote.getDeliveryNoteNumber()).isEqualTo(UPDATED_DELIVERY_NOTE_NUMBER);
        assertThat(testDeliveryNote.getDocumentDate()).isEqualTo(UPDATED_DOCUMENT_DATE);
        assertThat(testDeliveryNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeliveryNote.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDeliveryNote.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryNoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryNote() throws Exception {
        int databaseSizeBeforeUpdate = deliveryNoteRepository.findAll().size();
        deliveryNote.setId(count.incrementAndGet());

        // Create the DeliveryNote
        DeliveryNoteDTO deliveryNoteDTO = deliveryNoteMapper.toDto(deliveryNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryNoteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryNote in the database
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(0)).save(deliveryNote);
    }

    @Test
    @Transactional
    void deleteDeliveryNote() throws Exception {
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);

        int databaseSizeBeforeDelete = deliveryNoteRepository.findAll().size();

        // Delete the deliveryNote
        restDeliveryNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryNote> deliveryNoteList = deliveryNoteRepository.findAll();
        assertThat(deliveryNoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeliveryNote in Elasticsearch
        verify(mockDeliveryNoteSearchRepository, times(1)).deleteById(deliveryNote.getId());
    }

    @Test
    @Transactional
    void searchDeliveryNote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        deliveryNoteRepository.saveAndFlush(deliveryNote);
        when(mockDeliveryNoteSearchRepository.search("id:" + deliveryNote.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deliveryNote), PageRequest.of(0, 1), 1));

        // Search the deliveryNote
        restDeliveryNoteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + deliveryNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryNoteNumber").value(hasItem(DEFAULT_DELIVERY_NOTE_NUMBER)))
            .andExpect(jsonPath("$.[*].documentDate").value(hasItem(DEFAULT_DOCUMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
}
