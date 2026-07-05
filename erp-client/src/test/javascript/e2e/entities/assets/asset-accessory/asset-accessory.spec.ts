import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetAccessoryComponentsPage,
  /* AssetAccessoryDeleteDialog, */
  AssetAccessoryUpdatePage,
} from './asset-accessory.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AssetAccessory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetAccessoryComponentsPage: AssetAccessoryComponentsPage;
  let assetAccessoryUpdatePage: AssetAccessoryUpdatePage;
  /* let assetAccessoryDeleteDialog: AssetAccessoryDeleteDialog; */
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetAccessories', async () => {
    await navBarPage.goToEntity('asset-accessory');
    assetAccessoryComponentsPage = new AssetAccessoryComponentsPage();
    await browser.wait(ec.visibilityOf(assetAccessoryComponentsPage.title), 5000);
    expect(await assetAccessoryComponentsPage.getTitle()).to.eq('Asset Accessories');
    await browser.wait(
      ec.or(ec.visibilityOf(assetAccessoryComponentsPage.entities), ec.visibilityOf(assetAccessoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetAccessory page', async () => {
    await assetAccessoryComponentsPage.clickOnCreateButton();
    assetAccessoryUpdatePage = new AssetAccessoryUpdatePage();
    expect(await assetAccessoryUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Accessory');
    await assetAccessoryUpdatePage.cancel();
  });

  /* it('should create and save AssetAccessories', async () => {
        const nbButtonsBeforeCreate = await assetAccessoryComponentsPage.countDeleteButtons();

        await assetAccessoryComponentsPage.clickOnCreateButton();

        await promise.all([
            assetAccessoryUpdatePage.setAssetTagInput('assetTag'),
            assetAccessoryUpdatePage.setAssetDetailsInput('assetDetails'),
            assetAccessoryUpdatePage.setCommentsInput(absolutePath),
            assetAccessoryUpdatePage.setModelNumberInput('modelNumber'),
            assetAccessoryUpdatePage.setSerialNumberInput('serialNumber'),
            // assetAccessoryUpdatePage.assetWarrantySelectLastOption(),
            // assetAccessoryUpdatePage.placeholderSelectLastOption(),
            // assetAccessoryUpdatePage.paymentInvoicesSelectLastOption(),
            assetAccessoryUpdatePage.serviceOutletSelectLastOption(),
            // assetAccessoryUpdatePage.settlementSelectLastOption(),
            assetAccessoryUpdatePage.assetCategorySelectLastOption(),
            // assetAccessoryUpdatePage.purchaseOrderSelectLastOption(),
            // assetAccessoryUpdatePage.deliveryNoteSelectLastOption(),
            // assetAccessoryUpdatePage.jobSheetSelectLastOption(),
            assetAccessoryUpdatePage.dealerSelectLastOption(),
            // assetAccessoryUpdatePage.designatedUsersSelectLastOption(),
            // assetAccessoryUpdatePage.businessDocumentSelectLastOption(),
            // assetAccessoryUpdatePage.universallyUniqueMappingSelectLastOption(),
        ]);

        await assetAccessoryUpdatePage.save();
        expect(await assetAccessoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetAccessoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetAccessory', async () => {
        const nbButtonsBeforeDelete = await assetAccessoryComponentsPage.countDeleteButtons();
        await assetAccessoryComponentsPage.clickOnLastDeleteButton();

        assetAccessoryDeleteDialog = new AssetAccessoryDeleteDialog();
        expect(await assetAccessoryDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Accessory?');
        await assetAccessoryDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetAccessoryComponentsPage.title), 5000);

        expect(await assetAccessoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
