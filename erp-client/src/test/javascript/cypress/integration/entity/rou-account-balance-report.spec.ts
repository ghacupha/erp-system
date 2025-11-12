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

describe('RouAccountBalanceReport e2e test', () => {
  const rouAccountBalanceReportPageUrl = '/rou-account-balance-report';
  const rouAccountBalanceReportPageUrlPattern = new RegExp('/rou-account-balance-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouAccountBalanceReportSample = { requestId: 'fb3cf659-44f8-4f16-ac85-fd79d3485b53' };

  let rouAccountBalanceReport: any;
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
      body: {"sequenceNumber":57296,"startDate":"2024-05-14","periodCode":"RAM"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-account-balance-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-account-balance-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-account-balance-reports/*').as('deleteEntityRequest');
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
    if (rouAccountBalanceReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-account-balance-reports/${rouAccountBalanceReport.id}`,
      }).then(() => {
        rouAccountBalanceReport = undefined;
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

  it('RouAccountBalanceReports menu should load RouAccountBalanceReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-account-balance-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouAccountBalanceReport').should('exist');
    cy.url().should('match', rouAccountBalanceReportPageUrlPattern);
  });

  describe('RouAccountBalanceReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouAccountBalanceReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouAccountBalanceReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-account-balance-report/new$'));
        cy.getEntityCreateUpdateHeading('RouAccountBalanceReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAccountBalanceReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-account-balance-reports',
  
          body: {
            ...rouAccountBalanceReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          rouAccountBalanceReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-account-balance-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouAccountBalanceReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouAccountBalanceReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouAccountBalanceReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouAccountBalanceReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouAccountBalanceReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAccountBalanceReportPageUrlPattern);
      });

      it('edit button click should load edit RouAccountBalanceReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouAccountBalanceReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAccountBalanceReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouAccountBalanceReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouAccountBalanceReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAccountBalanceReportPageUrlPattern);

        rouAccountBalanceReport = undefined;
      });
    });
  });

  describe('new RouAccountBalanceReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouAccountBalanceReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouAccountBalanceReport');
    });

    it.skip('should create an instance of RouAccountBalanceReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('1624fa8a-504e-431e-8d3a-b56c9413d9e3')
        .invoke('val')
        .should('match', new RegExp('1624fa8a-504e-431e-8d3a-b56c9413d9e3'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-12T19:41').should('have.value', '2024-03-12T19:41');

      cy.get(`[data-cy="reportIsCompiled"]`).should('not.be.checked');
      cy.get(`[data-cy="reportIsCompiled"]`).click().should('be.checked');

      cy.get(`[data-cy="fileChecksum"]`).type('bypassing').should('have.value', 'bypassing');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('bffbabf1-eae7-4f84-bf57-13ed8729aa0e')
        .invoke('val')
        .should('match', new RegExp('bffbabf1-eae7-4f84-bf57-13ed8729aa0e'));

      cy.get(`[data-cy="reportParameters"]`).type('cross-media').should('have.value', 'cross-media');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouAccountBalanceReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouAccountBalanceReportPageUrlPattern);
    });
  });
});
