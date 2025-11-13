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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILeaseLiabilityCompilation, LeaseLiabilityCompilation } from '../lease-liability-compilation.model';

import { LeaseLiabilityCompilationService } from './lease-liability-compilation.service';

describe('LeaseLiabilityCompilation Service', () => {
  let service: LeaseLiabilityCompilationService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseLiabilityCompilation;
  let expectedResult: ILeaseLiabilityCompilation | ILeaseLiabilityCompilation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseLiabilityCompilationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      requestId: 'AAAAAAA',
      timeOfRequest: currentDate,
      active: true,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LeaseLiabilityCompilation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.create(new LeaseLiabilityCompilation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseLiabilityCompilation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseLiabilityCompilation', () => {
      const patchObject = Object.assign({}, new LeaseLiabilityCompilation());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaseLiabilityCompilation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestId: 'BBBBBB',
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
          active: false,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should activate a LeaseLiabilityCompilation', () => {
      const returnedFromService = Object.assign(
        {
          active: true,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.activate(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne(`${service['resourceUrl']}/123/activate`);
      expect(req.request.method).toBe('POST');
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should deactivate a LeaseLiabilityCompilation', () => {
      const returnedFromService = Object.assign(
        {
          active: false,
          timeOfRequest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
        },
        returnedFromService
      );

      service.deactivate(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne(`${service['resourceUrl']}/123/deactivate`);
      expect(req.request.method).toBe('POST');
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should delete a LeaseLiabilityCompilation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseLiabilityCompilationToCollectionIfMissing', () => {
      it('should add a LeaseLiabilityCompilation to an empty array', () => {
        const leaseLiabilityCompilation: ILeaseLiabilityCompilation = { id: 123 };
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing([], leaseLiabilityCompilation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityCompilation);
      });

      it('should not add a LeaseLiabilityCompilation to an array that contains it', () => {
        const leaseLiabilityCompilation: ILeaseLiabilityCompilation = { id: 123 };
        const leaseLiabilityCompilationCollection: ILeaseLiabilityCompilation[] = [
          {
            ...leaseLiabilityCompilation,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing(
          leaseLiabilityCompilationCollection,
          leaseLiabilityCompilation
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseLiabilityCompilation to an array that doesn't contain it", () => {
        const leaseLiabilityCompilation: ILeaseLiabilityCompilation = { id: 123 };
        const leaseLiabilityCompilationCollection: ILeaseLiabilityCompilation[] = [{ id: 456 }];
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing(
          leaseLiabilityCompilationCollection,
          leaseLiabilityCompilation
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityCompilation);
      });

      it('should add only unique LeaseLiabilityCompilation to an array', () => {
        const leaseLiabilityCompilationArray: ILeaseLiabilityCompilation[] = [{ id: 123 }, { id: 456 }, { id: 33774 }];
        const leaseLiabilityCompilationCollection: ILeaseLiabilityCompilation[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing(
          leaseLiabilityCompilationCollection,
          ...leaseLiabilityCompilationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseLiabilityCompilation: ILeaseLiabilityCompilation = { id: 123 };
        const leaseLiabilityCompilation2: ILeaseLiabilityCompilation = { id: 456 };
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing(
          [],
          leaseLiabilityCompilation,
          leaseLiabilityCompilation2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseLiabilityCompilation);
        expect(expectedResult).toContain(leaseLiabilityCompilation2);
      });

      it('should accept null and undefined values', () => {
        const leaseLiabilityCompilation: ILeaseLiabilityCompilation = { id: 123 };
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing([], null, leaseLiabilityCompilation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseLiabilityCompilation);
      });

      it('should return initial array if no LeaseLiabilityCompilation is added', () => {
        const leaseLiabilityCompilationCollection: ILeaseLiabilityCompilation[] = [{ id: 123 }];
        expectedResult = service.addLeaseLiabilityCompilationToCollectionIfMissing(leaseLiabilityCompilationCollection, undefined, null);
        expect(expectedResult).toEqual(leaseLiabilityCompilationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
