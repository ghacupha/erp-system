import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TAInterestPaidTransferRuleComponentsPage,
  /* TAInterestPaidTransferRuleDeleteDialog, */
  TAInterestPaidTransferRuleUpdatePage,
} from './ta-interest-paid-transfer-rule.page-object';

const expect = chai.expect;

describe('TAInterestPaidTransferRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tAInterestPaidTransferRuleComponentsPage: TAInterestPaidTransferRuleComponentsPage;
  let tAInterestPaidTransferRuleUpdatePage: TAInterestPaidTransferRuleUpdatePage;
  /* let tAInterestPaidTransferRuleDeleteDialog: TAInterestPaidTransferRuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TAInterestPaidTransferRules', async () => {
    await navBarPage.goToEntity('ta-interest-paid-transfer-rule');
    tAInterestPaidTransferRuleComponentsPage = new TAInterestPaidTransferRuleComponentsPage();
    await browser.wait(ec.visibilityOf(tAInterestPaidTransferRuleComponentsPage.title), 5000);
    expect(await tAInterestPaidTransferRuleComponentsPage.getTitle()).to.eq('TA Interest Paid Transfer Rules');
    await browser.wait(
      ec.or(
        ec.visibilityOf(tAInterestPaidTransferRuleComponentsPage.entities),
        ec.visibilityOf(tAInterestPaidTransferRuleComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create TAInterestPaidTransferRule page', async () => {
    await tAInterestPaidTransferRuleComponentsPage.clickOnCreateButton();
    tAInterestPaidTransferRuleUpdatePage = new TAInterestPaidTransferRuleUpdatePage();
    expect(await tAInterestPaidTransferRuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Interest Paid Transfer Rule');
    await tAInterestPaidTransferRuleUpdatePage.cancel();
  });

  /* it('should create and save TAInterestPaidTransferRules', async () => {
        const nbButtonsBeforeCreate = await tAInterestPaidTransferRuleComponentsPage.countDeleteButtons();

        await tAInterestPaidTransferRuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tAInterestPaidTransferRuleUpdatePage.setNameInput('name'),
            tAInterestPaidTransferRuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tAInterestPaidTransferRuleUpdatePage.leaseContractSelectLastOption(),
            tAInterestPaidTransferRuleUpdatePage.debitSelectLastOption(),
            tAInterestPaidTransferRuleUpdatePage.creditSelectLastOption(),
            // tAInterestPaidTransferRuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tAInterestPaidTransferRuleUpdatePage.save();
        expect(await tAInterestPaidTransferRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tAInterestPaidTransferRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TAInterestPaidTransferRule', async () => {
        const nbButtonsBeforeDelete = await tAInterestPaidTransferRuleComponentsPage.countDeleteButtons();
        await tAInterestPaidTransferRuleComponentsPage.clickOnLastDeleteButton();

        tAInterestPaidTransferRuleDeleteDialog = new TAInterestPaidTransferRuleDeleteDialog();
        expect(await tAInterestPaidTransferRuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Interest Paid Transfer Rule?');
        await tAInterestPaidTransferRuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tAInterestPaidTransferRuleComponentsPage.title), 5000);

        expect(await tAInterestPaidTransferRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
