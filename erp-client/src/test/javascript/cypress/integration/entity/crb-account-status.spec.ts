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

describe('CrbAccountStatus e2e test', () => {
  const crbAccountStatusPageUrl = '/crb-account-status';
  const crbAccountStatusPageUrlPattern = new RegExp('/crb-account-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbAccountStatusSample = { accountStatusTypeCode: 'sensor', accountStatusType: 'Borders' };

  let crbAccountStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-account-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-account-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-account-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbAccountStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-account-statuses/${crbAccountStatus.id}`,
      }).then(() => {
        crbAccountStatus = undefined;
      });
    }
  });

  it('CrbAccountStatuses menu should load CrbAccountStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-account-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbAccountStatus').should('exist');
    cy.url().should('match', crbAccountStatusPageUrlPattern);
  });

  describe('CrbAccountStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbAccountStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbAccountStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-account-status/new$'));
        cy.getEntityCreateUpdateHeading('CrbAccountStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-account-statuses',
          body: crbAccountStatusSample,
        }).then(({ body }) => {
          crbAccountStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-account-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbAccountStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbAccountStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbAccountStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbAccountStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountStatusPageUrlPattern);
      });

      it('edit button click should load edit CrbAccountStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbAccountStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbAccountStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbAccountStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountStatusPageUrlPattern);

        crbAccountStatus = undefined;
      });
    });
  });

  describe('new CrbAccountStatus page', () => {
    beforeEach(() => {
      cy.visit(`${crbAccountStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbAccountStatus');
    });

    it('should create an instance of CrbAccountStatus', () => {
      cy.get(`[data-cy="accountStatusTypeCode"]`).type('Cotton').should('have.value', 'Cotton');

      cy.get(`[data-cy="accountStatusType"]`).type('bypass').should('have.value', 'bypass');

      cy.get(`[data-cy="accountStatusTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbAccountStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbAccountStatusPageUrlPattern);
    });
  });
});
