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

describe('CategoryOfSecurity e2e test', () => {
  const categoryOfSecurityPageUrl = '/category-of-security';
  const categoryOfSecurityPageUrlPattern = new RegExp('/category-of-security(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const categoryOfSecuritySample = {
    categoryOfSecurity: 'Hampshire',
    categoryOfSecurityDetails: 'methodologies optical attitude-oriented',
  };

  let categoryOfSecurity: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/category-of-securities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/category-of-securities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/category-of-securities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (categoryOfSecurity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/category-of-securities/${categoryOfSecurity.id}`,
      }).then(() => {
        categoryOfSecurity = undefined;
      });
    }
  });

  it('CategoryOfSecurities menu should load CategoryOfSecurities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('category-of-security');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CategoryOfSecurity').should('exist');
    cy.url().should('match', categoryOfSecurityPageUrlPattern);
  });

  describe('CategoryOfSecurity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(categoryOfSecurityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CategoryOfSecurity page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/category-of-security/new$'));
        cy.getEntityCreateUpdateHeading('CategoryOfSecurity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryOfSecurityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/category-of-securities',
          body: categoryOfSecuritySample,
        }).then(({ body }) => {
          categoryOfSecurity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/category-of-securities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [categoryOfSecurity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(categoryOfSecurityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CategoryOfSecurity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('categoryOfSecurity');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryOfSecurityPageUrlPattern);
      });

      it('edit button click should load edit CategoryOfSecurity page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CategoryOfSecurity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryOfSecurityPageUrlPattern);
      });

      it('last delete button click should delete instance of CategoryOfSecurity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('categoryOfSecurity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryOfSecurityPageUrlPattern);

        categoryOfSecurity = undefined;
      });
    });
  });

  describe('new CategoryOfSecurity page', () => {
    beforeEach(() => {
      cy.visit(`${categoryOfSecurityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CategoryOfSecurity');
    });

    it('should create an instance of CategoryOfSecurity', () => {
      cy.get(`[data-cy="categoryOfSecurity"]`).type('hard International').should('have.value', 'hard International');

      cy.get(`[data-cy="categoryOfSecurityDetails"]`).type('logistical card').should('have.value', 'logistical card');

      cy.get(`[data-cy="categoryOfSecurityDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        categoryOfSecurity = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', categoryOfSecurityPageUrlPattern);
    });
  });
});
