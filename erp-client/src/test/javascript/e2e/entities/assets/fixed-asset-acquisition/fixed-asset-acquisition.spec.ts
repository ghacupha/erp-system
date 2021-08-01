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
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
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
    await browser.wait(ec.visibilityOf(fixedAssetAcquisitionComponentsPage.title), 5000);

    expect(await fixedAssetAcquisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
