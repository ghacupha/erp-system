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

import { IUltimateBeneficiaryTypes, UltimateBeneficiaryTypes } from '../ultimate-beneficiary-types.model';

import { UltimateBeneficiaryTypesService } from './ultimate-beneficiary-types.service';

describe('UltimateBeneficiaryTypes Service', () => {
  let service: UltimateBeneficiaryTypesService;
  let httpMock: HttpTestingController;
  let elemDefault: IUltimateBeneficiaryTypes;
  let expectedResult: IUltimateBeneficiaryTypes | IUltimateBeneficiaryTypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UltimateBeneficiaryTypesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ultimateBeneficiaryTypeCode: 'AAAAAAA',
      ultimateBeneficiaryType: 'AAAAAAA',
      ultimateBeneficiaryTypeDetails: 'AAAAAAA',
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

    it('should create a UltimateBeneficiaryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UltimateBeneficiaryTypes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UltimateBeneficiaryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ultimateBeneficiaryTypeCode: 'BBBBBB',
          ultimateBeneficiaryType: 'BBBBBB',
          ultimateBeneficiaryTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UltimateBeneficiaryTypes', () => {
      const patchObject = Object.assign(
        {
          ultimateBeneficiaryTypeCode: 'BBBBBB',
          ultimateBeneficiaryTypeDetails: 'BBBBBB',
        },
        new UltimateBeneficiaryTypes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UltimateBeneficiaryTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ultimateBeneficiaryTypeCode: 'BBBBBB',
          ultimateBeneficiaryType: 'BBBBBB',
          ultimateBeneficiaryTypeDetails: 'BBBBBB',
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

    it('should delete a UltimateBeneficiaryTypes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUltimateBeneficiaryTypesToCollectionIfMissing', () => {
      it('should add a UltimateBeneficiaryTypes to an empty array', () => {
        const ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes = { id: 123 };
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing([], ultimateBeneficiaryTypes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ultimateBeneficiaryTypes);
      });

      it('should not add a UltimateBeneficiaryTypes to an array that contains it', () => {
        const ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes = { id: 123 };
        const ultimateBeneficiaryTypesCollection: IUltimateBeneficiaryTypes[] = [
          {
            ...ultimateBeneficiaryTypes,
          },
          { id: 456 },
        ];
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing(
          ultimateBeneficiaryTypesCollection,
          ultimateBeneficiaryTypes
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UltimateBeneficiaryTypes to an array that doesn't contain it", () => {
        const ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes = { id: 123 };
        const ultimateBeneficiaryTypesCollection: IUltimateBeneficiaryTypes[] = [{ id: 456 }];
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing(
          ultimateBeneficiaryTypesCollection,
          ultimateBeneficiaryTypes
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ultimateBeneficiaryTypes);
      });

      it('should add only unique UltimateBeneficiaryTypes to an array', () => {
        const ultimateBeneficiaryTypesArray: IUltimateBeneficiaryTypes[] = [{ id: 123 }, { id: 456 }, { id: 60414 }];
        const ultimateBeneficiaryTypesCollection: IUltimateBeneficiaryTypes[] = [{ id: 123 }];
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing(
          ultimateBeneficiaryTypesCollection,
          ...ultimateBeneficiaryTypesArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes = { id: 123 };
        const ultimateBeneficiaryTypes2: IUltimateBeneficiaryTypes = { id: 456 };
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing([], ultimateBeneficiaryTypes, ultimateBeneficiaryTypes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ultimateBeneficiaryTypes);
        expect(expectedResult).toContain(ultimateBeneficiaryTypes2);
      });

      it('should accept null and undefined values', () => {
        const ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes = { id: 123 };
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing([], null, ultimateBeneficiaryTypes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ultimateBeneficiaryTypes);
      });

      it('should return initial array if no UltimateBeneficiaryTypes is added', () => {
        const ultimateBeneficiaryTypesCollection: IUltimateBeneficiaryTypes[] = [{ id: 123 }];
        expectedResult = service.addUltimateBeneficiaryTypesToCollectionIfMissing(ultimateBeneficiaryTypesCollection, undefined, null);
        expect(expectedResult).toEqual(ultimateBeneficiaryTypesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
