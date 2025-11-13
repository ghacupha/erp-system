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

describe('TransactionAccountCategory e2e test', () => {
  const transactionAccountCategoryPageUrl = '/transaction-account-category';
  const transactionAccountCategoryPageUrlPattern = new RegExp('/transaction-account-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionAccountCategorySample = { name: 'Technician holistic', transactionAccountPostingType: 'CREDIT' };

  let transactionAccountCategory: any;
  let transactionAccountLedger: any;

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
      url: '/api/transaction-account-ledgers',
      body: { ledgerCode: 'harness', ledgerName: 'Data Account' },
    }).then(({ body }) => {
      transactionAccountLedger = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-account-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-account-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-account-categories/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/transaction-account-ledgers', {
      statusCode: 200,
      body: [transactionAccountLedger],
    });
  });

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

  afterEach(() => {
    if (transactionAccountLedger) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-ledgers/${transactionAccountLedger.id}`,
      }).then(() => {
        transactionAccountLedger = undefined;
      });
    }
  });

  it('TransactionAccountCategories menu should load TransactionAccountCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-account-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccountCategory').should('exist');
    cy.url().should('match', transactionAccountCategoryPageUrlPattern);
  });

  describe('TransactionAccountCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionAccountCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionAccountCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/transaction-account-category/new$'));
        cy.getEntityCreateUpdateHeading('TransactionAccountCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-account-categories',

          body: {
            ...transactionAccountCategorySample,
            accountLedger: transactionAccountLedger,
          },
        }).then(({ body }) => {
          transactionAccountCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-account-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionAccountCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionAccountCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TransactionAccountCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccountCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountCategoryPageUrlPattern);
      });

      it('edit button click should load edit TransactionAccountCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccountCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of TransactionAccountCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionAccountCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountCategoryPageUrlPattern);

        transactionAccountCategory = undefined;
      });
    });
  });

  describe('new TransactionAccountCategory page', () => {
    beforeEach(() => {
      cy.visit(`${transactionAccountCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TransactionAccountCategory');
    });

    it('should create an instance of TransactionAccountCategory', () => {
      cy.get(`[data-cy="name"]`).type('service-desk').should('have.value', 'service-desk');

      cy.get(`[data-cy="transactionAccountPostingType"]`).select('CREDIT');

      cy.get(`[data-cy="accountLedger"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transactionAccountCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionAccountCategoryPageUrlPattern);
    });
  });
});
