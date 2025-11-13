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

describe('ReportRequisition e2e test', () => {
  const reportRequisitionPageUrl = '/report-requisition';
  const reportRequisitionPageUrlPattern = new RegExp('/report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportRequisitionSample = {
    reportName: 'Islands, mobile',
    reportRequestTime: '2022-06-15T12:06:48.425Z',
    reportPassword: 'overriding pixel',
    reportId: '85e6ffd1-340d-46cc-9af8-082bd886b6da',
  };

  let reportRequisition: any;
  //let reportTemplate: any;
  //let reportContentType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/report-templates',
      body: {"catalogueNumber":"Practical ability","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","reportFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","reportFileContentType":"unknown","compileReportFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","compileReportFileContentType":"unknown"},
    }).then(({ body }) => {
      reportTemplate = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/report-content-types',
      body: {"reportTypeName":"Unions Expressway Communications","reportFileExtension":"turquoise Metal reintermediate"},
    }).then(({ body }) => {
      reportContentType = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-requisitions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/report-templates', {
      statusCode: 200,
      body: [reportTemplate],
    });

    cy.intercept('GET', '/api/report-content-types', {
      statusCode: 200,
      body: [reportContentType],
    });

  });
   */

  afterEach(() => {
    if (reportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-requisitions/${reportRequisition.id}`,
      }).then(() => {
        reportRequisition = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (reportTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-templates/${reportTemplate.id}`,
      }).then(() => {
        reportTemplate = undefined;
      });
    }
    if (reportContentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-content-types/${reportContentType.id}`,
      }).then(() => {
        reportContentType = undefined;
      });
    }
  });
   */

  it('ReportRequisitions menu should load ReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportRequisition').should('exist');
    cy.url().should('match', reportRequisitionPageUrlPattern);
  });

  describe('ReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('ReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-requisitions',
  
          body: {
            ...reportRequisitionSample,
            reportTemplate: reportTemplate,
            reportContentType: reportContentType,
          },
        }).then(({ body }) => {
          reportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(reportRequisitionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit ReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportRequisitionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportRequisitionPageUrlPattern);

        reportRequisition = undefined;
      });
    });
  });

  describe('new ReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${reportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportRequisition');
    });

    it.skip('should create an instance of ReportRequisition', () => {
      cy.get(`[data-cy="reportName"]`).type('transform schemas Investor').should('have.value', 'transform schemas Investor');

      cy.get(`[data-cy="reportRequestTime"]`).type('2022-06-16T04:26').should('have.value', '2022-06-16T04:26');

      cy.get(`[data-cy="reportPassword"]`).type('Swedish').should('have.value', 'Swedish');

      cy.get(`[data-cy="reportStatus"]`).select('GENERATING');

      cy.get(`[data-cy="reportId"]`)
        .type('a1d6e9d8-387d-4fce-811a-edf82228c543')
        .invoke('val')
        .should('match', new RegExp('a1d6e9d8-387d-4fce-811a-edf82228c543'));

      cy.setFieldImageAsBytesOfEntity('reportFileAttachment', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportFileCheckSum"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('reportNotes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportTemplate"]`).select(1);
      cy.get(`[data-cy="reportContentType"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportRequisitionPageUrlPattern);
    });
  });
});
