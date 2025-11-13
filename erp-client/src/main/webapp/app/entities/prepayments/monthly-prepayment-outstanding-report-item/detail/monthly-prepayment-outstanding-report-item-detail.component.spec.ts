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
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MonthlyPrepaymentOutstandingReportItemDetailComponent } from './monthly-prepayment-outstanding-report-item-detail.component';

describe('MonthlyPrepaymentOutstandingReportItem Management Detail Component', () => {
  let comp: MonthlyPrepaymentOutstandingReportItemDetailComponent;
  let fixture: ComponentFixture<MonthlyPrepaymentOutstandingReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MonthlyPrepaymentOutstandingReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ monthlyPrepaymentOutstandingReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MonthlyPrepaymentOutstandingReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MonthlyPrepaymentOutstandingReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load monthlyPrepaymentOutstandingReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.monthlyPrepaymentOutstandingReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
