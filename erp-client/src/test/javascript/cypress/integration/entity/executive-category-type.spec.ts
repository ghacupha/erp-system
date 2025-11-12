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

describe('ExecutiveCategoryType e2e test', () => {
  const executiveCategoryTypePageUrl = '/executive-category-type';
  const executiveCategoryTypePageUrlPattern = new RegExp('/executive-category-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const executiveCategoryTypeSample = { directorCategoryTypeCode: 'Handmade', directorCategoryType: 'solution' };

  let executiveCategoryType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/executive-category-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/executive-category-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/executive-category-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (executiveCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/executive-category-types/${executiveCategoryType.id}`,
      }).then(() => {
        executiveCategoryType = undefined;
      });
    }
  });

  it('ExecutiveCategoryTypes menu should load ExecutiveCategoryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('executive-category-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExecutiveCategoryType').should('exist');
    cy.url().should('match', executiveCategoryTypePageUrlPattern);
  });

  describe('ExecutiveCategoryType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(executiveCategoryTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExecutiveCategoryType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/executive-category-type/new$'));
        cy.getEntityCreateUpdateHeading('ExecutiveCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', executiveCategoryTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/executive-category-types',
          body: executiveCategoryTypeSample,
        }).then(({ body }) => {
          executiveCategoryType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/executive-category-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [executiveCategoryType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(executiveCategoryTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExecutiveCategoryType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('executiveCategoryType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', executiveCategoryTypePageUrlPattern);
      });

      it('edit button click should load edit ExecutiveCategoryType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExecutiveCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', executiveCategoryTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ExecutiveCategoryType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('executiveCategoryType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', executiveCategoryTypePageUrlPattern);

        executiveCategoryType = undefined;
      });
    });
  });

  describe('new ExecutiveCategoryType page', () => {
    beforeEach(() => {
      cy.visit(`${executiveCategoryTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ExecutiveCategoryType');
    });

    it('should create an instance of ExecutiveCategoryType', () => {
      cy.get(`[data-cy="directorCategoryTypeCode"]`).type('Romania Shoes').should('have.value', 'Romania Shoes');

      cy.get(`[data-cy="directorCategoryType"]`)
        .type('Spur Persevering Cambridgeshire')
        .should('have.value', 'Spur Persevering Cambridgeshire');

      cy.get(`[data-cy="directorCategoryTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        executiveCategoryType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', executiveCategoryTypePageUrlPattern);
    });
  });
});
