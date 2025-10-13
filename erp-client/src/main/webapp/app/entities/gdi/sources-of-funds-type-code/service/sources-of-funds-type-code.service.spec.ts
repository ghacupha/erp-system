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

import { ISourcesOfFundsTypeCode, SourcesOfFundsTypeCode } from '../sources-of-funds-type-code.model';

import { SourcesOfFundsTypeCodeService } from './sources-of-funds-type-code.service';

describe('SourcesOfFundsTypeCode Service', () => {
  let service: SourcesOfFundsTypeCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISourcesOfFundsTypeCode;
  let expectedResult: ISourcesOfFundsTypeCode | ISourcesOfFundsTypeCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SourcesOfFundsTypeCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      sourceOfFundsTypeCode: 'AAAAAAA',
      sourceOfFundsType: 'AAAAAAA',
      sourceOfFundsTypeDetails: 'AAAAAAA',
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

    it('should create a SourcesOfFundsTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SourcesOfFundsTypeCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SourcesOfFundsTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOfFundsTypeCode: 'BBBBBB',
          sourceOfFundsType: 'BBBBBB',
          sourceOfFundsTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SourcesOfFundsTypeCode', () => {
      const patchObject = Object.assign(
        {
          sourceOfFundsType: 'BBBBBB',
          sourceOfFundsTypeDetails: 'BBBBBB',
        },
        new SourcesOfFundsTypeCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SourcesOfFundsTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOfFundsTypeCode: 'BBBBBB',
          sourceOfFundsType: 'BBBBBB',
          sourceOfFundsTypeDetails: 'BBBBBB',
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

    it('should delete a SourcesOfFundsTypeCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSourcesOfFundsTypeCodeToCollectionIfMissing', () => {
      it('should add a SourcesOfFundsTypeCode to an empty array', () => {
        const sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode = { id: 123 };
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing([], sourcesOfFundsTypeCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourcesOfFundsTypeCode);
      });

      it('should not add a SourcesOfFundsTypeCode to an array that contains it', () => {
        const sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode = { id: 123 };
        const sourcesOfFundsTypeCodeCollection: ISourcesOfFundsTypeCode[] = [
          {
            ...sourcesOfFundsTypeCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing(sourcesOfFundsTypeCodeCollection, sourcesOfFundsTypeCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SourcesOfFundsTypeCode to an array that doesn't contain it", () => {
        const sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode = { id: 123 };
        const sourcesOfFundsTypeCodeCollection: ISourcesOfFundsTypeCode[] = [{ id: 456 }];
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing(sourcesOfFundsTypeCodeCollection, sourcesOfFundsTypeCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourcesOfFundsTypeCode);
      });

      it('should add only unique SourcesOfFundsTypeCode to an array', () => {
        const sourcesOfFundsTypeCodeArray: ISourcesOfFundsTypeCode[] = [{ id: 123 }, { id: 456 }, { id: 37282 }];
        const sourcesOfFundsTypeCodeCollection: ISourcesOfFundsTypeCode[] = [{ id: 123 }];
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing(
          sourcesOfFundsTypeCodeCollection,
          ...sourcesOfFundsTypeCodeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode = { id: 123 };
        const sourcesOfFundsTypeCode2: ISourcesOfFundsTypeCode = { id: 456 };
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing([], sourcesOfFundsTypeCode, sourcesOfFundsTypeCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourcesOfFundsTypeCode);
        expect(expectedResult).toContain(sourcesOfFundsTypeCode2);
      });

      it('should accept null and undefined values', () => {
        const sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode = { id: 123 };
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing([], null, sourcesOfFundsTypeCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourcesOfFundsTypeCode);
      });

      it('should return initial array if no SourcesOfFundsTypeCode is added', () => {
        const sourcesOfFundsTypeCodeCollection: ISourcesOfFundsTypeCode[] = [{ id: 123 }];
        expectedResult = service.addSourcesOfFundsTypeCodeToCollectionIfMissing(sourcesOfFundsTypeCodeCollection, undefined, null);
        expect(expectedResult).toEqual(sourcesOfFundsTypeCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
