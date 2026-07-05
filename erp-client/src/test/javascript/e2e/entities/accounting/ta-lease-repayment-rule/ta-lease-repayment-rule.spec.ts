import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TALeaseRepaymentRuleComponentsPage,
  /* TALeaseRepaymentRuleDeleteDialog, */
  TALeaseRepaymentRuleUpdatePage,
} from './ta-lease-repayment-rule.page-object';

const expect = chai.expect;

describe('TALeaseRepaymentRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tALeaseRepaymentRuleComponentsPage: TALeaseRepaymentRuleComponentsPage;
  let tALeaseRepaymentRuleUpdatePage: TALeaseRepaymentRuleUpdatePage;
  /* let tALeaseRepaymentRuleDeleteDialog: TALeaseRepaymentRuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TALeaseRepaymentRules', async () => {
    await navBarPage.goToEntity('ta-lease-repayment-rule');
    tALeaseRepaymentRuleComponentsPage = new TALeaseRepaymentRuleComponentsPage();
    await browser.wait(ec.visibilityOf(tALeaseRepaymentRuleComponentsPage.title), 5000);
    expect(await tALeaseRepaymentRuleComponentsPage.getTitle()).to.eq('TA Lease Repayment Rules');
    await browser.wait(
      ec.or(ec.visibilityOf(tALeaseRepaymentRuleComponentsPage.entities), ec.visibilityOf(tALeaseRepaymentRuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TALeaseRepaymentRule page', async () => {
    await tALeaseRepaymentRuleComponentsPage.clickOnCreateButton();
    tALeaseRepaymentRuleUpdatePage = new TALeaseRepaymentRuleUpdatePage();
    expect(await tALeaseRepaymentRuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Lease Repayment Rule');
    await tALeaseRepaymentRuleUpdatePage.cancel();
  });

  /* it('should create and save TALeaseRepaymentRules', async () => {
        const nbButtonsBeforeCreate = await tALeaseRepaymentRuleComponentsPage.countDeleteButtons();

        await tALeaseRepaymentRuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tALeaseRepaymentRuleUpdatePage.setNameInput('name'),
            tALeaseRepaymentRuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tALeaseRepaymentRuleUpdatePage.leaseContractSelectLastOption(),
            tALeaseRepaymentRuleUpdatePage.debitSelectLastOption(),
            tALeaseRepaymentRuleUpdatePage.creditSelectLastOption(),
            // tALeaseRepaymentRuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tALeaseRepaymentRuleUpdatePage.save();
        expect(await tALeaseRepaymentRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tALeaseRepaymentRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TALeaseRepaymentRule', async () => {
        const nbButtonsBeforeDelete = await tALeaseRepaymentRuleComponentsPage.countDeleteButtons();
        await tALeaseRepaymentRuleComponentsPage.clickOnLastDeleteButton();

        tALeaseRepaymentRuleDeleteDialog = new TALeaseRepaymentRuleDeleteDialog();
        expect(await tALeaseRepaymentRuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Lease Repayment Rule?');
        await tALeaseRepaymentRuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tALeaseRepaymentRuleComponentsPage.title), 5000);

        expect(await tALeaseRepaymentRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
