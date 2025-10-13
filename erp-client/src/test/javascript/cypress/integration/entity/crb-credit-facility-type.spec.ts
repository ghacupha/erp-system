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

describe('CrbCreditFacilityType e2e test', () => {
  const crbCreditFacilityTypePageUrl = '/crb-credit-facility-type';
  const crbCreditFacilityTypePageUrlPattern = new RegExp('/crb-credit-facility-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbCreditFacilityTypeSample = { creditFacilityTypeCode: 'Toys Drives', creditFacilityType: 'Zealand impactful Metrics' };

  let crbCreditFacilityType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-credit-facility-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-credit-facility-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-credit-facility-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbCreditFacilityType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-credit-facility-types/${crbCreditFacilityType.id}`,
      }).then(() => {
        crbCreditFacilityType = undefined;
      });
    }
  });

  it('CrbCreditFacilityTypes menu should load CrbCreditFacilityTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-credit-facility-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbCreditFacilityType').should('exist');
    cy.url().should('match', crbCreditFacilityTypePageUrlPattern);
  });

  describe('CrbCreditFacilityType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbCreditFacilityTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbCreditFacilityType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-credit-facility-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbCreditFacilityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditFacilityTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-credit-facility-types',
          body: crbCreditFacilityTypeSample,
        }).then(({ body }) => {
          crbCreditFacilityType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-credit-facility-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbCreditFacilityType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbCreditFacilityTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbCreditFacilityType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbCreditFacilityType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditFacilityTypePageUrlPattern);
      });

      it('edit button click should load edit CrbCreditFacilityType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbCreditFacilityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditFacilityTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbCreditFacilityType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbCreditFacilityType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditFacilityTypePageUrlPattern);

        crbCreditFacilityType = undefined;
      });
    });
  });

  describe('new CrbCreditFacilityType page', () => {
    beforeEach(() => {
      cy.visit(`${crbCreditFacilityTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbCreditFacilityType');
    });

    it('should create an instance of CrbCreditFacilityType', () => {
      cy.get(`[data-cy="creditFacilityTypeCode"]`).type('mobile Cove Automotive').should('have.value', 'mobile Cove Automotive');

      cy.get(`[data-cy="creditFacilityType"]`).type('compress PCI').should('have.value', 'compress PCI');

      cy.get(`[data-cy="creditFacilityDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbCreditFacilityType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbCreditFacilityTypePageUrlPattern);
    });
  });
});
