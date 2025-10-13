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

describe('SecurityType e2e test', () => {
  const securityTypePageUrl = '/security-type';
  const securityTypePageUrlPattern = new RegExp('/security-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const securityTypeSample = { securityTypeCode: 'client-driven Salad', securityType: 'circuit' };

  let securityType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/security-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/security-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/security-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (securityType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-types/${securityType.id}`,
      }).then(() => {
        securityType = undefined;
      });
    }
  });

  it('SecurityTypes menu should load SecurityTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('security-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SecurityType').should('exist');
    cy.url().should('match', securityTypePageUrlPattern);
  });

  describe('SecurityType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(securityTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SecurityType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/security-type/new$'));
        cy.getEntityCreateUpdateHeading('SecurityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/security-types',
          body: securityTypeSample,
        }).then(({ body }) => {
          securityType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/security-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [securityType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(securityTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SecurityType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('securityType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTypePageUrlPattern);
      });

      it('edit button click should load edit SecurityType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SecurityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTypePageUrlPattern);
      });

      it('last delete button click should delete instance of SecurityType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('securityType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTypePageUrlPattern);

        securityType = undefined;
      });
    });
  });

  describe('new SecurityType page', () => {
    beforeEach(() => {
      cy.visit(`${securityTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SecurityType');
    });

    it('should create an instance of SecurityType', () => {
      cy.get(`[data-cy="securityTypeCode"]`).type('Borders').should('have.value', 'Borders');

      cy.get(`[data-cy="securityType"]`).type('Wisconsin').should('have.value', 'Wisconsin');

      cy.get(`[data-cy="securityTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="securityTypeDescription"]`).type('transition Baby').should('have.value', 'transition Baby');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        securityType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', securityTypePageUrlPattern);
    });
  });
});
