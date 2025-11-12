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

import { IAcquiringIssuingFlag, AcquiringIssuingFlag } from '../acquiring-issuing-flag.model';

import { AcquiringIssuingFlagService } from './acquiring-issuing-flag.service';

describe('AcquiringIssuingFlag Service', () => {
  let service: AcquiringIssuingFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: IAcquiringIssuingFlag;
  let expectedResult: IAcquiringIssuingFlag | IAcquiringIssuingFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AcquiringIssuingFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      cardAcquiringIssuingFlagCode: 'AAAAAAA',
      cardAcquiringIssuingDescription: 'AAAAAAA',
      cardAcquiringIssuingDetails: 'AAAAAAA',
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

    it('should create a AcquiringIssuingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AcquiringIssuingFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AcquiringIssuingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardAcquiringIssuingFlagCode: 'BBBBBB',
          cardAcquiringIssuingDescription: 'BBBBBB',
          cardAcquiringIssuingDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AcquiringIssuingFlag', () => {
      const patchObject = Object.assign({}, new AcquiringIssuingFlag());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AcquiringIssuingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cardAcquiringIssuingFlagCode: 'BBBBBB',
          cardAcquiringIssuingDescription: 'BBBBBB',
          cardAcquiringIssuingDetails: 'BBBBBB',
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

    it('should delete a AcquiringIssuingFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAcquiringIssuingFlagToCollectionIfMissing', () => {
      it('should add a AcquiringIssuingFlag to an empty array', () => {
        const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 123 };
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing([], acquiringIssuingFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acquiringIssuingFlag);
      });

      it('should not add a AcquiringIssuingFlag to an array that contains it', () => {
        const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 123 };
        const acquiringIssuingFlagCollection: IAcquiringIssuingFlag[] = [
          {
            ...acquiringIssuingFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing(acquiringIssuingFlagCollection, acquiringIssuingFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AcquiringIssuingFlag to an array that doesn't contain it", () => {
        const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 123 };
        const acquiringIssuingFlagCollection: IAcquiringIssuingFlag[] = [{ id: 456 }];
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing(acquiringIssuingFlagCollection, acquiringIssuingFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acquiringIssuingFlag);
      });

      it('should add only unique AcquiringIssuingFlag to an array', () => {
        const acquiringIssuingFlagArray: IAcquiringIssuingFlag[] = [{ id: 123 }, { id: 456 }, { id: 11213 }];
        const acquiringIssuingFlagCollection: IAcquiringIssuingFlag[] = [{ id: 123 }];
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing(acquiringIssuingFlagCollection, ...acquiringIssuingFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 123 };
        const acquiringIssuingFlag2: IAcquiringIssuingFlag = { id: 456 };
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing([], acquiringIssuingFlag, acquiringIssuingFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acquiringIssuingFlag);
        expect(expectedResult).toContain(acquiringIssuingFlag2);
      });

      it('should accept null and undefined values', () => {
        const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 123 };
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing([], null, acquiringIssuingFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acquiringIssuingFlag);
      });

      it('should return initial array if no AcquiringIssuingFlag is added', () => {
        const acquiringIssuingFlagCollection: IAcquiringIssuingFlag[] = [{ id: 123 }];
        expectedResult = service.addAcquiringIssuingFlagToCollectionIfMissing(acquiringIssuingFlagCollection, undefined, null);
        expect(expectedResult).toEqual(acquiringIssuingFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
