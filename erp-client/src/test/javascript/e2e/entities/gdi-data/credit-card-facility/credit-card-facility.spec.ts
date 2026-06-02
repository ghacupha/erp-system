import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CreditCardFacilityComponentsPage,
  /* CreditCardFacilityDeleteDialog, */
  CreditCardFacilityUpdatePage,
} from './credit-card-facility.page-object';

const expect = chai.expect;

describe('CreditCardFacility e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let creditCardFacilityComponentsPage: CreditCardFacilityComponentsPage;
  let creditCardFacilityUpdatePage: CreditCardFacilityUpdatePage;
  /* let creditCardFacilityDeleteDialog: CreditCardFacilityDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CreditCardFacilities', async () => {
    await navBarPage.goToEntity('credit-card-facility');
    creditCardFacilityComponentsPage = new CreditCardFacilityComponentsPage();
    await browser.wait(ec.visibilityOf(creditCardFacilityComponentsPage.title), 5000);
    expect(await creditCardFacilityComponentsPage.getTitle()).to.eq('Credit Card Facilities');
    await browser.wait(
      ec.or(ec.visibilityOf(creditCardFacilityComponentsPage.entities), ec.visibilityOf(creditCardFacilityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CreditCardFacility page', async () => {
    await creditCardFacilityComponentsPage.clickOnCreateButton();
    creditCardFacilityUpdatePage = new CreditCardFacilityUpdatePage();
    expect(await creditCardFacilityUpdatePage.getPageTitle()).to.eq('Create or edit a Credit Card Facility');
    await creditCardFacilityUpdatePage.cancel();
  });

  /* it('should create and save CreditCardFacilities', async () => {
        const nbButtonsBeforeCreate = await creditCardFacilityComponentsPage.countDeleteButtons();

        await creditCardFacilityComponentsPage.clickOnCreateButton();

        await promise.all([
            creditCardFacilityUpdatePage.setReportingDateInput('2000-12-31'),
            creditCardFacilityUpdatePage.setTotalNumberOfActiveCreditCardsInput('5'),
            creditCardFacilityUpdatePage.setTotalCreditCardLimitsInCCYInput('5'),
            creditCardFacilityUpdatePage.setTotalCreditCardLimitsInLCYInput('5'),
            creditCardFacilityUpdatePage.setTotalCreditCardAmountUtilisedInCCYInput('5'),
            creditCardFacilityUpdatePage.setTotalCreditCardAmountUtilisedInLcyInput('5'),
            creditCardFacilityUpdatePage.setTotalNPACreditCardAmountInFCYInput('5'),
            creditCardFacilityUpdatePage.setTotalNPACreditCardAmountInLCYInput('5'),
            creditCardFacilityUpdatePage.bankCodeSelectLastOption(),
            creditCardFacilityUpdatePage.customerCategorySelectLastOption(),
            creditCardFacilityUpdatePage.currencyCodeSelectLastOption(),
        ]);

        await creditCardFacilityUpdatePage.save();
        expect(await creditCardFacilityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await creditCardFacilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last CreditCardFacility', async () => {
        const nbButtonsBeforeDelete = await creditCardFacilityComponentsPage.countDeleteButtons();
        await creditCardFacilityComponentsPage.clickOnLastDeleteButton();

        creditCardFacilityDeleteDialog = new CreditCardFacilityDeleteDialog();
        expect(await creditCardFacilityDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Credit Card Facility?');
        await creditCardFacilityDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(creditCardFacilityComponentsPage.title), 5000);

        expect(await creditCardFacilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
