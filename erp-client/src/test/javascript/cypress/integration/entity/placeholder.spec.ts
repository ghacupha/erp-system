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

describe('Placeholder e2e test', () => {
  const placeholderPageUrl = '/placeholder';
  const placeholderPageUrlPattern = new RegExp('/placeholder(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const placeholderSample = { description: 'copy Customer-focused' };

  let placeholder: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/placeholders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/placeholders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/placeholders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (placeholder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/placeholders/${placeholder.id}`,
      }).then(() => {
        placeholder = undefined;
      });
    }
  });

  it('Placeholders menu should load Placeholders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('placeholder');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Placeholder').should('exist');
    cy.url().should('match', placeholderPageUrlPattern);
  });

  describe('Placeholder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(placeholderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Placeholder page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/placeholder/new$'));
        cy.getEntityCreateUpdateHeading('Placeholder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/placeholders',
          body: placeholderSample,
        }).then(({ body }) => {
          placeholder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/placeholders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [placeholder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(placeholderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Placeholder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('placeholder');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });

      it('edit button click should load edit Placeholder page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Placeholder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);
      });

      it('last delete button click should delete instance of Placeholder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('placeholder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', placeholderPageUrlPattern);

        placeholder = undefined;
      });
    });
  });

  describe('new Placeholder page', () => {
    beforeEach(() => {
      cy.visit(`${placeholderPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Placeholder');
    });

    it('should create an instance of Placeholder', () => {
      cy.get(`[data-cy="description"]`).type('Cambridgeshire transform').should('have.value', 'Cambridgeshire transform');

      cy.get(`[data-cy="token"]`).type('quantify functionalities 24/365').should('have.value', 'quantify functionalities 24/365');

      cy.get(`[data-cy="fileUploadToken"]`).type('Principal').should('have.value', 'Principal');

      cy.get(`[data-cy="compilationToken"]`).type('UAE IB').should('have.value', 'UAE IB');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        placeholder = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', placeholderPageUrlPattern);
    });
  });
});
