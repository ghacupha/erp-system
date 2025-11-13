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

describe('TransactionDetails e2e test', () => {
  const transactionDetailsPageUrl = '/transaction-details';
  const transactionDetailsPageUrlPattern = new RegExp('/transaction-details(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionDetailsSample = { entryId: 79450, transactionDate: '2024-10-14', amount: 60287, createdAt: '2024-10-14T04:54:49.986Z' };

  let transactionDetails: any;
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
      url: '/api/transaction-accounts',
      body: {"accountNumber":"Operative redundant","accountName":"Credit Card Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"EQUITY","accountSubType":"LONG_LIVED_ASSET","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-details+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-details').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-details/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (transactionDetails) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-details/${transactionDetails.id}`,
      }).then(() => {
        transactionDetails = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
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

  it('TransactionDetails menu should load TransactionDetails page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-details');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionDetails').should('exist');
    cy.url().should('match', transactionDetailsPageUrlPattern);
  });

  describe('TransactionDetails page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionDetailsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionDetails page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/transaction-details/new$'));
        cy.getEntityCreateUpdateHeading('TransactionDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionDetailsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-details',
  
          body: {
            ...transactionDetailsSample,
            debitAccount: transactionAccount,
            creditAccount: transactionAccount,
          },
        }).then(({ body }) => {
          transactionDetails = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-details+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionDetails],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionDetailsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(transactionDetailsPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TransactionDetails page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionDetails');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionDetailsPageUrlPattern);
      });

      it('edit button click should load edit TransactionDetails page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionDetailsPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TransactionDetails', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionDetailsPageUrlPattern);

        transactionDetails = undefined;
      });
    });
  });

  describe('new TransactionDetails page', () => {
    beforeEach(() => {
      cy.visit(`${transactionDetailsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TransactionDetails');
    });

    it.skip('should create an instance of TransactionDetails', () => {
      cy.get(`[data-cy="entryId"]`).type('40750').should('have.value', '40750');

      cy.get(`[data-cy="transactionDate"]`).type('2024-10-14').should('have.value', '2024-10-14');

      cy.get(`[data-cy="description"]`).type('District Granite').should('have.value', 'District Granite');

      cy.get(`[data-cy="amount"]`).type('18886').should('have.value', '18886');

      cy.get(`[data-cy="createdAt"]`).type('2024-10-13T16:22').should('have.value', '2024-10-13T16:22');

      cy.get(`[data-cy="modifiedAt"]`).type('2024-10-13T23:47').should('have.value', '2024-10-13T23:47');

      cy.get(`[data-cy="transactionType"]`).type('Orchard').should('have.value', 'Orchard');

      cy.get(`[data-cy="debitAccount"]`).select(1);
      cy.get(`[data-cy="creditAccount"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transactionDetails = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionDetailsPageUrlPattern);
    });
  });
});
