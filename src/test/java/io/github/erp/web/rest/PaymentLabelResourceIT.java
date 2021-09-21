package io.github.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.Payment;
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SignedPayment;
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import io.github.erp.service.PaymentLabelService;
import io.github.erp.service.criteria.PaymentLabelCriteria;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.mapper.PaymentLabelMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link PaymentLabelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentLabelResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/payment-labels";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentLabelRepository paymentLabelRepository;

    @Mock
    private PaymentLabelRepository paymentLabelRepositoryMock;

    @Autowired
    private PaymentLabelMapper paymentLabelMapper;

    @Mock
    private PaymentLabelService paymentLabelServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentLabelSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentLabelSearchRepository mockPaymentLabelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentLabelMockMvc;

    private PaymentLabel paymentLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentLabel createEntity(EntityManager em) {
        PaymentLabel paymentLabel = new PaymentLabel().description(DEFAULT_DESCRIPTION).comments(DEFAULT_COMMENTS);
        return paymentLabel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentLabel createUpdatedEntity(EntityManager em) {
        PaymentLabel paymentLabel = new PaymentLabel().description(UPDATED_DESCRIPTION).comments(UPDATED_COMMENTS);
        return paymentLabel;
    }

    @BeforeEach
    public void initTest() {
        paymentLabel = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentLabel() throws Exception {
        int databaseSizeBeforeCreate = paymentLabelRepository.findAll().size();
        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);
        restPaymentLabelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentLabel testPaymentLabel = paymentLabelList.get(paymentLabelList.size() - 1);
        assertThat(testPaymentLabel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentLabel.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(1)).save(testPaymentLabel);
    }

    @Test
    @Transactional
    void createPaymentLabelWithExistingId() throws Exception {
        // Create the PaymentLabel with an existing ID
        paymentLabel.setId(1L);
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        int databaseSizeBeforeCreate = paymentLabelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentLabelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentLabelRepository.findAll().size();
        // set the field null
        paymentLabel.setDescription(null);

        // Create the PaymentLabel, which fails.
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        restPaymentLabelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentLabels() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentLabelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentLabelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentLabelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentLabelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentLabelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentLabelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentLabelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentLabelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPaymentLabel() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get the paymentLabel
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentLabel.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getPaymentLabelsByIdFiltering() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        Long id = paymentLabel.getId();

        defaultPaymentLabelShouldBeFound("id.equals=" + id);
        defaultPaymentLabelShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentLabelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentLabelShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentLabelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentLabelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description equals to DEFAULT_DESCRIPTION
        defaultPaymentLabelShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentLabelList where description equals to UPDATED_DESCRIPTION
        defaultPaymentLabelShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description not equals to DEFAULT_DESCRIPTION
        defaultPaymentLabelShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentLabelList where description not equals to UPDATED_DESCRIPTION
        defaultPaymentLabelShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPaymentLabelShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the paymentLabelList where description equals to UPDATED_DESCRIPTION
        defaultPaymentLabelShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description is not null
        defaultPaymentLabelShouldBeFound("description.specified=true");

        // Get all the paymentLabelList where description is null
        defaultPaymentLabelShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description contains DEFAULT_DESCRIPTION
        defaultPaymentLabelShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the paymentLabelList where description contains UPDATED_DESCRIPTION
        defaultPaymentLabelShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where description does not contain DEFAULT_DESCRIPTION
        defaultPaymentLabelShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the paymentLabelList where description does not contain UPDATED_DESCRIPTION
        defaultPaymentLabelShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments equals to DEFAULT_COMMENTS
        defaultPaymentLabelShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the paymentLabelList where comments equals to UPDATED_COMMENTS
        defaultPaymentLabelShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments not equals to DEFAULT_COMMENTS
        defaultPaymentLabelShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the paymentLabelList where comments not equals to UPDATED_COMMENTS
        defaultPaymentLabelShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultPaymentLabelShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the paymentLabelList where comments equals to UPDATED_COMMENTS
        defaultPaymentLabelShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments is not null
        defaultPaymentLabelShouldBeFound("comments.specified=true");

        // Get all the paymentLabelList where comments is null
        defaultPaymentLabelShouldNotBeFound("comments.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments contains DEFAULT_COMMENTS
        defaultPaymentLabelShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the paymentLabelList where comments contains UPDATED_COMMENTS
        defaultPaymentLabelShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        // Get all the paymentLabelList where comments does not contain DEFAULT_COMMENTS
        defaultPaymentLabelShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the paymentLabelList where comments does not contain UPDATED_COMMENTS
        defaultPaymentLabelShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByContainingPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        PaymentLabel containingPaymentLabel;
        if (TestUtil.findAll(em, PaymentLabel.class).isEmpty()) {
            containingPaymentLabel = PaymentLabelResourceIT.createEntity(em);
            em.persist(containingPaymentLabel);
            em.flush();
        } else {
            containingPaymentLabel = TestUtil.findAll(em, PaymentLabel.class).get(0);
        }
        em.persist(containingPaymentLabel);
        em.flush();
        paymentLabel.setContainingPaymentLabel(containingPaymentLabel);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long containingPaymentLabelId = containingPaymentLabel.getId();

        // Get all the paymentLabelList where containingPaymentLabel equals to containingPaymentLabelId
        defaultPaymentLabelShouldBeFound("containingPaymentLabelId.equals=" + containingPaymentLabelId);

        // Get all the paymentLabelList where containingPaymentLabel equals to (containingPaymentLabelId + 1)
        defaultPaymentLabelShouldNotBeFound("containingPaymentLabelId.equals=" + (containingPaymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
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
        paymentLabel.addPlaceholder(placeholder);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long placeholderId = placeholder.getId();

        // Get all the paymentLabelList where placeholder equals to placeholderId
        defaultPaymentLabelShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the paymentLabelList where placeholder equals to (placeholderId + 1)
        defaultPaymentLabelShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByPaymentCalculationIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        PaymentCalculation paymentCalculation;
        if (TestUtil.findAll(em, PaymentCalculation.class).isEmpty()) {
            paymentCalculation = PaymentCalculationResourceIT.createEntity(em);
            em.persist(paymentCalculation);
            em.flush();
        } else {
            paymentCalculation = TestUtil.findAll(em, PaymentCalculation.class).get(0);
        }
        em.persist(paymentCalculation);
        em.flush();
        paymentLabel.addPaymentCalculation(paymentCalculation);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long paymentCalculationId = paymentCalculation.getId();

        // Get all the paymentLabelList where paymentCalculation equals to paymentCalculationId
        defaultPaymentLabelShouldBeFound("paymentCalculationId.equals=" + paymentCalculationId);

        // Get all the paymentLabelList where paymentCalculation equals to (paymentCalculationId + 1)
        defaultPaymentLabelShouldNotBeFound("paymentCalculationId.equals=" + (paymentCalculationId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        PaymentCategory paymentCategory;
        if (TestUtil.findAll(em, PaymentCategory.class).isEmpty()) {
            paymentCategory = PaymentCategoryResourceIT.createEntity(em);
            em.persist(paymentCategory);
            em.flush();
        } else {
            paymentCategory = TestUtil.findAll(em, PaymentCategory.class).get(0);
        }
        em.persist(paymentCategory);
        em.flush();
        paymentLabel.addPaymentCategory(paymentCategory);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long paymentCategoryId = paymentCategory.getId();

        // Get all the paymentLabelList where paymentCategory equals to paymentCategoryId
        defaultPaymentLabelShouldBeFound("paymentCategoryId.equals=" + paymentCategoryId);

        // Get all the paymentLabelList where paymentCategory equals to (paymentCategoryId + 1)
        defaultPaymentLabelShouldNotBeFound("paymentCategoryId.equals=" + (paymentCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByPaymentRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        PaymentRequisition paymentRequisition;
        if (TestUtil.findAll(em, PaymentRequisition.class).isEmpty()) {
            paymentRequisition = PaymentRequisitionResourceIT.createEntity(em);
            em.persist(paymentRequisition);
            em.flush();
        } else {
            paymentRequisition = TestUtil.findAll(em, PaymentRequisition.class).get(0);
        }
        em.persist(paymentRequisition);
        em.flush();
        paymentLabel.addPaymentRequisition(paymentRequisition);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long paymentRequisitionId = paymentRequisition.getId();

        // Get all the paymentLabelList where paymentRequisition equals to paymentRequisitionId
        defaultPaymentLabelShouldBeFound("paymentRequisitionId.equals=" + paymentRequisitionId);

        // Get all the paymentLabelList where paymentRequisition equals to (paymentRequisitionId + 1)
        defaultPaymentLabelShouldNotBeFound("paymentRequisitionId.equals=" + (paymentRequisitionId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        paymentLabel.addPayment(payment);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long paymentId = payment.getId();

        // Get all the paymentLabelList where payment equals to paymentId
        defaultPaymentLabelShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the paymentLabelList where payment equals to (paymentId + 1)
        defaultPaymentLabelShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        em.persist(invoice);
        em.flush();
        paymentLabel.addInvoice(invoice);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long invoiceId = invoice.getId();

        // Get all the paymentLabelList where invoice equals to invoiceId
        defaultPaymentLabelShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the paymentLabelList where invoice equals to (invoiceId + 1)
        defaultPaymentLabelShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        paymentLabel.addDealer(dealer);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long dealerId = dealer.getId();

        // Get all the paymentLabelList where dealer equals to dealerId
        defaultPaymentLabelShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the paymentLabelList where dealer equals to (dealerId + 1)
        defaultPaymentLabelShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentLabelsBySignedPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        SignedPayment signedPayment;
        if (TestUtil.findAll(em, SignedPayment.class).isEmpty()) {
            signedPayment = SignedPaymentResourceIT.createEntity(em);
            em.persist(signedPayment);
            em.flush();
        } else {
            signedPayment = TestUtil.findAll(em, SignedPayment.class).get(0);
        }
        em.persist(signedPayment);
        em.flush();
        paymentLabel.addSignedPayment(signedPayment);
        paymentLabelRepository.saveAndFlush(paymentLabel);
        Long signedPaymentId = signedPayment.getId();

        // Get all the paymentLabelList where signedPayment equals to signedPaymentId
        defaultPaymentLabelShouldBeFound("signedPaymentId.equals=" + signedPaymentId);

        // Get all the paymentLabelList where signedPayment equals to (signedPaymentId + 1)
        defaultPaymentLabelShouldNotBeFound("signedPaymentId.equals=" + (signedPaymentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentLabelShouldBeFound(String filter) throws Exception {
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentLabelShouldNotBeFound(String filter) throws Exception {
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentLabelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentLabel() throws Exception {
        // Get the paymentLabel
        restPaymentLabelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentLabel() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();

        // Update the paymentLabel
        PaymentLabel updatedPaymentLabel = paymentLabelRepository.findById(paymentLabel.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentLabel are not directly saved in db
        em.detach(updatedPaymentLabel);
        updatedPaymentLabel.description(UPDATED_DESCRIPTION).comments(UPDATED_COMMENTS);
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(updatedPaymentLabel);

        restPaymentLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentLabelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);
        PaymentLabel testPaymentLabel = paymentLabelList.get(paymentLabelList.size() - 1);
        assertThat(testPaymentLabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentLabel.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository).save(testPaymentLabel);
    }

    @Test
    @Transactional
    void putNonExistingPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentLabelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void partialUpdatePaymentLabelWithPatch() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();

        // Update the paymentLabel using partial update
        PaymentLabel partialUpdatedPaymentLabel = new PaymentLabel();
        partialUpdatedPaymentLabel.setId(paymentLabel.getId());

        partialUpdatedPaymentLabel.description(UPDATED_DESCRIPTION);

        restPaymentLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentLabel))
            )
            .andExpect(status().isOk());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);
        PaymentLabel testPaymentLabel = paymentLabelList.get(paymentLabelList.size() - 1);
        assertThat(testPaymentLabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentLabel.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdatePaymentLabelWithPatch() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();

        // Update the paymentLabel using partial update
        PaymentLabel partialUpdatedPaymentLabel = new PaymentLabel();
        partialUpdatedPaymentLabel.setId(paymentLabel.getId());

        partialUpdatedPaymentLabel.description(UPDATED_DESCRIPTION).comments(UPDATED_COMMENTS);

        restPaymentLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentLabel))
            )
            .andExpect(status().isOk());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);
        PaymentLabel testPaymentLabel = paymentLabelList.get(paymentLabelList.size() - 1);
        assertThat(testPaymentLabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentLabel.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentLabelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentLabel() throws Exception {
        int databaseSizeBeforeUpdate = paymentLabelRepository.findAll().size();
        paymentLabel.setId(count.incrementAndGet());

        // Create the PaymentLabel
        PaymentLabelDTO paymentLabelDTO = paymentLabelMapper.toDto(paymentLabel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentLabelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentLabelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentLabel in the database
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(0)).save(paymentLabel);
    }

    @Test
    @Transactional
    void deletePaymentLabel() throws Exception {
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);

        int databaseSizeBeforeDelete = paymentLabelRepository.findAll().size();

        // Delete the paymentLabel
        restPaymentLabelMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentLabel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentLabel> paymentLabelList = paymentLabelRepository.findAll();
        assertThat(paymentLabelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentLabel in Elasticsearch
        verify(mockPaymentLabelSearchRepository, times(1)).deleteById(paymentLabel.getId());
    }

    @Test
    @Transactional
    void searchPaymentLabel() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentLabelRepository.saveAndFlush(paymentLabel);
        when(mockPaymentLabelSearchRepository.search("id:" + paymentLabel.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentLabel), PageRequest.of(0, 1), 1));

        // Search the paymentLabel
        restPaymentLabelMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + paymentLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
}
