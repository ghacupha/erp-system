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

describe('SystemContentType e2e test', () => {
  const systemContentTypePageUrl = '/system-content-type';
  const systemContentTypePageUrlPattern = new RegExp('/system-content-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const systemContentTypeSample = { contentTypeName: 'Frozen', contentTypeHeader: 'feed', availability: 'SUPPORTED' };

  let systemContentType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/system-content-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/system-content-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/system-content-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (systemContentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-content-types/${systemContentType.id}`,
      }).then(() => {
        systemContentType = undefined;
      });
    }
  });

  it('SystemContentTypes menu should load SystemContentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('system-content-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SystemContentType').should('exist');
    cy.url().should('match', systemContentTypePageUrlPattern);
  });

  describe('SystemContentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(systemContentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SystemContentType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/system-content-type/new$'));
        cy.getEntityCreateUpdateHeading('SystemContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemContentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/system-content-types',
          body: systemContentTypeSample,
        }).then(({ body }) => {
          systemContentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/system-content-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [systemContentType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(systemContentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SystemContentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('systemContentType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemContentTypePageUrlPattern);
      });

      it('edit button click should load edit SystemContentType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemContentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of SystemContentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('systemContentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemContentTypePageUrlPattern);

        systemContentType = undefined;
      });
    });
  });

  describe('new SystemContentType page', () => {
    beforeEach(() => {
      cy.visit(`${systemContentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SystemContentType');
    });

    it('should create an instance of SystemContentType', () => {
      cy.get(`[data-cy="contentTypeName"]`).type('Handcrafted Architect Borders').should('have.value', 'Handcrafted Architect Borders');

      cy.get(`[data-cy="contentTypeHeader"]`).type('secondary').should('have.value', 'secondary');

      cy.get(`[data-cy="comments"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="availability"]`).select('NOT_SUPPORTED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        systemContentType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', systemContentTypePageUrlPattern);
    });
  });
});
