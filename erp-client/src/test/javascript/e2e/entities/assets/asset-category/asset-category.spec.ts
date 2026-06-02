import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetCategoryComponentsPage,
  /* AssetCategoryDeleteDialog, */
  AssetCategoryUpdatePage,
} from './asset-category.page-object';

const expect = chai.expect;

describe('AssetCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetCategoryComponentsPage: AssetCategoryComponentsPage;
  let assetCategoryUpdatePage: AssetCategoryUpdatePage;
  /* let assetCategoryDeleteDialog: AssetCategoryDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetCategories', async () => {
    await navBarPage.goToEntity('asset-category');
    assetCategoryComponentsPage = new AssetCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(assetCategoryComponentsPage.title), 5000);
    expect(await assetCategoryComponentsPage.getTitle()).to.eq('Asset Categories');
    await browser.wait(
      ec.or(ec.visibilityOf(assetCategoryComponentsPage.entities), ec.visibilityOf(assetCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetCategory page', async () => {
    await assetCategoryComponentsPage.clickOnCreateButton();
    assetCategoryUpdatePage = new AssetCategoryUpdatePage();
    expect(await assetCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Category');
    await assetCategoryUpdatePage.cancel();
  });

  /* it('should create and save AssetCategories', async () => {
        const nbButtonsBeforeCreate = await assetCategoryComponentsPage.countDeleteButtons();

        await assetCategoryComponentsPage.clickOnCreateButton();

        await promise.all([
            assetCategoryUpdatePage.setAssetCategoryNameInput('assetCategoryName'),
            assetCategoryUpdatePage.setDescriptionInput('description'),
            assetCategoryUpdatePage.setNotesInput('notes'),
            assetCategoryUpdatePage.setRemarksInput('remarks'),
            assetCategoryUpdatePage.setDepreciationRateYearlyInput('5'),
            assetCategoryUpdatePage.depreciationMethodSelectLastOption(),
            // assetCategoryUpdatePage.placeholderSelectLastOption(),
        ]);

        await assetCategoryUpdatePage.save();
        expect(await assetCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await assetCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AssetCategory', async () => {
        const nbButtonsBeforeDelete = await assetCategoryComponentsPage.countDeleteButtons();
        await assetCategoryComponentsPage.clickOnLastDeleteButton();

        assetCategoryDeleteDialog = new AssetCategoryDeleteDialog();
        expect(await assetCategoryDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset Category?');
        await assetCategoryDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(assetCategoryComponentsPage.title), 5000);

        expect(await assetCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
