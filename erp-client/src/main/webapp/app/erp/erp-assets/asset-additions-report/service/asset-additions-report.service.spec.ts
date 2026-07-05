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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAssetAdditionsReport, AssetAdditionsReport } from '../asset-additions-report.model';

import { AssetAdditionsReportService } from './asset-additions-report.service';

describe('AssetAdditionsReport Service', () => {
  let service: AssetAdditionsReportService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetAdditionsReport;
  let expectedResult: IAssetAdditionsReport | IAssetAdditionsReport[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetAdditionsReportService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      timeOfRequest: currentDate,
      reportStartDate: currentDate,
      reportEndDate: currentDate,
      requestId: 'AAAAAAA',
      tampered: false,
      reportParameters: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_FORMAT),
          reportStartDate: currentDate.format(DATE_FORMAT),
          reportEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetAdditionsReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeOfRequest: currentDate.format(DATE_FORMAT),
          reportStartDate: currentDate.format(DATE_FORMAT),
          reportEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          reportStartDate: currentDate,
          reportEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetAdditionsReport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetAdditionsReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_FORMAT),
          reportStartDate: currentDate.format(DATE_FORMAT),
          reportEndDate: currentDate.format(DATE_FORMAT),
          requestId: 'BBBBBB',
          tampered: true,
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          reportStartDate: currentDate,
          reportEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetAdditionsReport', () => {
      const patchObject = Object.assign(
        {
          timeOfRequest: currentDate.format(DATE_FORMAT),
          reportStartDate: currentDate.format(DATE_FORMAT),
          reportEndDate: currentDate.format(DATE_FORMAT),
          reportParameters: 'BBBBBB',
        },
        new AssetAdditionsReport()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          reportStartDate: currentDate,
          reportEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetAdditionsReport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          timeOfRequest: currentDate.format(DATE_FORMAT),
          reportStartDate: currentDate.format(DATE_FORMAT),
          reportEndDate: currentDate.format(DATE_FORMAT),
          requestId: 'BBBBBB',
          tampered: true,
          reportParameters: 'BBBBBB',
          reportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeOfRequest: currentDate,
          reportStartDate: currentDate,
          reportEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetAdditionsReport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetAdditionsReportToCollectionIfMissing', () => {
      it('should add a AssetAdditionsReport to an empty array', () => {
        const assetAdditionsReport: IAssetAdditionsReport = { id: 123 };
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing([], assetAdditionsReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAdditionsReport);
      });

      it('should not add a AssetAdditionsReport to an array that contains it', () => {
        const assetAdditionsReport: IAssetAdditionsReport = { id: 123 };
        const assetAdditionsReportCollection: IAssetAdditionsReport[] = [
          {
            ...assetAdditionsReport,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing(assetAdditionsReportCollection, assetAdditionsReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetAdditionsReport to an array that doesn't contain it", () => {
        const assetAdditionsReport: IAssetAdditionsReport = { id: 123 };
        const assetAdditionsReportCollection: IAssetAdditionsReport[] = [{ id: 456 }];
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing(assetAdditionsReportCollection, assetAdditionsReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAdditionsReport);
      });

      it('should add only unique AssetAdditionsReport to an array', () => {
        const assetAdditionsReportArray: IAssetAdditionsReport[] = [{ id: 123 }, { id: 456 }, { id: 50883 }];
        const assetAdditionsReportCollection: IAssetAdditionsReport[] = [{ id: 123 }];
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing(assetAdditionsReportCollection, ...assetAdditionsReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetAdditionsReport: IAssetAdditionsReport = { id: 123 };
        const assetAdditionsReport2: IAssetAdditionsReport = { id: 456 };
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing([], assetAdditionsReport, assetAdditionsReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetAdditionsReport);
        expect(expectedResult).toContain(assetAdditionsReport2);
      });

      it('should accept null and undefined values', () => {
        const assetAdditionsReport: IAssetAdditionsReport = { id: 123 };
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing([], null, assetAdditionsReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetAdditionsReport);
      });

      it('should return initial array if no AssetAdditionsReport is added', () => {
        const assetAdditionsReportCollection: IAssetAdditionsReport[] = [{ id: 123 }];
        expectedResult = service.addAssetAdditionsReportToCollectionIfMissing(assetAdditionsReportCollection, undefined, null);
        expect(expectedResult).toEqual(assetAdditionsReportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
