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

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MetricsComponent } from './metrics.component';
import { MetricsService } from './metrics.service';
import { Metrics } from './metrics.model';

describe('MetricsComponent', () => {
  let comp: MetricsComponent;
  let fixture: ComponentFixture<MetricsComponent>;
  let service: MetricsService;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MetricsComponent],
      })
        .overrideTemplate(MetricsComponent, '')
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MetricsService);
  });

  describe('refresh', () => {
    it('should call refresh on init', () => {
      // GIVEN
      jest.spyOn(service, 'getMetrics').mockReturnValue(of({} as Metrics));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.getMetrics).toHaveBeenCalled();
    });
  });
});
