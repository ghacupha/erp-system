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
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsoCountryCodeDetailComponent } from './iso-country-code-detail.component';

describe('IsoCountryCode Management Detail Component', () => {
  let comp: IsoCountryCodeDetailComponent;
  let fixture: ComponentFixture<IsoCountryCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IsoCountryCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ isoCountryCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IsoCountryCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IsoCountryCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load isoCountryCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.isoCountryCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
