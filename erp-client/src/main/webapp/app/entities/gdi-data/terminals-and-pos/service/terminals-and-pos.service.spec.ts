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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITerminalsAndPOS, TerminalsAndPOS } from '../terminals-and-pos.model';

import { TerminalsAndPOSService } from './terminals-and-pos.service';

describe('TerminalsAndPOS Service', () => {
  let service: TerminalsAndPOSService;
  let httpMock: HttpTestingController;
  let elemDefault: ITerminalsAndPOS;
  let expectedResult: ITerminalsAndPOS | ITerminalsAndPOS[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TerminalsAndPOSService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      terminalId: 'AAAAAAA',
      merchantId: 'AAAAAAA',
      terminalName: 'AAAAAAA',
      terminalLocation: 'AAAAAAA',
      iso6709Latitute: 0,
      iso6709Longitude: 0,
      terminalOpeningDate: currentDate,
      terminalClosureDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalOpeningDate: currentDate.format(DATE_FORMAT),
          terminalClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TerminalsAndPOS', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalOpeningDate: currentDate.format(DATE_FORMAT),
          terminalClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          terminalOpeningDate: currentDate,
          terminalClosureDate: currentDate,
        },
        returnedFromService
      );

      service.create(new TerminalsAndPOS()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TerminalsAndPOS', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalId: 'BBBBBB',
          merchantId: 'BBBBBB',
          terminalName: 'BBBBBB',
          terminalLocation: 'BBBBBB',
          iso6709Latitute: 1,
          iso6709Longitude: 1,
          terminalOpeningDate: currentDate.format(DATE_FORMAT),
          terminalClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          terminalOpeningDate: currentDate,
          terminalClosureDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TerminalsAndPOS', () => {
      const patchObject = Object.assign(
        {
          terminalId: 'BBBBBB',
          terminalLocation: 'BBBBBB',
          iso6709Longitude: 1,
          terminalClosureDate: currentDate.format(DATE_FORMAT),
        },
        new TerminalsAndPOS()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          terminalOpeningDate: currentDate,
          terminalClosureDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TerminalsAndPOS', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          terminalId: 'BBBBBB',
          merchantId: 'BBBBBB',
          terminalName: 'BBBBBB',
          terminalLocation: 'BBBBBB',
          iso6709Latitute: 1,
          iso6709Longitude: 1,
          terminalOpeningDate: currentDate.format(DATE_FORMAT),
          terminalClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          terminalOpeningDate: currentDate,
          terminalClosureDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TerminalsAndPOS', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTerminalsAndPOSToCollectionIfMissing', () => {
      it('should add a TerminalsAndPOS to an empty array', () => {
        const terminalsAndPOS: ITerminalsAndPOS = { id: 123 };
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing([], terminalsAndPOS);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalsAndPOS);
      });

      it('should not add a TerminalsAndPOS to an array that contains it', () => {
        const terminalsAndPOS: ITerminalsAndPOS = { id: 123 };
        const terminalsAndPOSCollection: ITerminalsAndPOS[] = [
          {
            ...terminalsAndPOS,
          },
          { id: 456 },
        ];
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing(terminalsAndPOSCollection, terminalsAndPOS);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TerminalsAndPOS to an array that doesn't contain it", () => {
        const terminalsAndPOS: ITerminalsAndPOS = { id: 123 };
        const terminalsAndPOSCollection: ITerminalsAndPOS[] = [{ id: 456 }];
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing(terminalsAndPOSCollection, terminalsAndPOS);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalsAndPOS);
      });

      it('should add only unique TerminalsAndPOS to an array', () => {
        const terminalsAndPOSArray: ITerminalsAndPOS[] = [{ id: 123 }, { id: 456 }, { id: 3465 }];
        const terminalsAndPOSCollection: ITerminalsAndPOS[] = [{ id: 123 }];
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing(terminalsAndPOSCollection, ...terminalsAndPOSArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const terminalsAndPOS: ITerminalsAndPOS = { id: 123 };
        const terminalsAndPOS2: ITerminalsAndPOS = { id: 456 };
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing([], terminalsAndPOS, terminalsAndPOS2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalsAndPOS);
        expect(expectedResult).toContain(terminalsAndPOS2);
      });

      it('should accept null and undefined values', () => {
        const terminalsAndPOS: ITerminalsAndPOS = { id: 123 };
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing([], null, terminalsAndPOS, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalsAndPOS);
      });

      it('should return initial array if no TerminalsAndPOS is added', () => {
        const terminalsAndPOSCollection: ITerminalsAndPOS[] = [{ id: 123 }];
        expectedResult = service.addTerminalsAndPOSToCollectionIfMissing(terminalsAndPOSCollection, undefined, null);
        expect(expectedResult).toEqual(terminalsAndPOSCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
