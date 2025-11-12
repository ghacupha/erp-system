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

import { ICountySubCountyCode, CountySubCountyCode } from '../county-sub-county-code.model';

import { CountySubCountyCodeService } from './county-sub-county-code.service';

describe('CountySubCountyCode Service', () => {
  let service: CountySubCountyCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountySubCountyCode;
  let expectedResult: ICountySubCountyCode | ICountySubCountyCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountySubCountyCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      subCountyCode: 'AAAAAAA',
      subCountyName: 'AAAAAAA',
      countyCode: 'AAAAAAA',
      countyName: 'AAAAAAA',
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

    it('should create a CountySubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CountySubCountyCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountySubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subCountyCode: 'BBBBBB',
          subCountyName: 'BBBBBB',
          countyCode: 'BBBBBB',
          countyName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountySubCountyCode', () => {
      const patchObject = Object.assign(
        {
          subCountyName: 'BBBBBB',
          countyName: 'BBBBBB',
        },
        new CountySubCountyCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountySubCountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subCountyCode: 'BBBBBB',
          subCountyName: 'BBBBBB',
          countyCode: 'BBBBBB',
          countyName: 'BBBBBB',
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

    it('should delete a CountySubCountyCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountySubCountyCodeToCollectionIfMissing', () => {
      it('should add a CountySubCountyCode to an empty array', () => {
        const countySubCountyCode: ICountySubCountyCode = { id: 123 };
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing([], countySubCountyCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countySubCountyCode);
      });

      it('should not add a CountySubCountyCode to an array that contains it', () => {
        const countySubCountyCode: ICountySubCountyCode = { id: 123 };
        const countySubCountyCodeCollection: ICountySubCountyCode[] = [
          {
            ...countySubCountyCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing(countySubCountyCodeCollection, countySubCountyCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountySubCountyCode to an array that doesn't contain it", () => {
        const countySubCountyCode: ICountySubCountyCode = { id: 123 };
        const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 456 }];
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing(countySubCountyCodeCollection, countySubCountyCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countySubCountyCode);
      });

      it('should add only unique CountySubCountyCode to an array', () => {
        const countySubCountyCodeArray: ICountySubCountyCode[] = [{ id: 123 }, { id: 456 }, { id: 19526 }];
        const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 123 }];
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing(countySubCountyCodeCollection, ...countySubCountyCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countySubCountyCode: ICountySubCountyCode = { id: 123 };
        const countySubCountyCode2: ICountySubCountyCode = { id: 456 };
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing([], countySubCountyCode, countySubCountyCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countySubCountyCode);
        expect(expectedResult).toContain(countySubCountyCode2);
      });

      it('should accept null and undefined values', () => {
        const countySubCountyCode: ICountySubCountyCode = { id: 123 };
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing([], null, countySubCountyCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countySubCountyCode);
      });

      it('should return initial array if no CountySubCountyCode is added', () => {
        const countySubCountyCodeCollection: ICountySubCountyCode[] = [{ id: 123 }];
        expectedResult = service.addCountySubCountyCodeToCollectionIfMissing(countySubCountyCodeCollection, undefined, null);
        expect(expectedResult).toEqual(countySubCountyCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
