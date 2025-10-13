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
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouDepreciationEntryReportItemDetailComponent } from './rou-depreciation-entry-report-item-detail.component';

describe('RouDepreciationEntryReportItem Management Detail Component', () => {
  let comp: RouDepreciationEntryReportItemDetailComponent;
  let fixture: ComponentFixture<RouDepreciationEntryReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouDepreciationEntryReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouDepreciationEntryReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouDepreciationEntryReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouDepreciationEntryReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouDepreciationEntryReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouDepreciationEntryReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
