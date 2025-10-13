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

describe('LeaseLiabilityReport e2e test', () => {
  const leaseLiabilityReportPageUrl = '/lease-liability-report';
  const leaseLiabilityReportPageUrlPattern = new RegExp('/lease-liability-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityReportSample = { requestId: '2a30bf4e-dc01-4d63-9f03-6783bd33b57a' };

  let leaseLiabilityReport: any;
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
      body: {"sequenceNumber":85252,"periodCode":"HDD"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-reports/*').as('deleteEntityRequest');
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
    if (leaseLiabilityReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-reports/${leaseLiabilityReport.id}`,
      }).then(() => {
        leaseLiabilityReport = undefined;
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

  it('LeaseLiabilityReports menu should load LeaseLiabilityReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityReport').should('exist');
    cy.url().should('match', leaseLiabilityReportPageUrlPattern);
  });

  describe('LeaseLiabilityReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-report/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-reports',
  
          body: {
            ...leaseLiabilityReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          leaseLiabilityReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityReportPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiabilityReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityReportPageUrlPattern);

        leaseLiabilityReport = undefined;
      });
    });
  });

  describe('new LeaseLiabilityReport page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityReport');
    });

    it.skip('should create an instance of LeaseLiabilityReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('30fb7a9c-58de-4b05-8419-7741a02ed597')
        .invoke('val')
        .should('match', new RegExp('30fb7a9c-58de-4b05-8419-7741a02ed597'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-08-28T01:11').should('have.value', '2024-08-28T01:11');

      cy.get(`[data-cy="fileChecksum"]`).type('Steel Beauty action-items').should('have.value', 'Steel Beauty action-items');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('7cf5be3e-a726-468e-a3ba-8c8d474e80ef')
        .invoke('val')
        .should('match', new RegExp('7cf5be3e-a726-468e-a3ba-8c8d474e80ef'));

      cy.get(`[data-cy="reportParameters"]`).type('Malawi').should('have.value', 'Malawi');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityReportPageUrlPattern);
    });
  });
});
