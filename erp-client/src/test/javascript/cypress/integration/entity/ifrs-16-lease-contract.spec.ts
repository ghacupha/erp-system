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

describe('IFRS16LeaseContract e2e test', () => {
  const iFRS16LeaseContractPageUrl = '/ifrs-16-lease-contract';
  const iFRS16LeaseContractPageUrlPattern = new RegExp('/ifrs-16-lease-contract(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const iFRS16LeaseContractSample = {
    bookingId: 'SSL Uganda Metal',
    leaseTitle: 'incentivize',
    inceptionDate: '2024-03-06',
    commencementDate: '2024-03-06',
  };

  let iFRS16LeaseContract: any;
  //let serviceOutlet: any;
  //let dealer: any;
  //let fiscalMonth: any;

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
      url: '/api/service-outlets',
      body: {"outletCode":"Serbia expedite Panama","outletName":"Plains open-source","town":"Bedfordshire","parliamentaryConstituency":"Function-based hacking","gpsCoordinates":"Director reboot","outletOpeningDate":"2022-03-01","regulatorApprovalDate":"2022-02-28","outletClosureDate":"2022-02-28","dateLastModified":"2022-03-01","licenseFeePayable":47934},
    }).then(({ body }) => {
      serviceOutlet = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"Proactive Representative","taxNumber":"system","identificationDocumentNumber":"Savings connect","organizationName":"Administrator","department":"Sausages","position":"synthesize needs-based","postalAddress":"neural Guinea-Bissau New","physicalAddress":"Intelligent Lead","accountName":"Credit Card Account","accountNumber":"online","bankersName":"Money","bankersBranch":"Table partnerships Industrial","bankersSwiftCode":"Exclusive Soft hybrid","fileUploadToken":"Rustic","compilationToken":"content Trafficway","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"Cambridgeshire"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-months',
      body: {"monthNumber":35436,"startDate":"2023-08-15","endDate":"2023-08-15","fiscalMonthCode":"Markets"},
    }).then(({ body }) => {
      fiscalMonth = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ifrs-16-lease-contracts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ifrs-16-lease-contracts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ifrs-16-lease-contracts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/service-outlets', {
      statusCode: 200,
      body: [serviceOutlet],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/fiscal-months', {
      statusCode: 200,
      body: [fiscalMonth],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lease-payments', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (serviceOutlet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-outlets/${serviceOutlet.id}`,
      }).then(() => {
        serviceOutlet = undefined;
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
    if (fiscalMonth) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-months/${fiscalMonth.id}`,
      }).then(() => {
        fiscalMonth = undefined;
      });
    }
  });
   */

  it('IFRS16LeaseContracts menu should load IFRS16LeaseContracts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ifrs-16-lease-contract');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IFRS16LeaseContract').should('exist');
    cy.url().should('match', iFRS16LeaseContractPageUrlPattern);
  });

  describe('IFRS16LeaseContract page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iFRS16LeaseContractPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IFRS16LeaseContract page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ifrs-16-lease-contract/new$'));
        cy.getEntityCreateUpdateHeading('IFRS16LeaseContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iFRS16LeaseContractPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ifrs-16-lease-contracts',
  
          body: {
            ...iFRS16LeaseContractSample,
            superintendentServiceOutlet: serviceOutlet,
            mainDealer: dealer,
            firstReportingPeriod: fiscalMonth,
            lastReportingPeriod: fiscalMonth,
          },
        }).then(({ body }) => {
          iFRS16LeaseContract = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ifrs-16-lease-contracts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iFRS16LeaseContract],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(iFRS16LeaseContractPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(iFRS16LeaseContractPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details IFRS16LeaseContract page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iFRS16LeaseContract');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iFRS16LeaseContractPageUrlPattern);
      });

      it('edit button click should load edit IFRS16LeaseContract page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IFRS16LeaseContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iFRS16LeaseContractPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of IFRS16LeaseContract', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('iFRS16LeaseContract').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', iFRS16LeaseContractPageUrlPattern);

        iFRS16LeaseContract = undefined;
      });
    });
  });

  describe('new IFRS16LeaseContract page', () => {
    beforeEach(() => {
      cy.visit(`${iFRS16LeaseContractPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IFRS16LeaseContract');
    });

    it.skip('should create an instance of IFRS16LeaseContract', () => {
      cy.get(`[data-cy="bookingId"]`).type('approach Cambridgeshire algorithm').should('have.value', 'approach Cambridgeshire algorithm');

      cy.get(`[data-cy="leaseTitle"]`).type('functionalities').should('have.value', 'functionalities');

      cy.get(`[data-cy="shortTitle"]`).type('Unit Bedfordshire').should('have.value', 'Unit Bedfordshire');

      cy.get(`[data-cy="description"]`).type('deposit').should('have.value', 'deposit');

      cy.get(`[data-cy="inceptionDate"]`).type('2024-03-06').should('have.value', '2024-03-06');

      cy.get(`[data-cy="commencementDate"]`).type('2024-03-06').should('have.value', '2024-03-06');

      cy.get(`[data-cy="serialNumber"]`)
        .type('92858e13-0320-489a-889d-a3341c978aac')
        .invoke('val')
        .should('match', new RegExp('92858e13-0320-489a-889d-a3341c978aac'));

      cy.get(`[data-cy="superintendentServiceOutlet"]`).select(1);
      cy.get(`[data-cy="mainDealer"]`).select(1);
      cy.get(`[data-cy="firstReportingPeriod"]`).select(1);
      cy.get(`[data-cy="lastReportingPeriod"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        iFRS16LeaseContract = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', iFRS16LeaseContractPageUrlPattern);
    });
  });
});
