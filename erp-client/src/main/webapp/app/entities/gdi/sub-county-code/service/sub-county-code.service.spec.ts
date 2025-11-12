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

import { ISubCountyCode, SubCountyCode } from '../sub-county-code.model';

import { SubCountyCodeService } from './sub-county-code.service';

describe('SubCountyCode Service', () => {
  let service: SubCountyCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISubCountyCode;
  let expectedResult: ISubCountyCode | ISubCountyCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubCountyCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      countyCode: 'AAAAAAA',
      countyName: 'AAAAAAA',
      subCountyCode: 'AAAAAAA',
      subCountyName: 'AAAAAAA',
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

    it('should create a SubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SubCountyCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyCode: 'BBBBBB',
          countyName: 'BBBBBB',
          subCountyCode: 'BBBBBB',
          subCountyName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubCountyCode', () => {
      const patchObject = Object.assign(
        {
          countyCode: 'BBBBBB',
          subCountyCode: 'BBBBBB',
        },
        new SubCountyCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyCode: 'BBBBBB',
          countyName: 'BBBBBB',
          subCountyCode: 'BBBBBB',
          subCountyName: 'BBBBBB',
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

    it('should delete a SubCountyCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSubCountyCodeToCollectionIfMissing', () => {
      it('should add a SubCountyCode to an empty array', () => {
        const subCountyCode: ISubCountyCode = { id: 123 };
        expectedResult = service.addSubCountyCodeToCollectionIfMissing([], subCountyCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subCountyCode);
      });

      it('should not add a SubCountyCode to an array that contains it', () => {
        const subCountyCode: ISubCountyCode = { id: 123 };
        const subCountyCodeCollection: ISubCountyCode[] = [
          {
            ...subCountyCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addSubCountyCodeToCollectionIfMissing(subCountyCodeCollection, subCountyCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubCountyCode to an array that doesn't contain it", () => {
        const subCountyCode: ISubCountyCode = { id: 123 };
        const subCountyCodeCollection: ISubCountyCode[] = [{ id: 456 }];
        expectedResult = service.addSubCountyCodeToCollectionIfMissing(subCountyCodeCollection, subCountyCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subCountyCode);
      });

      it('should add only unique SubCountyCode to an array', () => {
        const subCountyCodeArray: ISubCountyCode[] = [{ id: 123 }, { id: 456 }, { id: 48704 }];
        const subCountyCodeCollection: ISubCountyCode[] = [{ id: 123 }];
        expectedResult = service.addSubCountyCodeToCollectionIfMissing(subCountyCodeCollection, ...subCountyCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subCountyCode: ISubCountyCode = { id: 123 };
        const subCountyCode2: ISubCountyCode = { id: 456 };
        expectedResult = service.addSubCountyCodeToCollectionIfMissing([], subCountyCode, subCountyCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subCountyCode);
        expect(expectedResult).toContain(subCountyCode2);
      });

      it('should accept null and undefined values', () => {
        const subCountyCode: ISubCountyCode = { id: 123 };
        expectedResult = service.addSubCountyCodeToCollectionIfMissing([], null, subCountyCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subCountyCode);
      });

      it('should return initial array if no SubCountyCode is added', () => {
        const subCountyCodeCollection: ISubCountyCode[] = [{ id: 123 }];
        expectedResult = service.addSubCountyCodeToCollectionIfMissing(subCountyCodeCollection, undefined, null);
        expect(expectedResult).toEqual(subCountyCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
