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

describe('AmortizationPostingReportRequisition e2e test', () => {
  const amortizationPostingReportRequisitionPageUrl = '/amortization-posting-report-requisition';
  const amortizationPostingReportRequisitionPageUrlPattern = new RegExp('/amortization-posting-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const amortizationPostingReportRequisitionSample = {
    requestId: '686772d4-d775-4639-95fb-367b06265ad5',
    timeOfRequisition: '2024-05-01T12:41:44.992Z',
  };

  let amortizationPostingReportRequisition: any;
  //let amortizationPeriod: any;

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
      url: '/api/amortization-periods',
      body: {"sequenceNumber":46614,"periodCode":"Avon extend Frozen"},
    }).then(({ body }) => {
      amortizationPeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/amortization-posting-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/amortization-posting-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/amortization-posting-report-requisitions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/amortization-periods', {
      statusCode: 200,
      body: [amortizationPeriod],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (amortizationPostingReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-posting-report-requisitions/${amortizationPostingReportRequisition.id}`,
      }).then(() => {
        amortizationPostingReportRequisition = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (amortizationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-periods/${amortizationPeriod.id}`,
      }).then(() => {
        amortizationPeriod = undefined;
      });
    }
  });
   */

  it('AmortizationPostingReportRequisitions menu should load AmortizationPostingReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('amortization-posting-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AmortizationPostingReportRequisition').should('exist');
    cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);
  });

  describe('AmortizationPostingReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(amortizationPostingReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AmortizationPostingReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/amortization-posting-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('AmortizationPostingReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/amortization-posting-report-requisitions',
  
          body: {
            ...amortizationPostingReportRequisitionSample,
            amortizationPeriod: amortizationPeriod,
          },
        }).then(({ body }) => {
          amortizationPostingReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/amortization-posting-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [amortizationPostingReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(amortizationPostingReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(amortizationPostingReportRequisitionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AmortizationPostingReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('amortizationPostingReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit AmortizationPostingReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AmortizationPostingReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AmortizationPostingReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amortizationPostingReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);

        amortizationPostingReportRequisition = undefined;
      });
    });
  });

  describe('new AmortizationPostingReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${amortizationPostingReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AmortizationPostingReportRequisition');
    });

    it.skip('should create an instance of AmortizationPostingReportRequisition', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('08691b9c-dea2-4dec-b244-c88dcdf9ca0b')
        .invoke('val')
        .should('match', new RegExp('08691b9c-dea2-4dec-b244-c88dcdf9ca0b'));

      cy.get(`[data-cy="timeOfRequisition"]`).type('2024-05-01T12:07').should('have.value', '2024-05-01T12:07');

      cy.get(`[data-cy="fileChecksum"]`).type('port wireless Bedfordshire').should('have.value', 'port wireless Bedfordshire');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('3a4265c8-322d-4b5f-b20f-b69854fbf695')
        .invoke('val')
        .should('match', new RegExp('3a4265c8-322d-4b5f-b20f-b69854fbf695'));

      cy.get(`[data-cy="reportParameters"]`).type('Berkshire front-end').should('have.value', 'Berkshire front-end');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="amortizationPeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        amortizationPostingReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', amortizationPostingReportRequisitionPageUrlPattern);
    });
  });
});
