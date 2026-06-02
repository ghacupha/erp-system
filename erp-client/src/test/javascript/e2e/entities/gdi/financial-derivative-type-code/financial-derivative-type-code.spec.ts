import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FinancialDerivativeTypeCodeComponentsPage,
  FinancialDerivativeTypeCodeDeleteDialog,
  FinancialDerivativeTypeCodeUpdatePage,
} from './financial-derivative-type-code.page-object';

const expect = chai.expect;

describe('FinancialDerivativeTypeCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let financialDerivativeTypeCodeComponentsPage: FinancialDerivativeTypeCodeComponentsPage;
  let financialDerivativeTypeCodeUpdatePage: FinancialDerivativeTypeCodeUpdatePage;
  let financialDerivativeTypeCodeDeleteDialog: FinancialDerivativeTypeCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FinancialDerivativeTypeCodes', async () => {
    await navBarPage.goToEntity('financial-derivative-type-code');
    financialDerivativeTypeCodeComponentsPage = new FinancialDerivativeTypeCodeComponentsPage();
    await browser.wait(ec.visibilityOf(financialDerivativeTypeCodeComponentsPage.title), 5000);
    expect(await financialDerivativeTypeCodeComponentsPage.getTitle()).to.eq('Financial Derivative Type Codes');
    await browser.wait(
      ec.or(
        ec.visibilityOf(financialDerivativeTypeCodeComponentsPage.entities),
        ec.visibilityOf(financialDerivativeTypeCodeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create FinancialDerivativeTypeCode page', async () => {
    await financialDerivativeTypeCodeComponentsPage.clickOnCreateButton();
    financialDerivativeTypeCodeUpdatePage = new FinancialDerivativeTypeCodeUpdatePage();
    expect(await financialDerivativeTypeCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Financial Derivative Type Code');
    await financialDerivativeTypeCodeUpdatePage.cancel();
  });

  it('should create and save FinancialDerivativeTypeCodes', async () => {
    const nbButtonsBeforeCreate = await financialDerivativeTypeCodeComponentsPage.countDeleteButtons();

    await financialDerivativeTypeCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      financialDerivativeTypeCodeUpdatePage.setFinancialDerivativeTypeCodeInput('financialDerivativeTypeCode'),
      financialDerivativeTypeCodeUpdatePage.setFinancialDerivativeTypeInput('financialDerivativeType'),
      financialDerivativeTypeCodeUpdatePage.setFinancialDerivativeTypeDetailsInput('financialDerivativeTypeDetails'),
    ]);

    await financialDerivativeTypeCodeUpdatePage.save();
    expect(await financialDerivativeTypeCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await financialDerivativeTypeCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FinancialDerivativeTypeCode', async () => {
    const nbButtonsBeforeDelete = await financialDerivativeTypeCodeComponentsPage.countDeleteButtons();
    await financialDerivativeTypeCodeComponentsPage.clickOnLastDeleteButton();

    financialDerivativeTypeCodeDeleteDialog = new FinancialDerivativeTypeCodeDeleteDialog();
    expect(await financialDerivativeTypeCodeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Financial Derivative Type Code?'
    );
    await financialDerivativeTypeCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(financialDerivativeTypeCodeComponentsPage.title), 5000);

    expect(await financialDerivativeTypeCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
