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

describe('CrbCustomerType e2e test', () => {
  const crbCustomerTypePageUrl = '/crb-customer-type';
  const crbCustomerTypePageUrlPattern = new RegExp('/crb-customer-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbCustomerTypeSample = { customerTypeCode: 'firewall', customerType: 'functionalities Stream' };

  let crbCustomerType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-customer-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-customer-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-customer-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbCustomerType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-customer-types/${crbCustomerType.id}`,
      }).then(() => {
        crbCustomerType = undefined;
      });
    }
  });

  it('CrbCustomerTypes menu should load CrbCustomerTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-customer-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbCustomerType').should('exist');
    cy.url().should('match', crbCustomerTypePageUrlPattern);
  });

  describe('CrbCustomerType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbCustomerTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbCustomerType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-customer-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbCustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCustomerTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-customer-types',
          body: crbCustomerTypeSample,
        }).then(({ body }) => {
          crbCustomerType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-customer-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbCustomerType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbCustomerTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbCustomerType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbCustomerType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCustomerTypePageUrlPattern);
      });

      it('edit button click should load edit CrbCustomerType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbCustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCustomerTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbCustomerType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbCustomerType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCustomerTypePageUrlPattern);

        crbCustomerType = undefined;
      });
    });
  });

  describe('new CrbCustomerType page', () => {
    beforeEach(() => {
      cy.visit(`${crbCustomerTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbCustomerType');
    });

    it('should create an instance of CrbCustomerType', () => {
      cy.get(`[data-cy="customerTypeCode"]`).type('toolset Pizza XML').should('have.value', 'toolset Pizza XML');

      cy.get(`[data-cy="customerType"]`).type('haptic target silver').should('have.value', 'haptic target silver');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbCustomerType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbCustomerTypePageUrlPattern);
    });
  });
});
