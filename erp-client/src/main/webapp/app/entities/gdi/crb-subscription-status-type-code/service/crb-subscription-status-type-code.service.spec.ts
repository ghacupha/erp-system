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

import { ICrbSubscriptionStatusTypeCode, CrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';

import { CrbSubscriptionStatusTypeCodeService } from './crb-subscription-status-type-code.service';

describe('CrbSubscriptionStatusTypeCode Service', () => {
  let service: CrbSubscriptionStatusTypeCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbSubscriptionStatusTypeCode;
  let expectedResult: ICrbSubscriptionStatusTypeCode | ICrbSubscriptionStatusTypeCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbSubscriptionStatusTypeCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      subscriptionStatusTypeCode: 'AAAAAAA',
      subscriptionStatusType: 'AAAAAAA',
      subscriptionStatusTypeDescription: 'AAAAAAA',
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

    it('should create a CrbSubscriptionStatusTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbSubscriptionStatusTypeCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbSubscriptionStatusTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subscriptionStatusTypeCode: 'BBBBBB',
          subscriptionStatusType: 'BBBBBB',
          subscriptionStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbSubscriptionStatusTypeCode', () => {
      const patchObject = Object.assign(
        {
          subscriptionStatusTypeCode: 'BBBBBB',
          subscriptionStatusType: 'BBBBBB',
          subscriptionStatusTypeDescription: 'BBBBBB',
        },
        new CrbSubscriptionStatusTypeCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbSubscriptionStatusTypeCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subscriptionStatusTypeCode: 'BBBBBB',
          subscriptionStatusType: 'BBBBBB',
          subscriptionStatusTypeDescription: 'BBBBBB',
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

    it('should delete a CrbSubscriptionStatusTypeCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbSubscriptionStatusTypeCodeToCollectionIfMissing', () => {
      it('should add a CrbSubscriptionStatusTypeCode to an empty array', () => {
        const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 123 };
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing([], crbSubscriptionStatusTypeCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSubscriptionStatusTypeCode);
      });

      it('should not add a CrbSubscriptionStatusTypeCode to an array that contains it', () => {
        const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 123 };
        const crbSubscriptionStatusTypeCodeCollection: ICrbSubscriptionStatusTypeCode[] = [
          {
            ...crbSubscriptionStatusTypeCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
          crbSubscriptionStatusTypeCodeCollection,
          crbSubscriptionStatusTypeCode
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbSubscriptionStatusTypeCode to an array that doesn't contain it", () => {
        const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 123 };
        const crbSubscriptionStatusTypeCodeCollection: ICrbSubscriptionStatusTypeCode[] = [{ id: 456 }];
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
          crbSubscriptionStatusTypeCodeCollection,
          crbSubscriptionStatusTypeCode
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSubscriptionStatusTypeCode);
      });

      it('should add only unique CrbSubscriptionStatusTypeCode to an array', () => {
        const crbSubscriptionStatusTypeCodeArray: ICrbSubscriptionStatusTypeCode[] = [{ id: 123 }, { id: 456 }, { id: 74847 }];
        const crbSubscriptionStatusTypeCodeCollection: ICrbSubscriptionStatusTypeCode[] = [{ id: 123 }];
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
          crbSubscriptionStatusTypeCodeCollection,
          ...crbSubscriptionStatusTypeCodeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 123 };
        const crbSubscriptionStatusTypeCode2: ICrbSubscriptionStatusTypeCode = { id: 456 };
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
          [],
          crbSubscriptionStatusTypeCode,
          crbSubscriptionStatusTypeCode2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbSubscriptionStatusTypeCode);
        expect(expectedResult).toContain(crbSubscriptionStatusTypeCode2);
      });

      it('should accept null and undefined values', () => {
        const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 123 };
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing([], null, crbSubscriptionStatusTypeCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbSubscriptionStatusTypeCode);
      });

      it('should return initial array if no CrbSubscriptionStatusTypeCode is added', () => {
        const crbSubscriptionStatusTypeCodeCollection: ICrbSubscriptionStatusTypeCode[] = [{ id: 123 }];
        expectedResult = service.addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
          crbSubscriptionStatusTypeCodeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(crbSubscriptionStatusTypeCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
