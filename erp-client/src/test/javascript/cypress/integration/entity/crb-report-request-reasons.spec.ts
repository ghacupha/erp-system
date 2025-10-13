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

describe('CrbReportRequestReasons e2e test', () => {
  const crbReportRequestReasonsPageUrl = '/crb-report-request-reasons';
  const crbReportRequestReasonsPageUrlPattern = new RegExp('/crb-report-request-reasons(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbReportRequestReasonsSample = {
    creditReportRequestReasonTypeCode: '24/365',
    creditReportRequestReasonType: 'efficient payment multi-byte',
  };

  let crbReportRequestReasons: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-report-request-reasons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-report-request-reasons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-report-request-reasons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbReportRequestReasons) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-report-request-reasons/${crbReportRequestReasons.id}`,
      }).then(() => {
        crbReportRequestReasons = undefined;
      });
    }
  });

  it('CrbReportRequestReasons menu should load CrbReportRequestReasons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-report-request-reasons');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbReportRequestReasons').should('exist');
    cy.url().should('match', crbReportRequestReasonsPageUrlPattern);
  });

  describe('CrbReportRequestReasons page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbReportRequestReasonsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbReportRequestReasons page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-report-request-reasons/new$'));
        cy.getEntityCreateUpdateHeading('CrbReportRequestReasons');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportRequestReasonsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-report-request-reasons',
          body: crbReportRequestReasonsSample,
        }).then(({ body }) => {
          crbReportRequestReasons = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-report-request-reasons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbReportRequestReasons],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbReportRequestReasonsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbReportRequestReasons page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbReportRequestReasons');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportRequestReasonsPageUrlPattern);
      });

      it('edit button click should load edit CrbReportRequestReasons page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbReportRequestReasons');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportRequestReasonsPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbReportRequestReasons', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbReportRequestReasons').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportRequestReasonsPageUrlPattern);

        crbReportRequestReasons = undefined;
      });
    });
  });

  describe('new CrbReportRequestReasons page', () => {
    beforeEach(() => {
      cy.visit(`${crbReportRequestReasonsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbReportRequestReasons');
    });

    it('should create an instance of CrbReportRequestReasons', () => {
      cy.get(`[data-cy="creditReportRequestReasonTypeCode"]`).type('grey Steel').should('have.value', 'grey Steel');

      cy.get(`[data-cy="creditReportRequestReasonType"]`).type('Berkshire user-centric').should('have.value', 'Berkshire user-centric');

      cy.get(`[data-cy="creditReportRequestDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbReportRequestReasons = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbReportRequestReasonsPageUrlPattern);
    });
  });
});
