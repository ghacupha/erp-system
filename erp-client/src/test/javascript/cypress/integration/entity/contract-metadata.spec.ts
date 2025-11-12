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

describe('ContractMetadata e2e test', () => {
  const contractMetadataPageUrl = '/contract-metadata';
  const contractMetadataPageUrlPattern = new RegExp('/contract-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const contractMetadataSample = {
    typeOfContract: 'BUSINESS_PARTNER',
    contractStatus: 'ACTIVE',
    startDate: '2023-03-20',
    terminationDate: '2023-03-21',
    contractTitle: 'Card Bhutan Metal',
    contractIdentifier: 'ddf0c81d-1bc7-4113-932a-1de9ab401214',
    contractIdentifierShort: 'Wall orchid Borders',
  };

  let contractMetadata: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contract-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contract-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contract-metadata/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contractMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contract-metadata/${contractMetadata.id}`,
      }).then(() => {
        contractMetadata = undefined;
      });
    }
  });

  it('ContractMetadata menu should load ContractMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contract-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContractMetadata').should('exist');
    cy.url().should('match', contractMetadataPageUrlPattern);
  });

  describe('ContractMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contractMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContractMetadata page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/contract-metadata/new$'));
        cy.getEntityCreateUpdateHeading('ContractMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contract-metadata',
          body: contractMetadataSample,
        }).then(({ body }) => {
          contractMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contract-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contractMetadata],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contractMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContractMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contractMetadata');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractMetadataPageUrlPattern);
      });

      it('edit button click should load edit ContractMetadata page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractMetadataPageUrlPattern);
      });

      it('last delete button click should delete instance of ContractMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contractMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractMetadataPageUrlPattern);

        contractMetadata = undefined;
      });
    });
  });

  describe('new ContractMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${contractMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ContractMetadata');
    });

    it('should create an instance of ContractMetadata', () => {
      cy.get(`[data-cy="description"]`).type('Armenia').should('have.value', 'Armenia');

      cy.get(`[data-cy="typeOfContract"]`).select('CUSTOMER');

      cy.get(`[data-cy="contractStatus"]`).select('UNDER_RENEGOTIATION');

      cy.get(`[data-cy="startDate"]`).type('2023-03-21').should('have.value', '2023-03-21');

      cy.get(`[data-cy="terminationDate"]`).type('2023-03-21').should('have.value', '2023-03-21');

      cy.get(`[data-cy="commentsAndAttachment"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="contractTitle"]`).type('Vermont Kansas').should('have.value', 'Vermont Kansas');

      cy.get(`[data-cy="contractIdentifier"]`)
        .type('7affaa8a-51cc-4374-9e5b-896b121897cf')
        .invoke('val')
        .should('match', new RegExp('7affaa8a-51cc-4374-9e5b-896b121897cf'));

      cy.get(`[data-cy="contractIdentifierShort"]`).type('Salad open-source').should('have.value', 'Salad open-source');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        contractMetadata = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', contractMetadataPageUrlPattern);
    });
  });
});
