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

import {
  FixedAssetNetBookValueComponentsPage,
  FixedAssetNetBookValueDeleteDialog,
  FixedAssetNetBookValueUpdatePage,
} from './fixed-asset-net-book-value.page-object';

const expect = chai.expect;

describe('FixedAssetNetBookValue e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fixedAssetNetBookValueComponentsPage: FixedAssetNetBookValueComponentsPage;
  let fixedAssetNetBookValueUpdatePage: FixedAssetNetBookValueUpdatePage;
  let fixedAssetNetBookValueDeleteDialog: FixedAssetNetBookValueDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FixedAssetNetBookValues', async () => {
    await navBarPage.goToEntity('fixed-asset-net-book-value');
    fixedAssetNetBookValueComponentsPage = new FixedAssetNetBookValueComponentsPage();
    await browser.wait(ec.visibilityOf(fixedAssetNetBookValueComponentsPage.title), 5000);
    expect(await fixedAssetNetBookValueComponentsPage.getTitle()).to.eq('Fixed Asset Net Book Values');
    await browser.wait(
      ec.or(ec.visibilityOf(fixedAssetNetBookValueComponentsPage.entities), ec.visibilityOf(fixedAssetNetBookValueComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FixedAssetNetBookValue page', async () => {
    await fixedAssetNetBookValueComponentsPage.clickOnCreateButton();
    fixedAssetNetBookValueUpdatePage = new FixedAssetNetBookValueUpdatePage();
    expect(await fixedAssetNetBookValueUpdatePage.getPageTitle()).to.eq('Create or edit a Fixed Asset Net Book Value');
    await fixedAssetNetBookValueUpdatePage.cancel();
  });

  it('should create and save FixedAssetNetBookValues', async () => {
    const nbButtonsBeforeCreate = await fixedAssetNetBookValueComponentsPage.countDeleteButtons();

    await fixedAssetNetBookValueComponentsPage.clickOnCreateButton();

    await promise.all([
      fixedAssetNetBookValueUpdatePage.setAssetNumberInput('5'),
      fixedAssetNetBookValueUpdatePage.setServiceOutletCodeInput('serviceOutletCode'),
      fixedAssetNetBookValueUpdatePage.setAssetTagInput('assetTag'),
      fixedAssetNetBookValueUpdatePage.setAssetDescriptionInput('assetDescription'),
      fixedAssetNetBookValueUpdatePage.setNetBookValueDateInput('2000-12-31'),
      fixedAssetNetBookValueUpdatePage.setAssetCategoryInput('assetCategory'),
      fixedAssetNetBookValueUpdatePage.setNetBookValueInput('5'),
      fixedAssetNetBookValueUpdatePage.depreciationRegimeSelectLastOption(),
      fixedAssetNetBookValueUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      fixedAssetNetBookValueUpdatePage.setCompilationTokenInput('compilationToken'),
      // fixedAssetNetBookValueUpdatePage.placeholderSelectLastOption(),
    ]);

    await fixedAssetNetBookValueUpdatePage.save();
    expect(await fixedAssetNetBookValueUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fixedAssetNetBookValueComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FixedAssetNetBookValue', async () => {
    const nbButtonsBeforeDelete = await fixedAssetNetBookValueComponentsPage.countDeleteButtons();
    await fixedAssetNetBookValueComponentsPage.clickOnLastDeleteButton();

    fixedAssetNetBookValueDeleteDialog = new FixedAssetNetBookValueDeleteDialog();
    expect(await fixedAssetNetBookValueDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Fixed Asset Net Book Value?'
    );
    await fixedAssetNetBookValueDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fixedAssetNetBookValueComponentsPage.title), 5000);

    expect(await fixedAssetNetBookValueComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
