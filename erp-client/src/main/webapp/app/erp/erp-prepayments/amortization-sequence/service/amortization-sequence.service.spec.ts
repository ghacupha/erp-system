///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { IAmortizationSequence, AmortizationSequence } from '../amortization-sequence.model';

import { AmortizationSequenceService } from './amortization-sequence.service';

describe('AmortizationSequence Service', () => {
  let service: AmortizationSequenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmortizationSequence;
  let expectedResult: IAmortizationSequence | IAmortizationSequence[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmortizationSequenceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      prepaymentAccountGuid: 'AAAAAAA',
      recurrenceGuid: 'AAAAAAA',
      sequenceNumber: 0,
      particulars: 'AAAAAAA',
      currentAmortizationDate: currentDate,
      previousAmortizationDate: currentDate,
      nextAmortizationDate: currentDate,
      isCommencementSequence: false,
      isTerminalSequence: false,
      amortizationAmount: 0,
      sequenceGuid: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          currentAmortizationDate: currentDate.format(DATE_FORMAT),
          previousAmortizationDate: currentDate.format(DATE_FORMAT),
          nextAmortizationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AmortizationSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          currentAmortizationDate: currentDate.format(DATE_FORMAT),
          previousAmortizationDate: currentDate.format(DATE_FORMAT),
          nextAmortizationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentAmortizationDate: currentDate,
          previousAmortizationDate: currentDate,
          nextAmortizationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AmortizationSequence()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AmortizationSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          prepaymentAccountGuid: 'BBBBBB',
          recurrenceGuid: 'BBBBBB',
          sequenceNumber: 1,
          particulars: 'BBBBBB',
          currentAmortizationDate: currentDate.format(DATE_FORMAT),
          previousAmortizationDate: currentDate.format(DATE_FORMAT),
          nextAmortizationDate: currentDate.format(DATE_FORMAT),
          isCommencementSequence: true,
          isTerminalSequence: true,
          amortizationAmount: 1,
          sequenceGuid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentAmortizationDate: currentDate,
          previousAmortizationDate: currentDate,
          nextAmortizationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AmortizationSequence', () => {
      const patchObject = Object.assign(
        {
          prepaymentAccountGuid: 'BBBBBB',
          sequenceNumber: 1,
          currentAmortizationDate: currentDate.format(DATE_FORMAT),
          previousAmortizationDate: currentDate.format(DATE_FORMAT),
          isCommencementSequence: true,
          isTerminalSequence: true,
          amortizationAmount: 1,
          sequenceGuid: 'BBBBBB',
        },
        new AmortizationSequence()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          currentAmortizationDate: currentDate,
          previousAmortizationDate: currentDate,
          nextAmortizationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AmortizationSequence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          prepaymentAccountGuid: 'BBBBBB',
          recurrenceGuid: 'BBBBBB',
          sequenceNumber: 1,
          particulars: 'BBBBBB',
          currentAmortizationDate: currentDate.format(DATE_FORMAT),
          previousAmortizationDate: currentDate.format(DATE_FORMAT),
          nextAmortizationDate: currentDate.format(DATE_FORMAT),
          isCommencementSequence: true,
          isTerminalSequence: true,
          amortizationAmount: 1,
          sequenceGuid: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentAmortizationDate: currentDate,
          previousAmortizationDate: currentDate,
          nextAmortizationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AmortizationSequence', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmortizationSequenceToCollectionIfMissing', () => {
      it('should add a AmortizationSequence to an empty array', () => {
        const amortizationSequence: IAmortizationSequence = { id: 123 };
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing([], amortizationSequence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationSequence);
      });

      it('should not add a AmortizationSequence to an array that contains it', () => {
        const amortizationSequence: IAmortizationSequence = { id: 123 };
        const amortizationSequenceCollection: IAmortizationSequence[] = [
          {
            ...amortizationSequence,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing(amortizationSequenceCollection, amortizationSequence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmortizationSequence to an array that doesn't contain it", () => {
        const amortizationSequence: IAmortizationSequence = { id: 123 };
        const amortizationSequenceCollection: IAmortizationSequence[] = [{ id: 456 }];
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing(amortizationSequenceCollection, amortizationSequence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationSequence);
      });

      it('should add only unique AmortizationSequence to an array', () => {
        const amortizationSequenceArray: IAmortizationSequence[] = [{ id: 123 }, { id: 456 }, { id: 98304 }];
        const amortizationSequenceCollection: IAmortizationSequence[] = [{ id: 123 }];
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing(amortizationSequenceCollection, ...amortizationSequenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amortizationSequence: IAmortizationSequence = { id: 123 };
        const amortizationSequence2: IAmortizationSequence = { id: 456 };
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing([], amortizationSequence, amortizationSequence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amortizationSequence);
        expect(expectedResult).toContain(amortizationSequence2);
      });

      it('should accept null and undefined values', () => {
        const amortizationSequence: IAmortizationSequence = { id: 123 };
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing([], null, amortizationSequence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amortizationSequence);
      });

      it('should return initial array if no AmortizationSequence is added', () => {
        const amortizationSequenceCollection: IAmortizationSequence[] = [{ id: 123 }];
        expectedResult = service.addAmortizationSequenceToCollectionIfMissing(amortizationSequenceCollection, undefined, null);
        expect(expectedResult).toEqual(amortizationSequenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
