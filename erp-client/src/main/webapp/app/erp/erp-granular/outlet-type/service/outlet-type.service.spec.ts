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

import { IOutletType, OutletType } from '../outlet-type.model';

import { OutletTypeService } from './outlet-type.service';

describe('OutletType Service', () => {
  let service: OutletTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IOutletType;
  let expectedResult: IOutletType | IOutletType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OutletTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      outletTypeCode: 'AAAAAAA',
      outletType: 'AAAAAAA',
      outletTypeDetails: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a OutletType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OutletType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OutletType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          outletTypeCode: 'BBBBBB',
          outletType: 'BBBBBB',
          outletTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OutletType', () => {
      const patchObject = Object.assign(
        {
          outletTypeCode: 'BBBBBB',
          outletType: 'BBBBBB',
        },
        new OutletType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OutletType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          outletTypeCode: 'BBBBBB',
          outletType: 'BBBBBB',
          outletTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a OutletType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOutletTypeToCollectionIfMissing', () => {
      it('should add a OutletType to an empty array', () => {
        const outletType: IOutletType = { id: 123 };
        expectedResult = service.addOutletTypeToCollectionIfMissing([], outletType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(outletType);
      });

      it('should not add a OutletType to an array that contains it', () => {
        const outletType: IOutletType = { id: 123 };
        const outletTypeCollection: IOutletType[] = [
          {
            ...outletType,
          },
          { id: 456 },
        ];
        expectedResult = service.addOutletTypeToCollectionIfMissing(outletTypeCollection, outletType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OutletType to an array that doesn't contain it", () => {
        const outletType: IOutletType = { id: 123 };
        const outletTypeCollection: IOutletType[] = [{ id: 456 }];
        expectedResult = service.addOutletTypeToCollectionIfMissing(outletTypeCollection, outletType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(outletType);
      });

      it('should add only unique OutletType to an array', () => {
        const outletTypeArray: IOutletType[] = [{ id: 123 }, { id: 456 }, { id: 5298 }];
        const outletTypeCollection: IOutletType[] = [{ id: 123 }];
        expectedResult = service.addOutletTypeToCollectionIfMissing(outletTypeCollection, ...outletTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const outletType: IOutletType = { id: 123 };
        const outletType2: IOutletType = { id: 456 };
        expectedResult = service.addOutletTypeToCollectionIfMissing([], outletType, outletType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(outletType);
        expect(expectedResult).toContain(outletType2);
      });

      it('should accept null and undefined values', () => {
        const outletType: IOutletType = { id: 123 };
        expectedResult = service.addOutletTypeToCollectionIfMissing([], null, outletType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(outletType);
      });

      it('should return initial array if no OutletType is added', () => {
        const outletTypeCollection: IOutletType[] = [{ id: 123 }];
        expectedResult = service.addOutletTypeToCollectionIfMissing(outletTypeCollection, undefined, null);
        expect(expectedResult).toEqual(outletTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
