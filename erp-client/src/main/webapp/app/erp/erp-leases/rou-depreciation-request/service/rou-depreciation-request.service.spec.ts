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
import { depreciationProcessStatusTypes } from 'app/entities/enumerations/depreciation-process-status-types.model';
import { IRouDepreciationRequest, RouDepreciationRequest } from '../rou-depreciation-request.model';

import { RouDepreciationRequestService } from './rou-depreciation-request.service';

describe('RouDepreciationRequest Service', () => {
  let service: RouDepreciationRequestService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouDepreciationRequest;
  let expectedResult: IRouDepreciationRequest | IRouDepreciationRequest[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouDepreciationRequestService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requisitionId: 'AAAAAAA',
      timeOfRequest: currentDate,
      depreciationProcessStatus: depreciationProcessStatusTypes.STARTED,
      numberOfEnumeratedItems: 0,
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

    it('should create a RouDepreciationRequest', () => {
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

      service.create(new RouDepreciationRequest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouDepreciationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requisitionId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          depreciationProcessStatus: 'BBBBBB',
          numberOfEnumeratedItems: 1,
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

    it('should partial update a RouDepreciationRequest', () => {
      const patchObject = Object.assign(
        {
          requisitionId: 'BBBBBB',
          numberOfEnumeratedItems: 1,
        },
        new RouDepreciationRequest()
      );

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

    it('should return a list of RouDepreciationRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requisitionId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          depreciationProcessStatus: 'BBBBBB',
          numberOfEnumeratedItems: 1,
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

    it('should delete a RouDepreciationRequest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouDepreciationRequestToCollectionIfMissing', () => {
      it('should add a RouDepreciationRequest to an empty array', () => {
        const rouDepreciationRequest: IRouDepreciationRequest = { id: 123 };
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing([], rouDepreciationRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationRequest);
      });

      it('should not add a RouDepreciationRequest to an array that contains it', () => {
        const rouDepreciationRequest: IRouDepreciationRequest = { id: 123 };
        const rouDepreciationRequestCollection: IRouDepreciationRequest[] = [
          {
            ...rouDepreciationRequest,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing(rouDepreciationRequestCollection, rouDepreciationRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouDepreciationRequest to an array that doesn't contain it", () => {
        const rouDepreciationRequest: IRouDepreciationRequest = { id: 123 };
        const rouDepreciationRequestCollection: IRouDepreciationRequest[] = [{ id: 456 }];
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing(rouDepreciationRequestCollection, rouDepreciationRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationRequest);
      });

      it('should add only unique RouDepreciationRequest to an array', () => {
        const rouDepreciationRequestArray: IRouDepreciationRequest[] = [{ id: 123 }, { id: 456 }, { id: 96332 }];
        const rouDepreciationRequestCollection: IRouDepreciationRequest[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing(
          rouDepreciationRequestCollection,
          ...rouDepreciationRequestArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouDepreciationRequest: IRouDepreciationRequest = { id: 123 };
        const rouDepreciationRequest2: IRouDepreciationRequest = { id: 456 };
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing([], rouDepreciationRequest, rouDepreciationRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouDepreciationRequest);
        expect(expectedResult).toContain(rouDepreciationRequest2);
      });

      it('should accept null and undefined values', () => {
        const rouDepreciationRequest: IRouDepreciationRequest = { id: 123 };
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing([], null, rouDepreciationRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouDepreciationRequest);
      });

      it('should return initial array if no RouDepreciationRequest is added', () => {
        const rouDepreciationRequestCollection: IRouDepreciationRequest[] = [{ id: 123 }];
        expectedResult = service.addRouDepreciationRequestToCollectionIfMissing(rouDepreciationRequestCollection, undefined, null);
        expect(expectedResult).toEqual(rouDepreciationRequestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
