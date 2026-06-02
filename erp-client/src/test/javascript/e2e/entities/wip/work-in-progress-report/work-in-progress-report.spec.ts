import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { WorkInProgressReportComponentsPage } from './work-in-progress-report.page-object';

const expect = chai.expect;

describe('WorkInProgressReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressReportComponentsPage: WorkInProgressReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WorkInProgressReports', async () => {
    await navBarPage.goToEntity('work-in-progress-report');
    workInProgressReportComponentsPage = new WorkInProgressReportComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressReportComponentsPage.title), 5000);
    expect(await workInProgressReportComponentsPage.getTitle()).to.eq('Work In Progress Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(workInProgressReportComponentsPage.entities), ec.visibilityOf(workInProgressReportComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
