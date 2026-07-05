import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AutonomousReportComponentsPage } from './autonomous-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AutonomousReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autonomousReportComponentsPage: AutonomousReportComponentsPage;
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

  it('should load AutonomousReports', async () => {
    await navBarPage.goToEntity('autonomous-report');
    autonomousReportComponentsPage = new AutonomousReportComponentsPage();
    await browser.wait(ec.visibilityOf(autonomousReportComponentsPage.title), 5000);
    expect(await autonomousReportComponentsPage.getTitle()).to.eq('Autonomous Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(autonomousReportComponentsPage.entities), ec.visibilityOf(autonomousReportComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
