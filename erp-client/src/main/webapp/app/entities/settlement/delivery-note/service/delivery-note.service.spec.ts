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
import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';

import { DeliveryNoteService } from './delivery-note.service';

describe('DeliveryNote Service', () => {
  let service: DeliveryNoteService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeliveryNote;
  let expectedResult: IDeliveryNote | IDeliveryNote[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryNoteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      deliveryNoteNumber: 'AAAAAAA',
      documentDate: currentDate,
      description: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
      quantity: 0,
      remarks: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          documentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DeliveryNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          documentDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          documentDate: currentDate,
        },
        returnedFromService
      );

      service.create(new DeliveryNote()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          deliveryNoteNumber: 'BBBBBB',
          documentDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          serialNumber: 'BBBBBB',
          quantity: 1,
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          documentDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeliveryNote', () => {
      const patchObject = Object.assign(
        {
          deliveryNoteNumber: 'BBBBBB',
          description: 'BBBBBB',
          quantity: 1,
        },
        new DeliveryNote()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          documentDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          deliveryNoteNumber: 'BBBBBB',
          documentDate: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          serialNumber: 'BBBBBB',
          quantity: 1,
          remarks: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          documentDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DeliveryNote', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeliveryNoteToCollectionIfMissing', () => {
      it('should add a DeliveryNote to an empty array', () => {
        const deliveryNote: IDeliveryNote = { id: 123 };
        expectedResult = service.addDeliveryNoteToCollectionIfMissing([], deliveryNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryNote);
      });

      it('should not add a DeliveryNote to an array that contains it', () => {
        const deliveryNote: IDeliveryNote = { id: 123 };
        const deliveryNoteCollection: IDeliveryNote[] = [
          {
            ...deliveryNote,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, deliveryNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryNote to an array that doesn't contain it", () => {
        const deliveryNote: IDeliveryNote = { id: 123 };
        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 456 }];
        expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, deliveryNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryNote);
      });

      it('should add only unique DeliveryNote to an array', () => {
        const deliveryNoteArray: IDeliveryNote[] = [{ id: 123 }, { id: 456 }, { id: 56476 }];
        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 123 }];
        expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, ...deliveryNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryNote: IDeliveryNote = { id: 123 };
        const deliveryNote2: IDeliveryNote = { id: 456 };
        expectedResult = service.addDeliveryNoteToCollectionIfMissing([], deliveryNote, deliveryNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryNote);
        expect(expectedResult).toContain(deliveryNote2);
      });

      it('should accept null and undefined values', () => {
        const deliveryNote: IDeliveryNote = { id: 123 };
        expectedResult = service.addDeliveryNoteToCollectionIfMissing([], null, deliveryNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryNote);
      });

      it('should return initial array if no DeliveryNote is added', () => {
        const deliveryNoteCollection: IDeliveryNote[] = [{ id: 123 }];
        expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryNoteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
