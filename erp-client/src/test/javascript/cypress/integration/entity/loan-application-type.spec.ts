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

describe('LoanApplicationType e2e test', () => {
  const loanApplicationTypePageUrl = '/loan-application-type';
  const loanApplicationTypePageUrlPattern = new RegExp('/loan-application-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanApplicationTypeSample = { loanApplicationTypeCode: 'Computers', loanApplicationType: 'bluetooth turquoise Specialist' };

  let loanApplicationType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-application-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-application-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-application-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanApplicationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-application-types/${loanApplicationType.id}`,
      }).then(() => {
        loanApplicationType = undefined;
      });
    }
  });

  it('LoanApplicationTypes menu should load LoanApplicationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-application-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanApplicationType').should('exist');
    cy.url().should('match', loanApplicationTypePageUrlPattern);
  });

  describe('LoanApplicationType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanApplicationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanApplicationType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-application-type/new$'));
        cy.getEntityCreateUpdateHeading('LoanApplicationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-application-types',
          body: loanApplicationTypeSample,
        }).then(({ body }) => {
          loanApplicationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-application-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanApplicationType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanApplicationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanApplicationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanApplicationType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationTypePageUrlPattern);
      });

      it('edit button click should load edit LoanApplicationType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanApplicationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationTypePageUrlPattern);
      });

      it('last delete button click should delete instance of LoanApplicationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanApplicationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanApplicationTypePageUrlPattern);

        loanApplicationType = undefined;
      });
    });
  });

  describe('new LoanApplicationType page', () => {
    beforeEach(() => {
      cy.visit(`${loanApplicationTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanApplicationType');
    });

    it('should create an instance of LoanApplicationType', () => {
      cy.get(`[data-cy="loanApplicationTypeCode"]`).type('Steel Corners Account').should('have.value', 'Steel Corners Account');

      cy.get(`[data-cy="loanApplicationType"]`).type('Sausages').should('have.value', 'Sausages');

      cy.get(`[data-cy="loanApplicationDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanApplicationType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanApplicationTypePageUrlPattern);
    });
  });
});
