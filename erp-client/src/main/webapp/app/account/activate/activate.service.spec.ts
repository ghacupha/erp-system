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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ActivateService } from './activate.service';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

describe('ActivateService Service', () => {
  let service: ActivateService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(ActivateService);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should call api/activate endpoint with correct values', () => {
      // GIVEN
      let expectedResult;
      const key = 'key';
      const value = true;

      // WHEN
      service.get(key).subscribe(received => {
        expectedResult = received;
      });
      const testRequest = httpMock.expectOne({
        method: 'GET',
        url: applicationConfigService.getEndpointFor(`api/activate?key=${key}`),
      });
      testRequest.flush(value);

      // THEN
      expect(expectedResult).toEqual(value);
    });
  });
});
