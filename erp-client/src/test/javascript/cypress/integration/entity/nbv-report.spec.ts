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

describe('NbvReport e2e test', () => {
  const nbvReportPageUrl = '/nbv-report';
  const nbvReportPageUrlPattern = new RegExp('/nbv-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const nbvReportSample = { reportName: 'Ball Incredible face', timeOfReportRequest: '2024-02-19T06:39:25.680Z' };

  let nbvReport: any;
  //let depreciationPeriod: any;

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
      url: '/api/depreciation-periods',
      body: {"startDate":"2023-07-04","endDate":"2023-07-03","depreciationPeriodStatus":"CLOSED","periodCode":"Berkshire 1080p","processLocked":true},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/nbv-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nbv-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nbv-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [depreciationPeriod],
    });

    cy.intercept('GET', '/api/service-outlets', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-categories', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (nbvReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nbv-reports/${nbvReport.id}`,
      }).then(() => {
        nbvReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (depreciationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-periods/${depreciationPeriod.id}`,
      }).then(() => {
        depreciationPeriod = undefined;
      });
    }
  });
   */

  it('NbvReports menu should load NbvReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nbv-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NbvReport').should('exist');
    cy.url().should('match', nbvReportPageUrlPattern);
  });

  describe('NbvReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(nbvReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NbvReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/nbv-report/new$'));
        cy.getEntityCreateUpdateHeading('NbvReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nbv-reports',
  
          body: {
            ...nbvReportSample,
            depreciationPeriod: depreciationPeriod,
          },
        }).then(({ body }) => {
          nbvReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nbv-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [nbvReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(nbvReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(nbvReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details NbvReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('nbvReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvReportPageUrlPattern);
      });

      it('edit button click should load edit NbvReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NbvReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of NbvReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('nbvReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvReportPageUrlPattern);

        nbvReport = undefined;
      });
    });
  });

  describe('new NbvReport page', () => {
    beforeEach(() => {
      cy.visit(`${nbvReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('NbvReport');
    });

    it.skip('should create an instance of NbvReport', () => {
      cy.get(`[data-cy="reportName"]`).type('impactful').should('have.value', 'impactful');

      cy.get(`[data-cy="timeOfReportRequest"]`).type('2024-02-18T17:10').should('have.value', '2024-02-18T17:10');

      cy.get(`[data-cy="fileChecksum"]`).type('Overpass').should('have.value', 'Overpass');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('1e2899bd-e857-447e-9244-340da4862777')
        .invoke('val')
        .should('match', new RegExp('1e2899bd-e857-447e-9244-340da4862777'));

      cy.get(`[data-cy="reportParameters"]`).type('mesh Baby').should('have.value', 'mesh Baby');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="depreciationPeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        nbvReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', nbvReportPageUrlPattern);
    });
  });
});
