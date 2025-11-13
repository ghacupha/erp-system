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

describe('RouAssetNBVReport e2e test', () => {
  const rouAssetNBVReportPageUrl = '/rou-asset-nbv-report';
  const rouAssetNBVReportPageUrlPattern = new RegExp('/rou-asset-nbv-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouAssetNBVReportSample = { requestId: '21d449e7-c61f-490f-a6f2-8c546ce7c8b8' };

  let rouAssetNBVReport: any;
  //let leasePeriod: any;

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
      url: '/api/lease-periods',
      body: {"sequenceNumber":60312,"startDate":"2024-05-13","endDate":"2024-05-13","periodCode":"reinvent payment Granite"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-asset-nbv-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-asset-nbv-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-asset-nbv-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/lease-periods', {
      statusCode: 200,
      body: [leasePeriod],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (rouAssetNBVReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-asset-nbv-reports/${rouAssetNBVReport.id}`,
      }).then(() => {
        rouAssetNBVReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (leasePeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-periods/${leasePeriod.id}`,
      }).then(() => {
        leasePeriod = undefined;
      });
    }
  });
   */

  it('RouAssetNBVReports menu should load RouAssetNBVReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-asset-nbv-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouAssetNBVReport').should('exist');
    cy.url().should('match', rouAssetNBVReportPageUrlPattern);
  });

  describe('RouAssetNBVReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouAssetNBVReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouAssetNBVReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-asset-nbv-report/new$'));
        cy.getEntityCreateUpdateHeading('RouAssetNBVReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetNBVReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-asset-nbv-reports',
  
          body: {
            ...rouAssetNBVReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          rouAssetNBVReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-asset-nbv-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouAssetNBVReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouAssetNBVReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouAssetNBVReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouAssetNBVReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouAssetNBVReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetNBVReportPageUrlPattern);
      });

      it('edit button click should load edit RouAssetNBVReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouAssetNBVReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetNBVReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouAssetNBVReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouAssetNBVReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetNBVReportPageUrlPattern);

        rouAssetNBVReport = undefined;
      });
    });
  });

  describe('new RouAssetNBVReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouAssetNBVReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouAssetNBVReport');
    });

    it.skip('should create an instance of RouAssetNBVReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('c0e49ce5-5e30-43a2-884e-95f4f6f8a008')
        .invoke('val')
        .should('match', new RegExp('c0e49ce5-5e30-43a2-884e-95f4f6f8a008'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-12T18:53').should('have.value', '2024-03-12T18:53');

      cy.get(`[data-cy="reportIsCompiled"]`).should('not.be.checked');
      cy.get(`[data-cy="reportIsCompiled"]`).click().should('be.checked');

      cy.get(`[data-cy="fileChecksum"]`).type('Music Dynamic').should('have.value', 'Music Dynamic');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('4cdfa635-5f4f-4a7f-b2ba-54bbbd5258a2')
        .invoke('val')
        .should('match', new RegExp('4cdfa635-5f4f-4a7f-b2ba-54bbbd5258a2'));

      cy.get(`[data-cy="reportParameters"]`).type('invoice convergence').should('have.value', 'invoice convergence');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouAssetNBVReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouAssetNBVReportPageUrlPattern);
    });
  });
});
