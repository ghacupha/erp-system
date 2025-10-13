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

import { ShareholdingFlagTypes } from 'app/entities/enumerations/shareholding-flag-types.model';
import { IShareHoldingFlag, ShareHoldingFlag } from '../share-holding-flag.model';

import { ShareHoldingFlagService } from './share-holding-flag.service';

describe('ShareHoldingFlag Service', () => {
  let service: ShareHoldingFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: IShareHoldingFlag;
  let expectedResult: IShareHoldingFlag | IShareHoldingFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShareHoldingFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      shareholdingFlagTypeCode: ShareholdingFlagTypes.Y,
      shareholdingFlagType: 'AAAAAAA',
      shareholdingTypeDescription: 'AAAAAAA',
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

    it('should create a ShareHoldingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ShareHoldingFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShareHoldingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shareholdingFlagTypeCode: 'BBBBBB',
          shareholdingFlagType: 'BBBBBB',
          shareholdingTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShareHoldingFlag', () => {
      const patchObject = Object.assign(
        {
          shareholdingTypeDescription: 'BBBBBB',
        },
        new ShareHoldingFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShareHoldingFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shareholdingFlagTypeCode: 'BBBBBB',
          shareholdingFlagType: 'BBBBBB',
          shareholdingTypeDescription: 'BBBBBB',
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

    it('should delete a ShareHoldingFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShareHoldingFlagToCollectionIfMissing', () => {
      it('should add a ShareHoldingFlag to an empty array', () => {
        const shareHoldingFlag: IShareHoldingFlag = { id: 123 };
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing([], shareHoldingFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shareHoldingFlag);
      });

      it('should not add a ShareHoldingFlag to an array that contains it', () => {
        const shareHoldingFlag: IShareHoldingFlag = { id: 123 };
        const shareHoldingFlagCollection: IShareHoldingFlag[] = [
          {
            ...shareHoldingFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing(shareHoldingFlagCollection, shareHoldingFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShareHoldingFlag to an array that doesn't contain it", () => {
        const shareHoldingFlag: IShareHoldingFlag = { id: 123 };
        const shareHoldingFlagCollection: IShareHoldingFlag[] = [{ id: 456 }];
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing(shareHoldingFlagCollection, shareHoldingFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shareHoldingFlag);
      });

      it('should add only unique ShareHoldingFlag to an array', () => {
        const shareHoldingFlagArray: IShareHoldingFlag[] = [{ id: 123 }, { id: 456 }, { id: 36909 }];
        const shareHoldingFlagCollection: IShareHoldingFlag[] = [{ id: 123 }];
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing(shareHoldingFlagCollection, ...shareHoldingFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shareHoldingFlag: IShareHoldingFlag = { id: 123 };
        const shareHoldingFlag2: IShareHoldingFlag = { id: 456 };
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing([], shareHoldingFlag, shareHoldingFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shareHoldingFlag);
        expect(expectedResult).toContain(shareHoldingFlag2);
      });

      it('should accept null and undefined values', () => {
        const shareHoldingFlag: IShareHoldingFlag = { id: 123 };
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing([], null, shareHoldingFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shareHoldingFlag);
      });

      it('should return initial array if no ShareHoldingFlag is added', () => {
        const shareHoldingFlagCollection: IShareHoldingFlag[] = [{ id: 123 }];
        expectedResult = service.addShareHoldingFlagToCollectionIfMissing(shareHoldingFlagCollection, undefined, null);
        expect(expectedResult).toEqual(shareHoldingFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
