import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TransactionDetailsComponentsPage,
  /* TransactionDetailsDeleteDialog, */
  TransactionDetailsUpdatePage,
} from './transaction-details.page-object';

const expect = chai.expect;

describe('TransactionDetails e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionDetailsComponentsPage: TransactionDetailsComponentsPage;
  let transactionDetailsUpdatePage: TransactionDetailsUpdatePage;
  /* let transactionDetailsDeleteDialog: TransactionDetailsDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionDetails', async () => {
    await navBarPage.goToEntity('transaction-details');
    transactionDetailsComponentsPage = new TransactionDetailsComponentsPage();
    await browser.wait(ec.visibilityOf(transactionDetailsComponentsPage.title), 5000);
    expect(await transactionDetailsComponentsPage.getTitle()).to.eq('Transaction Details');
    await browser.wait(
      ec.or(ec.visibilityOf(transactionDetailsComponentsPage.entities), ec.visibilityOf(transactionDetailsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TransactionDetails page', async () => {
    await transactionDetailsComponentsPage.clickOnCreateButton();
    transactionDetailsUpdatePage = new TransactionDetailsUpdatePage();
    expect(await transactionDetailsUpdatePage.getPageTitle()).to.eq('Create or edit a Transaction Details');
    await transactionDetailsUpdatePage.cancel();
  });

  /* it('should create and save TransactionDetails', async () => {
        const nbButtonsBeforeCreate = await transactionDetailsComponentsPage.countDeleteButtons();

        await transactionDetailsComponentsPage.clickOnCreateButton();

        await promise.all([
            transactionDetailsUpdatePage.setEntryIdInput('5'),
            transactionDetailsUpdatePage.setTransactionDateInput('2000-12-31'),
            transactionDetailsUpdatePage.setDescriptionInput('description'),
            transactionDetailsUpdatePage.setAmountInput('5'),
            transactionDetailsUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            transactionDetailsUpdatePage.setModifiedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            transactionDetailsUpdatePage.setTransactionTypeInput('transactionType'),
            transactionDetailsUpdatePage.debitAccountSelectLastOption(),
            transactionDetailsUpdatePage.creditAccountSelectLastOption(),
            // transactionDetailsUpdatePage.placeholderSelectLastOption(),
            transactionDetailsUpdatePage.postedBySelectLastOption(),
        ]);

        await transactionDetailsUpdatePage.save();
        expect(await transactionDetailsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await transactionDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TransactionDetails', async () => {
        const nbButtonsBeforeDelete = await transactionDetailsComponentsPage.countDeleteButtons();
        await transactionDetailsComponentsPage.clickOnLastDeleteButton();

        transactionDetailsDeleteDialog = new TransactionDetailsDeleteDialog();
        expect(await transactionDetailsDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Transaction Details?');
        await transactionDetailsDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(transactionDetailsComponentsPage.title), 5000);

        expect(await transactionDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
