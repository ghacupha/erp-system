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

import { UniversallyUniqueMappingDetailComponent } from './universally-unique-mapping-detail.component';

describe('UniversallyUniqueMapping Management Detail Component', () => {
  let comp: UniversallyUniqueMappingDetailComponent;
  let fixture: ComponentFixture<UniversallyUniqueMappingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UniversallyUniqueMappingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ universallyUniqueMapping: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UniversallyUniqueMappingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UniversallyUniqueMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load universallyUniqueMapping on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.universallyUniqueMapping).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
