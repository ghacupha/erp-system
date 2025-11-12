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

describe('LegalStatus e2e test', () => {
  const legalStatusPageUrl = '/legal-status';
  const legalStatusPageUrlPattern = new RegExp('/legal-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const legalStatusSample = { legalStatusCode: 'synergies Arab matrices', legalStatusType: 'primary exploit Principal' };

  let legalStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/legal-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/legal-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/legal-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (legalStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/legal-statuses/${legalStatus.id}`,
      }).then(() => {
        legalStatus = undefined;
      });
    }
  });

  it('LegalStatuses menu should load LegalStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('legal-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LegalStatus').should('exist');
    cy.url().should('match', legalStatusPageUrlPattern);
  });

  describe('LegalStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(legalStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LegalStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/legal-status/new$'));
        cy.getEntityCreateUpdateHeading('LegalStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', legalStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/legal-statuses',
          body: legalStatusSample,
        }).then(({ body }) => {
          legalStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/legal-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [legalStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(legalStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LegalStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('legalStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', legalStatusPageUrlPattern);
      });

      it('edit button click should load edit LegalStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LegalStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', legalStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of LegalStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('legalStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', legalStatusPageUrlPattern);

        legalStatus = undefined;
      });
    });
  });

  describe('new LegalStatus page', () => {
    beforeEach(() => {
      cy.visit(`${legalStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LegalStatus');
    });

    it('should create an instance of LegalStatus', () => {
      cy.get(`[data-cy="legalStatusCode"]`).type('Som Kuna').should('have.value', 'Som Kuna');

      cy.get(`[data-cy="legalStatusType"]`).type('synthesize withdrawal').should('have.value', 'synthesize withdrawal');

      cy.get(`[data-cy="legalStatusDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        legalStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', legalStatusPageUrlPattern);
    });
  });
});
