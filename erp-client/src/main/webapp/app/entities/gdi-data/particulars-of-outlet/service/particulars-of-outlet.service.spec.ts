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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IParticularsOfOutlet, ParticularsOfOutlet } from '../particulars-of-outlet.model';

import { ParticularsOfOutletService } from './particulars-of-outlet.service';

describe('ParticularsOfOutlet Service', () => {
  let service: ParticularsOfOutletService;
  let httpMock: HttpTestingController;
  let elemDefault: IParticularsOfOutlet;
  let expectedResult: IParticularsOfOutlet | IParticularsOfOutlet[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParticularsOfOutletService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      businessReportingDate: currentDate,
      outletName: 'AAAAAAA',
      town: 'AAAAAAA',
      iso6709Latitute: 0,
      iso6709Longitude: 0,
      cbkApprovalDate: currentDate,
      outletOpeningDate: currentDate,
      outletClosureDate: currentDate,
      licenseFeePayable: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          businessReportingDate: currentDate.format(DATE_FORMAT),
          cbkApprovalDate: currentDate.format(DATE_FORMAT),
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ParticularsOfOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          businessReportingDate: currentDate.format(DATE_FORMAT),
          cbkApprovalDate: currentDate.format(DATE_FORMAT),
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDate: currentDate,
          cbkApprovalDate: currentDate,
          outletOpeningDate: currentDate,
          outletClosureDate: currentDate,
        },
        returnedFromService
      );

      service.create(new ParticularsOfOutlet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ParticularsOfOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessReportingDate: currentDate.format(DATE_FORMAT),
          outletName: 'BBBBBB',
          town: 'BBBBBB',
          iso6709Latitute: 1,
          iso6709Longitude: 1,
          cbkApprovalDate: currentDate.format(DATE_FORMAT),
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          licenseFeePayable: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDate: currentDate,
          cbkApprovalDate: currentDate,
          outletOpeningDate: currentDate,
          outletClosureDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ParticularsOfOutlet', () => {
      const patchObject = Object.assign(
        {
          iso6709Latitute: 1,
          iso6709Longitude: 1,
          cbkApprovalDate: currentDate.format(DATE_FORMAT),
          outletOpeningDate: currentDate.format(DATE_FORMAT),
        },
        new ParticularsOfOutlet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          businessReportingDate: currentDate,
          cbkApprovalDate: currentDate,
          outletOpeningDate: currentDate,
          outletClosureDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ParticularsOfOutlet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessReportingDate: currentDate.format(DATE_FORMAT),
          outletName: 'BBBBBB',
          town: 'BBBBBB',
          iso6709Latitute: 1,
          iso6709Longitude: 1,
          cbkApprovalDate: currentDate.format(DATE_FORMAT),
          outletOpeningDate: currentDate.format(DATE_FORMAT),
          outletClosureDate: currentDate.format(DATE_FORMAT),
          licenseFeePayable: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          businessReportingDate: currentDate,
          cbkApprovalDate: currentDate,
          outletOpeningDate: currentDate,
          outletClosureDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ParticularsOfOutlet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addParticularsOfOutletToCollectionIfMissing', () => {
      it('should add a ParticularsOfOutlet to an empty array', () => {
        const particularsOfOutlet: IParticularsOfOutlet = { id: 123 };
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing([], particularsOfOutlet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(particularsOfOutlet);
      });

      it('should not add a ParticularsOfOutlet to an array that contains it', () => {
        const particularsOfOutlet: IParticularsOfOutlet = { id: 123 };
        const particularsOfOutletCollection: IParticularsOfOutlet[] = [
          {
            ...particularsOfOutlet,
          },
          { id: 456 },
        ];
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing(particularsOfOutletCollection, particularsOfOutlet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ParticularsOfOutlet to an array that doesn't contain it", () => {
        const particularsOfOutlet: IParticularsOfOutlet = { id: 123 };
        const particularsOfOutletCollection: IParticularsOfOutlet[] = [{ id: 456 }];
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing(particularsOfOutletCollection, particularsOfOutlet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(particularsOfOutlet);
      });

      it('should add only unique ParticularsOfOutlet to an array', () => {
        const particularsOfOutletArray: IParticularsOfOutlet[] = [{ id: 123 }, { id: 456 }, { id: 58759 }];
        const particularsOfOutletCollection: IParticularsOfOutlet[] = [{ id: 123 }];
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing(particularsOfOutletCollection, ...particularsOfOutletArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const particularsOfOutlet: IParticularsOfOutlet = { id: 123 };
        const particularsOfOutlet2: IParticularsOfOutlet = { id: 456 };
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing([], particularsOfOutlet, particularsOfOutlet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(particularsOfOutlet);
        expect(expectedResult).toContain(particularsOfOutlet2);
      });

      it('should accept null and undefined values', () => {
        const particularsOfOutlet: IParticularsOfOutlet = { id: 123 };
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing([], null, particularsOfOutlet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(particularsOfOutlet);
      });

      it('should return initial array if no ParticularsOfOutlet is added', () => {
        const particularsOfOutletCollection: IParticularsOfOutlet[] = [{ id: 123 }];
        expectedResult = service.addParticularsOfOutletToCollectionIfMissing(particularsOfOutletCollection, undefined, null);
        expect(expectedResult).toEqual(particularsOfOutletCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
