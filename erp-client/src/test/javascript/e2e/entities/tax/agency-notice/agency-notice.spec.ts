import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AgencyNoticeComponentsPage, AgencyNoticeDeleteDialog, AgencyNoticeUpdatePage } from './agency-notice.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AgencyNotice e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let agencyNoticeComponentsPage: AgencyNoticeComponentsPage;
  let agencyNoticeUpdatePage: AgencyNoticeUpdatePage;
  let agencyNoticeDeleteDialog: AgencyNoticeDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AgencyNotices', async () => {
    await navBarPage.goToEntity('agency-notice');
    agencyNoticeComponentsPage = new AgencyNoticeComponentsPage();
    await browser.wait(ec.visibilityOf(agencyNoticeComponentsPage.title), 5000);
    expect(await agencyNoticeComponentsPage.getTitle()).to.eq('Agency Notices');
    await browser.wait(
      ec.or(ec.visibilityOf(agencyNoticeComponentsPage.entities), ec.visibilityOf(agencyNoticeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AgencyNotice page', async () => {
    await agencyNoticeComponentsPage.clickOnCreateButton();
    agencyNoticeUpdatePage = new AgencyNoticeUpdatePage();
    expect(await agencyNoticeUpdatePage.getPageTitle()).to.eq('Create or edit a Agency Notice');
    await agencyNoticeUpdatePage.cancel();
  });

  it('should create and save AgencyNotices', async () => {
    const nbButtonsBeforeCreate = await agencyNoticeComponentsPage.countDeleteButtons();

    await agencyNoticeComponentsPage.clickOnCreateButton();

    await promise.all([
      agencyNoticeUpdatePage.setReferenceNumberInput('referenceNumber'),
      agencyNoticeUpdatePage.setReferenceDateInput('2000-12-31'),
      agencyNoticeUpdatePage.setAssessmentAmountInput('5'),
      agencyNoticeUpdatePage.agencyStatusSelectLastOption(),
      agencyNoticeUpdatePage.setAssessmentNoticeInput(absolutePath),
      // agencyNoticeUpdatePage.correspondentsSelectLastOption(),
      agencyNoticeUpdatePage.settlementCurrencySelectLastOption(),
      agencyNoticeUpdatePage.assessorSelectLastOption(),
      // agencyNoticeUpdatePage.placeholderSelectLastOption(),
      // agencyNoticeUpdatePage.businessDocumentSelectLastOption(),
    ]);

    await agencyNoticeUpdatePage.save();
    expect(await agencyNoticeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await agencyNoticeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last AgencyNotice', async () => {
    const nbButtonsBeforeDelete = await agencyNoticeComponentsPage.countDeleteButtons();
    await agencyNoticeComponentsPage.clickOnLastDeleteButton();

    agencyNoticeDeleteDialog = new AgencyNoticeDeleteDialog();
    expect(await agencyNoticeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Agency Notice?');
    await agencyNoticeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(agencyNoticeComponentsPage.title), 5000);

    expect(await agencyNoticeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
