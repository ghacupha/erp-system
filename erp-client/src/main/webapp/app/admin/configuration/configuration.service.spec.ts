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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ConfigurationService } from './configuration.service';
import { Bean, ConfigProps, Env, PropertySource } from './configuration.model';

describe('Logs Service', () => {
  let service: ConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: Bean[] | PropertySource[] | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    expectedResult = null;
    service = TestBed.inject(ConfigurationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should get the config', () => {
      const bean: Bean = {
        prefix: 'jhipster',
        properties: {
          clientApp: {
            name: 'jhipsterApp',
          },
        },
      };
      const configProps: ConfigProps = {
        contexts: {
          jhipster: {
            beans: {
              'tech.jhipster.config.JHipsterProperties': bean,
            },
          },
        },
      };
      service.getBeans().subscribe(received => (expectedResult = received));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(configProps);
      expect(expectedResult).toEqual([bean]);
    });

    it('should get the env', () => {
      const propertySources: PropertySource[] = [
        {
          name: 'server.ports',
          properties: {
            'local.server.port': {
              value: '8080',
            },
          },
        },
      ];
      const env: Env = { propertySources };
      service.getPropertySources().subscribe(received => (expectedResult = received));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(env);
      expect(expectedResult).toEqual(propertySources);
    });
  });
});
