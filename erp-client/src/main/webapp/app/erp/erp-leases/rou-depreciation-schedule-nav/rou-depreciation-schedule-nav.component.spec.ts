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
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { provideMockStore, MockStore } from '@ngrx/store/testing';

import { RouDepreciationScheduleNavComponent } from './rou-depreciation-schedule-nav.component';
import { ifrs16LeaseContractReportSelected } from '../../store/actions/ifrs16-lease-contract-report.actions';

describe('RouDepreciationScheduleNavComponent', () => {
  let component: RouDepreciationScheduleNavComponent;
  let fixture: ComponentFixture<RouDepreciationScheduleNavComponent>;
  let store: MockStore;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      declarations: [RouDepreciationScheduleNavComponent],
      providers: [provideMockStore()],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    store = TestBed.inject(MockStore);
    router = TestBed.inject(Router);
    spyOn(store, 'dispatch');
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(RouDepreciationScheduleNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should dispatch the selected lease contract and navigate to the depreciation dashboard', () => {
    const leaseContract = { id: 78 } as any;

    component.updateLeaseContract(leaseContract);
    component.launchReport();

    expect(store.dispatch).toHaveBeenCalledWith(
      ifrs16LeaseContractReportSelected({ selectedLeaseContractId: 78 })
    );
    expect(router.navigate).toHaveBeenCalledWith(['/erp/rou-depreciation-schedule-view', 78]);
  });

  it('should mark the lease contract control when navigation is attempted without a selection', () => {
    component.launchReport();

    expect(component.editForm.get('leaseContract')!.touched).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
