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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import * as dayjs from 'dayjs';
import * as XLSX from 'xlsx';

import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityService } from '../lease-liability/service/lease-liability.service';
import { LeaseLiabilityScheduleItemService } from '../lease-liability-schedule-item/service/lease-liability-schedule-item.service';
import { LeaseRepaymentPeriodService } from '../lease-repayment-period/service/lease-repayment-period.service';
import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ILeaseLiability } from '../lease-liability/lease-liability.model';
import { ILeaseLiabilityScheduleItem } from '../lease-liability-schedule-item/lease-liability-schedule-item.model';
import { ILeaseRepaymentPeriod } from '../lease-repayment-period/lease-repayment-period.model';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

import { LeaseLiabilityScheduleViewComponent } from './lease-liability-schedule-view.component';

describe('LeaseLiabilityScheduleViewComponent', () => {
  let fixture: ComponentFixture<LeaseLiabilityScheduleViewComponent>;
  let component: LeaseLiabilityScheduleViewComponent;
  let leaseLiabilityService: jest.Mocked<LeaseLiabilityService>;
  let scheduleItemService: jest.Mocked<LeaseLiabilityScheduleItemService>;
  let leaseRepaymentPeriodService: jest.Mocked<LeaseRepaymentPeriodService>;
  let contractService: jest.Mocked<IFRS16LeaseContractService>;

  const contractId = 5;
  const periodOne: ILeaseRepaymentPeriod = {
    id: 1,
    sequenceNumber: 1,
    startDate: dayjs('2024-01-01'),
    endDate: dayjs('2024-01-31'),
    periodCode: '2024-01',
  };
  const periodTwo: ILeaseRepaymentPeriod = {
    id: 2,
    sequenceNumber: 2,
    startDate: dayjs('2024-02-01'),
    endDate: dayjs('2024-02-29'),
    periodCode: '2024-02',
  };

  const contract: IIFRS16LeaseContract = {
    id: contractId,
    leaseTitle: 'Main Warehouse Lease',
  };

  const liability: ILeaseLiability = {
    id: 33,
    leaseId: 'LL-001',
    liabilityAmount: 125000,
    startDate: periodOne.startDate,
  };

  const scheduleItems: ILeaseLiabilityScheduleItem[] = [
    {
      id: 10,
      sequenceNumber: 1,
      openingBalance: 125000,
      cashPayment: 1500,
      principalPayment: 1100,
      interestPayment: 400,
      outstandingBalance: 123900,
      interestPayableClosing: 275,
      leasePeriod: periodOne,
      active: true,
    },
    {
      id: 11,
      sequenceNumber: 2,
      openingBalance: 123900,
      cashPayment: 1600,
      principalPayment: 1150,
      interestPayment: 450,
      outstandingBalance: 122750,
      interestPayableClosing: 250,
      leasePeriod: periodTwo,
      active: true,
    },
    {
      id: 12,
      sequenceNumber: 3,
      openingBalance: 122750,
      cashPayment: 1650,
      principalPayment: 1200,
      interestPayment: 450,
      outstandingBalance: 121550,
      interestPayableClosing: 225,
      leasePeriod: periodTwo,
      active: true,
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [LeaseLiabilityScheduleViewComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({ contractId: `${contractId}` })),
          },
        },
        {
          provide: LeaseLiabilityService,
          useValue: { query: jest.fn() },
        },
        {
          provide: LeaseLiabilityScheduleItemService,
          useValue: { query: jest.fn() },
        },
        {
          provide: LeaseRepaymentPeriodService,
          useValue: { query: jest.fn() },
        },
        {
          provide: IFRS16LeaseContractService,
          useValue: { find: jest.fn() },
        },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityScheduleViewComponent);
    component = fixture.componentInstance;

    leaseLiabilityService = TestBed.inject(LeaseLiabilityService) as jest.Mocked<LeaseLiabilityService>;
    scheduleItemService = TestBed.inject(LeaseLiabilityScheduleItemService) as jest.Mocked<LeaseLiabilityScheduleItemService>;
    leaseRepaymentPeriodService = TestBed.inject(LeaseRepaymentPeriodService) as jest.Mocked<LeaseRepaymentPeriodService>;
    contractService = TestBed.inject(IFRS16LeaseContractService) as jest.Mocked<IFRS16LeaseContractService>;

    contractService.find.mockReturnValue(of(new HttpResponse<IIFRS16LeaseContract>({ body: contract })));
    leaseLiabilityService.query.mockReturnValue(of(new HttpResponse<ILeaseLiability[]>({ body: [liability] })));
    leaseRepaymentPeriodService.query.mockReturnValue(
      of(
        new HttpResponse<ILeaseRepaymentPeriod[]>({
          body: [
            { ...periodOne, startDate: periodOne.startDate?.toISOString(), endDate: periodOne.endDate?.toISOString() },
            { ...periodTwo, startDate: periodTwo.startDate?.toISOString(), endDate: periodTwo.endDate?.toISOString() },
          ],
        })
      )
    );
    scheduleItemService.query.mockReturnValue(
      of(
        new HttpResponse<ILeaseLiabilityScheduleItem[]>({
          body: scheduleItems.map(item => ({
            ...item,
            leasePeriod: item.leasePeriod
              ? {
                  ...item.leasePeriod,
                  startDate: item.leasePeriod.startDate?.toISOString(),
                  endDate: item.leasePeriod.endDate?.toISOString(),
                }
              : undefined,
          })),
        })
      )
    );
  });

  it('should load contract and related schedule data on init', () => {
    fixture.detectChanges();

    expect(contractService.find).toHaveBeenCalledWith(contractId);
    expect(leaseLiabilityService.query).toHaveBeenCalledWith({ 'leaseContractId.equals': contractId, size: 1 });
    expect(scheduleItemService.query).toHaveBeenCalledWith({
      'leaseContractId.equals': contractId,
      'active.equals': true,
      sort: ['sequenceNumber,asc'],
      size: 1000,
    });
    expect(leaseRepaymentPeriodService.query).toHaveBeenCalledWith({
      'leaseContractId.equals': contractId,
      sort: ['sequenceNumber,asc'],
      size: 500,
    });

    expect(component.leaseContract).toEqual(contract);
    expect(component.leaseLiability).toEqual(liability);
    expect(component.reportingPeriods).toHaveLength(2);
    expect(component.visibleItems).toHaveLength(2);
    expect(component.summary).toEqual(
      expect.objectContaining({
        cashTotal: 3250,
        principalTotal: 2350,
        interestTotal: 900,
        outstandingTotal: 244300,
        interestPayableTotal: 475,
      })
    );
  });

  it('should drop inactive schedule rows from the prepared dataset', () => {
    fixture.detectChanges();

    const response = new HttpResponse<ILeaseLiabilityScheduleItem[]>({
      body: [
        { id: 1, sequenceNumber: 1, active: true },
        { id: 2, sequenceNumber: 2, active: false },
      ],
    });

    const prepared: ILeaseLiabilityScheduleItem[] = (component as any).prepareScheduleItems(response);

    expect(prepared).toHaveLength(1);
    expect(prepared[0].id).toBe(1);
    expect(prepared[0].active).toBe(true);
  });

  it('should update summary when the period selection changes without altering visible items', () => {
    fixture.detectChanges();

    component.onPeriodChange(`${periodOne.id}`);
    fixture.detectChanges();

    expect(component.visibleItems).toHaveLength(2);
    expect(component.summary).toEqual(
      expect.objectContaining({
        cashTotal: 1500,
        principalTotal: 1100,
        interestTotal: 400,
        outstandingTotal: 123900,
        interestPayableTotal: 275,
      })
    );
  });

  it('should calculate payment day gaps based on prior cash settlements', () => {
    fixture.detectChanges();

    const customPeriods: ILeaseRepaymentPeriod[] = [
      { id: 21, sequenceNumber: 1, startDate: dayjs('2024-01-01') },
      { id: 22, sequenceNumber: 2 },
      { id: 23, sequenceNumber: 3, startDate: dayjs('2024-02-10') },
      { id: 24, sequenceNumber: 4, startDate: dayjs('2024-03-05') },
    ];
    const customItems: ILeaseLiabilityScheduleItem[] = [
      { sequenceNumber: 1, cashPayment: 500, leasePeriod: customPeriods[0] },
      { sequenceNumber: 2, cashPayment: 450, leasePeriod: customPeriods[1] },
      { sequenceNumber: 3, cashPayment: 0, leasePeriod: customPeriods[2] },
      { sequenceNumber: 4, cashPayment: 700, leasePeriod: customPeriods[3] },
    ];

    component.visibleItems = customItems;

    expect(component.paymentDaysFromPrevious(0)).toBe(0);
    expect(component.paymentDaysFromPrevious(1)).toBeNull();
    expect(component.paymentDaysFromPrevious(2)).toBe(40);
    expect(component.paymentDaysFromPrevious(3)).toBe(64);
  });

  it('should render the schedule table with formatted values', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const cards = Array.from(compiled.querySelectorAll('.card-title'));
    expect(cards[0].textContent).toContain('125,000.00');

    const tableRows = Array.from(compiled.querySelectorAll('tbody tr'));
    expect(tableRows).toHaveLength(2);
    expect(tableRows[0].textContent).toContain('2024-02');
    expect(tableRows[0].textContent).toContain('01 Feb 2024');
    expect(tableRows[0].textContent).toContain('1,600.00');
    const dayCells = tableRows.map(row => row.querySelectorAll('td')[3]?.textContent?.trim());
    expect(dayCells[0]).toBe('0');
    expect(dayCells[1]).toBe('0');
  });

  it('should create a worksheet and trigger a download when exporting to Excel', () => {
    fixture.detectChanges();

    const aoaSpy = jest.spyOn(XLSX.utils, 'aoa_to_sheet');
    const writeSpy = jest.spyOn(XLSX, 'write').mockReturnValue(new ArrayBuffer(8) as any);
    const createObjectUrlSpy = jest.spyOn(URL, 'createObjectURL').mockReturnValue('blob:export');
    const revokeObjectUrlSpy = jest.spyOn(URL, 'revokeObjectURL').mockImplementation(() => undefined);
    const anchor = document.createElement('a');
    const clickSpy = jest.spyOn(anchor, 'click').mockImplementation(() => undefined);
    const createElementSpy = jest.spyOn(document, 'createElement').mockReturnValue(anchor);

    try {
      component.exportDashboardToExcel();

      expect(aoaSpy).toHaveBeenCalled();
      expect(writeSpy).toHaveBeenCalledWith(expect.anything(), expect.objectContaining({ bookType: 'xlsx', type: 'array' }));
      expect(createObjectUrlSpy).toHaveBeenCalled();
      expect(clickSpy).toHaveBeenCalled();
      expect(revokeObjectUrlSpy).toHaveBeenCalledWith('blob:export');

      const worksheetRows = aoaSpy.mock.calls[0][0] as (string | number)[][];
      const scheduleRow = worksheetRows.find(row => row[0] === component.visibleItems[0].sequenceNumber);
      const nextScheduleRow = worksheetRows.find(row => row[0] === component.visibleItems[1].sequenceNumber);
      expect(scheduleRow?.[4]).toBe(0);
      expect(nextScheduleRow?.[4]).toBe(0);
    } finally {
      aoaSpy.mockRestore();
      writeSpy.mockRestore();
      createObjectUrlSpy.mockRestore();
      revokeObjectUrlSpy.mockRestore();
      createElementSpy.mockRestore();
      clickSpy.mockRestore();
    }
  });
});
