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
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';

import { HealthComponent } from './health.component';
import { HealthService } from './health.service';
import { Health } from './health.model';

describe('HealthComponent', () => {
  let comp: HealthComponent;
  let fixture: ComponentFixture<HealthComponent>;
  let service: HealthService;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HealthComponent],
      })
        .overrideTemplate(HealthComponent, '')
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HealthService);
  });

  describe('getBadgeClass', () => {
    it('should get badge class', () => {
      const upBadgeClass = comp.getBadgeClass('UP');
      const downBadgeClass = comp.getBadgeClass('DOWN');
      expect(upBadgeClass).toEqual('badge-success');
      expect(downBadgeClass).toEqual('badge-danger');
    });
  });

  describe('refresh', () => {
    it('should call refresh on init', () => {
      // GIVEN
      const health: Health = { status: 'UP', components: { mail: { status: 'UP', details: { mailDetail: 'mail' } } } };
      jest.spyOn(service, 'checkHealth').mockReturnValue(of(health));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.checkHealth).toHaveBeenCalled();
      expect(comp.health).toEqual(health);
    });

    it('should handle a 503 on refreshing health data', () => {
      // GIVEN
      const health: Health = { status: 'DOWN', components: { mail: { status: 'DOWN' } } };
      jest.spyOn(service, 'checkHealth').mockReturnValue(throwError(new HttpErrorResponse({ status: 503, error: health })));

      // WHEN
      comp.refresh();

      // THEN
      expect(service.checkHealth).toHaveBeenCalled();
      expect(comp.health).toEqual(health);
    });
  });
});
