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

import { DataUtils } from './data-util.service';

describe('Data Utils Service Test', () => {
  let service: DataUtils;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataUtils],
    });
    service = TestBed.inject(DataUtils);
  });

  describe('byteSize', () => {
    it('should return the bytesize of the text', () => {
      expect(service.byteSize('Hello JHipster')).toBe(`10.5 bytes`);
    });
  });

  describe('openFile', () => {
    it('should open the file in the new window', () => {
      const newWindow = { ...window };
      newWindow.document.write = jest.fn();
      window.open = jest.fn(() => newWindow);
      window.URL.createObjectURL = jest.fn();
      // 'JHipster' in base64 is 'SkhpcHN0ZXI='
      const data = 'SkhpcHN0ZXI=';
      const contentType = 'text/plain';
      service.openFile(data, contentType);
      expect(window.open).toHaveBeenCalledTimes(1);
    });
  });
});
