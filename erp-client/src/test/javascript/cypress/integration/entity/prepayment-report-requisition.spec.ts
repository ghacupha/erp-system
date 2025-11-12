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

describe('PrepaymentReportRequisition e2e test', () => {
  const prepaymentReportRequisitionPageUrl = '/prepayment-report-requisition';
  const prepaymentReportRequisitionPageUrlPattern = new RegExp('/prepayment-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentReportRequisitionSample = {
    reportName: 'Account visualize function',
    reportDate: '2024-04-29',
    timeOfRequisition: '2024-04-29T23:08:39.274Z',
  };

  let prepaymentReportRequisition: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-report-requisitions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-report-requisitions/${prepaymentReportRequisition.id}`,
      }).then(() => {
        prepaymentReportRequisition = undefined;
      });
    }
  });

  it('PrepaymentReportRequisitions menu should load PrepaymentReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentReportRequisition').should('exist');
    cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);
  });

  describe('PrepaymentReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(prepaymentReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrepaymentReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/prepayment-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('PrepaymentReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/prepayment-report-requisitions',
          body: prepaymentReportRequisitionSample,
        }).then(({ body }) => {
          prepaymentReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/prepayment-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [prepaymentReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(prepaymentReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrepaymentReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit PrepaymentReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrepaymentReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of PrepaymentReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('prepaymentReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);

        prepaymentReportRequisition = undefined;
      });
    });
  });

  describe('new PrepaymentReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${prepaymentReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PrepaymentReportRequisition');
    });

    it('should create an instance of PrepaymentReportRequisition', () => {
      cy.get(`[data-cy="reportName"]`).type('grid-enabled capacitor Devolved').should('have.value', 'grid-enabled capacitor Devolved');

      cy.get(`[data-cy="reportDate"]`).type('2024-04-30').should('have.value', '2024-04-30');

      cy.get(`[data-cy="timeOfRequisition"]`).type('2024-04-29T14:08').should('have.value', '2024-04-29T14:08');

      cy.get(`[data-cy="fileChecksum"]`).type('input quantify transmit').should('have.value', 'input quantify transmit');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('cd1e62f1-608b-483d-a836-b42d7f5a7868')
        .invoke('val')
        .should('match', new RegExp('cd1e62f1-608b-483d-a836-b42d7f5a7868'));

      cy.get(`[data-cy="reportParameters"]`).type('synthesize redundant Corporate').should('have.value', 'synthesize redundant Corporate');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        prepaymentReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', prepaymentReportRequisitionPageUrlPattern);
    });
  });
});
