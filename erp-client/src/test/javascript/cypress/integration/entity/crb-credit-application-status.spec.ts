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

describe('CrbCreditApplicationStatus e2e test', () => {
  const crbCreditApplicationStatusPageUrl = '/crb-credit-application-status';
  const crbCreditApplicationStatusPageUrlPattern = new RegExp('/crb-credit-application-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbCreditApplicationStatusSample = {
    crbCreditApplicationStatusTypeCode: 'Kids transmitting',
    crbCreditApplicationStatusType: 'Soft Intelligent',
  };

  let crbCreditApplicationStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-credit-application-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-credit-application-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-credit-application-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbCreditApplicationStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-credit-application-statuses/${crbCreditApplicationStatus.id}`,
      }).then(() => {
        crbCreditApplicationStatus = undefined;
      });
    }
  });

  it('CrbCreditApplicationStatuses menu should load CrbCreditApplicationStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-credit-application-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbCreditApplicationStatus').should('exist');
    cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);
  });

  describe('CrbCreditApplicationStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbCreditApplicationStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbCreditApplicationStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-credit-application-status/new$'));
        cy.getEntityCreateUpdateHeading('CrbCreditApplicationStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-credit-application-statuses',
          body: crbCreditApplicationStatusSample,
        }).then(({ body }) => {
          crbCreditApplicationStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-credit-application-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbCreditApplicationStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbCreditApplicationStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbCreditApplicationStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbCreditApplicationStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);
      });

      it('edit button click should load edit CrbCreditApplicationStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbCreditApplicationStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbCreditApplicationStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbCreditApplicationStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);

        crbCreditApplicationStatus = undefined;
      });
    });
  });

  describe('new CrbCreditApplicationStatus page', () => {
    beforeEach(() => {
      cy.visit(`${crbCreditApplicationStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbCreditApplicationStatus');
    });

    it('should create an instance of CrbCreditApplicationStatus', () => {
      cy.get(`[data-cy="crbCreditApplicationStatusTypeCode"]`).type('efficient Concrete').should('have.value', 'efficient Concrete');

      cy.get(`[data-cy="crbCreditApplicationStatusType"]`).type('Reduced payment drive').should('have.value', 'Reduced payment drive');

      cy.get(`[data-cy="crbCreditApplicationStatusDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbCreditApplicationStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbCreditApplicationStatusPageUrlPattern);
    });
  });
});
