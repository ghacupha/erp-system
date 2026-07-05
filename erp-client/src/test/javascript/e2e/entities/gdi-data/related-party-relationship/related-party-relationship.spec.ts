import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  RelatedPartyRelationshipComponentsPage,
  /* RelatedPartyRelationshipDeleteDialog, */
  RelatedPartyRelationshipUpdatePage,
} from './related-party-relationship.page-object';

const expect = chai.expect;

describe('RelatedPartyRelationship e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let relatedPartyRelationshipComponentsPage: RelatedPartyRelationshipComponentsPage;
  let relatedPartyRelationshipUpdatePage: RelatedPartyRelationshipUpdatePage;
  /* let relatedPartyRelationshipDeleteDialog: RelatedPartyRelationshipDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RelatedPartyRelationships', async () => {
    await navBarPage.goToEntity('related-party-relationship');
    relatedPartyRelationshipComponentsPage = new RelatedPartyRelationshipComponentsPage();
    await browser.wait(ec.visibilityOf(relatedPartyRelationshipComponentsPage.title), 5000);
    expect(await relatedPartyRelationshipComponentsPage.getTitle()).to.eq('Related Party Relationships');
    await browser.wait(
      ec.or(
        ec.visibilityOf(relatedPartyRelationshipComponentsPage.entities),
        ec.visibilityOf(relatedPartyRelationshipComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RelatedPartyRelationship page', async () => {
    await relatedPartyRelationshipComponentsPage.clickOnCreateButton();
    relatedPartyRelationshipUpdatePage = new RelatedPartyRelationshipUpdatePage();
    expect(await relatedPartyRelationshipUpdatePage.getPageTitle()).to.eq('Create or edit a Related Party Relationship');
    await relatedPartyRelationshipUpdatePage.cancel();
  });

  /* it('should create and save RelatedPartyRelationships', async () => {
        const nbButtonsBeforeCreate = await relatedPartyRelationshipComponentsPage.countDeleteButtons();

        await relatedPartyRelationshipComponentsPage.clickOnCreateButton();

        await promise.all([
            relatedPartyRelationshipUpdatePage.setReportingDateInput('2000-12-31'),
            relatedPartyRelationshipUpdatePage.setCustomerIdInput('customerId'),
            relatedPartyRelationshipUpdatePage.setRelatedPartyIdInput('relatedPartyId'),
            relatedPartyRelationshipUpdatePage.bankCodeSelectLastOption(),
            relatedPartyRelationshipUpdatePage.branchIdSelectLastOption(),
            relatedPartyRelationshipUpdatePage.relationshipTypeSelectLastOption(),
        ]);

        await relatedPartyRelationshipUpdatePage.save();
        expect(await relatedPartyRelationshipUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await relatedPartyRelationshipComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RelatedPartyRelationship', async () => {
        const nbButtonsBeforeDelete = await relatedPartyRelationshipComponentsPage.countDeleteButtons();
        await relatedPartyRelationshipComponentsPage.clickOnLastDeleteButton();

        relatedPartyRelationshipDeleteDialog = new RelatedPartyRelationshipDeleteDialog();
        expect(await relatedPartyRelationshipDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Related Party Relationship?');
        await relatedPartyRelationshipDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(relatedPartyRelationshipComponentsPage.title), 5000);

        expect(await relatedPartyRelationshipComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
