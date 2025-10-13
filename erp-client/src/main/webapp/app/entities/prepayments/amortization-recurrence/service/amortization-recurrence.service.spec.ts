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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { recurrenceFrequency } from 'app/entities/enumerations/recurrence-frequency.model';
import { IAmortizationRecurrence, AmortizationRecurrence } from '../amortization-recurrence.model';

import { AmortizationRecurrenceService } from './amortization-recurrence.service';

describe('AmortizationRecurrence Service', () => {
  let service: AmortizationRecurrenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmortizationRecurrence;
  let expectedResult: IAmortizationRecurrence | IAmortizationRecurrence[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmortizationRecurrenceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      firstAmortizationDate: currentDate,
      amortizationFrequency: recurrenceFrequency.MONTHLY,
      numberOfRecurrences: 0,
      notesContentType: 'image/png',
      notes: 'AAAAAAA',
      particulars: 'AAAAAAA',
      isActive: false,
      isOverWritten: false,
      timeOfInstallation: currentDate,
      recurrenceGuid: 'AAAAAAA',
      prepaymentAccountGuid: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          firstAmortizationDate: currentDate.format(DATE_FORMAT),
          timeOfInstallation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AmortizationRecurrence', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          firstAmortizationDate: currentDate.format(DATE_FORMAT),
          timeOfInstallation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          firstAmortizationDate: currentDate,
          timeOfInstallation: currentDate,
        },
        returnedFromService
      );

      service.create(new AmortizationRecurrence()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AmortizationRecurrence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstAmortizationDate: currentDate.format(DATE_FORMAT),
          amortizationFrequency: 'BBBBBB',
          numberOfRecurrences: 1,
          notes: 'BBBBBB',
          particulars: 'BBBBBB',
          isActive: true,
          isOverWritten: true,
          timeOfInstallation: currentDate.format(DATE_TIME_FORMAT),
          recurrenceGuid: 'BBBBBB',
          prepaymentAccountGuid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          firstAmortizationDate: currentDate,
          timeOfInstallation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AmortizationRecurrence', () => {
      const patchObject = Object.assign(
        {
          firstAmortizationDate: currentDate.format(DATE_FORMAT),
          particulars: 'BBBBBB',
          isActive: true,
          isOverWritten: true,
          recurrenceGuid: 'BBBBBB',
        },
        new AmortizationRecurrence()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          firstAmortizationDate: currentDate,
          timeOfInstallation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AmortizationRecurrence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstAmortizationDate: currentDate.format(DATE_FORMAT),
          amortizationFrequency: 'BBBBBB',
          numberOfRecurrences: 1,
          notes: 'BBBBBB',
          particulars: 'BBBBBB',
          isActive: true,
          isOverWritten: true,
          timeOfInstallation: currentDate.format(DATE_TIME_FORMAT),
          recurrenceGuid: 'BBBBBB',
          prepaymentAccountGuid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          firstAmortizationDate: currentDate,
          timeOfInstallation: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AmortizationRecurrence', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmortizationRecurrenceToCollectionIfMissing', () => {
      it('should add a AmortizationRecurrence to an empty array', () => {
        const amortizationRecurrence: IAmortizationRecurrence = { id: 123 };
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing([], amortizationRecurrence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationRecurrence);
      });

      it('should not add a AmortizationRecurrence to an array that contains it', () => {
        const amortizationRecurrence: IAmortizationRecurrence = { id: 123 };
        const amortizationRecurrenceCollection: IAmortizationRecurrence[] = [
          {
            ...amortizationRecurrence,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing(amortizationRecurrenceCollection, amortizationRecurrence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmortizationRecurrence to an array that doesn't contain it", () => {
        const amortizationRecurrence: IAmortizationRecurrence = { id: 123 };
        const amortizationRecurrenceCollection: IAmortizationRecurrence[] = [{ id: 456 }];
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing(amortizationRecurrenceCollection, amortizationRecurrence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationRecurrence);
      });

      it('should add only unique AmortizationRecurrence to an array', () => {
        const amortizationRecurrenceArray: IAmortizationRecurrence[] = [{ id: 123 }, { id: 456 }, { id: 35175 }];
        const amortizationRecurrenceCollection: IAmortizationRecurrence[] = [{ id: 123 }];
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing(
          amortizationRecurrenceCollection,
          ...amortizationRecurrenceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amortizationRecurrence: IAmortizationRecurrence = { id: 123 };
        const amortizationRecurrence2: IAmortizationRecurrence = { id: 456 };
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing([], amortizationRecurrence, amortizationRecurrence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationRecurrence);
        expect(expectedResult).toContain(amortizationRecurrence2);
      });

      it('should accept null and undefined values', () => {
        const amortizationRecurrence: IAmortizationRecurrence = { id: 123 };
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing([], null, amortizationRecurrence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationRecurrence);
      });

      it('should return initial array if no AmortizationRecurrence is added', () => {
        const amortizationRecurrenceCollection: IAmortizationRecurrence[] = [{ id: 123 }];
        expectedResult = service.addAmortizationRecurrenceToCollectionIfMissing(amortizationRecurrenceCollection, undefined, null);
        expect(expectedResult).toEqual(amortizationRecurrenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
