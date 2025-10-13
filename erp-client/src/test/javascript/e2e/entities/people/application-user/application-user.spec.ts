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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ApplicationUserComponentsPage,
  /* ApplicationUserDeleteDialog, */
  ApplicationUserUpdatePage,
} from './application-user.page-object';

const expect = chai.expect;

describe('ApplicationUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let applicationUserComponentsPage: ApplicationUserComponentsPage;
  let applicationUserUpdatePage: ApplicationUserUpdatePage;
  /* let applicationUserDeleteDialog: ApplicationUserDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ApplicationUsers', async () => {
    await navBarPage.goToEntity('application-user');
    applicationUserComponentsPage = new ApplicationUserComponentsPage();
    await browser.wait(ec.visibilityOf(applicationUserComponentsPage.title), 5000);
    expect(await applicationUserComponentsPage.getTitle()).to.eq('Application Users');
    await browser.wait(
      ec.or(ec.visibilityOf(applicationUserComponentsPage.entities), ec.visibilityOf(applicationUserComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ApplicationUser page', async () => {
    await applicationUserComponentsPage.clickOnCreateButton();
    applicationUserUpdatePage = new ApplicationUserUpdatePage();
    expect(await applicationUserUpdatePage.getPageTitle()).to.eq('Create or edit a Application User');
    await applicationUserUpdatePage.cancel();
  });

  /* it('should create and save ApplicationUsers', async () => {
        const nbButtonsBeforeCreate = await applicationUserComponentsPage.countDeleteButtons();

        await applicationUserComponentsPage.clickOnCreateButton();

        await promise.all([
            applicationUserUpdatePage.setDesignationInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            applicationUserUpdatePage.setApplicationIdentityInput('applicationIdentity'),
            applicationUserUpdatePage.organizationSelectLastOption(),
            applicationUserUpdatePage.departmentSelectLastOption(),
            applicationUserUpdatePage.securityClearanceSelectLastOption(),
            applicationUserUpdatePage.systemIdentitySelectLastOption(),
            // applicationUserUpdatePage.userPropertiesSelectLastOption(),
            applicationUserUpdatePage.dealerIdentitySelectLastOption(),
        ]);

        await applicationUserUpdatePage.save();
        expect(await applicationUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await applicationUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ApplicationUser', async () => {
        const nbButtonsBeforeDelete = await applicationUserComponentsPage.countDeleteButtons();
        await applicationUserComponentsPage.clickOnLastDeleteButton();

        applicationUserDeleteDialog = new ApplicationUserDeleteDialog();
        expect(await applicationUserDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Application User?');
        await applicationUserDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(applicationUserComponentsPage.title), 5000);

        expect(await applicationUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
