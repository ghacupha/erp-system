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

describe('CrbSubscriptionStatusTypeCode e2e test', () => {
  const crbSubscriptionStatusTypeCodePageUrl = '/crb-subscription-status-type-code';
  const crbSubscriptionStatusTypeCodePageUrlPattern = new RegExp('/crb-subscription-status-type-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbSubscriptionStatusTypeCodeSample = {
    subscriptionStatusTypeCode: 'digital Configuration hacking',
    subscriptionStatusType: 'Chicken Avon Assurance',
  };

  let crbSubscriptionStatusTypeCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-subscription-status-type-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-subscription-status-type-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-subscription-status-type-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbSubscriptionStatusTypeCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-subscription-status-type-codes/${crbSubscriptionStatusTypeCode.id}`,
      }).then(() => {
        crbSubscriptionStatusTypeCode = undefined;
      });
    }
  });

  it('CrbSubscriptionStatusTypeCodes menu should load CrbSubscriptionStatusTypeCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-subscription-status-type-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbSubscriptionStatusTypeCode').should('exist');
    cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);
  });

  describe('CrbSubscriptionStatusTypeCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbSubscriptionStatusTypeCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbSubscriptionStatusTypeCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-subscription-status-type-code/new$'));
        cy.getEntityCreateUpdateHeading('CrbSubscriptionStatusTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-subscription-status-type-codes',
          body: crbSubscriptionStatusTypeCodeSample,
        }).then(({ body }) => {
          crbSubscriptionStatusTypeCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-subscription-status-type-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbSubscriptionStatusTypeCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbSubscriptionStatusTypeCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbSubscriptionStatusTypeCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbSubscriptionStatusTypeCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);
      });

      it('edit button click should load edit CrbSubscriptionStatusTypeCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbSubscriptionStatusTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbSubscriptionStatusTypeCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbSubscriptionStatusTypeCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);

        crbSubscriptionStatusTypeCode = undefined;
      });
    });
  });

  describe('new CrbSubscriptionStatusTypeCode page', () => {
    beforeEach(() => {
      cy.visit(`${crbSubscriptionStatusTypeCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbSubscriptionStatusTypeCode');
    });

    it('should create an instance of CrbSubscriptionStatusTypeCode', () => {
      cy.get(`[data-cy="subscriptionStatusTypeCode"]`).type('online overriding olive').should('have.value', 'online overriding olive');

      cy.get(`[data-cy="subscriptionStatusType"]`).type('Total portals Stream').should('have.value', 'Total portals Stream');

      cy.get(`[data-cy="subscriptionStatusTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbSubscriptionStatusTypeCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbSubscriptionStatusTypeCodePageUrlPattern);
    });
  });
});
