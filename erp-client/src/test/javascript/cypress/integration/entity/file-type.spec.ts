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

describe('FileType e2e test', () => {
  const fileTypePageUrl = '/file-type';
  const fileTypePageUrlPattern = new RegExp('/file-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fileTypeSample = { fileTypeName: 'Strategist', fileMediumType: 'EXCEL_XLS', fileType: 'PAYMENT' };

  let fileType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/file-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/file-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/file-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fileType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/file-types/${fileType.id}`,
      }).then(() => {
        fileType = undefined;
      });
    }
  });

  it('FileTypes menu should load FileTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FileType').should('exist');
    cy.url().should('match', fileTypePageUrlPattern);
  });

  describe('FileType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fileTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FileType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/file-type/new$'));
        cy.getEntityCreateUpdateHeading('FileType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/file-types',
          body: fileTypeSample,
        }).then(({ body }) => {
          fileType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/file-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fileType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fileTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FileType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fileType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileTypePageUrlPattern);
      });

      it('edit button click should load edit FileType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FileType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fileType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileTypePageUrlPattern);

        fileType = undefined;
      });
    });
  });

  describe('new FileType page', () => {
    beforeEach(() => {
      cy.visit(`${fileTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FileType');
    });

    it('should create an instance of FileType', () => {
      cy.get(`[data-cy="fileTypeName"]`).type('Florida Factors Computers').should('have.value', 'Florida Factors Computers');

      cy.get(`[data-cy="fileMediumType"]`).select('POWERPOINT');

      cy.get(`[data-cy="description"]`).type('Wooden deposit Books').should('have.value', 'Wooden deposit Books');

      cy.setFieldImageAsBytesOfEntity('fileTemplate', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileType"]`).select('FIXED_ASSET_NBV');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fileType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fileTypePageUrlPattern);
    });
  });
});
