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

describe('TransactionAccountLedger e2e test', () => {
  const transactionAccountLedgerPageUrl = '/transaction-account-ledger';
  const transactionAccountLedgerPageUrlPattern = new RegExp('/transaction-account-ledger(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionAccountLedgerSample = { ledgerCode: 'web-readiness', ledgerName: 'deposit array' };

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
    cy.intercept('GET', '/api/transaction-account-ledgers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-account-ledgers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-account-ledgers/*').as('deleteEntityRequest');
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

  it('TransactionAccountLedgers menu should load TransactionAccountLedgers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-account-ledger');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccountLedger').should('exist');
    cy.url().should('match', transactionAccountLedgerPageUrlPattern);
  });

  describe('TransactionAccountLedger page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionAccountLedgerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionAccountLedger page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/transaction-account-ledger/new$'));
        cy.getEntityCreateUpdateHeading('TransactionAccountLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountLedgerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-account-ledgers',
          body: transactionAccountLedgerSample,
        }).then(({ body }) => {
          transactionAccountLedger = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-account-ledgers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionAccountLedger],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionAccountLedgerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TransactionAccountLedger page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccountLedger');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountLedgerPageUrlPattern);
      });

      it('edit button click should load edit TransactionAccountLedger page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccountLedger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountLedgerPageUrlPattern);
      });

      it('last delete button click should delete instance of TransactionAccountLedger', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionAccountLedger').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountLedgerPageUrlPattern);

        transactionAccountLedger = undefined;
      });
    });
  });

  describe('new TransactionAccountLedger page', () => {
    beforeEach(() => {
      cy.visit(`${transactionAccountLedgerPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TransactionAccountLedger');
    });

    it('should create an instance of TransactionAccountLedger', () => {
      cy.get(`[data-cy="ledgerCode"]`).type('Idaho View').should('have.value', 'Idaho View');

      cy.get(`[data-cy="ledgerName"]`).type('Refined').should('have.value', 'Refined');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transactionAccountLedger = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionAccountLedgerPageUrlPattern);
    });
  });
});
