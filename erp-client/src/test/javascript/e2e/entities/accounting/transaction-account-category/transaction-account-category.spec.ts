import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TransactionAccountCategoryComponentsPage,
  /* TransactionAccountCategoryDeleteDialog, */
  TransactionAccountCategoryUpdatePage,
} from './transaction-account-category.page-object';

const expect = chai.expect;

describe('TransactionAccountCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionAccountCategoryComponentsPage: TransactionAccountCategoryComponentsPage;
  let transactionAccountCategoryUpdatePage: TransactionAccountCategoryUpdatePage;
  /* let transactionAccountCategoryDeleteDialog: TransactionAccountCategoryDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionAccountCategories', async () => {
    await navBarPage.goToEntity('transaction-account-category');
    transactionAccountCategoryComponentsPage = new TransactionAccountCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(transactionAccountCategoryComponentsPage.title), 5000);
    expect(await transactionAccountCategoryComponentsPage.getTitle()).to.eq('Transaction Account Categories');
    await browser.wait(
      ec.or(
        ec.visibilityOf(transactionAccountCategoryComponentsPage.entities),
        ec.visibilityOf(transactionAccountCategoryComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create TransactionAccountCategory page', async () => {
    await transactionAccountCategoryComponentsPage.clickOnCreateButton();
    transactionAccountCategoryUpdatePage = new TransactionAccountCategoryUpdatePage();
    expect(await transactionAccountCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Transaction Account Category');
    await transactionAccountCategoryUpdatePage.cancel();
  });

  /* it('should create and save TransactionAccountCategories', async () => {
        const nbButtonsBeforeCreate = await transactionAccountCategoryComponentsPage.countDeleteButtons();

        await transactionAccountCategoryComponentsPage.clickOnCreateButton();

        await promise.all([
            transactionAccountCategoryUpdatePage.setNameInput('name'),
            transactionAccountCategoryUpdatePage.transactionAccountPostingTypeSelectLastOption(),
            // transactionAccountCategoryUpdatePage.placeholderSelectLastOption(),
            transactionAccountCategoryUpdatePage.accountLedgerSelectLastOption(),
        ]);

        await transactionAccountCategoryUpdatePage.save();
        expect(await transactionAccountCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await transactionAccountCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TransactionAccountCategory', async () => {
        const nbButtonsBeforeDelete = await transactionAccountCategoryComponentsPage.countDeleteButtons();
        await transactionAccountCategoryComponentsPage.clickOnLastDeleteButton();

        transactionAccountCategoryDeleteDialog = new TransactionAccountCategoryDeleteDialog();
        expect(await transactionAccountCategoryDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Transaction Account Category?');
        await transactionAccountCategoryDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(transactionAccountCategoryComponentsPage.title), 5000);

        expect(await transactionAccountCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
