import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { EmploymentTermsComponentsPage, EmploymentTermsDeleteDialog, EmploymentTermsUpdatePage } from './employment-terms.page-object';

const expect = chai.expect;

describe('EmploymentTerms e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let employmentTermsComponentsPage: EmploymentTermsComponentsPage;
  let employmentTermsUpdatePage: EmploymentTermsUpdatePage;
  let employmentTermsDeleteDialog: EmploymentTermsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load EmploymentTerms', async () => {
    await navBarPage.goToEntity('employment-terms');
    employmentTermsComponentsPage = new EmploymentTermsComponentsPage();
    await browser.wait(ec.visibilityOf(employmentTermsComponentsPage.title), 5000);
    expect(await employmentTermsComponentsPage.getTitle()).to.eq('Employment Terms');
    await browser.wait(
      ec.or(ec.visibilityOf(employmentTermsComponentsPage.entities), ec.visibilityOf(employmentTermsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create EmploymentTerms page', async () => {
    await employmentTermsComponentsPage.clickOnCreateButton();
    employmentTermsUpdatePage = new EmploymentTermsUpdatePage();
    expect(await employmentTermsUpdatePage.getPageTitle()).to.eq('Create or edit a Employment Terms');
    await employmentTermsUpdatePage.cancel();
  });

  it('should create and save EmploymentTerms', async () => {
    const nbButtonsBeforeCreate = await employmentTermsComponentsPage.countDeleteButtons();

    await employmentTermsComponentsPage.clickOnCreateButton();

    await promise.all([
      employmentTermsUpdatePage.setEmploymentTermsCodeInput('employmentTermsCode'),
      employmentTermsUpdatePage.setEmploymentTermsStatusInput('employmentTermsStatus'),
    ]);

    await employmentTermsUpdatePage.save();
    expect(await employmentTermsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await employmentTermsComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last EmploymentTerms', async () => {
    const nbButtonsBeforeDelete = await employmentTermsComponentsPage.countDeleteButtons();
    await employmentTermsComponentsPage.clickOnLastDeleteButton();

    employmentTermsDeleteDialog = new EmploymentTermsDeleteDialog();
    expect(await employmentTermsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Employment Terms?');
    await employmentTermsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(employmentTermsComponentsPage.title), 5000);

    expect(await employmentTermsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
