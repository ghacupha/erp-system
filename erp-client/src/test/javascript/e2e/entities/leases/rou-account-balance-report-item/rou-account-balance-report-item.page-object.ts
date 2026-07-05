import { element, by } from 'protractor';

export class RouAccountBalanceReportItemComponentsPage {
  title = element.all(by.css('jhi-rou-account-balance-report-item div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}
