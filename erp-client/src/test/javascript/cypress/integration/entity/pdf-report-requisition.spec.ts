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

describe('PdfReportRequisition e2e test', () => {
  const pdfReportRequisitionPageUrl = '/pdf-report-requisition';
  const pdfReportRequisitionPageUrlPattern = new RegExp('/pdf-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const pdfReportRequisitionSample = {
    reportName: 'Krone',
    userPassword: 'eyeballs Iraqi Hill',
    ownerPassword: 'Director',
    reportId: '375a56ae-e3fe-4414-ad38-500bc10d6264',
  };

  let pdfReportRequisition: any;
  let reportTemplate: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/report-templates',
      body: {
        catalogueNumber: 'vortals',
        description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        notesContentType: 'unknown',
        reportFile: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        reportFileContentType: 'unknown',
        compileReportFile: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        compileReportFileContentType: 'unknown',
      },
    }).then(({ body }) => {
      reportTemplate = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pdf-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pdf-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pdf-report-requisitions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/report-templates', {
      statusCode: 200,
      body: [reportTemplate],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (pdfReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pdf-report-requisitions/${pdfReportRequisition.id}`,
      }).then(() => {
        pdfReportRequisition = undefined;
      });
    }
  });

  afterEach(() => {
    if (reportTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-templates/${reportTemplate.id}`,
      }).then(() => {
        reportTemplate = undefined;
      });
    }
  });

  it('PdfReportRequisitions menu should load PdfReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pdf-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PdfReportRequisition').should('exist');
    cy.url().should('match', pdfReportRequisitionPageUrlPattern);
  });

  describe('PdfReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pdfReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PdfReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/pdf-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('PdfReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', pdfReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pdf-report-requisitions',

          body: {
            ...pdfReportRequisitionSample,
            reportTemplate: reportTemplate,
          },
        }).then(({ body }) => {
          pdfReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pdf-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [pdfReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(pdfReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PdfReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pdfReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', pdfReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit PdfReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PdfReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', pdfReportRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of PdfReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pdfReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', pdfReportRequisitionPageUrlPattern);

        pdfReportRequisition = undefined;
      });
    });
  });

  describe('new PdfReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${pdfReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PdfReportRequisition');
    });

    it('should create an instance of PdfReportRequisition', () => {
      cy.get(`[data-cy="reportName"]`).type('Response technologies').should('have.value', 'Response technologies');

      cy.get(`[data-cy="reportDate"]`).type('2022-05-20').should('have.value', '2022-05-20');

      cy.get(`[data-cy="userPassword"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="ownerPassword"]`).type('violet Consultant Movies').should('have.value', 'violet Consultant Movies');

      cy.get(`[data-cy="reportFileChecksum"]`).type('drive').should('have.value', 'drive');

      cy.get(`[data-cy="reportStatus"]`).select('FAILED');

      cy.get(`[data-cy="reportId"]`)
        .type('b6445b91-906e-459d-81c5-ad69b3d1d0a1')
        .invoke('val')
        .should('match', new RegExp('b6445b91-906e-459d-81c5-ad69b3d1d0a1'));

      cy.get(`[data-cy="reportTemplate"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        pdfReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', pdfReportRequisitionPageUrlPattern);
    });
  });
});
