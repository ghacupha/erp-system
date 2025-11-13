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

describe('DerivativeSubType e2e test', () => {
  const derivativeSubTypePageUrl = '/derivative-sub-type';
  const derivativeSubTypePageUrlPattern = new RegExp('/derivative-sub-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const derivativeSubTypeSample = { financialDerivativeSubTypeCode: 'Views Awesome', financialDerivativeSubTye: 'Universal firewall' };

  let derivativeSubType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/derivative-sub-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/derivative-sub-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/derivative-sub-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (derivativeSubType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/derivative-sub-types/${derivativeSubType.id}`,
      }).then(() => {
        derivativeSubType = undefined;
      });
    }
  });

  it('DerivativeSubTypes menu should load DerivativeSubTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('derivative-sub-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DerivativeSubType').should('exist');
    cy.url().should('match', derivativeSubTypePageUrlPattern);
  });

  describe('DerivativeSubType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(derivativeSubTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DerivativeSubType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/derivative-sub-type/new$'));
        cy.getEntityCreateUpdateHeading('DerivativeSubType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeSubTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/derivative-sub-types',
          body: derivativeSubTypeSample,
        }).then(({ body }) => {
          derivativeSubType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/derivative-sub-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [derivativeSubType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(derivativeSubTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DerivativeSubType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('derivativeSubType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeSubTypePageUrlPattern);
      });

      it('edit button click should load edit DerivativeSubType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DerivativeSubType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeSubTypePageUrlPattern);
      });

      it('last delete button click should delete instance of DerivativeSubType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('derivativeSubType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeSubTypePageUrlPattern);

        derivativeSubType = undefined;
      });
    });
  });

  describe('new DerivativeSubType page', () => {
    beforeEach(() => {
      cy.visit(`${derivativeSubTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DerivativeSubType');
    });

    it('should create an instance of DerivativeSubType', () => {
      cy.get(`[data-cy="financialDerivativeSubTypeCode"]`).type('Avon parse').should('have.value', 'Avon parse');

      cy.get(`[data-cy="financialDerivativeSubTye"]`)
        .type('Skyway Towels relationships')
        .should('have.value', 'Skyway Towels relationships');

      cy.get(`[data-cy="financialDerivativeSubtypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        derivativeSubType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', derivativeSubTypePageUrlPattern);
    });
  });
});
