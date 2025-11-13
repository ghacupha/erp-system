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

describe('ExcelReportExport e2e test', () => {
  const excelReportExportPageUrl = '/excel-report-export';
  const excelReportExportPageUrlPattern = new RegExp('/excel-report-export(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const excelReportExportSample = {
    reportName: 'Solutions users salmon',
    reportPassword: 'Sports',
    reportTimeStamp: '2022-06-29T09:24:23.768Z',
    reportId: '5cccdd91-cea4-4fca-9ef3-989a466fa0c5',
  };

  let excelReportExport: any;
  //let securityClearance: any;
  //let applicationUser: any;
  //let dealer: any;
  //let systemModule: any;
  //let reportDesign: any;
  //let algorithm: any;

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
      url: '/api/security-clearances',
      body: {"clearanceLevel":"AI Gloves Berkshire"},
    }).then(({ body }) => {
      securityClearance = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/application-users',
      body: {"designation":"0d3353a6-1922-480f-9cd0-d6c8dba5d88f","applicationIdentity":"Dynamic Granite"},
    }).then(({ body }) => {
      applicationUser = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"payment EXE online","taxNumber":"unleash overriding","identificationDocumentNumber":"Optional Music Operations","organizationName":"green front-end","department":"connecting Dakota Customizable","position":"Sudan Cotton Legacy","postalAddress":"Universal","physicalAddress":"Chips","accountName":"Investment Account","accountNumber":"Awesome Analyst Shirt","bankersName":"Identity port Decentralized","bankersBranch":"Polarised","bankersSwiftCode":"Fresh","fileUploadToken":"Incredible Music Games","compilationToken":"Concrete Maine Loan","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"Gloves Investor"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/system-modules',
      body: {"moduleName":"Fish invoice driver"},
    }).then(({ body }) => {
      systemModule = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/report-designs',
      body: {"catalogueNumber":"bdca0b3d-2821-408e-8bbc-52a9b7ce5986","designation":"invoice","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","reportFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","reportFileContentType":"unknown","reportFileChecksum":"Mississippi"},
    }).then(({ body }) => {
      reportDesign = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/algorithms',
      body: {"name":"quantify"},
    }).then(({ body }) => {
      algorithm = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/excel-report-exports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/excel-report-exports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/excel-report-exports/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/report-statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/security-clearances', {
      statusCode: 200,
      body: [securityClearance],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [applicationUser],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/system-modules', {
      statusCode: 200,
      body: [systemModule],
    });

    cy.intercept('GET', '/api/report-designs', {
      statusCode: 200,
      body: [reportDesign],
    });

    cy.intercept('GET', '/api/algorithms', {
      statusCode: 200,
      body: [algorithm],
    });

  });
   */

  afterEach(() => {
    if (excelReportExport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/excel-report-exports/${excelReportExport.id}`,
      }).then(() => {
        excelReportExport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (securityClearance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-clearances/${securityClearance.id}`,
      }).then(() => {
        securityClearance = undefined;
      });
    }
    if (applicationUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-users/${applicationUser.id}`,
      }).then(() => {
        applicationUser = undefined;
      });
    }
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
    if (systemModule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-modules/${systemModule.id}`,
      }).then(() => {
        systemModule = undefined;
      });
    }
    if (reportDesign) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-designs/${reportDesign.id}`,
      }).then(() => {
        reportDesign = undefined;
      });
    }
    if (algorithm) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/algorithms/${algorithm.id}`,
      }).then(() => {
        algorithm = undefined;
      });
    }
  });
   */

  it('ExcelReportExports menu should load ExcelReportExports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('excel-report-export');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExcelReportExport').should('exist');
    cy.url().should('match', excelReportExportPageUrlPattern);
  });

  describe('ExcelReportExport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(excelReportExportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExcelReportExport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/excel-report-export/new$'));
        cy.getEntityCreateUpdateHeading('ExcelReportExport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', excelReportExportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/excel-report-exports',
  
          body: {
            ...excelReportExportSample,
            securityClearance: securityClearance,
            reportCreator: applicationUser,
            organization: dealer,
            department: dealer,
            systemModule: systemModule,
            reportDesign: reportDesign,
            fileCheckSumAlgorithm: algorithm,
          },
        }).then(({ body }) => {
          excelReportExport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/excel-report-exports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [excelReportExport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(excelReportExportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(excelReportExportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ExcelReportExport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('excelReportExport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', excelReportExportPageUrlPattern);
      });

      it('edit button click should load edit ExcelReportExport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExcelReportExport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', excelReportExportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ExcelReportExport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('excelReportExport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', excelReportExportPageUrlPattern);

        excelReportExport = undefined;
      });
    });
  });

  describe('new ExcelReportExport page', () => {
    beforeEach(() => {
      cy.visit(`${excelReportExportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ExcelReportExport');
    });

    it.skip('should create an instance of ExcelReportExport', () => {
      cy.get(`[data-cy="reportName"]`).type('program').should('have.value', 'program');

      cy.get(`[data-cy="reportPassword"]`).type('withdrawal white').should('have.value', 'withdrawal white');

      cy.setFieldImageAsBytesOfEntity('reportNotes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileCheckSum"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportTimeStamp"]`).type('2022-06-29T09:02').should('have.value', '2022-06-29T09:02');

      cy.get(`[data-cy="reportId"]`)
        .type('2fd42686-0b76-4901-b2d1-769a24709656')
        .invoke('val')
        .should('match', new RegExp('2fd42686-0b76-4901-b2d1-769a24709656'));

      cy.get(`[data-cy="securityClearance"]`).select(1);
      cy.get(`[data-cy="reportCreator"]`).select(1);
      cy.get(`[data-cy="organization"]`).select(1);
      cy.get(`[data-cy="department"]`).select(1);
      cy.get(`[data-cy="systemModule"]`).select(1);
      cy.get(`[data-cy="reportDesign"]`).select(1);
      cy.get(`[data-cy="fileCheckSumAlgorithm"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        excelReportExport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', excelReportExportPageUrlPattern);
    });
  });
});
