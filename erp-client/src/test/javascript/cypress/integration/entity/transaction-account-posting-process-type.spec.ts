///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

describe('TransactionAccountPostingProcessType e2e test', () => {
  const transactionAccountPostingProcessTypePageUrl = '/transaction-account-posting-process-type';
  const transactionAccountPostingProcessTypePageUrlPattern = new RegExp('/transaction-account-posting-process-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionAccountPostingProcessTypeSample = { name: 'Shoes' };

  let transactionAccountPostingProcessType: any;
  //let transactionAccountCategory: any;

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
      url: '/api/transaction-account-categories',
      body: {"name":"deposit invoice","transactionAccountPostingType":"DEBIT"},
    }).then(({ body }) => {
      transactionAccountCategory = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-account-posting-process-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-account-posting-process-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-account-posting-process-types/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transaction-account-categories', {
      statusCode: 200,
      body: [transactionAccountCategory],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (transactionAccountPostingProcessType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-posting-process-types/${transactionAccountPostingProcessType.id}`,
      }).then(() => {
        transactionAccountPostingProcessType = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (transactionAccountCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-categories/${transactionAccountCategory.id}`,
      }).then(() => {
        transactionAccountCategory = undefined;
      });
    }
  });
   */

  it('TransactionAccountPostingProcessTypes menu should load TransactionAccountPostingProcessTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-account-posting-process-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccountPostingProcessType').should('exist');
    cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);
  });

  describe('TransactionAccountPostingProcessType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionAccountPostingProcessTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionAccountPostingProcessType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/transaction-account-posting-process-type/new$'));
        cy.getEntityCreateUpdateHeading('TransactionAccountPostingProcessType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-account-posting-process-types',
  
          body: {
            ...transactionAccountPostingProcessTypeSample,
            debitAccountType: transactionAccountCategory,
            creditAccountType: transactionAccountCategory,
          },
        }).then(({ body }) => {
          transactionAccountPostingProcessType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-account-posting-process-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionAccountPostingProcessType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionAccountPostingProcessTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(transactionAccountPostingProcessTypePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TransactionAccountPostingProcessType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccountPostingProcessType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);
      });

      it('edit button click should load edit TransactionAccountPostingProcessType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccountPostingProcessType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TransactionAccountPostingProcessType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionAccountPostingProcessType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);

        transactionAccountPostingProcessType = undefined;
      });
    });
  });

  describe('new TransactionAccountPostingProcessType page', () => {
    beforeEach(() => {
      cy.visit(`${transactionAccountPostingProcessTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TransactionAccountPostingProcessType');
    });

    it.skip('should create an instance of TransactionAccountPostingProcessType', () => {
      cy.get(`[data-cy="name"]`).type('Intelligent array teal').should('have.value', 'Intelligent array teal');

      cy.get(`[data-cy="debitAccountType"]`).select(1);
      cy.get(`[data-cy="creditAccountType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transactionAccountPostingProcessType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionAccountPostingProcessTypePageUrlPattern);
    });
  });
});
