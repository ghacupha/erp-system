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

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LogsComponent } from './logs.component';
import { LogsService } from './logs.service';
import { Log, LoggersResponse } from './log.model';

describe('LogsComponent', () => {
  let comp: LogsComponent;
  let fixture: ComponentFixture<LogsComponent>;
  let service: LogsService;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LogsComponent],
        providers: [LogsService],
      })
        .overrideTemplate(LogsComponent, '')
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(LogsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LogsService);
  });

  describe('OnInit', () => {
    it('should set all default values correctly', () => {
      expect(comp.filter).toBe('');
      expect(comp.orderProp).toBe('name');
      expect(comp.ascending).toBe(true);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const log = new Log('main', 'WARN');
      jest.spyOn(service, 'findAll').mockReturnValue(
        of({
          loggers: {
            main: {
              effectiveLevel: 'WARN',
            },
          },
        } as unknown as LoggersResponse)
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.findAll).toHaveBeenCalled();
      expect(comp.loggers?.[0]).toEqual(expect.objectContaining(log));
    });
  });

  describe('change log level', () => {
    it('should change log level correctly', () => {
      // GIVEN
      const log = new Log('main', 'ERROR');
      jest.spyOn(service, 'changeLevel').mockReturnValue(of({}));
      jest.spyOn(service, 'findAll').mockReturnValue(
        of({
          loggers: {
            main: {
              effectiveLevel: 'ERROR',
            },
          },
        } as unknown as LoggersResponse)
      );

      // WHEN
      comp.changeLevel('main', 'ERROR');

      // THEN
      expect(service.changeLevel).toHaveBeenCalled();
      expect(service.findAll).toHaveBeenCalled();
      expect(comp.loggers?.[0]).toEqual(expect.objectContaining(log));
    });
  });
});
