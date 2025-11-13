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

describe('LoanDeclineReason e2e test', () => {
  const loanDeclineReasonPageUrl = '/loan-decline-reason';
  const loanDeclineReasonPageUrlPattern = new RegExp('/loan-decline-reason(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanDeclineReasonSample = { loanDeclineReasonTypeCode: 'Rhode Checking Pants', loanDeclineReasonType: 'Ergonomic Industrial blue' };

  let loanDeclineReason: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-decline-reasons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-decline-reasons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-decline-reasons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanDeclineReason) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-decline-reasons/${loanDeclineReason.id}`,
      }).then(() => {
        loanDeclineReason = undefined;
      });
    }
  });

  it('LoanDeclineReasons menu should load LoanDeclineReasons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-decline-reason');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanDeclineReason').should('exist');
    cy.url().should('match', loanDeclineReasonPageUrlPattern);
  });

  describe('LoanDeclineReason page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanDeclineReasonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanDeclineReason page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-decline-reason/new$'));
        cy.getEntityCreateUpdateHeading('LoanDeclineReason');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanDeclineReasonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-decline-reasons',
          body: loanDeclineReasonSample,
        }).then(({ body }) => {
          loanDeclineReason = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-decline-reasons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanDeclineReason],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanDeclineReasonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanDeclineReason page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanDeclineReason');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanDeclineReasonPageUrlPattern);
      });

      it('edit button click should load edit LoanDeclineReason page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanDeclineReason');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanDeclineReasonPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanDeclineReason', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanDeclineReason').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanDeclineReasonPageUrlPattern);

        loanDeclineReason = undefined;
      });
    });
  });

  describe('new LoanDeclineReason page', () => {
    beforeEach(() => {
      cy.visit(`${loanDeclineReasonPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanDeclineReason');
    });

    it('should create an instance of LoanDeclineReason', () => {
      cy.get(`[data-cy="loanDeclineReasonTypeCode"]`).type('USB morph Movies').should('have.value', 'USB morph Movies');

      cy.get(`[data-cy="loanDeclineReasonType"]`).type('Steel attitude Cyprus').should('have.value', 'Steel attitude Cyprus');

      cy.get(`[data-cy="loanDeclineReasonDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanDeclineReason = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanDeclineReasonPageUrlPattern);
    });
  });
});
