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

import { DATE_FORMAT } from 'app/config/input.constants';
import { AgencyStatusType } from 'app/entities/enumerations/agency-status-type.model';
import { IAgencyNotice, AgencyNotice } from '../agency-notice.model';

import { AgencyNoticeService } from './agency-notice.service';

describe('AgencyNotice Service', () => {
  let service: AgencyNoticeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAgencyNotice;
  let expectedResult: IAgencyNotice | IAgencyNotice[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgencyNoticeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      referenceNumber: 'AAAAAAA',
      referenceDate: currentDate,
      assessmentAmount: 0,
      agencyStatus: AgencyStatusType.CLEARED,
      assessmentNoticeContentType: 'image/png',
      assessmentNotice: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          referenceDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AgencyNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          referenceDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          referenceDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AgencyNotice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgencyNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referenceNumber: 'BBBBBB',
          referenceDate: currentDate.format(DATE_FORMAT),
          assessmentAmount: 1,
          agencyStatus: 'BBBBBB',
          assessmentNotice: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          referenceDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgencyNotice', () => {
      const patchObject = Object.assign(
        {
          referenceNumber: 'BBBBBB',
          referenceDate: currentDate.format(DATE_FORMAT),
          assessmentAmount: 1,
          agencyStatus: 'BBBBBB',
        },
        new AgencyNotice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          referenceDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgencyNotice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referenceNumber: 'BBBBBB',
          referenceDate: currentDate.format(DATE_FORMAT),
          assessmentAmount: 1,
          agencyStatus: 'BBBBBB',
          assessmentNotice: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          referenceDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AgencyNotice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAgencyNoticeToCollectionIfMissing', () => {
      it('should add a AgencyNotice to an empty array', () => {
        const agencyNotice: IAgencyNotice = { id: 123 };
        expectedResult = service.addAgencyNoticeToCollectionIfMissing([], agencyNotice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agencyNotice);
      });

      it('should not add a AgencyNotice to an array that contains it', () => {
        const agencyNotice: IAgencyNotice = { id: 123 };
        const agencyNoticeCollection: IAgencyNotice[] = [
          {
            ...agencyNotice,
          },
          { id: 456 },
        ];
        expectedResult = service.addAgencyNoticeToCollectionIfMissing(agencyNoticeCollection, agencyNotice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgencyNotice to an array that doesn't contain it", () => {
        const agencyNotice: IAgencyNotice = { id: 123 };
        const agencyNoticeCollection: IAgencyNotice[] = [{ id: 456 }];
        expectedResult = service.addAgencyNoticeToCollectionIfMissing(agencyNoticeCollection, agencyNotice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agencyNotice);
      });

      it('should add only unique AgencyNotice to an array', () => {
        const agencyNoticeArray: IAgencyNotice[] = [{ id: 123 }, { id: 456 }, { id: 99812 }];
        const agencyNoticeCollection: IAgencyNotice[] = [{ id: 123 }];
        expectedResult = service.addAgencyNoticeToCollectionIfMissing(agencyNoticeCollection, ...agencyNoticeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agencyNotice: IAgencyNotice = { id: 123 };
        const agencyNotice2: IAgencyNotice = { id: 456 };
        expectedResult = service.addAgencyNoticeToCollectionIfMissing([], agencyNotice, agencyNotice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agencyNotice);
        expect(expectedResult).toContain(agencyNotice2);
      });

      it('should accept null and undefined values', () => {
        const agencyNotice: IAgencyNotice = { id: 123 };
        expectedResult = service.addAgencyNoticeToCollectionIfMissing([], null, agencyNotice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agencyNotice);
      });

      it('should return initial array if no AgencyNotice is added', () => {
        const agencyNoticeCollection: IAgencyNotice[] = [{ id: 123 }];
        expectedResult = service.addAgencyNoticeToCollectionIfMissing(agencyNoticeCollection, undefined, null);
        expect(expectedResult).toEqual(agencyNoticeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
