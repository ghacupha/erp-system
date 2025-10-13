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

import { IIsoCountryCode, IsoCountryCode } from '../iso-country-code.model';

import { IsoCountryCodeService } from './iso-country-code.service';

describe('IsoCountryCode Service', () => {
  let service: IsoCountryCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IIsoCountryCode;
  let expectedResult: IIsoCountryCode | IIsoCountryCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsoCountryCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      countryCode: 'AAAAAAA',
      countryDescription: 'AAAAAAA',
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

    it('should create a IsoCountryCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IsoCountryCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IsoCountryCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countryCode: 'BBBBBB',
          countryDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IsoCountryCode', () => {
      const patchObject = Object.assign(
        {
          countryDescription: 'BBBBBB',
        },
        new IsoCountryCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IsoCountryCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countryCode: 'BBBBBB',
          countryDescription: 'BBBBBB',
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

    it('should delete a IsoCountryCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIsoCountryCodeToCollectionIfMissing', () => {
      it('should add a IsoCountryCode to an empty array', () => {
        const isoCountryCode: IIsoCountryCode = { id: 123 };
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing([], isoCountryCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isoCountryCode);
      });

      it('should not add a IsoCountryCode to an array that contains it', () => {
        const isoCountryCode: IIsoCountryCode = { id: 123 };
        const isoCountryCodeCollection: IIsoCountryCode[] = [
          {
            ...isoCountryCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing(isoCountryCodeCollection, isoCountryCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IsoCountryCode to an array that doesn't contain it", () => {
        const isoCountryCode: IIsoCountryCode = { id: 123 };
        const isoCountryCodeCollection: IIsoCountryCode[] = [{ id: 456 }];
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing(isoCountryCodeCollection, isoCountryCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isoCountryCode);
      });

      it('should add only unique IsoCountryCode to an array', () => {
        const isoCountryCodeArray: IIsoCountryCode[] = [{ id: 123 }, { id: 456 }, { id: 77241 }];
        const isoCountryCodeCollection: IIsoCountryCode[] = [{ id: 123 }];
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing(isoCountryCodeCollection, ...isoCountryCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isoCountryCode: IIsoCountryCode = { id: 123 };
        const isoCountryCode2: IIsoCountryCode = { id: 456 };
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing([], isoCountryCode, isoCountryCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isoCountryCode);
        expect(expectedResult).toContain(isoCountryCode2);
      });

      it('should accept null and undefined values', () => {
        const isoCountryCode: IIsoCountryCode = { id: 123 };
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing([], null, isoCountryCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isoCountryCode);
      });

      it('should return initial array if no IsoCountryCode is added', () => {
        const isoCountryCodeCollection: IIsoCountryCode[] = [{ id: 123 }];
        expectedResult = service.addIsoCountryCodeToCollectionIfMissing(isoCountryCodeCollection, undefined, null);
        expect(expectedResult).toEqual(isoCountryCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
