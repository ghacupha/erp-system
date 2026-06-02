import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  UltimateBeneficiaryCategoryComponentsPage,
  UltimateBeneficiaryCategoryDeleteDialog,
  UltimateBeneficiaryCategoryUpdatePage,
} from './ultimate-beneficiary-category.page-object';

const expect = chai.expect;

describe('UltimateBeneficiaryCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ultimateBeneficiaryCategoryComponentsPage: UltimateBeneficiaryCategoryComponentsPage;
  let ultimateBeneficiaryCategoryUpdatePage: UltimateBeneficiaryCategoryUpdatePage;
  let ultimateBeneficiaryCategoryDeleteDialog: UltimateBeneficiaryCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load UltimateBeneficiaryCategories', async () => {
    await navBarPage.goToEntity('ultimate-beneficiary-category');
    ultimateBeneficiaryCategoryComponentsPage = new UltimateBeneficiaryCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(ultimateBeneficiaryCategoryComponentsPage.title), 5000);
    expect(await ultimateBeneficiaryCategoryComponentsPage.getTitle()).to.eq('Ultimate Beneficiary Categories');
    await browser.wait(
      ec.or(
        ec.visibilityOf(ultimateBeneficiaryCategoryComponentsPage.entities),
        ec.visibilityOf(ultimateBeneficiaryCategoryComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create UltimateBeneficiaryCategory page', async () => {
    await ultimateBeneficiaryCategoryComponentsPage.clickOnCreateButton();
    ultimateBeneficiaryCategoryUpdatePage = new UltimateBeneficiaryCategoryUpdatePage();
    expect(await ultimateBeneficiaryCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Ultimate Beneficiary Category');
    await ultimateBeneficiaryCategoryUpdatePage.cancel();
  });

  it('should create and save UltimateBeneficiaryCategories', async () => {
    const nbButtonsBeforeCreate = await ultimateBeneficiaryCategoryComponentsPage.countDeleteButtons();

    await ultimateBeneficiaryCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      ultimateBeneficiaryCategoryUpdatePage.setUltimateBeneficiaryCategoryTypeCodeInput('ultimateBeneficiaryCategoryTypeCode'),
      ultimateBeneficiaryCategoryUpdatePage.setUltimateBeneficiaryTypeInput('ultimateBeneficiaryType'),
      ultimateBeneficiaryCategoryUpdatePage.setUltimateBeneficiaryCategoryTypeDetailsInput('ultimateBeneficiaryCategoryTypeDetails'),
    ]);

    await ultimateBeneficiaryCategoryUpdatePage.save();
    expect(await ultimateBeneficiaryCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ultimateBeneficiaryCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last UltimateBeneficiaryCategory', async () => {
    const nbButtonsBeforeDelete = await ultimateBeneficiaryCategoryComponentsPage.countDeleteButtons();
    await ultimateBeneficiaryCategoryComponentsPage.clickOnLastDeleteButton();

    ultimateBeneficiaryCategoryDeleteDialog = new UltimateBeneficiaryCategoryDeleteDialog();
    expect(await ultimateBeneficiaryCategoryDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Ultimate Beneficiary Category?'
    );
    await ultimateBeneficiaryCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ultimateBeneficiaryCategoryComponentsPage.title), 5000);

    expect(await ultimateBeneficiaryCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
