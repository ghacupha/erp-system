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

describe('WorkProjectRegister e2e test', () => {
  const workProjectRegisterPageUrl = '/work-project-register';
  const workProjectRegisterPageUrlPattern = new RegExp('/work-project-register(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const workProjectRegisterSample = { catalogueNumber: 'AGP Markets synergies', projectTitle: 'architecture' };

  let workProjectRegister: any;
  let dealer: any;

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
      url: '/api/dealers',
      body: {
        dealerName: 'tan function Namibia',
        taxNumber: 'Division circuit Borders',
        identificationDocumentNumber: 'Won navigate',
        organizationName: 'orange national Soap',
        department: 'global Morocco',
        position: 'Corporate Buckinghamshire virtual',
        postalAddress: 'metrics granular digital',
        physicalAddress: 'Enhanced generate Bahraini',
        accountName: 'Personal Loan Account',
        accountNumber: 'target',
        bankersName: 'ivory Generic Quality',
        bankersBranch: 'and Upgradable',
        bankersSwiftCode: 'Industrial Account',
        fileUploadToken: 'e-enable Quality',
        compilationToken: 'scalable Front-line',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'payment',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/work-project-registers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/work-project-registers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/work-project-registers/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (workProjectRegister) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/work-project-registers/${workProjectRegister.id}`,
      }).then(() => {
        workProjectRegister = undefined;
      });
    }
  });

  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('WorkProjectRegisters menu should load WorkProjectRegisters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('work-project-register');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkProjectRegister').should('exist');
    cy.url().should('match', workProjectRegisterPageUrlPattern);
  });

  describe('WorkProjectRegister page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workProjectRegisterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WorkProjectRegister page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/work-project-register/new$'));
        cy.getEntityCreateUpdateHeading('WorkProjectRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workProjectRegisterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/work-project-registers',

          body: {
            ...workProjectRegisterSample,
            dealers: dealer,
          },
        }).then(({ body }) => {
          workProjectRegister = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/work-project-registers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [workProjectRegister],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(workProjectRegisterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WorkProjectRegister page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workProjectRegister');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workProjectRegisterPageUrlPattern);
      });

      it('edit button click should load edit WorkProjectRegister page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorkProjectRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workProjectRegisterPageUrlPattern);
      });

      it('last delete button click should delete instance of WorkProjectRegister', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('workProjectRegister').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workProjectRegisterPageUrlPattern);

        workProjectRegister = undefined;
      });
    });
  });

  describe('new WorkProjectRegister page', () => {
    beforeEach(() => {
      cy.visit(`${workProjectRegisterPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WorkProjectRegister');
    });

    it('should create an instance of WorkProjectRegister', () => {
      cy.get(`[data-cy="catalogueNumber"]`).type('backing actuating').should('have.value', 'backing actuating');

      cy.get(`[data-cy="projectTitle"]`).type('product silver').should('have.value', 'product silver');

      cy.get(`[data-cy="description"]`).type('program red incremental').should('have.value', 'program red incremental');

      cy.setFieldImageAsBytesOfEntity('details', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="totalProjectCost"]`).type('62830').should('have.value', '62830');

      cy.setFieldImageAsBytesOfEntity('additionalNotes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="dealers"]`).select([0]);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        workProjectRegister = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', workProjectRegisterPageUrlPattern);
    });
  });
});
