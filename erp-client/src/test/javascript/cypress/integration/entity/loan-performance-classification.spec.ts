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

describe('LoanPerformanceClassification e2e test', () => {
  const loanPerformanceClassificationPageUrl = '/loan-performance-classification';
  const loanPerformanceClassificationPageUrlPattern = new RegExp('/loan-performance-classification(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanPerformanceClassificationSample = {
    loanPerformanceClassificationCode: 'navigating payment deposit',
    loanPerformanceClassificationType: 'Auto programming override',
  };

  let loanPerformanceClassification: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-performance-classifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-performance-classifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-performance-classifications/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanPerformanceClassification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-performance-classifications/${loanPerformanceClassification.id}`,
      }).then(() => {
        loanPerformanceClassification = undefined;
      });
    }
  });

  it('LoanPerformanceClassifications menu should load LoanPerformanceClassifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-performance-classification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanPerformanceClassification').should('exist');
    cy.url().should('match', loanPerformanceClassificationPageUrlPattern);
  });

  describe('LoanPerformanceClassification page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanPerformanceClassificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanPerformanceClassification page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-performance-classification/new$'));
        cy.getEntityCreateUpdateHeading('LoanPerformanceClassification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanPerformanceClassificationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-performance-classifications',
          body: loanPerformanceClassificationSample,
        }).then(({ body }) => {
          loanPerformanceClassification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-performance-classifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanPerformanceClassification],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanPerformanceClassificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanPerformanceClassification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanPerformanceClassification');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanPerformanceClassificationPageUrlPattern);
      });

      it('edit button click should load edit LoanPerformanceClassification page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanPerformanceClassification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanPerformanceClassificationPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanPerformanceClassification', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanPerformanceClassification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanPerformanceClassificationPageUrlPattern);

        loanPerformanceClassification = undefined;
      });
    });
  });

  describe('new LoanPerformanceClassification page', () => {
    beforeEach(() => {
      cy.visit(`${loanPerformanceClassificationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanPerformanceClassification');
    });

    it('should create an instance of LoanPerformanceClassification', () => {
      cy.get(`[data-cy="loanPerformanceClassificationCode"]`)
        .type('Creative Computer Sausages')
        .should('have.value', 'Creative Computer Sausages');

      cy.get(`[data-cy="loanPerformanceClassificationType"]`).type('quantifying').should('have.value', 'quantifying');

      cy.get(`[data-cy="commercialBankDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="microfinanceDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanPerformanceClassification = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanPerformanceClassificationPageUrlPattern);
    });
  });
});
