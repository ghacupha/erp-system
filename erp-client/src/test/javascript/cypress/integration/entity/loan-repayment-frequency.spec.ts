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

describe('LoanRepaymentFrequency e2e test', () => {
  const loanRepaymentFrequencyPageUrl = '/loan-repayment-frequency';
  const loanRepaymentFrequencyPageUrlPattern = new RegExp('/loan-repayment-frequency(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanRepaymentFrequencySample = { frequencyTypeCode: 'payment HDD', frequencyType: 'engine input hierarchy' };

  let loanRepaymentFrequency: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-repayment-frequencies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-repayment-frequencies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-repayment-frequencies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanRepaymentFrequency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-repayment-frequencies/${loanRepaymentFrequency.id}`,
      }).then(() => {
        loanRepaymentFrequency = undefined;
      });
    }
  });

  it('LoanRepaymentFrequencies menu should load LoanRepaymentFrequencies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-repayment-frequency');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanRepaymentFrequency').should('exist');
    cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);
  });

  describe('LoanRepaymentFrequency page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanRepaymentFrequencyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanRepaymentFrequency page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-repayment-frequency/new$'));
        cy.getEntityCreateUpdateHeading('LoanRepaymentFrequency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-repayment-frequencies',
          body: loanRepaymentFrequencySample,
        }).then(({ body }) => {
          loanRepaymentFrequency = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-repayment-frequencies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanRepaymentFrequency],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanRepaymentFrequencyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanRepaymentFrequency page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanRepaymentFrequency');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);
      });

      it('edit button click should load edit LoanRepaymentFrequency page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanRepaymentFrequency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanRepaymentFrequency', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanRepaymentFrequency').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);

        loanRepaymentFrequency = undefined;
      });
    });
  });

  describe('new LoanRepaymentFrequency page', () => {
    beforeEach(() => {
      cy.visit(`${loanRepaymentFrequencyPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanRepaymentFrequency');
    });

    it('should create an instance of LoanRepaymentFrequency', () => {
      cy.get(`[data-cy="frequencyTypeCode"]`).type('multimedia Games Factors').should('have.value', 'multimedia Games Factors');

      cy.get(`[data-cy="frequencyType"]`).type('invoice payment').should('have.value', 'invoice payment');

      cy.get(`[data-cy="frequencyTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanRepaymentFrequency = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanRepaymentFrequencyPageUrlPattern);
    });
  });
});
