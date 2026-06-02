import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { WorkInProgressOutstandingReportComponentsPage } from './work-in-progress-outstanding-report.page-object';

const expect = chai.expect;

describe('WorkInProgressOutstandingReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressOutstandingReportComponentsPage: WorkInProgressOutstandingReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WorkInProgressOutstandingReports', async () => {
    await navBarPage.goToEntity('work-in-progress-outstanding-report');
    workInProgressOutstandingReportComponentsPage = new WorkInProgressOutstandingReportComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressOutstandingReportComponentsPage.title), 5000);
    expect(await workInProgressOutstandingReportComponentsPage.getTitle()).to.eq('Work In Progress Outstanding Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(workInProgressOutstandingReportComponentsPage.entities),
        ec.visibilityOf(workInProgressOutstandingReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
