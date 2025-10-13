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

import { SystemModuleComponentsPage, SystemModuleDeleteDialog, SystemModuleUpdatePage } from './system-module.page-object';

const expect = chai.expect;

describe('SystemModule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemModuleComponentsPage: SystemModuleComponentsPage;
  let systemModuleUpdatePage: SystemModuleUpdatePage;
  let systemModuleDeleteDialog: SystemModuleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SystemModules', async () => {
    await navBarPage.goToEntity('system-module');
    systemModuleComponentsPage = new SystemModuleComponentsPage();
    await browser.wait(ec.visibilityOf(systemModuleComponentsPage.title), 5000);
    expect(await systemModuleComponentsPage.getTitle()).to.eq('System Modules');
    await browser.wait(
      ec.or(ec.visibilityOf(systemModuleComponentsPage.entities), ec.visibilityOf(systemModuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SystemModule page', async () => {
    await systemModuleComponentsPage.clickOnCreateButton();
    systemModuleUpdatePage = new SystemModuleUpdatePage();
    expect(await systemModuleUpdatePage.getPageTitle()).to.eq('Create or edit a System Module');
    await systemModuleUpdatePage.cancel();
  });

  it('should create and save SystemModules', async () => {
    const nbButtonsBeforeCreate = await systemModuleComponentsPage.countDeleteButtons();

    await systemModuleComponentsPage.clickOnCreateButton();

    await promise.all([systemModuleUpdatePage.setModuleNameInput('moduleName')]);

    await systemModuleUpdatePage.save();
    expect(await systemModuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await systemModuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SystemModule', async () => {
    const nbButtonsBeforeDelete = await systemModuleComponentsPage.countDeleteButtons();
    await systemModuleComponentsPage.clickOnLastDeleteButton();

    systemModuleDeleteDialog = new SystemModuleDeleteDialog();
    expect(await systemModuleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this System Module?');
    await systemModuleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(systemModuleComponentsPage.title), 5000);

    expect(await systemModuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
