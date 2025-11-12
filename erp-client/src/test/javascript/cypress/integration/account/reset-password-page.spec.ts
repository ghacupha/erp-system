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

import {
  usernameLoginSelector,
  forgetYourPasswordSelector,
  emailResetPasswordSelector,
  submitInitResetPasswordSelector,
  classInvalid,
  classValid,
} from '../../support/commands';

describe('forgot your password', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookies();
    cy.visit('');
    cy.clickOnLoginItem();
    cy.get(usernameLoginSelector).type(username);
    cy.get(forgetYourPasswordSelector).click();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/account/reset-password/init').as('initResetPassword');
  });

  it('requires email', () => {
    cy.get(emailResetPasswordSelector).should('have.class', classInvalid).type('user@gmail.com');
    cy.get(emailResetPasswordSelector).should('have.class', classValid);
    cy.get(emailResetPasswordSelector).clear();
  });

  it('should be able to init reset password', () => {
    cy.get(emailResetPasswordSelector).type('user@gmail.com');
    cy.get(submitInitResetPasswordSelector).click({ force: true });
    cy.wait('@initResetPassword').then(({ response }) => expect(response.statusCode).to.equal(200));
  });
});
