import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CardAcquiringTransactionComponentsPage,
  /* CardAcquiringTransactionDeleteDialog, */
  CardAcquiringTransactionUpdatePage,
} from './card-acquiring-transaction.page-object';

const expect = chai.expect;

describe('CardAcquiringTransaction e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardAcquiringTransactionComponentsPage: CardAcquiringTransactionComponentsPage;
  let cardAcquiringTransactionUpdatePage: CardAcquiringTransactionUpdatePage;
  /* let cardAcquiringTransactionDeleteDialog: CardAcquiringTransactionDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardAcquiringTransactions', async () => {
    await navBarPage.goToEntity('card-acquiring-transaction');
    cardAcquiringTransactionComponentsPage = new CardAcquiringTransactionComponentsPage();
    await browser.wait(ec.visibilityOf(cardAcquiringTransactionComponentsPage.title), 5000);
    expect(await cardAcquiringTransactionComponentsPage.getTitle()).to.eq('Card Acquiring Transactions');
    await browser.wait(
      ec.or(
        ec.visibilityOf(cardAcquiringTransactionComponentsPage.entities),
        ec.visibilityOf(cardAcquiringTransactionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CardAcquiringTransaction page', async () => {
    await cardAcquiringTransactionComponentsPage.clickOnCreateButton();
    cardAcquiringTransactionUpdatePage = new CardAcquiringTransactionUpdatePage();
    expect(await cardAcquiringTransactionUpdatePage.getPageTitle()).to.eq('Create or edit a Card Acquiring Transaction');
    await cardAcquiringTransactionUpdatePage.cancel();
  });

  /* it('should create and save CardAcquiringTransactions', async () => {
        const nbButtonsBeforeCreate = await cardAcquiringTransactionComponentsPage.countDeleteButtons();

        await cardAcquiringTransactionComponentsPage.clickOnCreateButton();

        await promise.all([
            cardAcquiringTransactionUpdatePage.setReportingDateInput('2000-12-31'),
            cardAcquiringTransactionUpdatePage.setTerminalIdInput('terminalId'),
            cardAcquiringTransactionUpdatePage.setNumberOfTransactionsInput('5'),
            cardAcquiringTransactionUpdatePage.setValueOfTransactionsInLCYInput('5'),
            cardAcquiringTransactionUpdatePage.bankCodeSelectLastOption(),
            cardAcquiringTransactionUpdatePage.channelTypeSelectLastOption(),
            cardAcquiringTransactionUpdatePage.cardBrandTypeSelectLastOption(),
            cardAcquiringTransactionUpdatePage.currencyOfTransactionSelectLastOption(),
            cardAcquiringTransactionUpdatePage.cardIssuerCategorySelectLastOption(),
        ]);

        await cardAcquiringTransactionUpdatePage.save();
        expect(await cardAcquiringTransactionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await cardAcquiringTransactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last CardAcquiringTransaction', async () => {
        const nbButtonsBeforeDelete = await cardAcquiringTransactionComponentsPage.countDeleteButtons();
        await cardAcquiringTransactionComponentsPage.clickOnLastDeleteButton();

        cardAcquiringTransactionDeleteDialog = new CardAcquiringTransactionDeleteDialog();
        expect(await cardAcquiringTransactionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Card Acquiring Transaction?');
        await cardAcquiringTransactionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(cardAcquiringTransactionComponentsPage.title), 5000);

        expect(await cardAcquiringTransactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
