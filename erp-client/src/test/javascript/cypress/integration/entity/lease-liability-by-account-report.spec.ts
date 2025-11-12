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

describe('LeaseLiabilityByAccountReport e2e test', () => {
  const leaseLiabilityByAccountReportPageUrl = '/lease-liability-by-account-report';
  const leaseLiabilityByAccountReportPageUrlPattern = new RegExp('/lease-liability-by-account-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityByAccountReportSample = { reportId: '2170fe21-8dbd-46e9-9fdf-9529a13002b8' };

  let leaseLiabilityByAccountReport: any;
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
      body: {"sequenceNumber":84253,"periodCode":"synergize"},
    }).then(({ body }) => {
      leasePeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-by-account-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-by-account-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-by-account-reports/*').as('deleteEntityRequest');
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
    if (leaseLiabilityByAccountReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-by-account-reports/${leaseLiabilityByAccountReport.id}`,
      }).then(() => {
        leaseLiabilityByAccountReport = undefined;
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

  it('LeaseLiabilityByAccountReports menu should load LeaseLiabilityByAccountReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-by-account-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityByAccountReport').should('exist');
    cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);
  });

  describe('LeaseLiabilityByAccountReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityByAccountReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityByAccountReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-by-account-report/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityByAccountReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-by-account-reports',
  
          body: {
            ...leaseLiabilityByAccountReportSample,
            leasePeriod: leasePeriod,
          },
        }).then(({ body }) => {
          leaseLiabilityByAccountReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-by-account-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityByAccountReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityByAccountReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityByAccountReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityByAccountReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityByAccountReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityByAccountReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityByAccountReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiabilityByAccountReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityByAccountReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);

        leaseLiabilityByAccountReport = undefined;
      });
    });
  });

  describe('new LeaseLiabilityByAccountReport page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityByAccountReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityByAccountReport');
    });

    it.skip('should create an instance of LeaseLiabilityByAccountReport', () => {
      cy.get(`[data-cy="reportId"]`)
        .type('bef78d03-eedd-4737-89c6-bea6c0269b4f')
        .invoke('val')
        .should('match', new RegExp('bef78d03-eedd-4737-89c6-bea6c0269b4f'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-08-27T12:38').should('have.value', '2024-08-27T12:38');

      cy.get(`[data-cy="fileChecksum"]`).type('Branding').should('have.value', 'Branding');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('55f79d8e-6179-4b83-a8d5-a108d6ffbaf8')
        .invoke('val')
        .should('match', new RegExp('55f79d8e-6179-4b83-a8d5-a108d6ffbaf8'));

      cy.get(`[data-cy="reportParameters"]`).type('Planner Avenue').should('have.value', 'Planner Avenue');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leasePeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityByAccountReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityByAccountReportPageUrlPattern);
    });
  });
});
