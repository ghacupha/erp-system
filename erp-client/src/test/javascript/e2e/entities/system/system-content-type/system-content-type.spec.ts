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
  SystemContentTypeComponentsPage,
  SystemContentTypeDeleteDialog,
  SystemContentTypeUpdatePage,
} from './system-content-type.page-object';

const expect = chai.expect;

describe('SystemContentType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemContentTypeComponentsPage: SystemContentTypeComponentsPage;
  let systemContentTypeUpdatePage: SystemContentTypeUpdatePage;
  let systemContentTypeDeleteDialog: SystemContentTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SystemContentTypes', async () => {
    await navBarPage.goToEntity('system-content-type');
    systemContentTypeComponentsPage = new SystemContentTypeComponentsPage();
    await browser.wait(ec.visibilityOf(systemContentTypeComponentsPage.title), 5000);
    expect(await systemContentTypeComponentsPage.getTitle()).to.eq('System Content Types');
    await browser.wait(
      ec.or(ec.visibilityOf(systemContentTypeComponentsPage.entities), ec.visibilityOf(systemContentTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SystemContentType page', async () => {
    await systemContentTypeComponentsPage.clickOnCreateButton();
    systemContentTypeUpdatePage = new SystemContentTypeUpdatePage();
    expect(await systemContentTypeUpdatePage.getPageTitle()).to.eq('Create or edit a System Content Type');
    await systemContentTypeUpdatePage.cancel();
  });

  it('should create and save SystemContentTypes', async () => {
    const nbButtonsBeforeCreate = await systemContentTypeComponentsPage.countDeleteButtons();

    await systemContentTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      systemContentTypeUpdatePage.setContentTypeNameInput('contentTypeName'),
      systemContentTypeUpdatePage.setContentTypeHeaderInput('contentTypeHeader'),
      systemContentTypeUpdatePage.setCommentsInput('comments'),
      systemContentTypeUpdatePage.availabilitySelectLastOption(),
      // systemContentTypeUpdatePage.placeholdersSelectLastOption(),
      // systemContentTypeUpdatePage.sysMapsSelectLastOption(),
    ]);

    await systemContentTypeUpdatePage.save();
    expect(await systemContentTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await systemContentTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SystemContentType', async () => {
    const nbButtonsBeforeDelete = await systemContentTypeComponentsPage.countDeleteButtons();
    await systemContentTypeComponentsPage.clickOnLastDeleteButton();

    systemContentTypeDeleteDialog = new SystemContentTypeDeleteDialog();
    expect(await systemContentTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this System Content Type?');
    await systemContentTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(systemContentTypeComponentsPage.title), 5000);

    expect(await systemContentTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
