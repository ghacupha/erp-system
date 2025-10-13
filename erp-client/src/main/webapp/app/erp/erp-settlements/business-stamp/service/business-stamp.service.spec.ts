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
import { IBusinessStamp, BusinessStamp } from '../business-stamp.model';

import { BusinessStampService } from './business-stamp.service';

describe('BusinessStamp Service', () => {
  let service: BusinessStampService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusinessStamp;
  let expectedResult: IBusinessStamp | IBusinessStamp[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessStampService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      stampDate: currentDate,
      purpose: 'AAAAAAA',
      details: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          stampDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a BusinessStamp', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          stampDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          stampDate: currentDate,
        },
        returnedFromService
      );

      service.create(new BusinessStamp()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusinessStamp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          stampDate: currentDate.format(DATE_FORMAT),
          purpose: 'BBBBBB',
          details: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          stampDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusinessStamp', () => {
      const patchObject = Object.assign(
        {
          details: 'BBBBBB',
        },
        new BusinessStamp()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          stampDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusinessStamp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          stampDate: currentDate.format(DATE_FORMAT),
          purpose: 'BBBBBB',
          details: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          stampDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a BusinessStamp', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusinessStampToCollectionIfMissing', () => {
      it('should add a BusinessStamp to an empty array', () => {
        const businessStamp: IBusinessStamp = { id: 123 };
        expectedResult = service.addBusinessStampToCollectionIfMissing([], businessStamp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessStamp);
      });

      it('should not add a BusinessStamp to an array that contains it', () => {
        const businessStamp: IBusinessStamp = { id: 123 };
        const businessStampCollection: IBusinessStamp[] = [
          {
            ...businessStamp,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusinessStampToCollectionIfMissing(businessStampCollection, businessStamp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusinessStamp to an array that doesn't contain it", () => {
        const businessStamp: IBusinessStamp = { id: 123 };
        const businessStampCollection: IBusinessStamp[] = [{ id: 456 }];
        expectedResult = service.addBusinessStampToCollectionIfMissing(businessStampCollection, businessStamp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessStamp);
      });

      it('should add only unique BusinessStamp to an array', () => {
        const businessStampArray: IBusinessStamp[] = [{ id: 123 }, { id: 456 }, { id: 11034 }];
        const businessStampCollection: IBusinessStamp[] = [{ id: 123 }];
        expectedResult = service.addBusinessStampToCollectionIfMissing(businessStampCollection, ...businessStampArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessStamp: IBusinessStamp = { id: 123 };
        const businessStamp2: IBusinessStamp = { id: 456 };
        expectedResult = service.addBusinessStampToCollectionIfMissing([], businessStamp, businessStamp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessStamp);
        expect(expectedResult).toContain(businessStamp2);
      });

      it('should accept null and undefined values', () => {
        const businessStamp: IBusinessStamp = { id: 123 };
        expectedResult = service.addBusinessStampToCollectionIfMissing([], null, businessStamp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessStamp);
      });

      it('should return initial array if no BusinessStamp is added', () => {
        const businessStampCollection: IBusinessStamp[] = [{ id: 123 }];
        expectedResult = service.addBusinessStampToCollectionIfMissing(businessStampCollection, undefined, null);
        expect(expectedResult).toEqual(businessStampCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
