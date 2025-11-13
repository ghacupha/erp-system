///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('CardAcquiringTransaction e2e test', () => {
  const cardAcquiringTransactionPageUrl = '/card-acquiring-transaction';
  const cardAcquiringTransactionPageUrlPattern = new RegExp('/card-acquiring-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardAcquiringTransactionSample = {
    reportingDate: '2023-10-03',
    terminalId: 'Home Paradigm',
    numberOfTransactions: 87735,
    valueOfTransactionsInLCY: 42057,
  };

  let cardAcquiringTransaction: any;
  let institutionCode: any;
  let channelType: any;
  let cardBrandType: any;
  let isoCurrencyCode: any;
  let cardCategoryType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'Handmade',
        institutionName: 'logistical',
        shortName: 'Small schemas Afghani',
        category: 'Sausages next-generation Face',
        institutionCategory: 'bluetooth Pizza South',
        institutionOwnership: 'Poland Implementation state',
        dateLicensed: '2022-04-05',
        institutionStatus: 'neural moderator',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/channel-types',
      body: {
        channelsTypeCode: 'Account',
        channelTypes: 'AI SAS Technician',
        channelTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      channelType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-brand-types',
      body: {
        cardBrandTypeCode: 'pink',
        cardBrandType: 'Nevada base open',
        cardBrandTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardBrandType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/iso-currency-codes',
      body: {
        alphabeticCode: 'Rubber Loan knowledge',
        numericCode: 'Forward Bike',
        minorUnit: 'circuit Rubber Card',
        currency: 'lavender Architect',
        country: 'Venezuela',
      },
    }).then(({ body }) => {
      isoCurrencyCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-category-types',
      body: { cardCategoryFlag: 'I', cardCategoryDescription: 'Health', cardCategoryDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' },
    }).then(({ body }) => {
      cardCategoryType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-acquiring-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-acquiring-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-acquiring-transactions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/channel-types', {
      statusCode: 200,
      body: [channelType],
    });

    cy.intercept('GET', '/api/card-brand-types', {
      statusCode: 200,
      body: [cardBrandType],
    });

    cy.intercept('GET', '/api/iso-currency-codes', {
      statusCode: 200,
      body: [isoCurrencyCode],
    });

    cy.intercept('GET', '/api/card-category-types', {
      statusCode: 200,
      body: [cardCategoryType],
    });
  });

  afterEach(() => {
    if (cardAcquiringTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-acquiring-transactions/${cardAcquiringTransaction.id}`,
      }).then(() => {
        cardAcquiringTransaction = undefined;
      });
    }
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
    if (channelType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/channel-types/${channelType.id}`,
      }).then(() => {
        channelType = undefined;
      });
    }
    if (cardBrandType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-brand-types/${cardBrandType.id}`,
      }).then(() => {
        cardBrandType = undefined;
      });
    }
    if (isoCurrencyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-currency-codes/${isoCurrencyCode.id}`,
      }).then(() => {
        isoCurrencyCode = undefined;
      });
    }
    if (cardCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-category-types/${cardCategoryType.id}`,
      }).then(() => {
        cardCategoryType = undefined;
      });
    }
  });

  it('CardAcquiringTransactions menu should load CardAcquiringTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-acquiring-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardAcquiringTransaction').should('exist');
    cy.url().should('match', cardAcquiringTransactionPageUrlPattern);
  });

  describe('CardAcquiringTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardAcquiringTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardAcquiringTransaction page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-acquiring-transaction/new$'));
        cy.getEntityCreateUpdateHeading('CardAcquiringTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardAcquiringTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-acquiring-transactions',

          body: {
            ...cardAcquiringTransactionSample,
            bankCode: institutionCode,
            channelType: channelType,
            cardBrandType: cardBrandType,
            currencyOfTransaction: isoCurrencyCode,
            cardIssuerCategory: cardCategoryType,
          },
        }).then(({ body }) => {
          cardAcquiringTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-acquiring-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardAcquiringTransaction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardAcquiringTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardAcquiringTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardAcquiringTransaction');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardAcquiringTransactionPageUrlPattern);
      });

      it('edit button click should load edit CardAcquiringTransaction page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardAcquiringTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardAcquiringTransactionPageUrlPattern);
      });

      it('last delete button click should delete instance of CardAcquiringTransaction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardAcquiringTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardAcquiringTransactionPageUrlPattern);

        cardAcquiringTransaction = undefined;
      });
    });
  });

  describe('new CardAcquiringTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${cardAcquiringTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardAcquiringTransaction');
    });

    it('should create an instance of CardAcquiringTransaction', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="terminalId"]`).type('e-tailers').should('have.value', 'e-tailers');

      cy.get(`[data-cy="numberOfTransactions"]`).type('64412').should('have.value', '64412');

      cy.get(`[data-cy="valueOfTransactionsInLCY"]`).type('39666').should('have.value', '39666');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="channelType"]`).select(1);
      cy.get(`[data-cy="cardBrandType"]`).select(1);
      cy.get(`[data-cy="currencyOfTransaction"]`).select(1);
      cy.get(`[data-cy="cardIssuerCategory"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardAcquiringTransaction = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardAcquiringTransactionPageUrlPattern);
    });
  });
});
