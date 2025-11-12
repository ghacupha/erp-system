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
import { compilationProcessStatusTypes } from 'app/entities/enumerations/compilation-process-status-types.model';
import { ITACompilationRequest, TACompilationRequest } from '../ta-compilation-request.model';

import { TACompilationRequestService } from './ta-compilation-request.service';

describe('TACompilationRequest Service', () => {
  let service: TACompilationRequestService;
  let httpMock: HttpTestingController;
  let elemDefault: ITACompilationRequest;
  let expectedResult: ITACompilationRequest | ITACompilationRequest[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TACompilationRequestService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requisitionId: 'AAAAAAA',
      timeOfRequest: currentDate,
      compilationProcessStatus: compilationProcessStatusTypes.STARTED,
      numberOfEnumeratedItems: 0,
      batchJobIdentifier: 'AAAAAAA',
      compilationTime: currentDate,
      invalidated: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TACompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          compilationTime: currentDate,
        },
        returnedFromService
      );

      service.create(new TACompilationRequest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TACompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requisitionId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationProcessStatus: 'BBBBBB',
          numberOfEnumeratedItems: 1,
          batchJobIdentifier: 'BBBBBB',
          compilationTime: currentDate.format(DATE_TIME_FORMAT),
          invalidated: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          compilationTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TACompilationRequest', () => {
      const patchObject = Object.assign(
        {
          requisitionId: 'BBBBBB',
          compilationProcessStatus: 'BBBBBB',
          numberOfEnumeratedItems: 1,
          compilationTime: currentDate.format(DATE_TIME_FORMAT),
          invalidated: true,
        },
        new TACompilationRequest()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          compilationTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TACompilationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requisitionId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          compilationProcessStatus: 'BBBBBB',
          numberOfEnumeratedItems: 1,
          batchJobIdentifier: 'BBBBBB',
          compilationTime: currentDate.format(DATE_TIME_FORMAT),
          invalidated: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          compilationTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TACompilationRequest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTACompilationRequestToCollectionIfMissing', () => {
      it('should add a TACompilationRequest to an empty array', () => {
        const tACompilationRequest: ITACompilationRequest = { id: 123 };
        expectedResult = service.addTACompilationRequestToCollectionIfMissing([], tACompilationRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tACompilationRequest);
      });

      it('should not add a TACompilationRequest to an array that contains it', () => {
        const tACompilationRequest: ITACompilationRequest = { id: 123 };
        const tACompilationRequestCollection: ITACompilationRequest[] = [
          {
            ...tACompilationRequest,
          },
          { id: 456 },
        ];
        expectedResult = service.addTACompilationRequestToCollectionIfMissing(tACompilationRequestCollection, tACompilationRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TACompilationRequest to an array that doesn't contain it", () => {
        const tACompilationRequest: ITACompilationRequest = { id: 123 };
        const tACompilationRequestCollection: ITACompilationRequest[] = [{ id: 456 }];
        expectedResult = service.addTACompilationRequestToCollectionIfMissing(tACompilationRequestCollection, tACompilationRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tACompilationRequest);
      });

      it('should add only unique TACompilationRequest to an array', () => {
        const tACompilationRequestArray: ITACompilationRequest[] = [{ id: 123 }, { id: 456 }, { id: 6801 }];
        const tACompilationRequestCollection: ITACompilationRequest[] = [{ id: 123 }];
        expectedResult = service.addTACompilationRequestToCollectionIfMissing(tACompilationRequestCollection, ...tACompilationRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tACompilationRequest: ITACompilationRequest = { id: 123 };
        const tACompilationRequest2: ITACompilationRequest = { id: 456 };
        expectedResult = service.addTACompilationRequestToCollectionIfMissing([], tACompilationRequest, tACompilationRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tACompilationRequest);
        expect(expectedResult).toContain(tACompilationRequest2);
      });

      it('should accept null and undefined values', () => {
        const tACompilationRequest: ITACompilationRequest = { id: 123 };
        expectedResult = service.addTACompilationRequestToCollectionIfMissing([], null, tACompilationRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tACompilationRequest);
      });

      it('should return initial array if no TACompilationRequest is added', () => {
        const tACompilationRequestCollection: ITACompilationRequest[] = [{ id: 123 }];
        expectedResult = service.addTACompilationRequestToCollectionIfMissing(tACompilationRequestCollection, undefined, null);
        expect(expectedResult).toEqual(tACompilationRequestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
