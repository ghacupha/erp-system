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

import { DepartmentTypeComponentsPage, DepartmentTypeDeleteDialog, DepartmentTypeUpdatePage } from './department-type.page-object';

const expect = chai.expect;

describe('DepartmentType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let departmentTypeComponentsPage: DepartmentTypeComponentsPage;
  let departmentTypeUpdatePage: DepartmentTypeUpdatePage;
  let departmentTypeDeleteDialog: DepartmentTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepartmentTypes', async () => {
    await navBarPage.goToEntity('department-type');
    departmentTypeComponentsPage = new DepartmentTypeComponentsPage();
    await browser.wait(ec.visibilityOf(departmentTypeComponentsPage.title), 5000);
    expect(await departmentTypeComponentsPage.getTitle()).to.eq('Department Types');
    await browser.wait(
      ec.or(ec.visibilityOf(departmentTypeComponentsPage.entities), ec.visibilityOf(departmentTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepartmentType page', async () => {
    await departmentTypeComponentsPage.clickOnCreateButton();
    departmentTypeUpdatePage = new DepartmentTypeUpdatePage();
    expect(await departmentTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Department Type');
    await departmentTypeUpdatePage.cancel();
  });

  it('should create and save DepartmentTypes', async () => {
    const nbButtonsBeforeCreate = await departmentTypeComponentsPage.countDeleteButtons();

    await departmentTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      departmentTypeUpdatePage.setDepartmentTypeCodeInput('departmentTypeCode'),
      departmentTypeUpdatePage.setDepartmentTypeInput('departmentType'),
      departmentTypeUpdatePage.setDepartmentTypeDetailsInput('departmentTypeDetails'),
      // departmentTypeUpdatePage.placeholderSelectLastOption(),
    ]);

    await departmentTypeUpdatePage.save();
    expect(await departmentTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await departmentTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepartmentType', async () => {
    const nbButtonsBeforeDelete = await departmentTypeComponentsPage.countDeleteButtons();
    await departmentTypeComponentsPage.clickOnLastDeleteButton();

    departmentTypeDeleteDialog = new DepartmentTypeDeleteDialog();
    expect(await departmentTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Department Type?');
    await departmentTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(departmentTypeComponentsPage.title), 5000);

    expect(await departmentTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
