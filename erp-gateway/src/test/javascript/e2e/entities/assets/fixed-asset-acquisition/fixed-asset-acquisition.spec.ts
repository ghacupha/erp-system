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
  FixedAssetAcquisitionComponentsPage,
  FixedAssetAcquisitionDeleteDialog,
  FixedAssetAcquisitionUpdatePage,
} from './fixed-asset-acquisition.page-object';

const expect = chai.expect;

describe('FixedAssetAcquisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fixedAssetAcquisitionComponentsPage: FixedAssetAcquisitionComponentsPage;
  let fixedAssetAcquisitionUpdatePage: FixedAssetAcquisitionUpdatePage;
  let fixedAssetAcquisitionDeleteDialog: FixedAssetAcquisitionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FixedAssetAcquisitions', async () => {
    await navBarPage.goToEntity('fixed-asset-acquisition');
    fixedAssetAcquisitionComponentsPage = new FixedAssetAcquisitionComponentsPage();
    await browser.wait(ec.visibilityOf(fixedAssetAcquisitionComponentsPage.title), 5000);
    expect(await fixedAssetAcquisitionComponentsPage.getTitle()).to.eq('Fixed Asset Acquisitions');
    await browser.wait(
      ec.or(ec.visibilityOf(fixedAssetAcquisitionComponentsPage.entities), ec.visibilityOf(fixedAssetAcquisitionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FixedAssetAcquisition page', async () => {
    await fixedAssetAcquisitionComponentsPage.clickOnCreateButton();
    fixedAssetAcquisitionUpdatePage = new FixedAssetAcquisitionUpdatePage();
    expect(await fixedAssetAcquisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Fixed Asset Acquisition');
    await fixedAssetAcquisitionUpdatePage.cancel();
  });

  it('should create and save FixedAssetAcquisitions', async () => {
    const nbButtonsBeforeCreate = await fixedAssetAcquisitionComponentsPage.countDeleteButtons();

    await fixedAssetAcquisitionComponentsPage.clickOnCreateButton();

    await promise.all([
      fixedAssetAcquisitionUpdatePage.setAssetNumberInput('5'),
      fixedAssetAcquisitionUpdatePage.setServiceOutletCodeInput('serviceOutletCode'),
      fixedAssetAcquisitionUpdatePage.setAssetTagInput('assetTag'),
      fixedAssetAcquisitionUpdatePage.setAssetDescriptionInput('assetDescription'),
      fixedAssetAcquisitionUpdatePage.setPurchaseDateInput('2000-12-31'),
      fixedAssetAcquisitionUpdatePage.setAssetCategoryInput('assetCategory'),
      fixedAssetAcquisitionUpdatePage.setPurchasePriceInput('5'),
      fixedAssetAcquisitionUpdatePage.setFileUploadTokenInput('fileUploadToken'),
    ]);

    expect(await fixedAssetAcquisitionUpdatePage.getAssetNumberInput()).to.eq('5', 'Expected assetNumber value to be equals to 5');
    expect(await fixedAssetAcquisitionUpdatePage.getServiceOutletCodeInput()).to.eq(
      'serviceOutletCode',
      'Expected ServiceOutletCode value to be equals to serviceOutletCode'
    );
    expect(await fixedAssetAcquisitionUpdatePage.getAssetTagInput()).to.eq('assetTag', 'Expected AssetTag value to be equals to assetTag');
    expect(await fixedAssetAcquisitionUpdatePage.getAssetDescriptionInput()).to.eq(
      'assetDescription',
      'Expected AssetDescription value to be equals to assetDescription'
    );
    expect(await fixedAssetAcquisitionUpdatePage.getPurchaseDateInput()).to.eq(
      '2000-12-31',
      'Expected purchaseDate value to be equals to 2000-12-31'
    );
    expect(await fixedAssetAcquisitionUpdatePage.getAssetCategoryInput()).to.eq(
      'assetCategory',
      'Expected AssetCategory value to be equals to assetCategory'
    );
    expect(await fixedAssetAcquisitionUpdatePage.getPurchasePriceInput()).to.eq('5', 'Expected purchasePrice value to be equals to 5');
    expect(await fixedAssetAcquisitionUpdatePage.getFileUploadTokenInput()).to.eq(
      'fileUploadToken',
      'Expected FileUploadToken value to be equals to fileUploadToken'
    );

    await fixedAssetAcquisitionUpdatePage.save();
    expect(await fixedAssetAcquisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fixedAssetAcquisitionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FixedAssetAcquisition', async () => {
    const nbButtonsBeforeDelete = await fixedAssetAcquisitionComponentsPage.countDeleteButtons();
    await fixedAssetAcquisitionComponentsPage.clickOnLastDeleteButton();

    fixedAssetAcquisitionDeleteDialog = new FixedAssetAcquisitionDeleteDialog();
    expect(await fixedAssetAcquisitionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fixed Asset Acquisition?');
    await fixedAssetAcquisitionDeleteDialog.clickOnConfirmButton();

    expect(await fixedAssetAcquisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
