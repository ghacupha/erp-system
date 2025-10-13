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

describe('WorkInProgressOutstandingReportRequisition e2e test', () => {
  const workInProgressOutstandingReportRequisitionPageUrl = '/work-in-progress-outstanding-report-requisition';
  const workInProgressOutstandingReportRequisitionPageUrlPattern = new RegExp('/work-in-progress-outstanding-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const workInProgressOutstandingReportRequisitionSample = {
    requestId: '21f02107-92bc-4ea0-9348-af7cdd3f2d58',
    reportDate: '2024-05-08',
    timeOfRequisition: '2024-05-08T13:23:52.512Z',
  };

  let workInProgressOutstandingReportRequisition: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/work-in-progress-outstanding-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/work-in-progress-outstanding-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/work-in-progress-outstanding-report-requisitions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (workInProgressOutstandingReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/work-in-progress-outstanding-report-requisitions/${workInProgressOutstandingReportRequisition.id}`,
      }).then(() => {
        workInProgressOutstandingReportRequisition = undefined;
      });
    }
  });

  it('WorkInProgressOutstandingReportRequisitions menu should load WorkInProgressOutstandingReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('work-in-progress-outstanding-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkInProgressOutstandingReportRequisition').should('exist');
    cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);
  });

  describe('WorkInProgressOutstandingReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workInProgressOutstandingReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WorkInProgressOutstandingReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/work-in-progress-outstanding-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('WorkInProgressOutstandingReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/work-in-progress-outstanding-report-requisitions',
          body: workInProgressOutstandingReportRequisitionSample,
        }).then(({ body }) => {
          workInProgressOutstandingReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/work-in-progress-outstanding-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [workInProgressOutstandingReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(workInProgressOutstandingReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WorkInProgressOutstandingReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workInProgressOutstandingReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit WorkInProgressOutstandingReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorkInProgressOutstandingReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of WorkInProgressOutstandingReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('workInProgressOutstandingReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);

        workInProgressOutstandingReportRequisition = undefined;
      });
    });
  });

  describe('new WorkInProgressOutstandingReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${workInProgressOutstandingReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WorkInProgressOutstandingReportRequisition');
    });

    it('should create an instance of WorkInProgressOutstandingReportRequisition', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('f103607a-228d-4a52-b901-de39e8ec37dd')
        .invoke('val')
        .should('match', new RegExp('f103607a-228d-4a52-b901-de39e8ec37dd'));

      cy.get(`[data-cy="reportDate"]`).type('2024-05-09').should('have.value', '2024-05-09');

      cy.get(`[data-cy="timeOfRequisition"]`).type('2024-05-08T14:02').should('have.value', '2024-05-08T14:02');

      cy.get(`[data-cy="fileChecksum"]`).type('Avon pixel').should('have.value', 'Avon pixel');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('baf536a0-92ee-4e9a-94db-58e98fb329af')
        .invoke('val')
        .should('match', new RegExp('baf536a0-92ee-4e9a-94db-58e98fb329af'));

      cy.get(`[data-cy="reportParameters"]`).type('Mississippi').should('have.value', 'Mississippi');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        workInProgressOutstandingReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', workInProgressOutstandingReportRequisitionPageUrlPattern);
    });
  });
});
