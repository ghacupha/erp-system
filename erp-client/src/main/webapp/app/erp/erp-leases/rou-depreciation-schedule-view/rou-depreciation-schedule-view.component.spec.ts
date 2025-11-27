///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import * as dayjs from 'dayjs';

import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { RouDepreciationScheduleViewService } from './rou-depreciation-schedule-view.service';
import { RouDepreciationScheduleViewComponent } from './rou-depreciation-schedule-view.component';

describe('RouDepreciationScheduleViewComponent', () => {
  let fixture: ComponentFixture<RouDepreciationScheduleViewComponent>;
  let component: RouDepreciationScheduleViewComponent;

  const paramMap$ = new BehaviorSubject(convertToParamMap({ leaseContractId: '10' }));
  const leaseContractServiceStub = {
    query: jasmine.createSpy('query').and.returnValue(
      of(
        new HttpResponse({
          body: [
            {
              id: 10,
              bookingId: 'LC-10',
              leaseTitle: 'HQ Lease',
            },
          ],
        })
      )
    ),
    find: jasmine.createSpy('find').and.returnValue(of(new HttpResponse({ body: { id: 10, bookingId: 'LC-10' } }))),
  } as Partial<IFRS16LeaseContractService>;
  const scheduleServiceStub = {
    loadSchedule: jasmine.createSpy('loadSchedule').and.returnValue(
      of([
        {
          entryId: 1,
          sequenceNumber: 1,
          periodCode: 'P1',
          periodEndDate: dayjs('2023-01-31'),
          initialAmount: 100,
          depreciationAmount: 20,
          outstandingAmount: 80,
        },
        {
          entryId: 2,
          sequenceNumber: 2,
          periodCode: 'P2',
          periodEndDate: dayjs('2023-02-28'),
          initialAmount: 100,
          depreciationAmount: 30,
          outstandingAmount: 50,
        },
      ])
    ),
  } as Partial<RouDepreciationScheduleViewService>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([])],
      declarations: [RouDepreciationScheduleViewComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { paramMap: paramMap$.asObservable() } },
        { provide: IFRS16LeaseContractService, useValue: leaseContractServiceStub },
        { provide: RouDepreciationScheduleViewService, useValue: scheduleServiceStub },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RouDepreciationScheduleViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load schedule rows from the service', () => {
    expect(scheduleServiceStub.loadSchedule).toHaveBeenCalledWith(10, jasmine.anything());
    const loadArgs = (scheduleServiceStub.loadSchedule as jasmine.Spy).calls.mostRecent().args;
    expect(dayjs.isDayjs(loadArgs[1])).toBeTrue();
    expect(component.scheduleRows.length).toBe(2);
  });

  it('should compute summary totals', () => {
    expect(component.initialAmount).toBe(100);
    expect(component.totalDepreciation).toBe(50);
    expect(component.closingBalance).toBe(50);
  });

  it('should filter rows and totals by the selected as-at date', () => {
    component.asAtDate = dayjs('2023-01-31');
    (component as any).fetchSchedule(10);

    expect(component.scheduleRows.length).toBe(1);
    expect(component.totalDepreciation).toBe(20);
    expect(component.closingBalance).toBe(80);
    expect(scheduleServiceStub.loadSchedule).toHaveBeenCalledWith(10, jasmine.anything());
  });
});
