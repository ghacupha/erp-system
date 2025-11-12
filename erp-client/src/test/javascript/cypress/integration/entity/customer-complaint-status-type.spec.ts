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

describe('CustomerComplaintStatusType e2e test', () => {
  const customerComplaintStatusTypePageUrl = '/customer-complaint-status-type';
  const customerComplaintStatusTypePageUrlPattern = new RegExp('/customer-complaint-status-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const customerComplaintStatusTypeSample = {
    customerComplaintStatusTypeCode: 'transparent networks',
    customerComplaintStatusType: 'lime Rubber Direct',
  };

  let customerComplaintStatusType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/customer-complaint-status-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/customer-complaint-status-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/customer-complaint-status-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (customerComplaintStatusType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/customer-complaint-status-types/${customerComplaintStatusType.id}`,
      }).then(() => {
        customerComplaintStatusType = undefined;
      });
    }
  });

  it('CustomerComplaintStatusTypes menu should load CustomerComplaintStatusTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('customer-complaint-status-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustomerComplaintStatusType').should('exist');
    cy.url().should('match', customerComplaintStatusTypePageUrlPattern);
  });

  describe('CustomerComplaintStatusType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(customerComplaintStatusTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustomerComplaintStatusType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/customer-complaint-status-type/new$'));
        cy.getEntityCreateUpdateHeading('CustomerComplaintStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerComplaintStatusTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/customer-complaint-status-types',
          body: customerComplaintStatusTypeSample,
        }).then(({ body }) => {
          customerComplaintStatusType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/customer-complaint-status-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [customerComplaintStatusType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(customerComplaintStatusTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustomerComplaintStatusType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('customerComplaintStatusType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerComplaintStatusTypePageUrlPattern);
      });

      it('edit button click should load edit CustomerComplaintStatusType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustomerComplaintStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerComplaintStatusTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CustomerComplaintStatusType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('customerComplaintStatusType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', customerComplaintStatusTypePageUrlPattern);

        customerComplaintStatusType = undefined;
      });
    });
  });

  describe('new CustomerComplaintStatusType page', () => {
    beforeEach(() => {
      cy.visit(`${customerComplaintStatusTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CustomerComplaintStatusType');
    });

    it('should create an instance of CustomerComplaintStatusType', () => {
      cy.get(`[data-cy="customerComplaintStatusTypeCode"]`).type('parsing Auto').should('have.value', 'parsing Auto');

      cy.get(`[data-cy="customerComplaintStatusType"]`).type('Hawaii Nepal Leu').should('have.value', 'Hawaii Nepal Leu');

      cy.get(`[data-cy="customerComplaintStatusTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        customerComplaintStatusType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', customerComplaintStatusTypePageUrlPattern);
    });
  });
});
