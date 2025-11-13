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

import { IInstitutionStatusType, InstitutionStatusType } from '../institution-status-type.model';

import { InstitutionStatusTypeService } from './institution-status-type.service';

describe('InstitutionStatusType Service', () => {
  let service: InstitutionStatusTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IInstitutionStatusType;
  let expectedResult: IInstitutionStatusType | IInstitutionStatusType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstitutionStatusTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      institutionStatusCode: 'AAAAAAA',
      institutionStatusType: 'AAAAAAA',
      insitutionStatusTypeDescription: 'AAAAAAA',
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

    it('should create a InstitutionStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InstitutionStatusType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InstitutionStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionStatusCode: 'BBBBBB',
          institutionStatusType: 'BBBBBB',
          insitutionStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InstitutionStatusType', () => {
      const patchObject = Object.assign(
        {
          insitutionStatusTypeDescription: 'BBBBBB',
        },
        new InstitutionStatusType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InstitutionStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionStatusCode: 'BBBBBB',
          institutionStatusType: 'BBBBBB',
          insitutionStatusTypeDescription: 'BBBBBB',
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

    it('should delete a InstitutionStatusType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInstitutionStatusTypeToCollectionIfMissing', () => {
      it('should add a InstitutionStatusType to an empty array', () => {
        const institutionStatusType: IInstitutionStatusType = { id: 123 };
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing([], institutionStatusType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionStatusType);
      });

      it('should not add a InstitutionStatusType to an array that contains it', () => {
        const institutionStatusType: IInstitutionStatusType = { id: 123 };
        const institutionStatusTypeCollection: IInstitutionStatusType[] = [
          {
            ...institutionStatusType,
          },
          { id: 456 },
        ];
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing(institutionStatusTypeCollection, institutionStatusType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InstitutionStatusType to an array that doesn't contain it", () => {
        const institutionStatusType: IInstitutionStatusType = { id: 123 };
        const institutionStatusTypeCollection: IInstitutionStatusType[] = [{ id: 456 }];
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing(institutionStatusTypeCollection, institutionStatusType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionStatusType);
      });

      it('should add only unique InstitutionStatusType to an array', () => {
        const institutionStatusTypeArray: IInstitutionStatusType[] = [{ id: 123 }, { id: 456 }, { id: 65729 }];
        const institutionStatusTypeCollection: IInstitutionStatusType[] = [{ id: 123 }];
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing(
          institutionStatusTypeCollection,
          ...institutionStatusTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const institutionStatusType: IInstitutionStatusType = { id: 123 };
        const institutionStatusType2: IInstitutionStatusType = { id: 456 };
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing([], institutionStatusType, institutionStatusType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionStatusType);
        expect(expectedResult).toContain(institutionStatusType2);
      });

      it('should accept null and undefined values', () => {
        const institutionStatusType: IInstitutionStatusType = { id: 123 };
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing([], null, institutionStatusType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionStatusType);
      });

      it('should return initial array if no InstitutionStatusType is added', () => {
        const institutionStatusTypeCollection: IInstitutionStatusType[] = [{ id: 123 }];
        expectedResult = service.addInstitutionStatusTypeToCollectionIfMissing(institutionStatusTypeCollection, undefined, null);
        expect(expectedResult).toEqual(institutionStatusTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
