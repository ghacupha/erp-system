import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  UltimateBeneficiaryTypesComponentsPage,
  UltimateBeneficiaryTypesDeleteDialog,
  UltimateBeneficiaryTypesUpdatePage,
} from './ultimate-beneficiary-types.page-object';

const expect = chai.expect;

describe('UltimateBeneficiaryTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ultimateBeneficiaryTypesComponentsPage: UltimateBeneficiaryTypesComponentsPage;
  let ultimateBeneficiaryTypesUpdatePage: UltimateBeneficiaryTypesUpdatePage;
  let ultimateBeneficiaryTypesDeleteDialog: UltimateBeneficiaryTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load UltimateBeneficiaryTypes', async () => {
    await navBarPage.goToEntity('ultimate-beneficiary-types');
    ultimateBeneficiaryTypesComponentsPage = new UltimateBeneficiaryTypesComponentsPage();
    await browser.wait(ec.visibilityOf(ultimateBeneficiaryTypesComponentsPage.title), 5000);
    expect(await ultimateBeneficiaryTypesComponentsPage.getTitle()).to.eq('Ultimate Beneficiary Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(ultimateBeneficiaryTypesComponentsPage.entities),
        ec.visibilityOf(ultimateBeneficiaryTypesComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create UltimateBeneficiaryTypes page', async () => {
    await ultimateBeneficiaryTypesComponentsPage.clickOnCreateButton();
    ultimateBeneficiaryTypesUpdatePage = new UltimateBeneficiaryTypesUpdatePage();
    expect(await ultimateBeneficiaryTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Ultimate Beneficiary Types');
    await ultimateBeneficiaryTypesUpdatePage.cancel();
  });

  it('should create and save UltimateBeneficiaryTypes', async () => {
    const nbButtonsBeforeCreate = await ultimateBeneficiaryTypesComponentsPage.countDeleteButtons();

    await ultimateBeneficiaryTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      ultimateBeneficiaryTypesUpdatePage.setUltimateBeneficiaryTypeCodeInput('ultimateBeneficiaryTypeCode'),
      ultimateBeneficiaryTypesUpdatePage.setUltimateBeneficiaryTypeInput('ultimateBeneficiaryType'),
      ultimateBeneficiaryTypesUpdatePage.setUltimateBeneficiaryTypeDetailsInput('ultimateBeneficiaryTypeDetails'),
    ]);

    await ultimateBeneficiaryTypesUpdatePage.save();
    expect(await ultimateBeneficiaryTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ultimateBeneficiaryTypesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last UltimateBeneficiaryTypes', async () => {
    const nbButtonsBeforeDelete = await ultimateBeneficiaryTypesComponentsPage.countDeleteButtons();
    await ultimateBeneficiaryTypesComponentsPage.clickOnLastDeleteButton();

    ultimateBeneficiaryTypesDeleteDialog = new UltimateBeneficiaryTypesDeleteDialog();
    expect(await ultimateBeneficiaryTypesDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Ultimate Beneficiary Types?'
    );
    await ultimateBeneficiaryTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ultimateBeneficiaryTypesComponentsPage.title), 5000);

    expect(await ultimateBeneficiaryTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
