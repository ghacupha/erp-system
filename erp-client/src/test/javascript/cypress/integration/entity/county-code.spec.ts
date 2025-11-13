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

describe('CountyCode e2e test', () => {
  const countyCodePageUrl = '/county-code';
  const countyCodePageUrlPattern = new RegExp('/county-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const countyCodeSample = { countyCode: 94396, countyName: 'content Berkshire', subCountyCode: 99132, subCountyName: 'lime' };

  let countyCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/county-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/county-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/county-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (countyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/county-codes/${countyCode.id}`,
      }).then(() => {
        countyCode = undefined;
      });
    }
  });

  it('CountyCodes menu should load CountyCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('county-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CountyCode').should('exist');
    cy.url().should('match', countyCodePageUrlPattern);
  });

  describe('CountyCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(countyCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CountyCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/county-code/new$'));
        cy.getEntityCreateUpdateHeading('CountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countyCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/county-codes',
          body: countyCodeSample,
        }).then(({ body }) => {
          countyCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/county-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [countyCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(countyCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CountyCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('countyCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countyCodePageUrlPattern);
      });

      it('edit button click should load edit CountyCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CountyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countyCodePageUrlPattern);
      });

      it('last delete button click should delete instance of CountyCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('countyCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', countyCodePageUrlPattern);

        countyCode = undefined;
      });
    });
  });

  describe('new CountyCode page', () => {
    beforeEach(() => {
      cy.visit(`${countyCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CountyCode');
    });

    it('should create an instance of CountyCode', () => {
      cy.get(`[data-cy="countyCode"]`).type('67097').should('have.value', '67097');

      cy.get(`[data-cy="countyName"]`).type('Car Plastic Shirt').should('have.value', 'Car Plastic Shirt');

      cy.get(`[data-cy="subCountyCode"]`).type('29194').should('have.value', '29194');

      cy.get(`[data-cy="subCountyName"]`).type('Outdoors Saint').should('have.value', 'Outdoors Saint');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        countyCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', countyCodePageUrlPattern);
    });
  });
});
