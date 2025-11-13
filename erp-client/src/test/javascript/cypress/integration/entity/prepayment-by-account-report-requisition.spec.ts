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

describe('PrepaymentByAccountReportRequisition e2e test', () => {
  const prepaymentByAccountReportRequisitionPageUrl = '/prepayment-by-account-report-requisition';
  const prepaymentByAccountReportRequisitionPageUrlPattern = new RegExp('/prepayment-by-account-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentByAccountReportRequisitionSample = { timeOfRequisition: '2024-05-05T22:37:05.988Z', reportDate: '2024-05-05' };

  let prepaymentByAccountReportRequisition: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-by-account-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-by-account-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-by-account-report-requisitions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentByAccountReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-by-account-report-requisitions/${prepaymentByAccountReportRequisition.id}`,
      }).then(() => {
        prepaymentByAccountReportRequisition = undefined;
      });
    }
  });

  it('PrepaymentByAccountReportRequisitions menu should load PrepaymentByAccountReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-by-account-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentByAccountReportRequisition').should('exist');
    cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);
  });

  describe('PrepaymentByAccountReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(prepaymentByAccountReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrepaymentByAccountReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/prepayment-by-account-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('PrepaymentByAccountReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/prepayment-by-account-report-requisitions',
          body: prepaymentByAccountReportRequisitionSample,
        }).then(({ body }) => {
          prepaymentByAccountReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/prepayment-by-account-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [prepaymentByAccountReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(prepaymentByAccountReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrepaymentByAccountReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentByAccountReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit PrepaymentByAccountReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrepaymentByAccountReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of PrepaymentByAccountReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('prepaymentByAccountReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);

        prepaymentByAccountReportRequisition = undefined;
      });
    });
  });

  describe('new PrepaymentByAccountReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${prepaymentByAccountReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PrepaymentByAccountReportRequisition');
    });

    it('should create an instance of PrepaymentByAccountReportRequisition', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('ca814bd3-bd36-4b95-a459-9d66028a4f74')
        .invoke('val')
        .should('match', new RegExp('ca814bd3-bd36-4b95-a459-9d66028a4f74'));

      cy.get(`[data-cy="timeOfRequisition"]`).type('2024-05-05T16:49').should('have.value', '2024-05-05T16:49');

      cy.get(`[data-cy="fileChecksum"]`).type('Manager RAM IB').should('have.value', 'Manager RAM IB');

      cy.get(`[data-cy="filename"]`)
        .type('8f9cf539-fea2-44bb-8b13-8f0f9ac174da')
        .invoke('val')
        .should('match', new RegExp('8f9cf539-fea2-44bb-8b13-8f0f9ac174da'));

      cy.get(`[data-cy="reportParameters"]`).type('optimize challenge viral').should('have.value', 'optimize challenge viral');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportDate"]`).type('2024-05-05').should('have.value', '2024-05-05');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        prepaymentByAccountReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', prepaymentByAccountReportRequisitionPageUrlPattern);
    });
  });
});
