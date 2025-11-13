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

describe('CountySubCountyCode e2e test', () => {
  const countySubCountyCodePageUrl = '/county-sub-county-code';
  const countySubCountyCodePageUrlPattern = new RegExp('/county-sub-county-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const countySubCountyCodeSample = {
    subCountyCode: '0323',
    subCountyName: 'Incredible open-source bluetooth',
    countyCode: '89',
    countyName: 'Bedfordshire',
  };

  let countySubCountyCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/county-sub-county-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/county-sub-county-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/county-sub-county-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (countySubCountyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/county-sub-county-codes/${countySubCountyCode.id}`,
      }).then(() => {
        countySubCountyCode = undefined;
      });
    }
  });

  it('CountySubCountyCodes menu should load CountySubCountyCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('county-sub-county-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CountySubCountyCode').should('exist');
    cy.url().should('match', countySubCountyCodePageUrlPattern);
  });

  describe('CountySubCountyCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(countySubCountyCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CountySubCountyCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/county-sub-county-code/new$'));
        cy.getEntityCreateUpdateHeading('CountySubCountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countySubCountyCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/county-sub-county-codes',
          body: countySubCountyCodeSample,
        }).then(({ body }) => {
          countySubCountyCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/county-sub-county-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [countySubCountyCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(countySubCountyCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CountySubCountyCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('countySubCountyCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countySubCountyCodePageUrlPattern);
      });

      it('edit button click should load edit CountySubCountyCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CountySubCountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countySubCountyCodePageUrlPattern);
      });

      it('last delete button click should delete instance of CountySubCountyCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('countySubCountyCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countySubCountyCodePageUrlPattern);

        countySubCountyCode = undefined;
      });
    });
  });

  describe('new CountySubCountyCode page', () => {
    beforeEach(() => {
      cy.visit(`${countySubCountyCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CountySubCountyCode');
    });

    it('should create an instance of CountySubCountyCode', () => {
      cy.get(`[data-cy="subCountyCode"]`).type('4650').should('have.value', '4650');

      cy.get(`[data-cy="subCountyName"]`).type('Senior').should('have.value', 'Senior');

      cy.get(`[data-cy="countyCode"]`).type('43').should('have.value', '43');

      cy.get(`[data-cy="countyName"]`).type('visualize Gloves').should('have.value', 'visualize Gloves');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        countySubCountyCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', countySubCountyCodePageUrlPattern);
    });
  });
});
