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

import { LeaseLiabilityDetailComponent } from './lease-liability-detail.component';

describe('LeaseLiability Management Detail Component', () => {
  let comp: LeaseLiabilityDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiability: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiability on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiability).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
