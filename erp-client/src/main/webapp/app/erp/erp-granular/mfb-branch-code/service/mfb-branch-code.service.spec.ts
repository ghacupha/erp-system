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

import { IMfbBranchCode, MfbBranchCode } from '../mfb-branch-code.model';

import { MfbBranchCodeService } from './mfb-branch-code.service';

describe('MfbBranchCode Service', () => {
  let service: MfbBranchCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IMfbBranchCode;
  let expectedResult: IMfbBranchCode | IMfbBranchCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MfbBranchCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bankCode: 'AAAAAAA',
      bankName: 'AAAAAAA',
      branchCode: 'AAAAAAA',
      branchName: 'AAAAAAA',
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

    it('should create a MfbBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MfbBranchCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MfbBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankCode: 'BBBBBB',
          bankName: 'BBBBBB',
          branchCode: 'BBBBBB',
          branchName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MfbBranchCode', () => {
      const patchObject = Object.assign(
        {
          bankCode: 'BBBBBB',
          bankName: 'BBBBBB',
          branchCode: 'BBBBBB',
          branchName: 'BBBBBB',
        },
        new MfbBranchCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MfbBranchCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankCode: 'BBBBBB',
          bankName: 'BBBBBB',
          branchCode: 'BBBBBB',
          branchName: 'BBBBBB',
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

    it('should delete a MfbBranchCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMfbBranchCodeToCollectionIfMissing', () => {
      it('should add a MfbBranchCode to an empty array', () => {
        const mfbBranchCode: IMfbBranchCode = { id: 123 };
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing([], mfbBranchCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mfbBranchCode);
      });

      it('should not add a MfbBranchCode to an array that contains it', () => {
        const mfbBranchCode: IMfbBranchCode = { id: 123 };
        const mfbBranchCodeCollection: IMfbBranchCode[] = [
          {
            ...mfbBranchCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing(mfbBranchCodeCollection, mfbBranchCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MfbBranchCode to an array that doesn't contain it", () => {
        const mfbBranchCode: IMfbBranchCode = { id: 123 };
        const mfbBranchCodeCollection: IMfbBranchCode[] = [{ id: 456 }];
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing(mfbBranchCodeCollection, mfbBranchCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mfbBranchCode);
      });

      it('should add only unique MfbBranchCode to an array', () => {
        const mfbBranchCodeArray: IMfbBranchCode[] = [{ id: 123 }, { id: 456 }, { id: 76041 }];
        const mfbBranchCodeCollection: IMfbBranchCode[] = [{ id: 123 }];
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing(mfbBranchCodeCollection, ...mfbBranchCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mfbBranchCode: IMfbBranchCode = { id: 123 };
        const mfbBranchCode2: IMfbBranchCode = { id: 456 };
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing([], mfbBranchCode, mfbBranchCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mfbBranchCode);
        expect(expectedResult).toContain(mfbBranchCode2);
      });

      it('should accept null and undefined values', () => {
        const mfbBranchCode: IMfbBranchCode = { id: 123 };
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing([], null, mfbBranchCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mfbBranchCode);
      });

      it('should return initial array if no MfbBranchCode is added', () => {
        const mfbBranchCodeCollection: IMfbBranchCode[] = [{ id: 123 }];
        expectedResult = service.addMfbBranchCodeToCollectionIfMissing(mfbBranchCodeCollection, undefined, null);
        expect(expectedResult).toEqual(mfbBranchCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
