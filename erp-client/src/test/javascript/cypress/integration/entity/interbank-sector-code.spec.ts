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

describe('InterbankSectorCode e2e test', () => {
  const interbankSectorCodePageUrl = '/interbank-sector-code';
  const interbankSectorCodePageUrlPattern = new RegExp('/interbank-sector-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const interbankSectorCodeSample = { interbankSectorCode: 'Peso' };

  let interbankSectorCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/interbank-sector-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/interbank-sector-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/interbank-sector-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (interbankSectorCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/interbank-sector-codes/${interbankSectorCode.id}`,
      }).then(() => {
        interbankSectorCode = undefined;
      });
    }
  });

  it('InterbankSectorCodes menu should load InterbankSectorCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('interbank-sector-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InterbankSectorCode').should('exist');
    cy.url().should('match', interbankSectorCodePageUrlPattern);
  });

  describe('InterbankSectorCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(interbankSectorCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InterbankSectorCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/interbank-sector-code/new$'));
        cy.getEntityCreateUpdateHeading('InterbankSectorCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interbankSectorCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/interbank-sector-codes',
          body: interbankSectorCodeSample,
        }).then(({ body }) => {
          interbankSectorCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/interbank-sector-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [interbankSectorCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(interbankSectorCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InterbankSectorCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('interbankSectorCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interbankSectorCodePageUrlPattern);
      });

      it('edit button click should load edit InterbankSectorCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InterbankSectorCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interbankSectorCodePageUrlPattern);
      });

      it('last delete button click should delete instance of InterbankSectorCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('interbankSectorCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interbankSectorCodePageUrlPattern);

        interbankSectorCode = undefined;
      });
    });
  });

  describe('new InterbankSectorCode page', () => {
    beforeEach(() => {
      cy.visit(`${interbankSectorCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InterbankSectorCode');
    });

    it('should create an instance of InterbankSectorCode', () => {
      cy.get(`[data-cy="interbankSectorCode"]`).type('Sports Operations Devolved').should('have.value', 'Sports Operations Devolved');

      cy.get(`[data-cy="interbankSectorCodeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        interbankSectorCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', interbankSectorCodePageUrlPattern);
    });
  });
});
