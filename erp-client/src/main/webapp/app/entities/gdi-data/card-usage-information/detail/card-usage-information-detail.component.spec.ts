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

import { CardUsageInformationDetailComponent } from './card-usage-information-detail.component';

describe('CardUsageInformation Management Detail Component', () => {
  let comp: CardUsageInformationDetailComponent;
  let fixture: ComponentFixture<CardUsageInformationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardUsageInformationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardUsageInformation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardUsageInformationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardUsageInformationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardUsageInformation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardUsageInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
