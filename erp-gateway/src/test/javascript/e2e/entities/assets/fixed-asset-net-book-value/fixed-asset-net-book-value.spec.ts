///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
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

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
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
    ]);

    expect(await fixedAssetNetBookValueUpdatePage.getAssetNumberInput()).to.eq('5', 'Expected assetNumber value to be equals to 5');
    expect(await fixedAssetNetBookValueUpdatePage.getServiceOutletCodeInput()).to.eq(
      'serviceOutletCode',
      'Expected ServiceOutletCode value to be equals to serviceOutletCode'
    );
    expect(await fixedAssetNetBookValueUpdatePage.getAssetTagInput()).to.eq('assetTag', 'Expected AssetTag value to be equals to assetTag');
    expect(await fixedAssetNetBookValueUpdatePage.getAssetDescriptionInput()).to.eq(
      'assetDescription',
      'Expected AssetDescription value to be equals to assetDescription'
    );
    expect(await fixedAssetNetBookValueUpdatePage.getNetBookValueDateInput()).to.eq(
      '2000-12-31',
      'Expected netBookValueDate value to be equals to 2000-12-31'
    );
    expect(await fixedAssetNetBookValueUpdatePage.getAssetCategoryInput()).to.eq(
      'assetCategory',
      'Expected AssetCategory value to be equals to assetCategory'
    );
    expect(await fixedAssetNetBookValueUpdatePage.getNetBookValueInput()).to.eq('5', 'Expected netBookValue value to be equals to 5');
    expect(await fixedAssetNetBookValueUpdatePage.getFileUploadTokenInput()).to.eq(
      'fileUploadToken',
      'Expected FileUploadToken value to be equals to fileUploadToken'
    );
    expect(await fixedAssetNetBookValueUpdatePage.getCompilationTokenInput()).to.eq(
      'compilationToken',
      'Expected CompilationToken value to be equals to compilationToken'
    );

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

    expect(await fixedAssetNetBookValueComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
