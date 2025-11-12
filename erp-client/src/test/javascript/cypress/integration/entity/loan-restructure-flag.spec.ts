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

describe('LoanRestructureFlag e2e test', () => {
  const loanRestructureFlagPageUrl = '/loan-restructure-flag';
  const loanRestructureFlagPageUrlPattern = new RegExp('/loan-restructure-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanRestructureFlagSample = { loanRestructureFlagCode: 'N', loanRestructureFlagType: 'schemas Cuba' };

  let loanRestructureFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-restructure-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-restructure-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-restructure-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanRestructureFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-restructure-flags/${loanRestructureFlag.id}`,
      }).then(() => {
        loanRestructureFlag = undefined;
      });
    }
  });

  it('LoanRestructureFlags menu should load LoanRestructureFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-restructure-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanRestructureFlag').should('exist');
    cy.url().should('match', loanRestructureFlagPageUrlPattern);
  });

  describe('LoanRestructureFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanRestructureFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanRestructureFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-restructure-flag/new$'));
        cy.getEntityCreateUpdateHeading('LoanRestructureFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-restructure-flags',
          body: loanRestructureFlagSample,
        }).then(({ body }) => {
          loanRestructureFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-restructure-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanRestructureFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanRestructureFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanRestructureFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanRestructureFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureFlagPageUrlPattern);
      });

      it('edit button click should load edit LoanRestructureFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanRestructureFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanRestructureFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanRestructureFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureFlagPageUrlPattern);

        loanRestructureFlag = undefined;
      });
    });
  });

  describe('new LoanRestructureFlag page', () => {
    beforeEach(() => {
      cy.visit(`${loanRestructureFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanRestructureFlag');
    });

    it('should create an instance of LoanRestructureFlag', () => {
      cy.get(`[data-cy="loanRestructureFlagCode"]`).select('Y');

      cy.get(`[data-cy="loanRestructureFlagType"]`)
        .type('Mauritania hacking interfaces')
        .should('have.value', 'Mauritania hacking interfaces');

      cy.get(`[data-cy="loanRestructureFlagDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanRestructureFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanRestructureFlagPageUrlPattern);
    });
  });
});
