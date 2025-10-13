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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { StaffRoleTypeComponentsPage, StaffRoleTypeDeleteDialog, StaffRoleTypeUpdatePage } from './staff-role-type.page-object';

const expect = chai.expect;

describe('StaffRoleType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let staffRoleTypeComponentsPage: StaffRoleTypeComponentsPage;
  let staffRoleTypeUpdatePage: StaffRoleTypeUpdatePage;
  let staffRoleTypeDeleteDialog: StaffRoleTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load StaffRoleTypes', async () => {
    await navBarPage.goToEntity('staff-role-type');
    staffRoleTypeComponentsPage = new StaffRoleTypeComponentsPage();
    await browser.wait(ec.visibilityOf(staffRoleTypeComponentsPage.title), 5000);
    expect(await staffRoleTypeComponentsPage.getTitle()).to.eq('Staff Role Types');
    await browser.wait(
      ec.or(ec.visibilityOf(staffRoleTypeComponentsPage.entities), ec.visibilityOf(staffRoleTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create StaffRoleType page', async () => {
    await staffRoleTypeComponentsPage.clickOnCreateButton();
    staffRoleTypeUpdatePage = new StaffRoleTypeUpdatePage();
    expect(await staffRoleTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Staff Role Type');
    await staffRoleTypeUpdatePage.cancel();
  });

  it('should create and save StaffRoleTypes', async () => {
    const nbButtonsBeforeCreate = await staffRoleTypeComponentsPage.countDeleteButtons();

    await staffRoleTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      staffRoleTypeUpdatePage.setStaffRoleTypeCodeInput('staffRoleTypeCode'),
      staffRoleTypeUpdatePage.setStaffRoleTypeInput('staffRoleType'),
      staffRoleTypeUpdatePage.setStaffRoleTypeDetailsInput('staffRoleTypeDetails'),
    ]);

    await staffRoleTypeUpdatePage.save();
    expect(await staffRoleTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await staffRoleTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last StaffRoleType', async () => {
    const nbButtonsBeforeDelete = await staffRoleTypeComponentsPage.countDeleteButtons();
    await staffRoleTypeComponentsPage.clickOnLastDeleteButton();

    staffRoleTypeDeleteDialog = new StaffRoleTypeDeleteDialog();
    expect(await staffRoleTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Staff Role Type?');
    await staffRoleTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(staffRoleTypeComponentsPage.title), 5000);

    expect(await staffRoleTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
