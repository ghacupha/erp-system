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

import { ICrbRecordFileType, CrbRecordFileType } from '../crb-record-file-type.model';

import { CrbRecordFileTypeService } from './crb-record-file-type.service';

describe('CrbRecordFileType Service', () => {
  let service: CrbRecordFileTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbRecordFileType;
  let expectedResult: ICrbRecordFileType | ICrbRecordFileType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbRecordFileTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      recordFileTypeCode: 'AAAAAAA',
      recordFileType: 'AAAAAAA',
      recordFileTypeDetails: 'AAAAAAA',
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

    it('should create a CrbRecordFileType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbRecordFileType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbRecordFileType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordFileTypeCode: 'BBBBBB',
          recordFileType: 'BBBBBB',
          recordFileTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbRecordFileType', () => {
      const patchObject = Object.assign(
        {
          recordFileTypeCode: 'BBBBBB',
        },
        new CrbRecordFileType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbRecordFileType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordFileTypeCode: 'BBBBBB',
          recordFileType: 'BBBBBB',
          recordFileTypeDetails: 'BBBBBB',
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

    it('should delete a CrbRecordFileType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbRecordFileTypeToCollectionIfMissing', () => {
      it('should add a CrbRecordFileType to an empty array', () => {
        const crbRecordFileType: ICrbRecordFileType = { id: 123 };
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing([], crbRecordFileType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbRecordFileType);
      });

      it('should not add a CrbRecordFileType to an array that contains it', () => {
        const crbRecordFileType: ICrbRecordFileType = { id: 123 };
        const crbRecordFileTypeCollection: ICrbRecordFileType[] = [
          {
            ...crbRecordFileType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing(crbRecordFileTypeCollection, crbRecordFileType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbRecordFileType to an array that doesn't contain it", () => {
        const crbRecordFileType: ICrbRecordFileType = { id: 123 };
        const crbRecordFileTypeCollection: ICrbRecordFileType[] = [{ id: 456 }];
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing(crbRecordFileTypeCollection, crbRecordFileType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbRecordFileType);
      });

      it('should add only unique CrbRecordFileType to an array', () => {
        const crbRecordFileTypeArray: ICrbRecordFileType[] = [{ id: 123 }, { id: 456 }, { id: 50629 }];
        const crbRecordFileTypeCollection: ICrbRecordFileType[] = [{ id: 123 }];
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing(crbRecordFileTypeCollection, ...crbRecordFileTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbRecordFileType: ICrbRecordFileType = { id: 123 };
        const crbRecordFileType2: ICrbRecordFileType = { id: 456 };
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing([], crbRecordFileType, crbRecordFileType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbRecordFileType);
        expect(expectedResult).toContain(crbRecordFileType2);
      });

      it('should accept null and undefined values', () => {
        const crbRecordFileType: ICrbRecordFileType = { id: 123 };
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing([], null, crbRecordFileType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbRecordFileType);
      });

      it('should return initial array if no CrbRecordFileType is added', () => {
        const crbRecordFileTypeCollection: ICrbRecordFileType[] = [{ id: 123 }];
        expectedResult = service.addCrbRecordFileTypeToCollectionIfMissing(crbRecordFileTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbRecordFileTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
