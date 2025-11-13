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

describe('TerminalsAndPOS e2e test', () => {
  const terminalsAndPOSPageUrl = '/terminals-and-pos';
  const terminalsAndPOSPageUrlPattern = new RegExp('/terminals-and-pos(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const terminalsAndPOSSample = {
    reportingDate: '2023-10-04',
    terminalId: 'payment',
    merchantId: 'state Cross-group Terrace',
    terminalName: 'Streamlined',
    terminalLocation: 'Saint',
    iso6709Latitute: 77501,
    iso6709Longitude: 15963,
    terminalOpeningDate: '2023-10-03',
  };

  let terminalsAndPOS: any;
  let terminalTypes: any;
  let terminalFunctions: any;
  let countySubCountyCode: any;
  let institutionCode: any;
  let bankBranchCode: any;

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
      url: '/api/terminal-types',
      body: {
        txnTerminalTypeCode: 'Designer',
        txnChannelType: 'Future',
        txnChannelTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      terminalTypes = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/terminal-functions',
      body: { functionCode: 'collaborative Fish next-generation', terminalFunctionality: 'recontextualize' },
    }).then(({ body }) => {
      terminalFunctions = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/county-sub-county-codes',
      body: { subCountyCode: '7403', subCountyName: 'Intelligent Gorgeous Gorgeous', countyCode: '86', countyName: 'responsive Lead' },
    }).then(({ body }) => {
      countySubCountyCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'Salad',
        institutionName: 'Universal',
        shortName: 'Pants haptic',
        category: 'AGP',
        institutionCategory: 'Computers Loan deploy',
        institutionOwnership: 'microchip',
        dateLicensed: '2022-04-06',
        institutionStatus: 'Towels lime Shirt',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'Generic',
        bankName: 'Berkshire circuit',
        branchCode: 'Solutions South',
        branchName: 'Shoal',
        notes: 'Applications',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/terminals-and-pos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/terminals-and-pos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/terminals-and-pos/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/terminal-types', {
      statusCode: 200,
      body: [terminalTypes],
    });

    cy.intercept('GET', '/api/terminal-functions', {
      statusCode: 200,
      body: [terminalFunctions],
    });

    cy.intercept('GET', '/api/county-sub-county-codes', {
      statusCode: 200,
      body: [countySubCountyCode],
    });

    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/bank-branch-codes', {
      statusCode: 200,
      body: [bankBranchCode],
    });
  });

  afterEach(() => {
    if (terminalsAndPOS) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/terminals-and-pos/${terminalsAndPOS.id}`,
      }).then(() => {
        terminalsAndPOS = undefined;
      });
    }
  });

  afterEach(() => {
    if (terminalTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/terminal-types/${terminalTypes.id}`,
      }).then(() => {
        terminalTypes = undefined;
      });
    }
    if (terminalFunctions) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/terminal-functions/${terminalFunctions.id}`,
      }).then(() => {
        terminalFunctions = undefined;
      });
    }
    if (countySubCountyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/county-sub-county-codes/${countySubCountyCode.id}`,
      }).then(() => {
        countySubCountyCode = undefined;
      });
    }
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
  });

  it('TerminalsAndPOS menu should load TerminalsAndPOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('terminals-and-pos');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TerminalsAndPOS').should('exist');
    cy.url().should('match', terminalsAndPOSPageUrlPattern);
  });

  describe('TerminalsAndPOS page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(terminalsAndPOSPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TerminalsAndPOS page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/terminals-and-pos/new$'));
        cy.getEntityCreateUpdateHeading('TerminalsAndPOS');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalsAndPOSPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/terminals-and-pos',

          body: {
            ...terminalsAndPOSSample,
            terminalType: terminalTypes,
            terminalFunctionality: terminalFunctions,
            physicalLocation: countySubCountyCode,
            bankId: institutionCode,
            branchId: bankBranchCode,
          },
        }).then(({ body }) => {
          terminalsAndPOS = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/terminals-and-pos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [terminalsAndPOS],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(terminalsAndPOSPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TerminalsAndPOS page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('terminalsAndPOS');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalsAndPOSPageUrlPattern);
      });

      it('edit button click should load edit TerminalsAndPOS page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TerminalsAndPOS');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalsAndPOSPageUrlPattern);
      });

      it('last delete button click should delete instance of TerminalsAndPOS', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('terminalsAndPOS').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalsAndPOSPageUrlPattern);

        terminalsAndPOS = undefined;
      });
    });
  });

  describe('new TerminalsAndPOS page', () => {
    beforeEach(() => {
      cy.visit(`${terminalsAndPOSPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TerminalsAndPOS');
    });

    it('should create an instance of TerminalsAndPOS', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="terminalId"]`).type('redundant Plaza Car').should('have.value', 'redundant Plaza Car');

      cy.get(`[data-cy="merchantId"]`).type('value-added').should('have.value', 'value-added');

      cy.get(`[data-cy="terminalName"]`).type('Cambridgeshire Chicken Mexico').should('have.value', 'Cambridgeshire Chicken Mexico');

      cy.get(`[data-cy="terminalLocation"]`).type('RSS Account').should('have.value', 'RSS Account');

      cy.get(`[data-cy="iso6709Latitute"]`).type('79954').should('have.value', '79954');

      cy.get(`[data-cy="iso6709Longitude"]`).type('78515').should('have.value', '78515');

      cy.get(`[data-cy="terminalOpeningDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="terminalClosureDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="terminalType"]`).select(1);
      cy.get(`[data-cy="terminalFunctionality"]`).select(1);
      cy.get(`[data-cy="physicalLocation"]`).select(1);
      cy.get(`[data-cy="bankId"]`).select(1);
      cy.get(`[data-cy="branchId"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        terminalsAndPOS = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', terminalsAndPOSPageUrlPattern);
    });
  });
});
