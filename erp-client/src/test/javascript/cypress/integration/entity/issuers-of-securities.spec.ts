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

describe('IssuersOfSecurities e2e test', () => {
  const issuersOfSecuritiesPageUrl = '/issuers-of-securities';
  const issuersOfSecuritiesPageUrlPattern = new RegExp('/issuers-of-securities(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const issuersOfSecuritiesSample = { issuerOfSecuritiesCode: 'innovate Port protocol', issuerOfSecurities: 'synthesize panel' };

  let issuersOfSecurities: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/issuers-of-securities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/issuers-of-securities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/issuers-of-securities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (issuersOfSecurities) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/issuers-of-securities/${issuersOfSecurities.id}`,
      }).then(() => {
        issuersOfSecurities = undefined;
      });
    }
  });

  it('IssuersOfSecurities menu should load IssuersOfSecurities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('issuers-of-securities');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IssuersOfSecurities').should('exist');
    cy.url().should('match', issuersOfSecuritiesPageUrlPattern);
  });

  describe('IssuersOfSecurities page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(issuersOfSecuritiesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IssuersOfSecurities page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/issuers-of-securities/new$'));
        cy.getEntityCreateUpdateHeading('IssuersOfSecurities');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', issuersOfSecuritiesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/issuers-of-securities',
          body: issuersOfSecuritiesSample,
        }).then(({ body }) => {
          issuersOfSecurities = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/issuers-of-securities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [issuersOfSecurities],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(issuersOfSecuritiesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IssuersOfSecurities page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('issuersOfSecurities');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', issuersOfSecuritiesPageUrlPattern);
      });

      it('edit button click should load edit IssuersOfSecurities page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssuersOfSecurities');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', issuersOfSecuritiesPageUrlPattern);
      });

      it('last delete button click should delete instance of IssuersOfSecurities', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('issuersOfSecurities').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', issuersOfSecuritiesPageUrlPattern);

        issuersOfSecurities = undefined;
      });
    });
  });

  describe('new IssuersOfSecurities page', () => {
    beforeEach(() => {
      cy.visit(`${issuersOfSecuritiesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IssuersOfSecurities');
    });

    it('should create an instance of IssuersOfSecurities', () => {
      cy.get(`[data-cy="issuerOfSecuritiesCode"]`)
        .type('applications Cambridgeshire archive')
        .should('have.value', 'applications Cambridgeshire archive');

      cy.get(`[data-cy="issuerOfSecurities"]`).type('Soft firewall Singapore').should('have.value', 'Soft firewall Singapore');

      cy.get(`[data-cy="issuerOfSecuritiesDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        issuersOfSecurities = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', issuersOfSecuritiesPageUrlPattern);
    });
  });
});
