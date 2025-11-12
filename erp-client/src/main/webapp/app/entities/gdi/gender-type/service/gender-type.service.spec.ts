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

import { genderTypes } from 'app/entities/enumerations/gender-types.model';
import { IGenderType, GenderType } from '../gender-type.model';

import { GenderTypeService } from './gender-type.service';

describe('GenderType Service', () => {
  let service: GenderTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IGenderType;
  let expectedResult: IGenderType | IGenderType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GenderTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      genderCode: 'AAAAAAA',
      genderType: genderTypes.MALE,
      genderDescription: 'AAAAAAA',
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

    it('should create a GenderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GenderType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GenderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          genderCode: 'BBBBBB',
          genderType: 'BBBBBB',
          genderDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GenderType', () => {
      const patchObject = Object.assign({}, new GenderType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GenderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          genderCode: 'BBBBBB',
          genderType: 'BBBBBB',
          genderDescription: 'BBBBBB',
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

    it('should delete a GenderType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGenderTypeToCollectionIfMissing', () => {
      it('should add a GenderType to an empty array', () => {
        const genderType: IGenderType = { id: 123 };
        expectedResult = service.addGenderTypeToCollectionIfMissing([], genderType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(genderType);
      });

      it('should not add a GenderType to an array that contains it', () => {
        const genderType: IGenderType = { id: 123 };
        const genderTypeCollection: IGenderType[] = [
          {
            ...genderType,
          },
          { id: 456 },
        ];
        expectedResult = service.addGenderTypeToCollectionIfMissing(genderTypeCollection, genderType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GenderType to an array that doesn't contain it", () => {
        const genderType: IGenderType = { id: 123 };
        const genderTypeCollection: IGenderType[] = [{ id: 456 }];
        expectedResult = service.addGenderTypeToCollectionIfMissing(genderTypeCollection, genderType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(genderType);
      });

      it('should add only unique GenderType to an array', () => {
        const genderTypeArray: IGenderType[] = [{ id: 123 }, { id: 456 }, { id: 54678 }];
        const genderTypeCollection: IGenderType[] = [{ id: 123 }];
        expectedResult = service.addGenderTypeToCollectionIfMissing(genderTypeCollection, ...genderTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const genderType: IGenderType = { id: 123 };
        const genderType2: IGenderType = { id: 456 };
        expectedResult = service.addGenderTypeToCollectionIfMissing([], genderType, genderType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(genderType);
        expect(expectedResult).toContain(genderType2);
      });

      it('should accept null and undefined values', () => {
        const genderType: IGenderType = { id: 123 };
        expectedResult = service.addGenderTypeToCollectionIfMissing([], null, genderType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(genderType);
      });

      it('should return initial array if no GenderType is added', () => {
        const genderTypeCollection: IGenderType[] = [{ id: 123 }];
        expectedResult = service.addGenderTypeToCollectionIfMissing(genderTypeCollection, undefined, null);
        expect(expectedResult).toEqual(genderTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
