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

describe('LoanAccountCategory e2e test', () => {
  const loanAccountCategoryPageUrl = '/loan-account-category';
  const loanAccountCategoryPageUrlPattern = new RegExp('/loan-account-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanAccountCategorySample = {
    loanAccountMutationCode: 'Operations Avon Borders',
    loanAccountMutationType: 'NPL_RECOVERY',
    loanAccountMutationDetails: 'analyzing',
  };

  let loanAccountCategory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-account-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-account-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-account-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanAccountCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-account-categories/${loanAccountCategory.id}`,
      }).then(() => {
        loanAccountCategory = undefined;
      });
    }
  });

  it('LoanAccountCategories menu should load LoanAccountCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-account-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanAccountCategory').should('exist');
    cy.url().should('match', loanAccountCategoryPageUrlPattern);
  });

  describe('LoanAccountCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanAccountCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanAccountCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-account-category/new$'));
        cy.getEntityCreateUpdateHeading('LoanAccountCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanAccountCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-account-categories',
          body: loanAccountCategorySample,
        }).then(({ body }) => {
          loanAccountCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-account-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanAccountCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanAccountCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanAccountCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanAccountCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanAccountCategoryPageUrlPattern);
      });

      it('edit button click should load edit LoanAccountCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanAccountCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanAccountCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanAccountCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanAccountCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanAccountCategoryPageUrlPattern);

        loanAccountCategory = undefined;
      });
    });
  });

  describe('new LoanAccountCategory page', () => {
    beforeEach(() => {
      cy.visit(`${loanAccountCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanAccountCategory');
    });

    it('should create an instance of LoanAccountCategory', () => {
      cy.get(`[data-cy="loanAccountMutationCode"]`).type('grow Borders').should('have.value', 'grow Borders');

      cy.get(`[data-cy="loanAccountMutationType"]`).select('NPL_RECOVERY');

      cy.get(`[data-cy="loanAccountMutationDetails"]`).type('GB').should('have.value', 'GB');

      cy.get(`[data-cy="loanAccountMutationDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanAccountCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanAccountCategoryPageUrlPattern);
    });
  });
});
