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

import { IInstitutionCode, InstitutionCode } from '../institution-code.model';

import { InstitutionCodeService } from './institution-code.service';

describe('InstitutionCode Service', () => {
  let service: InstitutionCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IInstitutionCode;
  let expectedResult: IInstitutionCode | IInstitutionCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstitutionCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      institutionCode: 'AAAAAAA',
      institutionName: 'AAAAAAA',
      shortName: 'AAAAAAA',
      category: 'AAAAAAA',
      institutionCategory: 'AAAAAAA',
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

    it('should create a InstitutionCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InstitutionCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InstitutionCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionCode: 'BBBBBB',
          institutionName: 'BBBBBB',
          shortName: 'BBBBBB',
          category: 'BBBBBB',
          institutionCategory: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InstitutionCode', () => {
      const patchObject = Object.assign(
        {
          institutionCategory: 'BBBBBB',
        },
        new InstitutionCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InstitutionCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionCode: 'BBBBBB',
          institutionName: 'BBBBBB',
          shortName: 'BBBBBB',
          category: 'BBBBBB',
          institutionCategory: 'BBBBBB',
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

    it('should delete a InstitutionCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInstitutionCodeToCollectionIfMissing', () => {
      it('should add a InstitutionCode to an empty array', () => {
        const institutionCode: IInstitutionCode = { id: 123 };
        expectedResult = service.addInstitutionCodeToCollectionIfMissing([], institutionCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionCode);
      });

      it('should not add a InstitutionCode to an array that contains it', () => {
        const institutionCode: IInstitutionCode = { id: 123 };
        const institutionCodeCollection: IInstitutionCode[] = [
          {
            ...institutionCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addInstitutionCodeToCollectionIfMissing(institutionCodeCollection, institutionCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InstitutionCode to an array that doesn't contain it", () => {
        const institutionCode: IInstitutionCode = { id: 123 };
        const institutionCodeCollection: IInstitutionCode[] = [{ id: 456 }];
        expectedResult = service.addInstitutionCodeToCollectionIfMissing(institutionCodeCollection, institutionCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionCode);
      });

      it('should add only unique InstitutionCode to an array', () => {
        const institutionCodeArray: IInstitutionCode[] = [{ id: 123 }, { id: 456 }, { id: 62466 }];
        const institutionCodeCollection: IInstitutionCode[] = [{ id: 123 }];
        expectedResult = service.addInstitutionCodeToCollectionIfMissing(institutionCodeCollection, ...institutionCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const institutionCode: IInstitutionCode = { id: 123 };
        const institutionCode2: IInstitutionCode = { id: 456 };
        expectedResult = service.addInstitutionCodeToCollectionIfMissing([], institutionCode, institutionCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionCode);
        expect(expectedResult).toContain(institutionCode2);
      });

      it('should accept null and undefined values', () => {
        const institutionCode: IInstitutionCode = { id: 123 };
        expectedResult = service.addInstitutionCodeToCollectionIfMissing([], null, institutionCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionCode);
      });

      it('should return initial array if no InstitutionCode is added', () => {
        const institutionCodeCollection: IInstitutionCode[] = [{ id: 123 }];
        expectedResult = service.addInstitutionCodeToCollectionIfMissing(institutionCodeCollection, undefined, null);
        expect(expectedResult).toEqual(institutionCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
