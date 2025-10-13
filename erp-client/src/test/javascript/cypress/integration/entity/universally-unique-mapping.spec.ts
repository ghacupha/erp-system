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

describe('UniversallyUniqueMapping e2e test', () => {
  const universallyUniqueMappingPageUrl = '/universally-unique-mapping';
  const universallyUniqueMappingPageUrlPattern = new RegExp('/universally-unique-mapping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const universallyUniqueMappingSample = { universalKey: 'Computers' };

  let universallyUniqueMapping: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/universally-unique-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/universally-unique-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/universally-unique-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (universallyUniqueMapping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/universally-unique-mappings/${universallyUniqueMapping.id}`,
      }).then(() => {
        universallyUniqueMapping = undefined;
      });
    }
  });

  it('UniversallyUniqueMappings menu should load UniversallyUniqueMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('universally-unique-mapping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UniversallyUniqueMapping').should('exist');
    cy.url().should('match', universallyUniqueMappingPageUrlPattern);
  });

  describe('UniversallyUniqueMapping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(universallyUniqueMappingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UniversallyUniqueMapping page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/universally-unique-mapping/new$'));
        cy.getEntityCreateUpdateHeading('UniversallyUniqueMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', universallyUniqueMappingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/universally-unique-mappings',
          body: universallyUniqueMappingSample,
        }).then(({ body }) => {
          universallyUniqueMapping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/universally-unique-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [universallyUniqueMapping],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(universallyUniqueMappingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UniversallyUniqueMapping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('universallyUniqueMapping');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', universallyUniqueMappingPageUrlPattern);
      });

      it('edit button click should load edit UniversallyUniqueMapping page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UniversallyUniqueMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', universallyUniqueMappingPageUrlPattern);
      });

      it('last delete button click should delete instance of UniversallyUniqueMapping', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('universallyUniqueMapping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', universallyUniqueMappingPageUrlPattern);

        universallyUniqueMapping = undefined;
      });
    });
  });

  describe('new UniversallyUniqueMapping page', () => {
    beforeEach(() => {
      cy.visit(`${universallyUniqueMappingPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('UniversallyUniqueMapping');
    });

    it('should create an instance of UniversallyUniqueMapping', () => {
      cy.get(`[data-cy="universalKey"]`).type('Alabama').should('have.value', 'Alabama');

      cy.get(`[data-cy="mappedValue"]`).type('Pine Health infrastructure').should('have.value', 'Pine Health infrastructure');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        universallyUniqueMapping = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', universallyUniqueMappingPageUrlPattern);
    });
  });
});
