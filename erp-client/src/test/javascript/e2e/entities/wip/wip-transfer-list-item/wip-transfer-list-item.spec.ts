import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { WIPTransferListItemComponentsPage } from './wip-transfer-list-item.page-object';

const expect = chai.expect;

describe('WIPTransferListItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let wIPTransferListItemComponentsPage: WIPTransferListItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WIPTransferListItems', async () => {
    await navBarPage.goToEntity('wip-transfer-list-item');
    wIPTransferListItemComponentsPage = new WIPTransferListItemComponentsPage();
    await browser.wait(ec.visibilityOf(wIPTransferListItemComponentsPage.title), 5000);
    expect(await wIPTransferListItemComponentsPage.getTitle()).to.eq('WIP Transfer List Items');
    await browser.wait(
      ec.or(ec.visibilityOf(wIPTransferListItemComponentsPage.entities), ec.visibilityOf(wIPTransferListItemComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
