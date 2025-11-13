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
import { ICreditNote, CreditNote } from '../credit-note.model';

import { CreditNoteService } from './credit-note.service';

describe('CreditNote Service', () => {
  let service: CreditNoteService;
  let httpMock: HttpTestingController;
  let elemDefault: ICreditNote;
  let expectedResult: ICreditNote | ICreditNote[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CreditNoteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      creditNumber: 'AAAAAAA',
      creditNoteDate: currentDate,
      creditAmount: 0,
      remarks: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creditNoteDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CreditNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creditNoteDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creditNoteDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CreditNote()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CreditNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditNumber: 'BBBBBB',
          creditNoteDate: currentDate.format(DATE_FORMAT),
          creditAmount: 1,
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creditNoteDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CreditNote', () => {
      const patchObject = Object.assign(
        {
          remarks: 'BBBBBB',
        },
        new CreditNote()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creditNoteDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CreditNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditNumber: 'BBBBBB',
          creditNoteDate: currentDate.format(DATE_FORMAT),
          creditAmount: 1,
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creditNoteDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CreditNote', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCreditNoteToCollectionIfMissing', () => {
      it('should add a CreditNote to an empty array', () => {
        const creditNote: ICreditNote = { id: 123 };
        expectedResult = service.addCreditNoteToCollectionIfMissing([], creditNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditNote);
      });

      it('should not add a CreditNote to an array that contains it', () => {
        const creditNote: ICreditNote = { id: 123 };
        const creditNoteCollection: ICreditNote[] = [
          {
            ...creditNote,
          },
          { id: 456 },
        ];
        expectedResult = service.addCreditNoteToCollectionIfMissing(creditNoteCollection, creditNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CreditNote to an array that doesn't contain it", () => {
        const creditNote: ICreditNote = { id: 123 };
        const creditNoteCollection: ICreditNote[] = [{ id: 456 }];
        expectedResult = service.addCreditNoteToCollectionIfMissing(creditNoteCollection, creditNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditNote);
      });

      it('should add only unique CreditNote to an array', () => {
        const creditNoteArray: ICreditNote[] = [{ id: 123 }, { id: 456 }, { id: 58333 }];
        const creditNoteCollection: ICreditNote[] = [{ id: 123 }];
        expectedResult = service.addCreditNoteToCollectionIfMissing(creditNoteCollection, ...creditNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const creditNote: ICreditNote = { id: 123 };
        const creditNote2: ICreditNote = { id: 456 };
        expectedResult = service.addCreditNoteToCollectionIfMissing([], creditNote, creditNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditNote);
        expect(expectedResult).toContain(creditNote2);
      });

      it('should accept null and undefined values', () => {
        const creditNote: ICreditNote = { id: 123 };
        expectedResult = service.addCreditNoteToCollectionIfMissing([], null, creditNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditNote);
      });

      it('should return initial array if no CreditNote is added', () => {
        const creditNoteCollection: ICreditNote[] = [{ id: 123 }];
        expectedResult = service.addCreditNoteToCollectionIfMissing(creditNoteCollection, undefined, null);
        expect(expectedResult).toEqual(creditNoteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
