///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

describe('Settlement e2e test', () => {
  const settlementPageUrl = '/settlement';
  const settlementPageUrlPattern = new RegExp('/settlement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const settlementSample = {};

  let settlement: any;
  let settlementCurrency: any;
  let paymentCategory: any;
  let dealer: any;

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
      url: '/api/settlement-currencies',
      body: {
        iso4217CurrencyCode: 'Nor',
        currencyName: 'Cedi',
        country: 'Slovakia (Slovak Republic)',
        numericCode: 'iterate hack Zambia',
        minorUnit: 'AI Terrace Architect',
        fileUploadToken: 'COM pink',
        compilationToken: 'process background',
      },
    }).then(({ body }) => {
      settlementCurrency = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/payment-categories',
      body: {
        categoryName: 'hard Fresh',
        categoryDescription: 'Industrial Maldives Consultant',
        categoryType: 'CATEGORY7',
        fileUploadToken: 'supply-chains',
        compilationToken: 'Green Health payment',
      },
    }).then(({ body }) => {
      paymentCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {
        dealerName: 'supply-chains Tasty',
        taxNumber: 'Account Officer',
        identificationDocumentNumber: 'matrix',
        organizationName: 'open-source Dynamic azure',
        department: 'quantifying',
        position: 'cross-media invoice',
        postalAddress: 'Lev Orchestrator Bedfordshire',
        physicalAddress: 'open-source Unbranded functionalities',
        accountName: 'Credit Card Account',
        accountNumber: 'expedite Parks overriding',
        bankersName: 'Central e-markets International',
        bankersBranch: 'maximize Account program',
        bankersSwiftCode: 'Rubber encompassing',
        fileUploadToken: 'Future',
        compilationToken: 'Nevada Account',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'project Industrial Agent',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/settlements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/settlements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/settlements/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [settlementCurrency],
    });

    cy.intercept('GET', '/api/payment-labels', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-categories', {
      statusCode: 200,
      body: [paymentCategory],
    });

    cy.intercept('GET', '/api/settlements', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/payment-invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (settlement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlements/${settlement.id}`,
      }).then(() => {
        settlement = undefined;
      });
    }
  });

  afterEach(() => {
    if (settlementCurrency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-currencies/${settlementCurrency.id}`,
      }).then(() => {
        settlementCurrency = undefined;
      });
    }
    if (paymentCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-categories/${paymentCategory.id}`,
      }).then(() => {
        paymentCategory = undefined;
      });
    }
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('Settlements menu should load Settlements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('settlement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Settlement').should('exist');
    cy.url().should('match', settlementPageUrlPattern);
  });

  describe('Settlement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(settlementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Settlement page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/settlement/new$'));
        cy.getEntityCreateUpdateHeading('Settlement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/settlements',

          body: {
            ...settlementSample,
            settlementCurrency: settlementCurrency,
            paymentCategory: paymentCategory,
            biller: dealer,
          },
        }).then(({ body }) => {
          settlement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/settlements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [settlement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(settlementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Settlement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('settlement');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementPageUrlPattern);
      });

      it('edit button click should load edit Settlement page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Settlement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementPageUrlPattern);
      });

      it('last delete button click should delete instance of Settlement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('settlement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementPageUrlPattern);

        settlement = undefined;
      });
    });
  });

  describe('new Settlement page', () => {
    beforeEach(() => {
      cy.visit(`${settlementPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Settlement');
    });

    it('should create an instance of Settlement', () => {
      cy.get(`[data-cy="paymentNumber"]`).type('Market array withdrawal').should('have.value', 'Market array withdrawal');

      cy.get(`[data-cy="paymentDate"]`).type('2022-02-02').should('have.value', '2022-02-02');

      cy.get(`[data-cy="paymentAmount"]`).type('58446').should('have.value', '58446');

      cy.get(`[data-cy="description"]`).type('Cambridgeshire stable').should('have.value', 'Cambridgeshire stable');

      cy.get(`[data-cy="notes"]`).type('Re-engineered 1080p').should('have.value', 'Re-engineered 1080p');

      cy.setFieldImageAsBytesOfEntity('calculationFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileUploadToken"]`).type('back').should('have.value', 'back');

      cy.get(`[data-cy="compilationToken"]`).type('Dynamic Reverse-engineered').should('have.value', 'Dynamic Reverse-engineered');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="settlementCurrency"]`).select(1);
      cy.get(`[data-cy="paymentCategory"]`).select(1);
      cy.get(`[data-cy="biller"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        settlement = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', settlementPageUrlPattern);
    });
  });
});
