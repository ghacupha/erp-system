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

import { TaxRuleDetailComponent } from './tax-rule-detail.component';

describe('TaxRule Management Detail Component', () => {
  let comp: TaxRuleDetailComponent;
  let fixture: ComponentFixture<TaxRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaxRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ taxRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TaxRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TaxRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load taxRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.taxRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
