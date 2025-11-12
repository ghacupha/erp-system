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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ManagementMemberTypeComponentsPage,
  ManagementMemberTypeDeleteDialog,
  ManagementMemberTypeUpdatePage,
} from './management-member-type.page-object';

const expect = chai.expect;

describe('ManagementMemberType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let managementMemberTypeComponentsPage: ManagementMemberTypeComponentsPage;
  let managementMemberTypeUpdatePage: ManagementMemberTypeUpdatePage;
  let managementMemberTypeDeleteDialog: ManagementMemberTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ManagementMemberTypes', async () => {
    await navBarPage.goToEntity('management-member-type');
    managementMemberTypeComponentsPage = new ManagementMemberTypeComponentsPage();
    await browser.wait(ec.visibilityOf(managementMemberTypeComponentsPage.title), 5000);
    expect(await managementMemberTypeComponentsPage.getTitle()).to.eq('Management Member Types');
    await browser.wait(
      ec.or(ec.visibilityOf(managementMemberTypeComponentsPage.entities), ec.visibilityOf(managementMemberTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ManagementMemberType page', async () => {
    await managementMemberTypeComponentsPage.clickOnCreateButton();
    managementMemberTypeUpdatePage = new ManagementMemberTypeUpdatePage();
    expect(await managementMemberTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Management Member Type');
    await managementMemberTypeUpdatePage.cancel();
  });

  it('should create and save ManagementMemberTypes', async () => {
    const nbButtonsBeforeCreate = await managementMemberTypeComponentsPage.countDeleteButtons();

    await managementMemberTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      managementMemberTypeUpdatePage.setManagementMemberTypeCodeInput('managementMemberTypeCode'),
      managementMemberTypeUpdatePage.setManagementMemberTypeInput('managementMemberType'),
    ]);

    await managementMemberTypeUpdatePage.save();
    expect(await managementMemberTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await managementMemberTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ManagementMemberType', async () => {
    const nbButtonsBeforeDelete = await managementMemberTypeComponentsPage.countDeleteButtons();
    await managementMemberTypeComponentsPage.clickOnLastDeleteButton();

    managementMemberTypeDeleteDialog = new ManagementMemberTypeDeleteDialog();
    expect(await managementMemberTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Management Member Type?');
    await managementMemberTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(managementMemberTypeComponentsPage.title), 5000);

    expect(await managementMemberTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
