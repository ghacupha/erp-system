import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DepreciationBatchSequenceComponentsPage,
  DepreciationBatchSequenceDeleteDialog,
  DepreciationBatchSequenceUpdatePage,
} from './depreciation-batch-sequence.page-object';

const expect = chai.expect;

describe('DepreciationBatchSequence e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationBatchSequenceComponentsPage: DepreciationBatchSequenceComponentsPage;
  let depreciationBatchSequenceUpdatePage: DepreciationBatchSequenceUpdatePage;
  let depreciationBatchSequenceDeleteDialog: DepreciationBatchSequenceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationBatchSequences', async () => {
    await navBarPage.goToEntity('depreciation-batch-sequence');
    depreciationBatchSequenceComponentsPage = new DepreciationBatchSequenceComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationBatchSequenceComponentsPage.title), 5000);
    expect(await depreciationBatchSequenceComponentsPage.getTitle()).to.eq('Depreciation Batch Sequences');
    await browser.wait(
      ec.or(
        ec.visibilityOf(depreciationBatchSequenceComponentsPage.entities),
        ec.visibilityOf(depreciationBatchSequenceComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create DepreciationBatchSequence page', async () => {
    await depreciationBatchSequenceComponentsPage.clickOnCreateButton();
    depreciationBatchSequenceUpdatePage = new DepreciationBatchSequenceUpdatePage();
    expect(await depreciationBatchSequenceUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Batch Sequence');
    await depreciationBatchSequenceUpdatePage.cancel();
  });

  it('should create and save DepreciationBatchSequences', async () => {
    const nbButtonsBeforeCreate = await depreciationBatchSequenceComponentsPage.countDeleteButtons();

    await depreciationBatchSequenceComponentsPage.clickOnCreateButton();

    await promise.all([
      depreciationBatchSequenceUpdatePage.setStartIndexInput('5'),
      depreciationBatchSequenceUpdatePage.setEndIndexInput('5'),
      depreciationBatchSequenceUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      depreciationBatchSequenceUpdatePage.depreciationBatchStatusSelectLastOption(),
      depreciationBatchSequenceUpdatePage.setBatchSizeInput('5'),
      depreciationBatchSequenceUpdatePage.setProcessedItemsInput('5'),
      depreciationBatchSequenceUpdatePage.setSequenceNumberInput('5'),
      depreciationBatchSequenceUpdatePage.getIsLastBatchInput().click(),
      depreciationBatchSequenceUpdatePage.setProcessingTimeInput('PT12S'),
      depreciationBatchSequenceUpdatePage.setTotalItemsInput('5'),
      depreciationBatchSequenceUpdatePage.depreciationJobSelectLastOption(),
    ]);

    await depreciationBatchSequenceUpdatePage.save();
    expect(await depreciationBatchSequenceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depreciationBatchSequenceComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepreciationBatchSequence', async () => {
    const nbButtonsBeforeDelete = await depreciationBatchSequenceComponentsPage.countDeleteButtons();
    await depreciationBatchSequenceComponentsPage.clickOnLastDeleteButton();

    depreciationBatchSequenceDeleteDialog = new DepreciationBatchSequenceDeleteDialog();
    expect(await depreciationBatchSequenceDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Depreciation Batch Sequence?'
    );
    await depreciationBatchSequenceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(depreciationBatchSequenceComponentsPage.title), 5000);

    expect(await depreciationBatchSequenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
