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

import { PasswordService } from './password.service';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

describe('Password Service', () => {
  let service: PasswordService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(PasswordService);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should call change-password endpoint with correct values', () => {
      // GIVEN
      const password1 = 'password1';
      const password2 = 'password2';

      // WHEN
      service.save(password2, password1).subscribe();

      const testRequest = httpMock.expectOne({
        method: 'POST',
        url: applicationConfigService.getEndpointFor('api/account/change-password'),
      });

      // THEN
      expect(testRequest.request.body).toEqual({ currentPassword: password1, newPassword: password2 });
    });
  });
});
