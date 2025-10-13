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

import { ApplicationConfigService } from './application-config.service';

describe('ApplicationConfigService', () => {
  let service: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('without prefix', () => {
    it('should return correctly', () => {
      expect(service.getEndpointFor('api')).toEqual('api');
    });

    it('should return correctly when passing microservice', () => {
      expect(service.getEndpointFor('api', 'microservice')).toEqual('services/microservice/api');
    });
  });

  describe('with prefix', () => {
    beforeEach(() => {
      service.setEndpointPrefix('prefix/');
    });

    it('should return correctly', () => {
      expect(service.getEndpointFor('api')).toEqual('prefix/api');
    });

    it('should return correctly when passing microservice', () => {
      expect(service.getEndpointFor('api', 'microservice')).toEqual('prefix/services/microservice/api');
    });
  });

  describe('with undefined prefix', () => {
    beforeEach(() => {
      service.setEndpointPrefix(undefined);
    });

    it('should return correctly', () => {
      expect(service.getEndpointFor('api')).toEqual('api');
    });

    it('should return correctly when passing microservice', () => {
      expect(service.getEndpointFor('api', 'microservice')).toEqual('services/microservice/api');
    });
  });
});
