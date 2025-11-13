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

import { ISnaSectorCode, SnaSectorCode } from '../sna-sector-code.model';

import { SnaSectorCodeService } from './sna-sector-code.service';

describe('SnaSectorCode Service', () => {
  let service: SnaSectorCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISnaSectorCode;
  let expectedResult: ISnaSectorCode | ISnaSectorCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SnaSectorCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      sectorTypeCode: 'AAAAAAA',
      mainSectorCode: 'AAAAAAA',
      mainSectorTypeName: 'AAAAAAA',
      subSectorCode: 'AAAAAAA',
      subSectorName: 'AAAAAAA',
      subSubSectorCode: 'AAAAAAA',
      subSubSectorName: 'AAAAAAA',
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

    it('should create a SnaSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SnaSectorCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SnaSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sectorTypeCode: 'BBBBBB',
          mainSectorCode: 'BBBBBB',
          mainSectorTypeName: 'BBBBBB',
          subSectorCode: 'BBBBBB',
          subSectorName: 'BBBBBB',
          subSubSectorCode: 'BBBBBB',
          subSubSectorName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SnaSectorCode', () => {
      const patchObject = Object.assign(
        {
          mainSectorTypeName: 'BBBBBB',
          subSectorName: 'BBBBBB',
        },
        new SnaSectorCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SnaSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sectorTypeCode: 'BBBBBB',
          mainSectorCode: 'BBBBBB',
          mainSectorTypeName: 'BBBBBB',
          subSectorCode: 'BBBBBB',
          subSectorName: 'BBBBBB',
          subSubSectorCode: 'BBBBBB',
          subSubSectorName: 'BBBBBB',
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

    it('should delete a SnaSectorCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSnaSectorCodeToCollectionIfMissing', () => {
      it('should add a SnaSectorCode to an empty array', () => {
        const snaSectorCode: ISnaSectorCode = { id: 123 };
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing([], snaSectorCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(snaSectorCode);
      });

      it('should not add a SnaSectorCode to an array that contains it', () => {
        const snaSectorCode: ISnaSectorCode = { id: 123 };
        const snaSectorCodeCollection: ISnaSectorCode[] = [
          {
            ...snaSectorCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing(snaSectorCodeCollection, snaSectorCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SnaSectorCode to an array that doesn't contain it", () => {
        const snaSectorCode: ISnaSectorCode = { id: 123 };
        const snaSectorCodeCollection: ISnaSectorCode[] = [{ id: 456 }];
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing(snaSectorCodeCollection, snaSectorCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(snaSectorCode);
      });

      it('should add only unique SnaSectorCode to an array', () => {
        const snaSectorCodeArray: ISnaSectorCode[] = [{ id: 123 }, { id: 456 }, { id: 21062 }];
        const snaSectorCodeCollection: ISnaSectorCode[] = [{ id: 123 }];
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing(snaSectorCodeCollection, ...snaSectorCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const snaSectorCode: ISnaSectorCode = { id: 123 };
        const snaSectorCode2: ISnaSectorCode = { id: 456 };
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing([], snaSectorCode, snaSectorCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(snaSectorCode);
        expect(expectedResult).toContain(snaSectorCode2);
      });

      it('should accept null and undefined values', () => {
        const snaSectorCode: ISnaSectorCode = { id: 123 };
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing([], null, snaSectorCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(snaSectorCode);
      });

      it('should return initial array if no SnaSectorCode is added', () => {
        const snaSectorCodeCollection: ISnaSectorCode[] = [{ id: 123 }];
        expectedResult = service.addSnaSectorCodeToCollectionIfMissing(snaSectorCodeCollection, undefined, null);
        expect(expectedResult).toEqual(snaSectorCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
