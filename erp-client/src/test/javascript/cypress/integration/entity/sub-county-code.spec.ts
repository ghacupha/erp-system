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

describe('SubCountyCode e2e test', () => {
  const subCountyCodePageUrl = '/sub-county-code';
  const subCountyCodePageUrlPattern = new RegExp('/sub-county-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const subCountyCodeSample = {};

  let subCountyCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sub-county-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sub-county-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sub-county-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subCountyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-county-codes/${subCountyCode.id}`,
      }).then(() => {
        subCountyCode = undefined;
      });
    }
  });

  it('SubCountyCodes menu should load SubCountyCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sub-county-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubCountyCode').should('exist');
    cy.url().should('match', subCountyCodePageUrlPattern);
  });

  describe('SubCountyCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subCountyCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubCountyCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/sub-county-code/new$'));
        cy.getEntityCreateUpdateHeading('SubCountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', subCountyCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sub-county-codes',
          body: subCountyCodeSample,
        }).then(({ body }) => {
          subCountyCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sub-county-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [subCountyCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subCountyCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubCountyCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subCountyCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', subCountyCodePageUrlPattern);
      });

      it('edit button click should load edit SubCountyCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubCountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', subCountyCodePageUrlPattern);
      });

      it('last delete button click should delete instance of SubCountyCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('subCountyCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', subCountyCodePageUrlPattern);

        subCountyCode = undefined;
      });
    });
  });

  describe('new SubCountyCode page', () => {
    beforeEach(() => {
      cy.visit(`${subCountyCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SubCountyCode');
    });

    it('should create an instance of SubCountyCode', () => {
      cy.get(`[data-cy="countyCode"]`).type('Lead').should('have.value', 'Lead');

      cy.get(`[data-cy="countyName"]`).type('e-business').should('have.value', 'e-business');

      cy.get(`[data-cy="subCountyCode"]`).type('magenta overriding system').should('have.value', 'magenta overriding system');

      cy.get(`[data-cy="subCountyName"]`).type('Account out-of-the-box Montana').should('have.value', 'Account out-of-the-box Montana');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        subCountyCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', subCountyCodePageUrlPattern);
    });
  });
});
