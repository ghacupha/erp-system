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
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { RouDepreciationScheduleViewService } from './rou-depreciation-schedule-view.service';
import { RouDepreciationScheduleViewComponent } from './rou-depreciation-schedule-view.component';

describe('RouDepreciationScheduleViewComponent', () => {
  let fixture: ComponentFixture<RouDepreciationScheduleViewComponent>;
  let component: RouDepreciationScheduleViewComponent;

  const paramMap$ = new BehaviorSubject(convertToParamMap({ leaseContractId: '10' }));
  const leaseContractServiceStub = {
    find: jasmine.createSpy('find').and.returnValue(of(new HttpResponse({ body: { id: 10, bookingId: 'LC-10' } }))),
  } as Partial<IFRS16LeaseContractService>;
  const scheduleServiceStub = {
    loadSchedule: jasmine.createSpy('loadSchedule').and.returnValue(
      of([
        {
          entryId: 1,
          sequenceNumber: 1,
          periodCode: 'P1',
          initialAmount: 100,
          depreciationAmount: 20,
          outstandingAmount: 80,
        },
        {
          entryId: 2,
          sequenceNumber: 2,
          periodCode: 'P2',
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
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(RouDepreciationScheduleViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load schedule rows from the service', () => {
    expect(scheduleServiceStub.loadSchedule).toHaveBeenCalledWith(10);
    expect(component.scheduleRows.length).toBe(2);
  });

  it('should load the selected contract for the route parameter', () => {
    expect(leaseContractServiceStub.find).toHaveBeenCalledWith(10);
    expect(component.selectedContract?.id).toBe(10);
  });

  it('should compute summary totals', () => {
    expect(component.initialAmount).toBe(100);
    expect(component.totalDepreciation).toBe(50);
    expect(component.closingBalance).toBe(50);
  });
});
