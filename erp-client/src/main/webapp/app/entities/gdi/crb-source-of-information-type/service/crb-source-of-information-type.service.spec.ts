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

import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';

import { CrbSourceOfInformationTypeService } from './crb-source-of-information-type.service';

describe('CrbSourceOfInformationType Service', () => {
  let service: CrbSourceOfInformationTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbSourceOfInformationType;
  let expectedResult: ICrbSourceOfInformationType | ICrbSourceOfInformationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbSourceOfInformationTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      sourceOfInformationTypeCode: 'AAAAAAA',
      sourceOfInformationTypeDescription: 'AAAAAAA',
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

    it('should create a CrbSourceOfInformationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbSourceOfInformationType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbSourceOfInformationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOfInformationTypeCode: 'BBBBBB',
          sourceOfInformationTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbSourceOfInformationType', () => {
      const patchObject = Object.assign(
        {
          sourceOfInformationTypeCode: 'BBBBBB',
          sourceOfInformationTypeDescription: 'BBBBBB',
        },
        new CrbSourceOfInformationType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbSourceOfInformationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOfInformationTypeCode: 'BBBBBB',
          sourceOfInformationTypeDescription: 'BBBBBB',
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

    it('should delete a CrbSourceOfInformationType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbSourceOfInformationTypeToCollectionIfMissing', () => {
      it('should add a CrbSourceOfInformationType to an empty array', () => {
        const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 123 };
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing([], crbSourceOfInformationType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSourceOfInformationType);
      });

      it('should not add a CrbSourceOfInformationType to an array that contains it', () => {
        const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 123 };
        const crbSourceOfInformationTypeCollection: ICrbSourceOfInformationType[] = [
          {
            ...crbSourceOfInformationType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing(
          crbSourceOfInformationTypeCollection,
          crbSourceOfInformationType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbSourceOfInformationType to an array that doesn't contain it", () => {
        const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 123 };
        const crbSourceOfInformationTypeCollection: ICrbSourceOfInformationType[] = [{ id: 456 }];
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing(
          crbSourceOfInformationTypeCollection,
          crbSourceOfInformationType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSourceOfInformationType);
      });

      it('should add only unique CrbSourceOfInformationType to an array', () => {
        const crbSourceOfInformationTypeArray: ICrbSourceOfInformationType[] = [{ id: 123 }, { id: 456 }, { id: 95997 }];
        const crbSourceOfInformationTypeCollection: ICrbSourceOfInformationType[] = [{ id: 123 }];
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing(
          crbSourceOfInformationTypeCollection,
          ...crbSourceOfInformationTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 123 };
        const crbSourceOfInformationType2: ICrbSourceOfInformationType = { id: 456 };
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing(
          [],
          crbSourceOfInformationType,
          crbSourceOfInformationType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSourceOfInformationType);
        expect(expectedResult).toContain(crbSourceOfInformationType2);
      });

      it('should accept null and undefined values', () => {
        const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 123 };
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing([], null, crbSourceOfInformationType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSourceOfInformationType);
      });

      it('should return initial array if no CrbSourceOfInformationType is added', () => {
        const crbSourceOfInformationTypeCollection: ICrbSourceOfInformationType[] = [{ id: 123 }];
        expectedResult = service.addCrbSourceOfInformationTypeToCollectionIfMissing(crbSourceOfInformationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbSourceOfInformationTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
