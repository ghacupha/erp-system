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

describe('TARecognitionROURule e2e test', () => {
  const tARecognitionROURulePageUrl = '/ta-recognition-rou-rule';
  const tARecognitionROURulePageUrlPattern = new RegExp('/ta-recognition-rou-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tARecognitionROURuleSample = { name: 'Trace Overpass', identifier: 'f9ae0455-e45b-4d51-a83a-eea9006cd48e' };

  let tARecognitionROURule: any;
  //let iFRS16LeaseContract: any;
  //let transactionAccount: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"Account encompassing maximize","leaseTitle":"quantifying","shortTitle":"recontextualize","description":"Table","inceptionDate":"2024-03-07","commencementDate":"2024-03-06","serialNumber":"78f5ce17-e4f2-41a5-97fb-e93182cf4c3e"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"copy","accountName":"Checking Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"ASSET","accountSubType":"LONG_LIVED_LIABILITY","dummyAccount":false},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-recognition-rou-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-recognition-rou-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-recognition-rou-rules/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (tARecognitionROURule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-recognition-rou-rules/${tARecognitionROURule.id}`,
      }).then(() => {
        tARecognitionROURule = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
  });
   */

  it('TARecognitionROURules menu should load TARecognitionROURules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-recognition-rou-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TARecognitionROURule').should('exist');
    cy.url().should('match', tARecognitionROURulePageUrlPattern);
  });

  describe('TARecognitionROURule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tARecognitionROURulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TARecognitionROURule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-recognition-rou-rule/new$'));
        cy.getEntityCreateUpdateHeading('TARecognitionROURule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tARecognitionROURulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-recognition-rou-rules',
  
          body: {
            ...tARecognitionROURuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tARecognitionROURule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-recognition-rou-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tARecognitionROURule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tARecognitionROURulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tARecognitionROURulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TARecognitionROURule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tARecognitionROURule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tARecognitionROURulePageUrlPattern);
      });

      it('edit button click should load edit TARecognitionROURule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TARecognitionROURule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tARecognitionROURulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TARecognitionROURule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tARecognitionROURule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tARecognitionROURulePageUrlPattern);

        tARecognitionROURule = undefined;
      });
    });
  });

  describe('new TARecognitionROURule page', () => {
    beforeEach(() => {
      cy.visit(`${tARecognitionROURulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TARecognitionROURule');
    });

    it.skip('should create an instance of TARecognitionROURule', () => {
      cy.get(`[data-cy="name"]`).type('markets Islands').should('have.value', 'markets Islands');

      cy.get(`[data-cy="identifier"]`)
        .type('2f14e444-2c60-465c-9b9e-f5ce21550d05')
        .invoke('val')
        .should('match', new RegExp('2f14e444-2c60-465c-9b9e-f5ce21550d05'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tARecognitionROURule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tARecognitionROURulePageUrlPattern);
    });
  });
});
