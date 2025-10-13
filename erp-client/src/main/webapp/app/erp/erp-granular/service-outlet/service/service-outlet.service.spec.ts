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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IServiceOutlet, ServiceOutlet } from '../service-outlet.model';

import { ServiceOutletService } from './service-outlet.service';

describe('ServiceOutlet Service', () => {
  let service: ServiceOutletService;
  let httpMock: HttpTestingController;
  let elemDefault: IServiceOutlet;
  let expectedResult: IServiceOutlet | IServiceOutlet[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceOutletService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      outletCode: 'AAAAAAA',
      outletName: 'AAAAAAA',
      town: 'AAAAAAA',
      parliamentaryConstituency: 'AAAAAAA',
      gpsCoordinates: 'AAAAAAA',
      outletOpeningDate: currentDate,
      regulatorApprovalDate: currentDate,
      outletClosureDate: currentDate,
      dateLastModified: currentDate,
      licenseFeePayable: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          regulatorApprovalDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          dateLastModified: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ServiceOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          regulatorApprovalDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          dateLastModified: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          outletOpeningDate: currentDate,
          regulatorApprovalDate: currentDate,
          outletClosureDate: currentDate,
          dateLastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new ServiceOutlet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          outletCode: 'BBBBBB',
          outletName: 'BBBBBB',
          town: 'BBBBBB',
          parliamentaryConstituency: 'BBBBBB',
          gpsCoordinates: 'BBBBBB',
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          regulatorApprovalDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          dateLastModified: currentDate.format(DATE_FORMAT),
          licenseFeePayable: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          outletOpeningDate: currentDate,
          regulatorApprovalDate: currentDate,
          outletClosureDate: currentDate,
          dateLastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceOutlet', () => {
      const patchObject = Object.assign(
        {
          town: 'BBBBBB',
          parliamentaryConstituency: 'BBBBBB',
          gpsCoordinates: 'BBBBBB',
          regulatorApprovalDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          dateLastModified: currentDate.format(DATE_FORMAT),
          licenseFeePayable: 1,
        },
        new ServiceOutlet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          outletOpeningDate: currentDate,
          regulatorApprovalDate: currentDate,
          outletClosureDate: currentDate,
          dateLastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          outletCode: 'BBBBBB',
          outletName: 'BBBBBB',
          town: 'BBBBBB',
          parliamentaryConstituency: 'BBBBBB',
          gpsCoordinates: 'BBBBBB',
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          regulatorApprovalDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          dateLastModified: currentDate.format(DATE_FORMAT),
          licenseFeePayable: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          outletOpeningDate: currentDate,
          regulatorApprovalDate: currentDate,
          outletClosureDate: currentDate,
          dateLastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ServiceOutlet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addServiceOutletToCollectionIfMissing', () => {
      it('should add a ServiceOutlet to an empty array', () => {
        const serviceOutlet: IServiceOutlet = { id: 123 };
        expectedResult = service.addServiceOutletToCollectionIfMissing([], serviceOutlet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOutlet);
      });

      it('should not add a ServiceOutlet to an array that contains it', () => {
        const serviceOutlet: IServiceOutlet = { id: 123 };
        const serviceOutletCollection: IServiceOutlet[] = [
          {
            ...serviceOutlet,
          },
          { id: 456 },
        ];
        expectedResult = service.addServiceOutletToCollectionIfMissing(serviceOutletCollection, serviceOutlet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceOutlet to an array that doesn't contain it", () => {
        const serviceOutlet: IServiceOutlet = { id: 123 };
        const serviceOutletCollection: IServiceOutlet[] = [{ id: 456 }];
        expectedResult = service.addServiceOutletToCollectionIfMissing(serviceOutletCollection, serviceOutlet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOutlet);
      });

      it('should add only unique ServiceOutlet to an array', () => {
        const serviceOutletArray: IServiceOutlet[] = [{ id: 123 }, { id: 456 }, { id: 90052 }];
        const serviceOutletCollection: IServiceOutlet[] = [{ id: 123 }];
        expectedResult = service.addServiceOutletToCollectionIfMissing(serviceOutletCollection, ...serviceOutletArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceOutlet: IServiceOutlet = { id: 123 };
        const serviceOutlet2: IServiceOutlet = { id: 456 };
        expectedResult = service.addServiceOutletToCollectionIfMissing([], serviceOutlet, serviceOutlet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceOutlet);
        expect(expectedResult).toContain(serviceOutlet2);
      });

      it('should accept null and undefined values', () => {
        const serviceOutlet: IServiceOutlet = { id: 123 };
        expectedResult = service.addServiceOutletToCollectionIfMissing([], null, serviceOutlet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceOutlet);
      });

      it('should return initial array if no ServiceOutlet is added', () => {
        const serviceOutletCollection: IServiceOutlet[] = [{ id: 123 }];
        expectedResult = service.addServiceOutletToCollectionIfMissing(serviceOutletCollection, undefined, null);
        expect(expectedResult).toEqual(serviceOutletCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
