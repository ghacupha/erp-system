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

import { InstitutionContactDetailsDetailComponent } from './institution-contact-details-detail.component';

describe('InstitutionContactDetails Management Detail Component', () => {
  let comp: InstitutionContactDetailsDetailComponent;
  let fixture: ComponentFixture<InstitutionContactDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstitutionContactDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ institutionContactDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InstitutionContactDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InstitutionContactDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load institutionContactDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.institutionContactDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
