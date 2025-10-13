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

describe('SettlementRequisition e2e test', () => {
  const settlementRequisitionPageUrl = '/settlement-requisition';
  const settlementRequisitionPageUrlPattern = new RegExp('/settlement-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const settlementRequisitionSample = {
    serialNumber: '51cdd82b-2028-4cbf-8025-037c56babe6c',
    timeOfRequisition: '2022-10-19T03:48:26.381Z',
    requisitionNumber: 'Bedfordshire',
    paymentAmount: 73467,
    paymentStatus: 'PROCESSED',
  };

  let settlementRequisition: any;
  //let settlementCurrency: any;
  //let applicationUser: any;
  //let dealer: any;

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
      url: '/api/settlement-currencies',
      body: {"iso4217CurrencyCode":"Hat","currencyName":"Armenian Dram","country":"Albania","numericCode":"Fully-configurable cross-platform","minorUnit":"THX Carolina Assurance","fileUploadToken":"Specialist Practical TCP","compilationToken":"Account Granite generate"},
    }).then(({ body }) => {
      settlementCurrency = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/application-users',
      body: {"designation":"10431f39-c3e1-4e08-bfd8-37c3378b9e7d","applicationIdentity":"Avon Extended Pizza"},
    }).then(({ body }) => {
      applicationUser = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"Cambridgeshire Director","taxNumber":"Point New tolerance","identificationDocumentNumber":"plug-and-play Customer Tennessee","organizationName":"Soft extensible portals","department":"bypass Buckinghamshire","position":"Buckinghamshire connecting streamline","postalAddress":"Chilean Cotton target","physicalAddress":"Advanced Automotive Handmade","accountName":"Investment Account","accountNumber":"Checking invoice","bankersName":"technologies XSS Analyst","bankersBranch":"PCI Handmade Account","bankersSwiftCode":"Alaska blockchains deliver","fileUploadToken":"Mouse Outdoors","compilationToken":"PCI","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"Bedfordshire customized"},
    }).then(({ body }) => {
      dealer = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/settlement-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/settlement-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/settlement-requisitions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [settlementCurrency],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [applicationUser],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/payment-invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/delivery-notes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/job-sheets', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/settlements', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (settlementRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-requisitions/${settlementRequisition.id}`,
      }).then(() => {
        settlementRequisition = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (settlementCurrency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-currencies/${settlementCurrency.id}`,
      }).then(() => {
        settlementCurrency = undefined;
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
  });
   */

  it('SettlementRequisitions menu should load SettlementRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('settlement-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SettlementRequisition').should('exist');
    cy.url().should('match', settlementRequisitionPageUrlPattern);
  });

  describe('SettlementRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(settlementRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SettlementRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/settlement-requisition/new$'));
        cy.getEntityCreateUpdateHeading('SettlementRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/settlement-requisitions',
  
          body: {
            ...settlementRequisitionSample,
            settlementCurrency: settlementCurrency,
            currentOwner: applicationUser,
            nativeOwner: applicationUser,
            nativeDepartment: dealer,
            biller: dealer,
          },
        }).then(({ body }) => {
          settlementRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/settlement-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [settlementRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(settlementRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(settlementRequisitionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SettlementRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('settlementRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementRequisitionPageUrlPattern);
      });

      it('edit button click should load edit SettlementRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SettlementRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementRequisitionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SettlementRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('settlementRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementRequisitionPageUrlPattern);

        settlementRequisition = undefined;
      });
    });
  });

  describe('new SettlementRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${settlementRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SettlementRequisition');
    });

    it.skip('should create an instance of SettlementRequisition', () => {
      cy.get(`[data-cy="description"]`).type('software Dynamic Movies').should('have.value', 'software Dynamic Movies');

      cy.get(`[data-cy="serialNumber"]`)
        .type('f79610fd-1681-4918-b6f5-21b8e43ab020')
        .invoke('val')
        .should('match', new RegExp('f79610fd-1681-4918-b6f5-21b8e43ab020'));

      cy.get(`[data-cy="timeOfRequisition"]`).type('2022-10-19T07:13').should('have.value', '2022-10-19T07:13');

      cy.get(`[data-cy="requisitionNumber"]`).type('compressing').should('have.value', 'compressing');

      cy.get(`[data-cy="paymentAmount"]`).type('86132').should('have.value', '86132');

      cy.get(`[data-cy="paymentStatus"]`).select('PROCESSED');

      cy.get(`[data-cy="settlementCurrency"]`).select(1);
      cy.get(`[data-cy="currentOwner"]`).select(1);
      cy.get(`[data-cy="nativeOwner"]`).select(1);
      cy.get(`[data-cy="nativeDepartment"]`).select(1);
      cy.get(`[data-cy="biller"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        settlementRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', settlementRequisitionPageUrlPattern);
    });
  });
});
