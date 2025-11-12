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

describe('ReportDesign e2e test', () => {
  const reportDesignPageUrl = '/report-design';
  const reportDesignPageUrlPattern = new RegExp('/report-design(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportDesignSample = { catalogueNumber: '079df1b9-5cd2-4fc0-8f50-5e21c52645b4', designation: 'installation' };

  let reportDesign: any;
  //let securityClearance: any;
  //let applicationUser: any;
  //let dealer: any;
  //let systemModule: any;
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
      body: {"clearanceLevel":"Soft"},
    }).then(({ body }) => {
      securityClearance = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/application-users',
      body: {"designation":"0c7e338b-043c-4399-917c-1b1fb9c6c778","applicationIdentity":"District Louisiana"},
    }).then(({ body }) => {
      applicationUser = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"override state blue","taxNumber":"bus","identificationDocumentNumber":"Beauty generating","organizationName":"Concrete","department":"Regional","position":"Nebraska encoding","postalAddress":"Cambridgeshire Representative","physicalAddress":"experiences open-source Steel","accountName":"Home Loan Account","accountNumber":"interface","bankersName":"Chicken","bankersBranch":"back-end best-of-breed","bankersSwiftCode":"Cambridgeshire Senior Orchestrator","fileUploadToken":"HDD parsing cutting-edge","compilationToken":"Practical","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"withdrawal"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/system-modules',
      body: {"moduleName":"feed Configuration"},
    }).then(({ body }) => {
      systemModule = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/algorithms',
      body: {"name":"Centralized Litas"},
    }).then(({ body }) => {
      algorithm = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/report-designs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-designs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-designs/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/universally-unique-mappings', {
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

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/system-modules', {
      statusCode: 200,
      body: [systemModule],
    });

    cy.intercept('GET', '/api/algorithms', {
      statusCode: 200,
      body: [algorithm],
    });

  });
   */

  afterEach(() => {
    if (reportDesign) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-designs/${reportDesign.id}`,
      }).then(() => {
        reportDesign = undefined;
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

  it('ReportDesigns menu should load ReportDesigns page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-design');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportDesign').should('exist');
    cy.url().should('match', reportDesignPageUrlPattern);
  });

  describe('ReportDesign page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportDesignPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportDesign page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/report-design/new$'));
        cy.getEntityCreateUpdateHeading('ReportDesign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDesignPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-designs',
  
          body: {
            ...reportDesignSample,
            securityClearance: securityClearance,
            reportDesigner: applicationUser,
            organization: dealer,
            department: dealer,
            systemModule: systemModule,
            fileCheckSumAlgorithm: algorithm,
          },
        }).then(({ body }) => {
          reportDesign = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-designs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportDesign],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportDesignPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(reportDesignPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ReportDesign page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportDesign');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDesignPageUrlPattern);
      });

      it('edit button click should load edit ReportDesign page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportDesign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDesignPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ReportDesign', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportDesign').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportDesignPageUrlPattern);

        reportDesign = undefined;
      });
    });
  });

  describe('new ReportDesign page', () => {
    beforeEach(() => {
      cy.visit(`${reportDesignPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportDesign');
    });

    it.skip('should create an instance of ReportDesign', () => {
      cy.get(`[data-cy="catalogueNumber"]`)
        .type('e7be8cc6-8b3f-45d8-8460-83fd4f7dd916')
        .invoke('val')
        .should('match', new RegExp('e7be8cc6-8b3f-45d8-8460-83fd4f7dd916'));

      cy.get(`[data-cy="designation"]`).type('Managed').should('have.value', 'Managed');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('notes', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportFileChecksum"]`).type('repurpose actuating').should('have.value', 'repurpose actuating');

      cy.get(`[data-cy="securityClearance"]`).select(1);
      cy.get(`[data-cy="reportDesigner"]`).select(1);
      cy.get(`[data-cy="organization"]`).select(1);
      cy.get(`[data-cy="department"]`).select(1);
      cy.get(`[data-cy="systemModule"]`).select(1);
      cy.get(`[data-cy="fileCheckSumAlgorithm"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportDesign = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportDesignPageUrlPattern);
    });
  });
});
