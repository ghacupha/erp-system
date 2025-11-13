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

describe('LeaseLiabilityScheduleItem e2e test', () => {
  const leaseLiabilityScheduleItemPageUrl = '/lease-liability-schedule-item';
  const leaseLiabilityScheduleItemPageUrlPattern = new RegExp('/lease-liability-schedule-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityScheduleItemSample = {};

  let leaseLiabilityScheduleItem: any;
  //let iFRS16LeaseContract: any;
  //let leaseLiability: any;
  //let leaseRepaymentPeriod: any;

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
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"ADP","leaseTitle":"Marketing","shortTitle":"payment XML Extended","description":"port","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"b201c2f2-3195-48a0-9f8c-a76a4f00fe77"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/lease-liabilities',
      body: {"leaseId":"payment extensible","liabilityAmount":30694,"startDate":"2024-06-17","endDate":"2024-06-17","interestRate":73782},
    }).then(({ body }) => {
      leaseLiability = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/lease-repayment-periods',
      body: {"sequenceNumber":67157,"startDate":"2024-07-22","endDate":"2024-07-22","periodCode":"Chief"},
    }).then(({ body }) => {
      leaseRepaymentPeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-schedule-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-schedule-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-schedule-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lease-amortization-schedules', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/lease-liabilities', {
      statusCode: 200,
      body: [leaseLiability],
    });

    cy.intercept('GET', '/api/lease-repayment-periods', {
      statusCode: 200,
      body: [leaseRepaymentPeriod],
    });

  });
   */

  afterEach(() => {
    if (leaseLiabilityScheduleItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-schedule-items/${leaseLiabilityScheduleItem.id}`,
      }).then(() => {
        leaseLiabilityScheduleItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
    if (leaseLiability) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liabilities/${leaseLiability.id}`,
      }).then(() => {
        leaseLiability = undefined;
      });
    }
    if (leaseRepaymentPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-repayment-periods/${leaseRepaymentPeriod.id}`,
      }).then(() => {
        leaseRepaymentPeriod = undefined;
      });
    }
  });
   */

  it('LeaseLiabilityScheduleItems menu should load LeaseLiabilityScheduleItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-schedule-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityScheduleItem').should('exist');
    cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);
  });

  describe('LeaseLiabilityScheduleItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityScheduleItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityScheduleItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-schedule-item/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-schedule-items',
  
          body: {
            ...leaseLiabilityScheduleItemSample,
            leaseContract: iFRS16LeaseContract,
            leaseLiability: leaseLiability,
            leasePeriod: leaseRepaymentPeriod,
          },
        }).then(({ body }) => {
          leaseLiabilityScheduleItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-schedule-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityScheduleItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityScheduleItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityScheduleItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityScheduleItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityScheduleItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityScheduleItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiabilityScheduleItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityScheduleItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);

        leaseLiabilityScheduleItem = undefined;
      });
    });
  });

  describe('new LeaseLiabilityScheduleItem page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityScheduleItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleItem');
    });

    it.skip('should create an instance of LeaseLiabilityScheduleItem', () => {
      cy.get(`[data-cy="sequenceNumber"]`).type('97481').should('have.value', '97481');

      cy.get(`[data-cy="openingBalance"]`).type('29117').should('have.value', '29117');

      cy.get(`[data-cy="cashPayment"]`).type('20173').should('have.value', '20173');

      cy.get(`[data-cy="principalPayment"]`).type('36960').should('have.value', '36960');

      cy.get(`[data-cy="interestPayment"]`).type('2512').should('have.value', '2512');

      cy.get(`[data-cy="outstandingBalance"]`).type('57989').should('have.value', '57989');

      cy.get(`[data-cy="interestPayableOpening"]`).type('1472').should('have.value', '1472');

      cy.get(`[data-cy="interestAccrued"]`).type('17465').should('have.value', '17465');

      cy.get(`[data-cy="interestPayableClosing"]`).type('60769').should('have.value', '60769');

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="leaseLiability"]`).select(1);
      cy.get(`[data-cy="leasePeriod"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityScheduleItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityScheduleItemPageUrlPattern);
    });
  });
});
