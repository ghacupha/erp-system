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

describe('GlMapping e2e test', () => {
  const glMappingPageUrl = '/gl-mapping';
  const glMappingPageUrlPattern = new RegExp('/gl-mapping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const glMappingSample = { subGLCode: 'SMTP Lock Strategist', mainGLCode: 'initiatives Generic Auto', glType: 'parsing Plastic payment' };

  let glMapping: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gl-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gl-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gl-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (glMapping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gl-mappings/${glMapping.id}`,
      }).then(() => {
        glMapping = undefined;
      });
    }
  });

  it('GlMappings menu should load GlMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gl-mapping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GlMapping').should('exist');
    cy.url().should('match', glMappingPageUrlPattern);
  });

  describe('GlMapping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(glMappingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GlMapping page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/gl-mapping/new$'));
        cy.getEntityCreateUpdateHeading('GlMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', glMappingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gl-mappings',
          body: glMappingSample,
        }).then(({ body }) => {
          glMapping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gl-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [glMapping],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(glMappingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GlMapping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('glMapping');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', glMappingPageUrlPattern);
      });

      it('edit button click should load edit GlMapping page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GlMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', glMappingPageUrlPattern);
      });

      it('last delete button click should delete instance of GlMapping', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('glMapping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', glMappingPageUrlPattern);

        glMapping = undefined;
      });
    });
  });

  describe('new GlMapping page', () => {
    beforeEach(() => {
      cy.visit(`${glMappingPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GlMapping');
    });

    it('should create an instance of GlMapping', () => {
      cy.get(`[data-cy="subGLCode"]`).type('Consultant').should('have.value', 'Consultant');

      cy.get(`[data-cy="subGLDescription"]`).type('Intranet ADP').should('have.value', 'Intranet ADP');

      cy.get(`[data-cy="mainGLCode"]`).type('Sleek RSS').should('have.value', 'Sleek RSS');

      cy.get(`[data-cy="mainGLDescription"]`).type('Cambridgeshire Borders').should('have.value', 'Cambridgeshire Borders');

      cy.get(`[data-cy="glType"]`).type('wireless Berkshire Agent').should('have.value', 'wireless Berkshire Agent');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        glMapping = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', glMappingPageUrlPattern);
    });
  });
});
