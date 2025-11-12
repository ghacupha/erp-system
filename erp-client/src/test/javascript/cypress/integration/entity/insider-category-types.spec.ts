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

describe('InsiderCategoryTypes e2e test', () => {
  const insiderCategoryTypesPageUrl = '/insider-category-types';
  const insiderCategoryTypesPageUrlPattern = new RegExp('/insider-category-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const insiderCategoryTypesSample = { insiderCategoryTypeCode: 'collaboration Up-sized Markets', insiderCategoryTypeDetail: 'reboot' };

  let insiderCategoryTypes: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/insider-category-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/insider-category-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/insider-category-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (insiderCategoryTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/insider-category-types/${insiderCategoryTypes.id}`,
      }).then(() => {
        insiderCategoryTypes = undefined;
      });
    }
  });

  it('InsiderCategoryTypes menu should load InsiderCategoryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('insider-category-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InsiderCategoryTypes').should('exist');
    cy.url().should('match', insiderCategoryTypesPageUrlPattern);
  });

  describe('InsiderCategoryTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(insiderCategoryTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InsiderCategoryTypes page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/insider-category-types/new$'));
        cy.getEntityCreateUpdateHeading('InsiderCategoryTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', insiderCategoryTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/insider-category-types',
          body: insiderCategoryTypesSample,
        }).then(({ body }) => {
          insiderCategoryTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/insider-category-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [insiderCategoryTypes],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(insiderCategoryTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InsiderCategoryTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('insiderCategoryTypes');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', insiderCategoryTypesPageUrlPattern);
      });

      it('edit button click should load edit InsiderCategoryTypes page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InsiderCategoryTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', insiderCategoryTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of InsiderCategoryTypes', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('insiderCategoryTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', insiderCategoryTypesPageUrlPattern);

        insiderCategoryTypes = undefined;
      });
    });
  });

  describe('new InsiderCategoryTypes page', () => {
    beforeEach(() => {
      cy.visit(`${insiderCategoryTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InsiderCategoryTypes');
    });

    it('should create an instance of InsiderCategoryTypes', () => {
      cy.get(`[data-cy="insiderCategoryTypeCode"]`).type('Officer compressing black').should('have.value', 'Officer compressing black');

      cy.get(`[data-cy="insiderCategoryTypeDetail"]`).type('Pizza deploy').should('have.value', 'Pizza deploy');

      cy.get(`[data-cy="insiderCategoryDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        insiderCategoryTypes = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', insiderCategoryTypesPageUrlPattern);
    });
  });
});
