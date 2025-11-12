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

describe('ParticularsOfOutlet e2e test', () => {
  const particularsOfOutletPageUrl = '/particulars-of-outlet';
  const particularsOfOutletPageUrlPattern = new RegExp('/particulars-of-outlet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const particularsOfOutletSample = {
    businessReportingDate: '2023-10-03',
    outletName: 'Steel Rubber Cambridgeshire',
    town: 'local Senior and',
    iso6709Latitute: 90184,
    iso6709Longitude: 64038,
    cbkApprovalDate: '2023-10-03',
    outletOpeningDate: '2023-10-03',
    licenseFeePayable: 25970,
  };

  let particularsOfOutlet: any;
  let countySubCountyCode: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let outletType: any;
  let outletStatus: any;

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
      url: '/api/county-sub-county-codes',
      body: { subCountyCode: '4505', subCountyName: 'RSS Focused', countyCode: '47', countyName: 'Metal Yuan payment' },
    }).then(({ body }) => {
      countySubCountyCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'Lead Assurance',
        institutionName: 'silver online',
        shortName: 'Drives connect',
        category: 'brand Lead',
        institutionCategory: 'Money Mexico',
        institutionOwnership: 'deposit',
        dateLicensed: '2022-04-06',
        institutionStatus: 'migration Tobago',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'Functionality robust',
        bankName: 'full-range solid',
        branchCode: 'Dollar parse',
        branchName: 'PCI Small',
        notes: 'Jersey Corporate',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/outlet-types',
      body: { outletTypeCode: 'sensor Intelligent', outletType: 'transparent', outletTypeDetails: 'customized Refined' },
    }).then(({ body }) => {
      outletType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/outlet-statuses',
      body: { branchStatusTypeCode: 'Open-source pixel utilize', branchStatusType: 'INACTIVE', branchStatusTypeDescription: 'green' },
    }).then(({ body }) => {
      outletStatus = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/particulars-of-outlets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/particulars-of-outlets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/particulars-of-outlets/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
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

    cy.intercept('GET', '/api/outlet-types', {
      statusCode: 200,
      body: [outletType],
    });

    cy.intercept('GET', '/api/outlet-statuses', {
      statusCode: 200,
      body: [outletStatus],
    });
  });

  afterEach(() => {
    if (particularsOfOutlet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/particulars-of-outlets/${particularsOfOutlet.id}`,
      }).then(() => {
        particularsOfOutlet = undefined;
      });
    }
  });

  afterEach(() => {
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
    if (outletType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/outlet-types/${outletType.id}`,
      }).then(() => {
        outletType = undefined;
      });
    }
    if (outletStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/outlet-statuses/${outletStatus.id}`,
      }).then(() => {
        outletStatus = undefined;
      });
    }
  });

  it('ParticularsOfOutlets menu should load ParticularsOfOutlets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('particulars-of-outlet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ParticularsOfOutlet').should('exist');
    cy.url().should('match', particularsOfOutletPageUrlPattern);
  });

  describe('ParticularsOfOutlet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(particularsOfOutletPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ParticularsOfOutlet page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/particulars-of-outlet/new$'));
        cy.getEntityCreateUpdateHeading('ParticularsOfOutlet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', particularsOfOutletPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/particulars-of-outlets',

          body: {
            ...particularsOfOutletSample,
            subCountyCode: countySubCountyCode,
            bankCode: institutionCode,
            outletId: bankBranchCode,
            typeOfOutlet: outletType,
            outletStatus: outletStatus,
          },
        }).then(({ body }) => {
          particularsOfOutlet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/particulars-of-outlets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [particularsOfOutlet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(particularsOfOutletPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ParticularsOfOutlet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('particularsOfOutlet');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', particularsOfOutletPageUrlPattern);
      });

      it('edit button click should load edit ParticularsOfOutlet page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ParticularsOfOutlet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', particularsOfOutletPageUrlPattern);
      });

      it('last delete button click should delete instance of ParticularsOfOutlet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('particularsOfOutlet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', particularsOfOutletPageUrlPattern);

        particularsOfOutlet = undefined;
      });
    });
  });

  describe('new ParticularsOfOutlet page', () => {
    beforeEach(() => {
      cy.visit(`${particularsOfOutletPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ParticularsOfOutlet');
    });

    it('should create an instance of ParticularsOfOutlet', () => {
      cy.get(`[data-cy="businessReportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="outletName"]`).type('Manager').should('have.value', 'Manager');

      cy.get(`[data-cy="town"]`).type('Croatia Branding robust').should('have.value', 'Croatia Branding robust');

      cy.get(`[data-cy="iso6709Latitute"]`).type('4229').should('have.value', '4229');

      cy.get(`[data-cy="iso6709Longitude"]`).type('32037').should('have.value', '32037');

      cy.get(`[data-cy="cbkApprovalDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="outletOpeningDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="outletClosureDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="licenseFeePayable"]`).type('20417').should('have.value', '20417');

      cy.get(`[data-cy="subCountyCode"]`).select(1);
      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="outletId"]`).select(1);
      cy.get(`[data-cy="typeOfOutlet"]`).select(1);
      cy.get(`[data-cy="outletStatus"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        particularsOfOutlet = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', particularsOfOutletPageUrlPattern);
    });
  });
});
