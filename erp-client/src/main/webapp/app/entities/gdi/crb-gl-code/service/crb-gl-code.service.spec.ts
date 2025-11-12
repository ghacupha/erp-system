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

import { ICrbGlCode, CrbGlCode } from '../crb-gl-code.model';

import { CrbGlCodeService } from './crb-gl-code.service';

describe('CrbGlCode Service', () => {
  let service: CrbGlCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbGlCode;
  let expectedResult: ICrbGlCode | ICrbGlCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbGlCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      glCode: 'AAAAAAA',
      glDescription: 'AAAAAAA',
      glType: 'AAAAAAA',
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

    it('should create a CrbGlCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbGlCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbGlCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          glCode: 'BBBBBB',
          glDescription: 'BBBBBB',
          glType: 'BBBBBB',
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

    it('should partial update a CrbGlCode', () => {
      const patchObject = Object.assign(
        {
          glType: 'BBBBBB',
        },
        new CrbGlCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbGlCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          glCode: 'BBBBBB',
          glDescription: 'BBBBBB',
          glType: 'BBBBBB',
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

    it('should delete a CrbGlCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbGlCodeToCollectionIfMissing', () => {
      it('should add a CrbGlCode to an empty array', () => {
        const crbGlCode: ICrbGlCode = { id: 123 };
        expectedResult = service.addCrbGlCodeToCollectionIfMissing([], crbGlCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbGlCode);
      });

      it('should not add a CrbGlCode to an array that contains it', () => {
        const crbGlCode: ICrbGlCode = { id: 123 };
        const crbGlCodeCollection: ICrbGlCode[] = [
          {
            ...crbGlCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbGlCodeToCollectionIfMissing(crbGlCodeCollection, crbGlCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbGlCode to an array that doesn't contain it", () => {
        const crbGlCode: ICrbGlCode = { id: 123 };
        const crbGlCodeCollection: ICrbGlCode[] = [{ id: 456 }];
        expectedResult = service.addCrbGlCodeToCollectionIfMissing(crbGlCodeCollection, crbGlCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbGlCode);
      });

      it('should add only unique CrbGlCode to an array', () => {
        const crbGlCodeArray: ICrbGlCode[] = [{ id: 123 }, { id: 456 }, { id: 50722 }];
        const crbGlCodeCollection: ICrbGlCode[] = [{ id: 123 }];
        expectedResult = service.addCrbGlCodeToCollectionIfMissing(crbGlCodeCollection, ...crbGlCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbGlCode: ICrbGlCode = { id: 123 };
        const crbGlCode2: ICrbGlCode = { id: 456 };
        expectedResult = service.addCrbGlCodeToCollectionIfMissing([], crbGlCode, crbGlCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbGlCode);
        expect(expectedResult).toContain(crbGlCode2);
      });

      it('should accept null and undefined values', () => {
        const crbGlCode: ICrbGlCode = { id: 123 };
        expectedResult = service.addCrbGlCodeToCollectionIfMissing([], null, crbGlCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbGlCode);
      });

      it('should return initial array if no CrbGlCode is added', () => {
        const crbGlCodeCollection: ICrbGlCode[] = [{ id: 123 }];
        expectedResult = service.addCrbGlCodeToCollectionIfMissing(crbGlCodeCollection, undefined, null);
        expect(expectedResult).toEqual(crbGlCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
