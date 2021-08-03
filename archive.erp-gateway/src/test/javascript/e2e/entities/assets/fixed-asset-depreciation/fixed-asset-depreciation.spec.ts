import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FixedAssetDepreciationComponentsPage,
  FixedAssetDepreciationDeleteDialog,
  FixedAssetDepreciationUpdatePage,
} from './fixed-asset-depreciation.page-object';

const expect = chai.expect;

describe('FixedAssetDepreciation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fixedAssetDepreciationComponentsPage: FixedAssetDepreciationComponentsPage;
  let fixedAssetDepreciationUpdatePage: FixedAssetDepreciationUpdatePage;
  let fixedAssetDepreciationDeleteDialog: FixedAssetDepreciationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FixedAssetDepreciations', async () => {
    await navBarPage.goToEntity('fixed-asset-depreciation');
    fixedAssetDepreciationComponentsPage = new FixedAssetDepreciationComponentsPage();
    await browser.wait(ec.visibilityOf(fixedAssetDepreciationComponentsPage.title), 5000);
    expect(await fixedAssetDepreciationComponentsPage.getTitle()).to.eq('Fixed Asset Depreciations');
    await browser.wait(
      ec.or(ec.visibilityOf(fixedAssetDepreciationComponentsPage.entities), ec.visibilityOf(fixedAssetDepreciationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FixedAssetDepreciation page', async () => {
    await fixedAssetDepreciationComponentsPage.clickOnCreateButton();
    fixedAssetDepreciationUpdatePage = new FixedAssetDepreciationUpdatePage();
    expect(await fixedAssetDepreciationUpdatePage.getPageTitle()).to.eq('Create or edit a Fixed Asset Depreciation');
    await fixedAssetDepreciationUpdatePage.cancel();
  });

  it('should create and save FixedAssetDepreciations', async () => {
    const nbButtonsBeforeCreate = await fixedAssetDepreciationComponentsPage.countDeleteButtons();

    await fixedAssetDepreciationComponentsPage.clickOnCreateButton();

    await promise.all([
      fixedAssetDepreciationUpdatePage.setAssetNumberInput('5'),
      fixedAssetDepreciationUpdatePage.setServiceOutletCodeInput('serviceOutletCode'),
      fixedAssetDepreciationUpdatePage.setAssetTagInput('assetTag'),
      fixedAssetDepreciationUpdatePage.setAssetDescriptionInput('assetDescription'),
      fixedAssetDepreciationUpdatePage.setDepreciationDateInput('2000-12-31'),
      fixedAssetDepreciationUpdatePage.setAssetCategoryInput('assetCategory'),
      fixedAssetDepreciationUpdatePage.setDepreciationAmountInput('5'),
      fixedAssetDepreciationUpdatePage.depreciationRegimeSelectLastOption(),
      fixedAssetDepreciationUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      fixedAssetDepreciationUpdatePage.setCompilationTokenInput('compilationToken'),
    ]);

    expect(await fixedAssetDepreciationUpdatePage.getAssetNumberInput()).to.eq('5', 'Expected assetNumber value to be equals to 5');
    expect(await fixedAssetDepreciationUpdatePage.getServiceOutletCodeInput()).to.eq(
      'serviceOutletCode',
      'Expected ServiceOutletCode value to be equals to serviceOutletCode'
    );
    expect(await fixedAssetDepreciationUpdatePage.getAssetTagInput()).to.eq('assetTag', 'Expected AssetTag value to be equals to assetTag');
    expect(await fixedAssetDepreciationUpdatePage.getAssetDescriptionInput()).to.eq(
      'assetDescription',
      'Expected AssetDescription value to be equals to assetDescription'
    );
    expect(await fixedAssetDepreciationUpdatePage.getDepreciationDateInput()).to.eq(
      '2000-12-31',
      'Expected depreciationDate value to be equals to 2000-12-31'
    );
    expect(await fixedAssetDepreciationUpdatePage.getAssetCategoryInput()).to.eq(
      'assetCategory',
      'Expected AssetCategory value to be equals to assetCategory'
    );
    expect(await fixedAssetDepreciationUpdatePage.getDepreciationAmountInput()).to.eq(
      '5',
      'Expected depreciationAmount value to be equals to 5'
    );
    expect(await fixedAssetDepreciationUpdatePage.getFileUploadTokenInput()).to.eq(
      'fileUploadToken',
      'Expected FileUploadToken value to be equals to fileUploadToken'
    );
    expect(await fixedAssetDepreciationUpdatePage.getCompilationTokenInput()).to.eq(
      'compilationToken',
      'Expected CompilationToken value to be equals to compilationToken'
    );

    await fixedAssetDepreciationUpdatePage.save();
    expect(await fixedAssetDepreciationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fixedAssetDepreciationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FixedAssetDepreciation', async () => {
    const nbButtonsBeforeDelete = await fixedAssetDepreciationComponentsPage.countDeleteButtons();
    await fixedAssetDepreciationComponentsPage.clickOnLastDeleteButton();

    fixedAssetDepreciationDeleteDialog = new FixedAssetDepreciationDeleteDialog();
    expect(await fixedAssetDepreciationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Fixed Asset Depreciation?'
    );
    await fixedAssetDepreciationDeleteDialog.clickOnConfirmButton();

    expect(await fixedAssetDepreciationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
