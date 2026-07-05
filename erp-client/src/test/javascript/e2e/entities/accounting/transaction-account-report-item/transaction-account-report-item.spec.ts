import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TransactionAccountReportItemComponentsPage } from './transaction-account-report-item.page-object';

const expect = chai.expect;

describe('TransactionAccountReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionAccountReportItemComponentsPage: TransactionAccountReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionAccountReportItems', async () => {
    await navBarPage.goToEntity('transaction-account-report-item');
    transactionAccountReportItemComponentsPage = new TransactionAccountReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(transactionAccountReportItemComponentsPage.title), 5000);
    expect(await transactionAccountReportItemComponentsPage.getTitle()).to.eq('Transaction Account Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(transactionAccountReportItemComponentsPage.entities),
        ec.visibilityOf(transactionAccountReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
