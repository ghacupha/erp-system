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

describe('PrepaymentMapping e2e test', () => {
  const prepaymentMappingPageUrl = '/prepayment-mapping';
  const prepaymentMappingPageUrlPattern = new RegExp('/prepayment-mapping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentMappingSample = {
    parameterKey: 'state Granite Account',
    parameterGuid: 'ecc0d5b6-46dc-4159-9998-55d749f23068',
    parameter: 'Facilitator Account',
  };

  let prepaymentMapping: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentMapping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-mappings/${prepaymentMapping.id}`,
      }).then(() => {
        prepaymentMapping = undefined;
      });
    }
  });

  it('PrepaymentMappings menu should load PrepaymentMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-mapping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentMapping').should('exist');
    cy.url().should('match', prepaymentMappingPageUrlPattern);
  });

  describe('PrepaymentMapping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(prepaymentMappingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrepaymentMapping page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/prepayment-mapping/new$'));
        cy.getEntityCreateUpdateHeading('PrepaymentMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentMappingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/prepayment-mappings',
          body: prepaymentMappingSample,
        }).then(({ body }) => {
          prepaymentMapping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/prepayment-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [prepaymentMapping],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(prepaymentMappingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrepaymentMapping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentMapping');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentMappingPageUrlPattern);
      });

      it('edit button click should load edit PrepaymentMapping page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrepaymentMapping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentMappingPageUrlPattern);
      });

      it('last delete button click should delete instance of PrepaymentMapping', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('prepaymentMapping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentMappingPageUrlPattern);

        prepaymentMapping = undefined;
      });
    });
  });

  describe('new PrepaymentMapping page', () => {
    beforeEach(() => {
      cy.visit(`${prepaymentMappingPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PrepaymentMapping');
    });

    it('should create an instance of PrepaymentMapping', () => {
      cy.get(`[data-cy="parameterKey"]`).type('Toys Loan').should('have.value', 'Toys Loan');

      cy.get(`[data-cy="parameterGuid"]`)
        .type('c4a878ff-d2aa-4716-8a3c-2ca8822f8430')
        .invoke('val')
        .should('match', new RegExp('c4a878ff-d2aa-4716-8a3c-2ca8822f8430'));

      cy.get(`[data-cy="parameter"]`).type('International access payment').should('have.value', 'International access payment');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        prepaymentMapping = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', prepaymentMappingPageUrlPattern);
    });
  });
});
