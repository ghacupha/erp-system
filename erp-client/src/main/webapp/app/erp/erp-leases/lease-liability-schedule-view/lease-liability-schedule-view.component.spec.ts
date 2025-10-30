///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
    expect(component.filteredItems).toHaveLength(2);
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

  it('should update filtered items and summary when the period selection changes', () => {
    fixture.detectChanges();

    component.onPeriodChange(`${periodOne.id}`);
    fixture.detectChanges();

    expect(component.filteredItems).toHaveLength(1);
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
  });
});
