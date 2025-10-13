///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import {
  titleLoginSelector,
  errorLoginSelector,
  usernameLoginSelector,
  passwordLoginSelector,
  submitLoginSelector,
} from '../../support/commands';

describe('login modal', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookies();
    cy.visit('');
    cy.clickOnLoginItem();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/authenticate').as('authenticate');
  });

  it('greets with signin', () => {
    cy.get(titleLoginSelector).should('be.visible');
  });

  it('requires username', () => {
    cy.get(passwordLoginSelector).type('a-password');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(400));
    // login page should stay open when login fails
    cy.get(titleLoginSelector).should('be.visible');
    cy.get(passwordLoginSelector).clear();
  });

  it('requires password', () => {
    cy.get(usernameLoginSelector).type('a-login');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(400));
    cy.get(errorLoginSelector).should('be.visible');
    cy.get(usernameLoginSelector).clear();
  });

  it('errors when password is incorrect', () => {
    cy.get(usernameLoginSelector).type(username);
    cy.get(passwordLoginSelector).type('bad-password');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(401));
    cy.get(errorLoginSelector).should('be.visible');
    cy.get(usernameLoginSelector).clear();
    cy.get(passwordLoginSelector).clear();
  });

  it('go to login page when successfully logs in', () => {
    cy.get(usernameLoginSelector).type(username);
    cy.get(passwordLoginSelector).type(password);
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response.statusCode).to.equal(200));
    cy.hash().should('eq', '');
  });
});
