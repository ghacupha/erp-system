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

import {
  currentPasswordSelector,
  newPasswordSelector,
  confirmPasswordSelector,
  submitPasswordSelector,
  classInvalid,
  classValid,
} from '../../support/commands';

describe('/account/password', () => {
  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookies();
    cy.visit('');
    cy.login('user', 'user');
    cy.clickOnPasswordItem();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/account/change-password').as('passwordSave');
  });

  it('requires current password', () => {
    cy.get(currentPasswordSelector).should('have.class', classInvalid).type('wrong-current-password');
    cy.get(currentPasswordSelector).should('have.class', classValid);
    cy.get(currentPasswordSelector).clear();
  });

  it('requires new password', () => {
    cy.get(newPasswordSelector).should('have.class', classInvalid).type('jhipster');
    cy.get(newPasswordSelector).should('have.class', classValid);
    cy.get(newPasswordSelector).clear();
  });

  it('requires confirm new password', () => {
    cy.get(newPasswordSelector).type('jhipster');
    cy.get(confirmPasswordSelector).should('have.class', classInvalid).type('jhipster');
    cy.get(confirmPasswordSelector).should('have.class', classValid);
    cy.get(newPasswordSelector).clear();
    cy.get(confirmPasswordSelector).clear();
  });

  it('should fail to update password when using incorrect current password', () => {
    cy.get(currentPasswordSelector).type('wrong-current-password');
    cy.get(newPasswordSelector).type('jhipster');
    cy.get(confirmPasswordSelector).type('jhipster');
    cy.get(submitPasswordSelector).click({ force: true });
    cy.wait('@passwordSave').then(({ response }) => expect(response.statusCode).to.equal(400));
    cy.get(currentPasswordSelector).clear();
    cy.get(newPasswordSelector).clear();
    cy.get(confirmPasswordSelector).clear();
  });

  it('should be able to update password', () => {
    cy.get(currentPasswordSelector).type('user');
    cy.get(newPasswordSelector).type('user');
    cy.get(confirmPasswordSelector).type('user');
    cy.get(submitPasswordSelector).click({ force: true });
    cy.wait('@passwordSave').then(({ response }) => expect(response.statusCode).to.equal(200));
  });
});
