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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPrepaymentCompilationRequest, PrepaymentCompilationRequest } from '../prepayment-compilation-request.model';

import { PrepaymentCompilationRequestService } from './prepayment-compilation-request.service';
import { CompilationStatusTypes } from '../../../erp-common/enumerations/compilation-status-types.model';

describe('PrepaymentCompilationRequest Service', () => {
  let service: PrepaymentCompilationRequestService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentCompilationRequest;
  let expectedResult: IPrepaymentCompilationRequest | IPrepaymentCompilationRequest[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentCompilationRequestService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      timeOfRequest: currentDate,
      compilationStatus: CompilationStatusTypes.STARTED,
      itemsProcessed: 0,
      compilationToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PrepaymentCompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new PrepaymentCompilationRequest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentCompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationStatus: 'BBBBBB',
          itemsProcessed: 1,
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentCompilationRequest', () => {
      const patchObject = Object.assign({}, new PrepaymentCompilationRequest());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentCompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationStatus: 'BBBBBB',
          itemsProcessed: 1,
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PrepaymentCompilationRequest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentCompilationRequestToCollectionIfMissing', () => {
      it('should add a PrepaymentCompilationRequest to an empty array', () => {
        const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 123 };
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing([], prepaymentCompilationRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentCompilationRequest);
      });

      it('should not add a PrepaymentCompilationRequest to an array that contains it', () => {
        const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 123 };
        const prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[] = [
          {
            ...prepaymentCompilationRequest,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing(
          prepaymentCompilationRequestCollection,
          prepaymentCompilationRequest
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentCompilationRequest to an array that doesn't contain it", () => {
        const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 123 };
        const prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing(
          prepaymentCompilationRequestCollection,
          prepaymentCompilationRequest
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentCompilationRequest);
      });

      it('should add only unique PrepaymentCompilationRequest to an array', () => {
        const prepaymentCompilationRequestArray: IPrepaymentCompilationRequest[] = [{ id: 123 }, { id: 456 }, { id: 31542 }];
        const prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing(
          prepaymentCompilationRequestCollection,
          ...prepaymentCompilationRequestArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 123 };
        const prepaymentCompilationRequest2: IPrepaymentCompilationRequest = { id: 456 };
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing(
          [],
          prepaymentCompilationRequest,
          prepaymentCompilationRequest2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentCompilationRequest);
        expect(expectedResult).toContain(prepaymentCompilationRequest2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentCompilationRequest: IPrepaymentCompilationRequest = { id: 123 };
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing([], null, prepaymentCompilationRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentCompilationRequest);
      });

      it('should return initial array if no PrepaymentCompilationRequest is added', () => {
        const prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentCompilationRequestToCollectionIfMissing(
          prepaymentCompilationRequestCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(prepaymentCompilationRequestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
