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

describe('RouDepreciationPostingReport e2e test', () => {
  const rouDepreciationPostingReportPageUrl = '/rou-depreciation-posting-report';
  const rouDepreciationPostingReportPageUrlPattern = new RegExp('/rou-depreciation-posting-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouDepreciationPostingReportSample = { requestId: '0ba4c111-59d1-4b99-a7a9-789331e44d74' };

  let rouDepreciationPostingReport: any;
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
      body: {"sequenceNumber":32951,"endDate":"2024-05-14","periodCode":"Direct"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-depreciation-posting-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-depreciation-posting-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-depreciation-posting-reports/*').as('deleteEntityRequest');
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
    if (rouDepreciationPostingReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-depreciation-posting-reports/${rouDepreciationPostingReport.id}`,
      }).then(() => {
        rouDepreciationPostingReport = undefined;
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

  it('RouDepreciationPostingReports menu should load RouDepreciationPostingReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-depreciation-posting-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouDepreciationPostingReport').should('exist');
    cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);
  });

  describe('RouDepreciationPostingReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouDepreciationPostingReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouDepreciationPostingReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-depreciation-posting-report/new$'));
        cy.getEntityCreateUpdateHeading('RouDepreciationPostingReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-depreciation-posting-reports',
  
          body: {
            ...rouDepreciationPostingReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          rouDepreciationPostingReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-depreciation-posting-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouDepreciationPostingReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouDepreciationPostingReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouDepreciationPostingReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouDepreciationPostingReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouDepreciationPostingReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);
      });

      it('edit button click should load edit RouDepreciationPostingReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouDepreciationPostingReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouDepreciationPostingReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouDepreciationPostingReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);

        rouDepreciationPostingReport = undefined;
      });
    });
  });

  describe('new RouDepreciationPostingReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouDepreciationPostingReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouDepreciationPostingReport');
    });

    it.skip('should create an instance of RouDepreciationPostingReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('4bb373fb-5405-4a5f-b55c-43bf88bc64d9')
        .invoke('val')
        .should('match', new RegExp('4bb373fb-5405-4a5f-b55c-43bf88bc64d9'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-13T04:17').should('have.value', '2024-03-13T04:17');

      cy.get(`[data-cy="reportIsCompiled"]`).should('not.be.checked');
      cy.get(`[data-cy="reportIsCompiled"]`).click().should('be.checked');

      cy.get(`[data-cy="fileChecksum"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('3e63833f-3efb-4d39-965e-e74463b63e57')
        .invoke('val')
        .should('match', new RegExp('3e63833f-3efb-4d39-965e-e74463b63e57'));

      cy.get(`[data-cy="reportParameters"]`).type('solid').should('have.value', 'solid');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouDepreciationPostingReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouDepreciationPostingReportPageUrlPattern);
    });
  });
});
