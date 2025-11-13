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

import { ProcessStatusDetailComponent } from './process-status-detail.component';

describe('ProcessStatus Management Detail Component', () => {
  let comp: ProcessStatusDetailComponent;
  let fixture: ComponentFixture<ProcessStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcessStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ processStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProcessStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProcessStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load processStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.processStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
