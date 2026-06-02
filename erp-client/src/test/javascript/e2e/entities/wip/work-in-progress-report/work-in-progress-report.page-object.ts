import { element, by } from 'protractor';

export class WorkInProgressReportComponentsPage {
  title = element.all(by.css('jhi-work-in-progress-report div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}
