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

describe('FileUpload e2e test', () => {
  const fileUploadPageUrl = '/file-upload';
  const fileUploadPageUrlPattern = new RegExp('/file-upload(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fileUploadSample = {
    description: 'Cambridgeshire optical Metal',
    fileName: 'transmitter',
    fileTypeId: 1812,
    dataFile: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    dataFileContentType: 'unknown',
  };

  let fileUpload: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/file-uploads+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/file-uploads').as('postEntityRequest');
    cy.intercept('DELETE', '/api/file-uploads/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fileUpload) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/file-uploads/${fileUpload.id}`,
      }).then(() => {
        fileUpload = undefined;
      });
    }
  });

  it('FileUploads menu should load FileUploads page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('file-upload');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FileUpload').should('exist');
    cy.url().should('match', fileUploadPageUrlPattern);
  });

  describe('FileUpload page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fileUploadPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FileUpload page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/file-upload/new$'));
        cy.getEntityCreateUpdateHeading('FileUpload');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileUploadPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/file-uploads',
          body: fileUploadSample,
        }).then(({ body }) => {
          fileUpload = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/file-uploads+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fileUpload],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fileUploadPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FileUpload page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fileUpload');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileUploadPageUrlPattern);
      });

      it('edit button click should load edit FileUpload page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FileUpload');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileUploadPageUrlPattern);
      });

      it('last delete button click should delete instance of FileUpload', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fileUpload').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fileUploadPageUrlPattern);

        fileUpload = undefined;
      });
    });
  });

  describe('new FileUpload page', () => {
    beforeEach(() => {
      cy.visit(`${fileUploadPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FileUpload');
    });

    it('should create an instance of FileUpload', () => {
      cy.get(`[data-cy="description"]`).type('Soft').should('have.value', 'Soft');

      cy.get(`[data-cy="fileName"]`).type('multi-tasking Vatu').should('have.value', 'multi-tasking Vatu');

      cy.get(`[data-cy="periodFrom"]`).type('2021-04-14').should('have.value', '2021-04-14');

      cy.get(`[data-cy="periodTo"]`).type('2021-04-14').should('have.value', '2021-04-14');

      cy.get(`[data-cy="fileTypeId"]`).type('27990').should('have.value', '27990');

      cy.setFieldImageAsBytesOfEntity('dataFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="uploadSuccessful"]`).should('not.be.checked');
      cy.get(`[data-cy="uploadSuccessful"]`).click().should('be.checked');

      cy.get(`[data-cy="uploadProcessed"]`).should('not.be.checked');
      cy.get(`[data-cy="uploadProcessed"]`).click().should('be.checked');

      cy.get(`[data-cy="uploadToken"]`).type('Optimization vertical backing').should('have.value', 'Optimization vertical backing');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fileUpload = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fileUploadPageUrlPattern);
    });
  });
});
