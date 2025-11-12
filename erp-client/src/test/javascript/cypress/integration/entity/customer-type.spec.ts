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

describe('CustomerType e2e test', () => {
  const customerTypePageUrl = '/customer-type';
  const customerTypePageUrlPattern = new RegExp('/customer-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const customerTypeSample = {};

  let customerType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/customer-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/customer-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/customer-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customerType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/customer-types/${customerType.id}`,
      }).then(() => {
        customerType = undefined;
      });
    }
  });

  it('CustomerTypes menu should load CustomerTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('customer-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomerType').should('exist');
    cy.url().should('match', customerTypePageUrlPattern);
  });

  describe('CustomerType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customerTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomerType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/customer-type/new$'));
        cy.getEntityCreateUpdateHeading('CustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/customer-types',
          body: customerTypeSample,
        }).then(({ body }) => {
          customerType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/customer-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customerType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(customerTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomerType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customerType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });

      it('edit button click should load edit CustomerType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CustomerType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customerType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerTypePageUrlPattern);

        customerType = undefined;
      });
    });
  });

  describe('new CustomerType page', () => {
    beforeEach(() => {
      cy.visit(`${customerTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CustomerType');
    });

    it('should create an instance of CustomerType', () => {
      cy.get(`[data-cy="customerTypeCode"]`).type('Agent Ethiopia').should('have.value', 'Agent Ethiopia');

      cy.get(`[data-cy="customerType"]`).type('Cambridgeshire').should('have.value', 'Cambridgeshire');

      cy.get(`[data-cy="customerTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        customerType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', customerTypePageUrlPattern);
    });
  });
});
