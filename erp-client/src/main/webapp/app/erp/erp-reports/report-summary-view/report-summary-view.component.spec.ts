import { of, throwError } from 'rxjs';

import { ReportSummaryViewComponent } from './report-summary-view.component';
import { ReportSummaryDataService } from './report-summary-data.service';
import * as ExportUtil from './report-summary-export.util';
import { IReportMetadata } from '../report-metadata/report-metadata.model';

describe('ReportSummaryViewComponent (export behaviour)', () => {
  let component: ReportSummaryViewComponent;
  let summaryDataService: jasmine.SpyObj<ReportSummaryDataService>;
  let anchorElement: HTMLAnchorElement;

  beforeEach(() => {
    summaryDataService = jasmine.createSpyObj('ReportSummaryDataService', ['fetchSummary', 'fetchAllSummary']);

    component = new ReportSummaryViewComponent(
      { paramMap: of() } as any,
      { setTitle: () => undefined } as any,
      {} as any,
      summaryDataService,
      {} as any,
      {} as any
    );

    component.metadata = {
      backendApi: '/api/report/summary',
      reportTitle: 'Sample Export',
    } as IReportMetadata;

    component.displayedColumns = ['account', 'amount'];
    anchorElement = document.createElement('a');
    spyOn(document, 'createElement').and.returnValue(anchorElement);
    spyOn(anchorElement, 'click');
  });

  afterEach(() => {
    (URL.createObjectURL as jasmine.Spy | undefined)?.and?.callThrough?.();
    (URL.revokeObjectURL as jasmine.Spy | undefined)?.and?.callThrough?.();
  });

  it('should export data as CSV using the export utility helpers', () => {
    const records = [{ account: 'Lease-001', amount: 1250 }];
    summaryDataService.fetchAllSummary.and.returnValue(of(records));
    spyOn(ExportUtil, 'deriveOrderedColumns').and.callThrough();
    spyOn(ExportUtil, 'buildCsvContent').and.callThrough();
    const createUrlSpy = spyOn(URL, 'createObjectURL').and.returnValue('blob:csv');
    const revokeSpy = spyOn(URL, 'revokeObjectURL');

    component.exportToCsv();

    expect(summaryDataService.fetchAllSummary).toHaveBeenCalledWith('/api/report/summary', {});
    expect(ExportUtil.deriveOrderedColumns).toHaveBeenCalledWith(records, ['account', 'amount']);
    expect(ExportUtil.buildCsvContent).toHaveBeenCalled();
    expect(createUrlSpy).toHaveBeenCalled();
    expect(anchorElement.click).toHaveBeenCalled();
    expect(revokeSpy).toHaveBeenCalledWith('blob:csv');
    expect(component.exportErrorMessage).toBeNull();
    expect(component.exportingCsv).toBeFalse();
  });

  it('should export data as Excel using the export utility helpers', () => {
    const records = [{ account: 'Lease-002', amount: 2000 }];
    summaryDataService.fetchAllSummary.and.returnValue(of(records));
    spyOn(ExportUtil, 'deriveOrderedColumns').and.returnValue(['account', 'amount']);
    spyOn(ExportUtil, 'buildExcelArrayBuffer').and.returnValue(new ArrayBuffer(8));
    const createUrlSpy = spyOn(URL, 'createObjectURL').and.returnValue('blob:xlsx');
    const revokeSpy = spyOn(URL, 'revokeObjectURL');

    component.exportToExcel();

    expect(summaryDataService.fetchAllSummary).toHaveBeenCalledWith('/api/report/summary', {});
    expect(ExportUtil.buildExcelArrayBuffer).toHaveBeenCalledWith(records, ['account', 'amount'], jasmine.any(Function));
    expect(createUrlSpy).toHaveBeenCalled();
    expect(anchorElement.click).toHaveBeenCalled();
    expect(revokeSpy).toHaveBeenCalledWith('blob:xlsx');
    expect(component.exportErrorMessage).toBeNull();
    expect(component.exportingExcel).toBeFalse();
  });

  it('should surface an error message when the export fails', () => {
    summaryDataService.fetchAllSummary.and.returnValue(throwError(() => new Error('network')));
    spyOn(URL, 'createObjectURL');

    component.exportToCsv();

    expect(component.exportErrorMessage).toBe('Unable to export report data. Please try again later.');
    expect(component.exportingCsv).toBeFalse();
  });

  it('should show a message when export data is empty', () => {
    summaryDataService.fetchAllSummary.and.returnValue(of([]));
    spyOn(URL, 'createObjectURL');

    component.exportToExcel();

    expect(component.exportErrorMessage).toBe('No data is available for export with the current filters.');
    expect(component.exportingExcel).toBeFalse();
    expect(anchorElement.click).not.toHaveBeenCalled();
  });
});
