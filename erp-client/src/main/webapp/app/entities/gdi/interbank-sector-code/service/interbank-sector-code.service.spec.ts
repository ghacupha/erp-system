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

import { IInterbankSectorCode, InterbankSectorCode } from '../interbank-sector-code.model';

import { InterbankSectorCodeService } from './interbank-sector-code.service';

describe('InterbankSectorCode Service', () => {
  let service: InterbankSectorCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IInterbankSectorCode;
  let expectedResult: IInterbankSectorCode | IInterbankSectorCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterbankSectorCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      interbankSectorCode: 'AAAAAAA',
      interbankSectorCodeDescription: 'AAAAAAA',
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

    it('should create a InterbankSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InterbankSectorCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InterbankSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interbankSectorCode: 'BBBBBB',
          interbankSectorCodeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InterbankSectorCode', () => {
      const patchObject = Object.assign(
        {
          interbankSectorCode: 'BBBBBB',
          interbankSectorCodeDescription: 'BBBBBB',
        },
        new InterbankSectorCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InterbankSectorCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          interbankSectorCode: 'BBBBBB',
          interbankSectorCodeDescription: 'BBBBBB',
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

    it('should delete a InterbankSectorCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInterbankSectorCodeToCollectionIfMissing', () => {
      it('should add a InterbankSectorCode to an empty array', () => {
        const interbankSectorCode: IInterbankSectorCode = { id: 123 };
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing([], interbankSectorCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interbankSectorCode);
      });

      it('should not add a InterbankSectorCode to an array that contains it', () => {
        const interbankSectorCode: IInterbankSectorCode = { id: 123 };
        const interbankSectorCodeCollection: IInterbankSectorCode[] = [
          {
            ...interbankSectorCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing(interbankSectorCodeCollection, interbankSectorCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InterbankSectorCode to an array that doesn't contain it", () => {
        const interbankSectorCode: IInterbankSectorCode = { id: 123 };
        const interbankSectorCodeCollection: IInterbankSectorCode[] = [{ id: 456 }];
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing(interbankSectorCodeCollection, interbankSectorCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interbankSectorCode);
      });

      it('should add only unique InterbankSectorCode to an array', () => {
        const interbankSectorCodeArray: IInterbankSectorCode[] = [{ id: 123 }, { id: 456 }, { id: 6964 }];
        const interbankSectorCodeCollection: IInterbankSectorCode[] = [{ id: 123 }];
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing(interbankSectorCodeCollection, ...interbankSectorCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const interbankSectorCode: IInterbankSectorCode = { id: 123 };
        const interbankSectorCode2: IInterbankSectorCode = { id: 456 };
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing([], interbankSectorCode, interbankSectorCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interbankSectorCode);
        expect(expectedResult).toContain(interbankSectorCode2);
      });

      it('should accept null and undefined values', () => {
        const interbankSectorCode: IInterbankSectorCode = { id: 123 };
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing([], null, interbankSectorCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interbankSectorCode);
      });

      it('should return initial array if no InterbankSectorCode is added', () => {
        const interbankSectorCodeCollection: IInterbankSectorCode[] = [{ id: 123 }];
        expectedResult = service.addInterbankSectorCodeToCollectionIfMissing(interbankSectorCodeCollection, undefined, null);
        expect(expectedResult).toEqual(interbankSectorCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
