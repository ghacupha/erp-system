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

import { ICountyCode, CountyCode } from '../county-code.model';

import { CountyCodeService } from './county-code.service';

describe('CountyCode Service', () => {
  let service: CountyCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyCode;
  let expectedResult: ICountyCode | ICountyCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      countyCode: 0,
      countyName: 'AAAAAAA',
      subCountyCode: 0,
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

    it('should create a CountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CountyCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyCode: 1,
          countyName: 'BBBBBB',
          subCountyCode: 1,
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

    it('should partial update a CountyCode', () => {
      const patchObject = Object.assign(
        {
          countyName: 'BBBBBB',
          subCountyCode: 1,
        },
        new CountyCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyCode: 1,
          countyName: 'BBBBBB',
          subCountyCode: 1,
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

    it('should delete a CountyCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyCodeToCollectionIfMissing', () => {
      it('should add a CountyCode to an empty array', () => {
        const countyCode: ICountyCode = { id: 123 };
        expectedResult = service.addCountyCodeToCollectionIfMissing([], countyCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyCode);
      });

      it('should not add a CountyCode to an array that contains it', () => {
        const countyCode: ICountyCode = { id: 123 };
        const countyCodeCollection: ICountyCode[] = [
          {
            ...countyCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyCodeToCollectionIfMissing(countyCodeCollection, countyCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyCode to an array that doesn't contain it", () => {
        const countyCode: ICountyCode = { id: 123 };
        const countyCodeCollection: ICountyCode[] = [{ id: 456 }];
        expectedResult = service.addCountyCodeToCollectionIfMissing(countyCodeCollection, countyCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyCode);
      });

      it('should add only unique CountyCode to an array', () => {
        const countyCodeArray: ICountyCode[] = [{ id: 123 }, { id: 456 }, { id: 4401 }];
        const countyCodeCollection: ICountyCode[] = [{ id: 123 }];
        expectedResult = service.addCountyCodeToCollectionIfMissing(countyCodeCollection, ...countyCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyCode: ICountyCode = { id: 123 };
        const countyCode2: ICountyCode = { id: 456 };
        expectedResult = service.addCountyCodeToCollectionIfMissing([], countyCode, countyCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyCode);
        expect(expectedResult).toContain(countyCode2);
      });

      it('should accept null and undefined values', () => {
        const countyCode: ICountyCode = { id: 123 };
        expectedResult = service.addCountyCodeToCollectionIfMissing([], null, countyCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyCode);
      });

      it('should return initial array if no CountyCode is added', () => {
        const countyCodeCollection: ICountyCode[] = [{ id: 123 }];
        expectedResult = service.addCountyCodeToCollectionIfMissing(countyCodeCollection, undefined, null);
        expect(expectedResult).toEqual(countyCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
