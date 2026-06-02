import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { WorkInProgressOverviewComponentsPage } from './work-in-progress-overview.page-object';

const expect = chai.expect;

describe('WorkInProgressOverview e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressOverviewComponentsPage: WorkInProgressOverviewComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WorkInProgressOverviews', async () => {
    await navBarPage.goToEntity('work-in-progress-overview');
    workInProgressOverviewComponentsPage = new WorkInProgressOverviewComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressOverviewComponentsPage.title), 5000);
    expect(await workInProgressOverviewComponentsPage.getTitle()).to.eq('Work In Progress Overviews');
    await browser.wait(
      ec.or(ec.visibilityOf(workInProgressOverviewComponentsPage.entities), ec.visibilityOf(workInProgressOverviewComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
