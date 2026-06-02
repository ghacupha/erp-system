import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LoanRepaymentFrequencyComponentsPage,
  LoanRepaymentFrequencyDeleteDialog,
  LoanRepaymentFrequencyUpdatePage,
} from './loan-repayment-frequency.page-object';

const expect = chai.expect;

describe('LoanRepaymentFrequency e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanRepaymentFrequencyComponentsPage: LoanRepaymentFrequencyComponentsPage;
  let loanRepaymentFrequencyUpdatePage: LoanRepaymentFrequencyUpdatePage;
  let loanRepaymentFrequencyDeleteDialog: LoanRepaymentFrequencyDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanRepaymentFrequencies', async () => {
    await navBarPage.goToEntity('loan-repayment-frequency');
    loanRepaymentFrequencyComponentsPage = new LoanRepaymentFrequencyComponentsPage();
    await browser.wait(ec.visibilityOf(loanRepaymentFrequencyComponentsPage.title), 5000);
    expect(await loanRepaymentFrequencyComponentsPage.getTitle()).to.eq('Loan Repayment Frequencies');
    await browser.wait(
      ec.or(ec.visibilityOf(loanRepaymentFrequencyComponentsPage.entities), ec.visibilityOf(loanRepaymentFrequencyComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanRepaymentFrequency page', async () => {
    await loanRepaymentFrequencyComponentsPage.clickOnCreateButton();
    loanRepaymentFrequencyUpdatePage = new LoanRepaymentFrequencyUpdatePage();
    expect(await loanRepaymentFrequencyUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Repayment Frequency');
    await loanRepaymentFrequencyUpdatePage.cancel();
  });

  it('should create and save LoanRepaymentFrequencies', async () => {
    const nbButtonsBeforeCreate = await loanRepaymentFrequencyComponentsPage.countDeleteButtons();

    await loanRepaymentFrequencyComponentsPage.clickOnCreateButton();

    await promise.all([
      loanRepaymentFrequencyUpdatePage.setFrequencyTypeCodeInput('frequencyTypeCode'),
      loanRepaymentFrequencyUpdatePage.setFrequencyTypeInput('frequencyType'),
      loanRepaymentFrequencyUpdatePage.setFrequencyTypeDetailsInput('frequencyTypeDetails'),
    ]);

    await loanRepaymentFrequencyUpdatePage.save();
    expect(await loanRepaymentFrequencyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanRepaymentFrequencyComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanRepaymentFrequency', async () => {
    const nbButtonsBeforeDelete = await loanRepaymentFrequencyComponentsPage.countDeleteButtons();
    await loanRepaymentFrequencyComponentsPage.clickOnLastDeleteButton();

    loanRepaymentFrequencyDeleteDialog = new LoanRepaymentFrequencyDeleteDialog();
    expect(await loanRepaymentFrequencyDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Loan Repayment Frequency?'
    );
    await loanRepaymentFrequencyDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanRepaymentFrequencyComponentsPage.title), 5000);

    expect(await loanRepaymentFrequencyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
