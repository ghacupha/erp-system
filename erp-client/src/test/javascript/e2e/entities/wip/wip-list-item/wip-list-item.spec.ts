import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { WIPListItemComponentsPage } from './wip-list-item.page-object';

const expect = chai.expect;

describe('WIPListItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let wIPListItemComponentsPage: WIPListItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WIPListItems', async () => {
    await navBarPage.goToEntity('wip-list-item');
    wIPListItemComponentsPage = new WIPListItemComponentsPage();
    await browser.wait(ec.visibilityOf(wIPListItemComponentsPage.title), 5000);
    expect(await wIPListItemComponentsPage.getTitle()).to.eq('WIP List Items');
    await browser.wait(
      ec.or(ec.visibilityOf(wIPListItemComponentsPage.entities), ec.visibilityOf(wIPListItemComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
