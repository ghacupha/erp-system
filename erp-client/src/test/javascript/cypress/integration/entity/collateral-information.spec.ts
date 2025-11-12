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

describe('CollateralInformation e2e test', () => {
  const collateralInformationPageUrl = '/collateral-information';
  const collateralInformationPageUrlPattern = new RegExp('/collateral-information(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const collateralInformationSample = {
    reportingDate: '2023-10-03',
    collateralId: 'quantify Parkway Indiana',
    loanContractId: '444523911602563',
    customerId: 'Mandatory hack Tasty',
    collateralOMVInCCY: 72703,
    collateralFSVInLCY: 51437,
    amountCharged: 79172,
    insuredFlag: 'Y',
  };

  let collateralInformation: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let collateralType: any;

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
        institutionCode: 'Avon',
        institutionName: 'RSS',
        shortName: 'Delaware',
        category: 'Cambridgeshire North',
        institutionCategory: 'firewall reintermediate',
        institutionOwnership: 'Representative CFP',
        dateLicensed: '2022-04-05',
        institutionStatus: 'dedicated knowledge',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'integrated Light El',
        bankName: 'orange Beauty',
        branchCode: 'port',
        branchName: 'online innovate Savings',
        notes: 'Toys',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/collateral-types',
      body: {
        collateralTypeCode: 'benchmark indexing',
        collateralType: 'algorithm auxiliary Horizontal',
        collateralTypeDescription: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      collateralType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/collateral-informations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/collateral-informations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/collateral-informations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/bank-branch-codes', {
      statusCode: 200,
      body: [bankBranchCode],
    });

    cy.intercept('GET', '/api/collateral-types', {
      statusCode: 200,
      body: [collateralType],
    });

    cy.intercept('GET', '/api/county-sub-county-codes', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (collateralInformation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/collateral-informations/${collateralInformation.id}`,
      }).then(() => {
        collateralInformation = undefined;
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
    if (bankBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-branch-codes/${bankBranchCode.id}`,
      }).then(() => {
        bankBranchCode = undefined;
      });
    }
    if (collateralType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/collateral-types/${collateralType.id}`,
      }).then(() => {
        collateralType = undefined;
      });
    }
  });

  it('CollateralInformations menu should load CollateralInformations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('collateral-information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CollateralInformation').should('exist');
    cy.url().should('match', collateralInformationPageUrlPattern);
  });

  describe('CollateralInformation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(collateralInformationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CollateralInformation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/collateral-information/new$'));
        cy.getEntityCreateUpdateHeading('CollateralInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralInformationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/collateral-informations',

          body: {
            ...collateralInformationSample,
            bankCode: institutionCode,
            branchCode: bankBranchCode,
            collateralType: collateralType,
          },
        }).then(({ body }) => {
          collateralInformation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/collateral-informations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [collateralInformation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(collateralInformationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CollateralInformation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('collateralInformation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralInformationPageUrlPattern);
      });

      it('edit button click should load edit CollateralInformation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CollateralInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralInformationPageUrlPattern);
      });

      it('last delete button click should delete instance of CollateralInformation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('collateralInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralInformationPageUrlPattern);

        collateralInformation = undefined;
      });
    });
  });

  describe('new CollateralInformation page', () => {
    beforeEach(() => {
      cy.visit(`${collateralInformationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CollateralInformation');
    });

    it('should create an instance of CollateralInformation', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="collateralId"]`).type('RAM Plastic systematic').should('have.value', 'RAM Plastic systematic');

      cy.get(`[data-cy="loanContractId"]`).type('785325989772637').should('have.value', '785325989772637');

      cy.get(`[data-cy="customerId"]`).type('Checking').should('have.value', 'Checking');

      cy.get(`[data-cy="registrationPropertyNumber"]`).type('platforms red').should('have.value', 'platforms red');

      cy.get(`[data-cy="collateralOMVInCCY"]`).type('65976').should('have.value', '65976');

      cy.get(`[data-cy="collateralFSVInLCY"]`).type('20747').should('have.value', '20747');

      cy.get(`[data-cy="collateralDiscountedValue"]`).type('54630').should('have.value', '54630');

      cy.get(`[data-cy="amountCharged"]`).type('92340').should('have.value', '92340');

      cy.get(`[data-cy="collateralDiscountRate"]`).type('62640').should('have.value', '62640');

      cy.get(`[data-cy="loanToValueRatio"]`).type('30274').should('have.value', '30274');

      cy.get(`[data-cy="nameOfPropertyValuer"]`).type('District').should('have.value', 'District');

      cy.get(`[data-cy="collateralLastValuationDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="insuredFlag"]`).select('Y');

      cy.get(`[data-cy="nameOfInsurer"]`).type('web-readiness Cocos').should('have.value', 'web-readiness Cocos');

      cy.get(`[data-cy="amountInsured"]`).type('57441').should('have.value', '57441');

      cy.get(`[data-cy="insuranceExpiryDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="guaranteeInsurers"]`).type('Hat Nepalese').should('have.value', 'Hat Nepalese');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchCode"]`).select(1);
      cy.get(`[data-cy="collateralType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        collateralInformation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', collateralInformationPageUrlPattern);
    });
  });
});
