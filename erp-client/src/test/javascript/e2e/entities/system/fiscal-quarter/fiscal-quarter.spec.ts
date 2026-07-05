import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FiscalQuarterComponentsPage,
  /* FiscalQuarterDeleteDialog, */
  FiscalQuarterUpdatePage,
} from './fiscal-quarter.page-object';

const expect = chai.expect;

describe('FiscalQuarter e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fiscalQuarterComponentsPage: FiscalQuarterComponentsPage;
  let fiscalQuarterUpdatePage: FiscalQuarterUpdatePage;
  /* let fiscalQuarterDeleteDialog: FiscalQuarterDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FiscalQuarters', async () => {
    await navBarPage.goToEntity('fiscal-quarter');
    fiscalQuarterComponentsPage = new FiscalQuarterComponentsPage();
    await browser.wait(ec.visibilityOf(fiscalQuarterComponentsPage.title), 5000);
    expect(await fiscalQuarterComponentsPage.getTitle()).to.eq('Fiscal Quarters');
    await browser.wait(
      ec.or(ec.visibilityOf(fiscalQuarterComponentsPage.entities), ec.visibilityOf(fiscalQuarterComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FiscalQuarter page', async () => {
    await fiscalQuarterComponentsPage.clickOnCreateButton();
    fiscalQuarterUpdatePage = new FiscalQuarterUpdatePage();
    expect(await fiscalQuarterUpdatePage.getPageTitle()).to.eq('Create or edit a Fiscal Quarter');
    await fiscalQuarterUpdatePage.cancel();
  });

  /* it('should create and save FiscalQuarters', async () => {
        const nbButtonsBeforeCreate = await fiscalQuarterComponentsPage.countDeleteButtons();

        await fiscalQuarterComponentsPage.clickOnCreateButton();

        await promise.all([
            fiscalQuarterUpdatePage.setQuarterNumberInput('5'),
            fiscalQuarterUpdatePage.setStartDateInput('2000-12-31'),
            fiscalQuarterUpdatePage.setEndDateInput('2000-12-31'),
            fiscalQuarterUpdatePage.setFiscalQuarterCodeInput('fiscalQuarterCode'),
            fiscalQuarterUpdatePage.fiscalYearSelectLastOption(),
            // fiscalQuarterUpdatePage.placeholderSelectLastOption(),
            // fiscalQuarterUpdatePage.universallyUniqueMappingSelectLastOption(),
        ]);

        await fiscalQuarterUpdatePage.save();
        expect(await fiscalQuarterUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await fiscalQuarterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last FiscalQuarter', async () => {
        const nbButtonsBeforeDelete = await fiscalQuarterComponentsPage.countDeleteButtons();
        await fiscalQuarterComponentsPage.clickOnLastDeleteButton();

        fiscalQuarterDeleteDialog = new FiscalQuarterDeleteDialog();
        expect(await fiscalQuarterDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Fiscal Quarter?');
        await fiscalQuarterDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(fiscalQuarterComponentsPage.title), 5000);

        expect(await fiscalQuarterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
