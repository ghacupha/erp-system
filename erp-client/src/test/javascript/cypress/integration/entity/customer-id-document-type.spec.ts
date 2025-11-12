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

describe('CustomerIDDocumentType e2e test', () => {
  const customerIDDocumentTypePageUrl = '/customer-id-document-type';
  const customerIDDocumentTypePageUrlPattern = new RegExp('/customer-id-document-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const customerIDDocumentTypeSample = { documentCode: 'Avon solution', documentType: 'Shirt Shirt wireless' };

  let customerIDDocumentType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/customer-id-document-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/customer-id-document-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/customer-id-document-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customerIDDocumentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/customer-id-document-types/${customerIDDocumentType.id}`,
      }).then(() => {
        customerIDDocumentType = undefined;
      });
    }
  });

  it('CustomerIDDocumentTypes menu should load CustomerIDDocumentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('customer-id-document-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomerIDDocumentType').should('exist');
    cy.url().should('match', customerIDDocumentTypePageUrlPattern);
  });

  describe('CustomerIDDocumentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customerIDDocumentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomerIDDocumentType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/customer-id-document-type/new$'));
        cy.getEntityCreateUpdateHeading('CustomerIDDocumentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerIDDocumentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/customer-id-document-types',
          body: customerIDDocumentTypeSample,
        }).then(({ body }) => {
          customerIDDocumentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/customer-id-document-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customerIDDocumentType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(customerIDDocumentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomerIDDocumentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customerIDDocumentType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerIDDocumentTypePageUrlPattern);
      });

      it('edit button click should load edit CustomerIDDocumentType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomerIDDocumentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerIDDocumentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CustomerIDDocumentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customerIDDocumentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerIDDocumentTypePageUrlPattern);

        customerIDDocumentType = undefined;
      });
    });
  });

  describe('new CustomerIDDocumentType page', () => {
    beforeEach(() => {
      cy.visit(`${customerIDDocumentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CustomerIDDocumentType');
    });

    it('should create an instance of CustomerIDDocumentType', () => {
      cy.get(`[data-cy="documentCode"]`).type('networks Dakota').should('have.value', 'networks Dakota');

      cy.get(`[data-cy="documentType"]`).type('bluetooth Tools object-oriented').should('have.value', 'bluetooth Tools object-oriented');

      cy.get(`[data-cy="documentTypeDescription"]`).type('Practical').should('have.value', 'Practical');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        customerIDDocumentType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', customerIDDocumentTypePageUrlPattern);
    });
  });
});
