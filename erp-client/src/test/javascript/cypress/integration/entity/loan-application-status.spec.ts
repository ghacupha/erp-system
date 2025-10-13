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

describe('LoanApplicationStatus e2e test', () => {
  const loanApplicationStatusPageUrl = '/loan-application-status';
  const loanApplicationStatusPageUrlPattern = new RegExp('/loan-application-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanApplicationStatusSample = { loanApplicationStatusTypeCode: 'payment', loanApplicationStatusType: 'Tasty Mayen' };

  let loanApplicationStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-application-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-application-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-application-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanApplicationStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-application-statuses/${loanApplicationStatus.id}`,
      }).then(() => {
        loanApplicationStatus = undefined;
      });
    }
  });

  it('LoanApplicationStatuses menu should load LoanApplicationStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-application-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanApplicationStatus').should('exist');
    cy.url().should('match', loanApplicationStatusPageUrlPattern);
  });

  describe('LoanApplicationStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanApplicationStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanApplicationStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-application-status/new$'));
        cy.getEntityCreateUpdateHeading('LoanApplicationStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-application-statuses',
          body: loanApplicationStatusSample,
        }).then(({ body }) => {
          loanApplicationStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-application-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanApplicationStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanApplicationStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanApplicationStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanApplicationStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationStatusPageUrlPattern);
      });

      it('edit button click should load edit LoanApplicationStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanApplicationStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanApplicationStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanApplicationStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationStatusPageUrlPattern);

        loanApplicationStatus = undefined;
      });
    });
  });

  describe('new LoanApplicationStatus page', () => {
    beforeEach(() => {
      cy.visit(`${loanApplicationStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanApplicationStatus');
    });

    it('should create an instance of LoanApplicationStatus', () => {
      cy.get(`[data-cy="loanApplicationStatusTypeCode"]`).type('dot-com navigating').should('have.value', 'dot-com navigating');

      cy.get(`[data-cy="loanApplicationStatusType"]`).type('next-generation Florida').should('have.value', 'next-generation Florida');

      cy.get(`[data-cy="loanApplicationStatusDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanApplicationStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanApplicationStatusPageUrlPattern);
    });
  });
});
