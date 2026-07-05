import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DepreciationPeriodComponentsPage,
  /* DepreciationPeriodDeleteDialog, */
  DepreciationPeriodUpdatePage,
} from './depreciation-period.page-object';

const expect = chai.expect;

describe('DepreciationPeriod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationPeriodComponentsPage: DepreciationPeriodComponentsPage;
  let depreciationPeriodUpdatePage: DepreciationPeriodUpdatePage;
  /* let depreciationPeriodDeleteDialog: DepreciationPeriodDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationPeriods', async () => {
    await navBarPage.goToEntity('depreciation-period');
    depreciationPeriodComponentsPage = new DepreciationPeriodComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationPeriodComponentsPage.title), 5000);
    expect(await depreciationPeriodComponentsPage.getTitle()).to.eq('Depreciation Periods');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationPeriodComponentsPage.entities), ec.visibilityOf(depreciationPeriodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationPeriod page', async () => {
    await depreciationPeriodComponentsPage.clickOnCreateButton();
    depreciationPeriodUpdatePage = new DepreciationPeriodUpdatePage();
    expect(await depreciationPeriodUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Period');
    await depreciationPeriodUpdatePage.cancel();
  });

  /* it('should create and save DepreciationPeriods', async () => {
        const nbButtonsBeforeCreate = await depreciationPeriodComponentsPage.countDeleteButtons();

        await depreciationPeriodComponentsPage.clickOnCreateButton();

        await promise.all([
            depreciationPeriodUpdatePage.setStartDateInput('2000-12-31'),
            depreciationPeriodUpdatePage.setEndDateInput('2000-12-31'),
            depreciationPeriodUpdatePage.depreciationPeriodStatusSelectLastOption(),
            depreciationPeriodUpdatePage.setPeriodCodeInput('periodCode'),
            depreciationPeriodUpdatePage.getProcessLockedInput().click(),
            depreciationPeriodUpdatePage.previousPeriodSelectLastOption(),
            depreciationPeriodUpdatePage.createdBySelectLastOption(),
            depreciationPeriodUpdatePage.fiscalYearSelectLastOption(),
            depreciationPeriodUpdatePage.fiscalMonthSelectLastOption(),
            depreciationPeriodUpdatePage.fiscalQuarterSelectLastOption(),
        ]);

        await depreciationPeriodUpdatePage.save();
        expect(await depreciationPeriodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await depreciationPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last DepreciationPeriod', async () => {
        const nbButtonsBeforeDelete = await depreciationPeriodComponentsPage.countDeleteButtons();
        await depreciationPeriodComponentsPage.clickOnLastDeleteButton();

        depreciationPeriodDeleteDialog = new DepreciationPeriodDeleteDialog();
        expect(await depreciationPeriodDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Depreciation Period?');
        await depreciationPeriodDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(depreciationPeriodComponentsPage.title), 5000);

        expect(await depreciationPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
