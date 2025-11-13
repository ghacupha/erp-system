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

describe('ChannelType e2e test', () => {
  const channelTypePageUrl = '/channel-type';
  const channelTypePageUrlPattern = new RegExp('/channel-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const channelTypeSample = { channelsTypeCode: 'purple', channelTypes: 'Virginia Cambridgeshire Handmade' };

  let channelType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/channel-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/channel-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/channel-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (channelType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/channel-types/${channelType.id}`,
      }).then(() => {
        channelType = undefined;
      });
    }
  });

  it('ChannelTypes menu should load ChannelTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('channel-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChannelType').should('exist');
    cy.url().should('match', channelTypePageUrlPattern);
  });

  describe('ChannelType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(channelTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChannelType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/channel-type/new$'));
        cy.getEntityCreateUpdateHeading('ChannelType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', channelTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/channel-types',
          body: channelTypeSample,
        }).then(({ body }) => {
          channelType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/channel-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [channelType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(channelTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChannelType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('channelType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', channelTypePageUrlPattern);
      });

      it('edit button click should load edit ChannelType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChannelType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', channelTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ChannelType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('channelType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', channelTypePageUrlPattern);

        channelType = undefined;
      });
    });
  });

  describe('new ChannelType page', () => {
    beforeEach(() => {
      cy.visit(`${channelTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ChannelType');
    });

    it('should create an instance of ChannelType', () => {
      cy.get(`[data-cy="channelsTypeCode"]`).type('Uganda Computers').should('have.value', 'Uganda Computers');

      cy.get(`[data-cy="channelTypes"]`).type('withdrawal').should('have.value', 'withdrawal');

      cy.get(`[data-cy="channelTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        channelType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', channelTypePageUrlPattern);
    });
  });
});
