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

describe('LeaseLiabilityPostingReport e2e test', () => {
  const leaseLiabilityPostingReportPageUrl = '/lease-liability-posting-report';
  const leaseLiabilityPostingReportPageUrlPattern = new RegExp('/lease-liability-posting-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityPostingReportSample = { requestId: 'd9336409-d933-4aec-9dfe-a97cb2ca4dcd' };

  let leaseLiabilityPostingReport: any;
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
      body: {"sequenceNumber":11507,"periodCode":"Loan"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-posting-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-posting-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-posting-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lease-periods', {
      statusCode: 200,
      body: [leasePeriod],
    });

  });
   */

  afterEach(() => {
    if (leaseLiabilityPostingReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-posting-reports/${leaseLiabilityPostingReport.id}`,
      }).then(() => {
        leaseLiabilityPostingReport = undefined;
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

  it('LeaseLiabilityPostingReports menu should load LeaseLiabilityPostingReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-posting-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityPostingReport').should('exist');
    cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);
  });

  describe('LeaseLiabilityPostingReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityPostingReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityPostingReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-posting-report/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityPostingReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-posting-reports',
  
          body: {
            ...leaseLiabilityPostingReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          leaseLiabilityPostingReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-posting-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityPostingReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityPostingReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityPostingReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityPostingReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityPostingReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityPostingReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityPostingReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiabilityPostingReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityPostingReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);

        leaseLiabilityPostingReport = undefined;
      });
    });
  });

  describe('new LeaseLiabilityPostingReport page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityPostingReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityPostingReport');
    });

    it.skip('should create an instance of LeaseLiabilityPostingReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('f5e1fc0e-1066-4a90-89ac-c0745873d991')
        .invoke('val')
        .should('match', new RegExp('f5e1fc0e-1066-4a90-89ac-c0745873d991'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-08-28T08:31').should('have.value', '2024-08-28T08:31');

      cy.get(`[data-cy="fileChecksum"]`).type('fuchsia Hat Franc').should('have.value', 'fuchsia Hat Franc');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('bd9f68f5-aa33-42a0-b110-f2a8c45ceb31')
        .invoke('val')
        .should('match', new RegExp('bd9f68f5-aa33-42a0-b110-f2a8c45ceb31'));

      cy.get(`[data-cy="reportParameters"]`).type('Market').should('have.value', 'Market');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityPostingReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityPostingReportPageUrlPattern);
    });
  });
});
