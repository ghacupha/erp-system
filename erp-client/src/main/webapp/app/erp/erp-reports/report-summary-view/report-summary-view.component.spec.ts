///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { of, throwError } from 'rxjs';
import { Store } from '@ngrx/store';

import { ReportSummaryViewComponent } from './report-summary-view.component';
import { ReportSummaryDataService } from './report-summary-data.service';
import * as ExportUtil from './report-summary-export.util';
import { IReportFilterDefinition, IReportMetadata } from '../report-metadata/report-metadata.model';
import { ReportFilterOptionService } from './report-filter-option.service';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';
import { ifrs16LeaseContractReportReset } from 'app/erp/store/actions/ifrs16-lease-contract-report.actions';

describe('ReportSummaryViewComponent (export behaviour)', () => {
  let component: ReportSummaryViewComponent;
  let summaryDataService: jasmine.SpyObj<ReportSummaryDataService>;
  let filterOptionService: jasmine.SpyObj<ReportFilterOptionService>;
  let anchorElement: HTMLAnchorElement;
  let router: { navigate: jasmine.Spy; url: string };
  let store: jasmine.SpyObj<Store<unknown>>;

  beforeEach(() => {
    summaryDataService = jasmine.createSpyObj('ReportSummaryDataService', ['fetchSummary', 'fetchAllSummary']);
    filterOptionService = jasmine.createSpyObj('ReportFilterOptionService', ['loadOptions']);
    router = { navigate: jasmine.createSpy('navigate'), url: '/reports/view/sample' };
    store = jasmine.createSpyObj('Store', ['select', 'dispatch']);
    store.select.and.returnValue(of(undefined));
    store.dispatch.and.stub();

    component = new ReportSummaryViewComponent(
      { paramMap: of() } as any,
      { setTitle: () => undefined } as any,
      {} as any,
      summaryDataService,
      filterOptionService,
      router as any,
      store
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

  it('should initialise metadata-driven filters and request summary data once options load', () => {
    const definitions: IReportFilterDefinition[] = [
      { label: 'Lease Period', queryParameterKey: 'leasePeriodId.equals', valueSource: 'leasePeriods' },
      { label: 'Lease Contract', queryParameterKey: 'leaseLiabilityId.equals', valueSource: 'leaseContracts' },
    ];
    const metadata = {
      backendApi: '/api/report/summary',
      reportTitle: 'Test Report',
      filters: definitions,
    } as IReportMetadata;

    component.metadata = metadata;
    summaryDataService.fetchSummary.and.returnValue(of([]));
    const leasePeriods: ILeasePeriod[] = [{ id: 101, periodCode: '2024-01' }];
    filterOptionService.loadOptions.and.returnValues(
      of([{ value: leasePeriods[0].id, label: 'Period 101', raw: leasePeriods[0] }]),
      of([{ value: 202, label: 'Contract 202' }])
    );

    (component as any).initializeFilters(definitions);

    expect(component.filters.length).toBe(2);
    expect(component.filters[0].selected?.value).toBe(101);
    expect(component.filters[1].selected?.value).toBe(202);
    expect(summaryDataService.fetchSummary).toHaveBeenCalledWith('/api/report/summary', {
      'leasePeriodId.equals': 101,
      'leaseLiabilityId.equals': 202,
    });
  });

  it('should redirect to the parameter screen when the lease contract selection is missing', () => {
    router.navigate.calls.reset();
    router.url = '/reports/view/lease-liability-schedule-report';

    (component as any).handleSlugChange('lease-liability-schedule-report');

    expect(router.navigate).toHaveBeenCalledWith(['/lease-liability-schedule-report/report-nav']);
  });

  it('should apply a stored lease contract selection to the dashboard filters', () => {
    router.navigate.calls.reset();
    router.url = '/reports/view/lease-liability-schedule-report';
    store.select.and.returnValue(of(303));

    (component as any).filters = [
      {
        definition: { label: 'Lease', queryParameterKey: 'leaseLiabilityId.equals', valueSource: 'leaseContracts' } as any,
        options: [{ value: 303, label: 'Lease 303' }],
        selected: null,
        loading: false,
        error: null,
        lastSearchTerm: undefined,
      },
    ];
    (component as any).leaseContractFilterKey = 'leaseLiabilityId.equals';
    (component as any).pendingFilterRequests = 0;
    const loadSpy = spyOn<any>(component, 'loadSummaryData');

    (component as any).handleSlugChange('lease-liability-schedule-report');

    expect(component.filters[0].selected?.value).toBe(303);
    expect(loadSpy).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should build query parameters from selected filter values', () => {
    const definitions: IReportFilterDefinition[] = [
      { label: 'Period', queryParameterKey: 'leasePeriodId.equals', valueSource: 'leasePeriods' },
      { label: 'Month', queryParameterKey: 'fiscalMonthId.equals', valueSource: 'fiscalMonths' },
      { label: 'Ignored', queryParameterKey: '', valueSource: 'unknown' },
    ];

    component.filters = [
      {
        definition: definitions[0],
        options: [],
        selected: { value: 11, label: 'Period 11' } as any,
        loading: false,
        error: null,
        lastSearchTerm: undefined,
      },
      {
        definition: definitions[1],
        options: [],
        selected: { value: 22, label: 'Month 22' } as any,
        loading: false,
        error: null,
        lastSearchTerm: undefined,
      },
      {
        definition: definitions[2],
        options: [],
        selected: { value: '', label: 'Blank' } as any,
        loading: false,
        error: null,
        lastSearchTerm: undefined,
      },
    ] as any;

    const params = (component as any).buildQueryParams();

    expect(params).toEqual({
      'leasePeriodId.equals': 11,
      'fiscalMonthId.equals': 22,
    });
  });

  afterEach(() => {
    (URL.createObjectURL as jasmine.Spy | undefined)?.and?.callThrough?.();
    (URL.revokeObjectURL as jasmine.Spy | undefined)?.and?.callThrough?.();
  });

  it('should dispatch a reset action when destroyed', () => {
    component.ngOnDestroy();

    expect(store.dispatch).toHaveBeenCalledWith(ifrs16LeaseContractReportReset());
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
