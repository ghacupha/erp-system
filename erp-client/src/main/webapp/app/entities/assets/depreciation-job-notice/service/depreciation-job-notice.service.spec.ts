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
import { DepreciationNoticeStatusType } from 'app/entities/enumerations/depreciation-notice-status-type.model';
import { IDepreciationJobNotice, DepreciationJobNotice } from '../depreciation-job-notice.model';

import { DepreciationJobNoticeService } from './depreciation-job-notice.service';

describe('DepreciationJobNotice Service', () => {
  let service: DepreciationJobNoticeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepreciationJobNotice;
  let expectedResult: IDepreciationJobNotice | IDepreciationJobNotice[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepreciationJobNoticeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      eventNarrative: 'AAAAAAA',
      eventTimeStamp: currentDate,
      depreciationNoticeStatus: DepreciationNoticeStatusType.INFO,
      sourceModule: 'AAAAAAA',
      sourceEntity: 'AAAAAAA',
      errorCode: 'AAAAAAA',
      errorMessage: 'AAAAAAA',
      userAction: 'AAAAAAA',
      technicalDetails: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          eventTimeStamp: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DepreciationJobNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          eventTimeStamp: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.create(new DepreciationJobNotice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepreciationJobNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          eventNarrative: 'BBBBBB',
          eventTimeStamp: currentDate.format(DATE_TIME_FORMAT),
          depreciationNoticeStatus: 'BBBBBB',
          sourceModule: 'BBBBBB',
          sourceEntity: 'BBBBBB',
          errorCode: 'BBBBBB',
          errorMessage: 'BBBBBB',
          userAction: 'BBBBBB',
          technicalDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepreciationJobNotice', () => {
      const patchObject = Object.assign(
        {
          eventNarrative: 'BBBBBB',
          sourceModule: 'BBBBBB',
          sourceEntity: 'BBBBBB',
          errorCode: 'BBBBBB',
          errorMessage: 'BBBBBB',
          userAction: 'BBBBBB',
          technicalDetails: 'BBBBBB',
        },
        new DepreciationJobNotice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          eventTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepreciationJobNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          eventNarrative: 'BBBBBB',
          eventTimeStamp: currentDate.format(DATE_TIME_FORMAT),
          depreciationNoticeStatus: 'BBBBBB',
          sourceModule: 'BBBBBB',
          sourceEntity: 'BBBBBB',
          errorCode: 'BBBBBB',
          errorMessage: 'BBBBBB',
          userAction: 'BBBBBB',
          technicalDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventTimeStamp: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DepreciationJobNotice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepreciationJobNoticeToCollectionIfMissing', () => {
      it('should add a DepreciationJobNotice to an empty array', () => {
        const depreciationJobNotice: IDepreciationJobNotice = { id: 123 };
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing([], depreciationJobNotice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationJobNotice);
      });

      it('should not add a DepreciationJobNotice to an array that contains it', () => {
        const depreciationJobNotice: IDepreciationJobNotice = { id: 123 };
        const depreciationJobNoticeCollection: IDepreciationJobNotice[] = [
          {
            ...depreciationJobNotice,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing(depreciationJobNoticeCollection, depreciationJobNotice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepreciationJobNotice to an array that doesn't contain it", () => {
        const depreciationJobNotice: IDepreciationJobNotice = { id: 123 };
        const depreciationJobNoticeCollection: IDepreciationJobNotice[] = [{ id: 456 }];
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing(depreciationJobNoticeCollection, depreciationJobNotice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationJobNotice);
      });

      it('should add only unique DepreciationJobNotice to an array', () => {
        const depreciationJobNoticeArray: IDepreciationJobNotice[] = [{ id: 123 }, { id: 456 }, { id: 73068 }];
        const depreciationJobNoticeCollection: IDepreciationJobNotice[] = [{ id: 123 }];
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing(
          depreciationJobNoticeCollection,
          ...depreciationJobNoticeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const depreciationJobNotice: IDepreciationJobNotice = { id: 123 };
        const depreciationJobNotice2: IDepreciationJobNotice = { id: 456 };
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing([], depreciationJobNotice, depreciationJobNotice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(depreciationJobNotice);
        expect(expectedResult).toContain(depreciationJobNotice2);
      });

      it('should accept null and undefined values', () => {
        const depreciationJobNotice: IDepreciationJobNotice = { id: 123 };
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing([], null, depreciationJobNotice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(depreciationJobNotice);
      });

      it('should return initial array if no DepreciationJobNotice is added', () => {
        const depreciationJobNoticeCollection: IDepreciationJobNotice[] = [{ id: 123 }];
        expectedResult = service.addDepreciationJobNoticeToCollectionIfMissing(depreciationJobNoticeCollection, undefined, null);
        expect(expectedResult).toEqual(depreciationJobNoticeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
