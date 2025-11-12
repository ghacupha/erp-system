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

describe('PerformanceOfForeignSubsidiaries e2e test', () => {
  const performanceOfForeignSubsidiariesPageUrl = '/performance-of-foreign-subsidiaries';
  const performanceOfForeignSubsidiariesPageUrlPattern = new RegExp('/performance-of-foreign-subsidiaries(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const performanceOfForeignSubsidiariesSample = {
    subsidiaryName: 'Savings hybrid Avon',
    reportingDate: '2023-10-03',
    subsidiaryId: 'Hollow seamless yellow',
    grossLoansAmount: 44665,
    grossNPALoanAmount: 71127,
    grossAssetsAmount: 82123,
    grossDepositsAmount: 91610,
    profitBeforeTax: 31490,
    totalCapitalAdequacyRatio: 59233,
    liquidityRatio: 24561,
    generalProvisions: 8224,
    specificProvisions: 81896,
    interestInSuspenseAmount: 81964,
    totalNumberOfStaff: 62654,
    numberOfBranches: 12030,
  };

  let performanceOfForeignSubsidiaries: any;
  let institutionCode: any;
  let isoCountryCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'Fantastic Idaho',
        institutionName: 'Proactive',
        shortName: 'PCI Avon',
        category: 'Designer',
        institutionCategory: 'Architect',
        institutionOwnership: 'Wooden plum',
        dateLicensed: '2022-04-05',
        institutionStatus: 'clicks-and-mortar',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/iso-country-codes',
      body: {
        countryCode: 'IN',
        countryDescription: 'action-items Michigan',
        continentCode: 'Light hacking copying',
        continentName: 'Facilitator Direct connecting',
        subRegion: 'backing withdrawal Optional',
      },
    }).then(({ body }) => {
      isoCountryCode = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/performance-of-foreign-subsidiaries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/performance-of-foreign-subsidiaries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/performance-of-foreign-subsidiaries/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/iso-country-codes', {
      statusCode: 200,
      body: [isoCountryCode],
    });
  });

  afterEach(() => {
    if (performanceOfForeignSubsidiaries) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/performance-of-foreign-subsidiaries/${performanceOfForeignSubsidiaries.id}`,
      }).then(() => {
        performanceOfForeignSubsidiaries = undefined;
      });
    }
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
    if (isoCountryCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-country-codes/${isoCountryCode.id}`,
      }).then(() => {
        isoCountryCode = undefined;
      });
    }
  });

  it('PerformanceOfForeignSubsidiaries menu should load PerformanceOfForeignSubsidiaries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('performance-of-foreign-subsidiaries');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PerformanceOfForeignSubsidiaries').should('exist');
    cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);
  });

  describe('PerformanceOfForeignSubsidiaries page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(performanceOfForeignSubsidiariesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PerformanceOfForeignSubsidiaries page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/performance-of-foreign-subsidiaries/new$'));
        cy.getEntityCreateUpdateHeading('PerformanceOfForeignSubsidiaries');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/performance-of-foreign-subsidiaries',

          body: {
            ...performanceOfForeignSubsidiariesSample,
            bankCode: institutionCode,
            subsidiaryCountryCode: isoCountryCode,
          },
        }).then(({ body }) => {
          performanceOfForeignSubsidiaries = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/performance-of-foreign-subsidiaries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [performanceOfForeignSubsidiaries],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(performanceOfForeignSubsidiariesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PerformanceOfForeignSubsidiaries page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('performanceOfForeignSubsidiaries');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);
      });

      it('edit button click should load edit PerformanceOfForeignSubsidiaries page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PerformanceOfForeignSubsidiaries');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);
      });

      it('last delete button click should delete instance of PerformanceOfForeignSubsidiaries', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('performanceOfForeignSubsidiaries').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);

        performanceOfForeignSubsidiaries = undefined;
      });
    });
  });

  describe('new PerformanceOfForeignSubsidiaries page', () => {
    beforeEach(() => {
      cy.visit(`${performanceOfForeignSubsidiariesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PerformanceOfForeignSubsidiaries');
    });

    it('should create an instance of PerformanceOfForeignSubsidiaries', () => {
      cy.get(`[data-cy="subsidiaryName"]`).type('Borders Generic Riyal').should('have.value', 'Borders Generic Riyal');

      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="subsidiaryId"]`).type('invoice Hollow Steel').should('have.value', 'invoice Hollow Steel');

      cy.get(`[data-cy="grossLoansAmount"]`).type('66995').should('have.value', '66995');

      cy.get(`[data-cy="grossNPALoanAmount"]`).type('99518').should('have.value', '99518');

      cy.get(`[data-cy="grossAssetsAmount"]`).type('5618').should('have.value', '5618');

      cy.get(`[data-cy="grossDepositsAmount"]`).type('84872').should('have.value', '84872');

      cy.get(`[data-cy="profitBeforeTax"]`).type('84133').should('have.value', '84133');

      cy.get(`[data-cy="totalCapitalAdequacyRatio"]`).type('4249').should('have.value', '4249');

      cy.get(`[data-cy="liquidityRatio"]`).type('62501').should('have.value', '62501');

      cy.get(`[data-cy="generalProvisions"]`).type('50186').should('have.value', '50186');

      cy.get(`[data-cy="specificProvisions"]`).type('94008').should('have.value', '94008');

      cy.get(`[data-cy="interestInSuspenseAmount"]`).type('24029').should('have.value', '24029');

      cy.get(`[data-cy="totalNumberOfStaff"]`).type('82698').should('have.value', '82698');

      cy.get(`[data-cy="numberOfBranches"]`).type('5275').should('have.value', '5275');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="subsidiaryCountryCode"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        performanceOfForeignSubsidiaries = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', performanceOfForeignSubsidiariesPageUrlPattern);
    });
  });
});
