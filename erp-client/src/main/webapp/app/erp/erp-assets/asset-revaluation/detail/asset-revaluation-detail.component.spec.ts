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

import { AssetRevaluationDetailComponent } from './asset-revaluation-detail.component';

describe('AssetRevaluation Management Detail Component', () => {
  let comp: AssetRevaluationDetailComponent;
  let fixture: ComponentFixture<AssetRevaluationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetRevaluationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetRevaluation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetRevaluationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetRevaluationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetRevaluation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetRevaluation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
