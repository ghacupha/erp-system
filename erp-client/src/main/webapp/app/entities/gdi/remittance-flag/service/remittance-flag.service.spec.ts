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

import { RemittanceTypeFlag } from 'app/entities/enumerations/remittance-type-flag.model';
import { RemittanceType } from 'app/entities/enumerations/remittance-type.model';
import { IRemittanceFlag, RemittanceFlag } from '../remittance-flag.model';

import { RemittanceFlagService } from './remittance-flag.service';

describe('RemittanceFlag Service', () => {
  let service: RemittanceFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: IRemittanceFlag;
  let expectedResult: IRemittanceFlag | IRemittanceFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RemittanceFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      remittanceTypeFlag: RemittanceTypeFlag.RMTIN,
      remittanceType: RemittanceType.INFLOWS,
      remittanceTypeDetails: 'AAAAAAA',
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

    it('should create a RemittanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RemittanceFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RemittanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          remittanceTypeFlag: 'BBBBBB',
          remittanceType: 'BBBBBB',
          remittanceTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RemittanceFlag', () => {
      const patchObject = Object.assign(
        {
          remittanceTypeDetails: 'BBBBBB',
        },
        new RemittanceFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RemittanceFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          remittanceTypeFlag: 'BBBBBB',
          remittanceType: 'BBBBBB',
          remittanceTypeDetails: 'BBBBBB',
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

    it('should delete a RemittanceFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRemittanceFlagToCollectionIfMissing', () => {
      it('should add a RemittanceFlag to an empty array', () => {
        const remittanceFlag: IRemittanceFlag = { id: 123 };
        expectedResult = service.addRemittanceFlagToCollectionIfMissing([], remittanceFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(remittanceFlag);
      });

      it('should not add a RemittanceFlag to an array that contains it', () => {
        const remittanceFlag: IRemittanceFlag = { id: 123 };
        const remittanceFlagCollection: IRemittanceFlag[] = [
          {
            ...remittanceFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addRemittanceFlagToCollectionIfMissing(remittanceFlagCollection, remittanceFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RemittanceFlag to an array that doesn't contain it", () => {
        const remittanceFlag: IRemittanceFlag = { id: 123 };
        const remittanceFlagCollection: IRemittanceFlag[] = [{ id: 456 }];
        expectedResult = service.addRemittanceFlagToCollectionIfMissing(remittanceFlagCollection, remittanceFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(remittanceFlag);
      });

      it('should add only unique RemittanceFlag to an array', () => {
        const remittanceFlagArray: IRemittanceFlag[] = [{ id: 123 }, { id: 456 }, { id: 44166 }];
        const remittanceFlagCollection: IRemittanceFlag[] = [{ id: 123 }];
        expectedResult = service.addRemittanceFlagToCollectionIfMissing(remittanceFlagCollection, ...remittanceFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const remittanceFlag: IRemittanceFlag = { id: 123 };
        const remittanceFlag2: IRemittanceFlag = { id: 456 };
        expectedResult = service.addRemittanceFlagToCollectionIfMissing([], remittanceFlag, remittanceFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(remittanceFlag);
        expect(expectedResult).toContain(remittanceFlag2);
      });

      it('should accept null and undefined values', () => {
        const remittanceFlag: IRemittanceFlag = { id: 123 };
        expectedResult = service.addRemittanceFlagToCollectionIfMissing([], null, remittanceFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(remittanceFlag);
      });

      it('should return initial array if no RemittanceFlag is added', () => {
        const remittanceFlagCollection: IRemittanceFlag[] = [{ id: 123 }];
        expectedResult = service.addRemittanceFlagToCollectionIfMissing(remittanceFlagCollection, undefined, null);
        expect(expectedResult).toEqual(remittanceFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
