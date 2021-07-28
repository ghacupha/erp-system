///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { FixedAssetAcquisitionService } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition.service';
import { IFixedAssetAcquisition, FixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';

describe('Service Tests', () => {
  describe('FixedAssetAcquisition Service', () => {
    let injector: TestBed;
    let service: FixedAssetAcquisitionService;
    let httpMock: HttpTestingController;
    let elemDefault: IFixedAssetAcquisition;
    let expectedResult: IFixedAssetAcquisition | IFixedAssetAcquisition[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FixedAssetAcquisitionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new FixedAssetAcquisition(0, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            purchaseDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FixedAssetAcquisition', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            purchaseDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchaseDate: currentDate,
          },
          returnedFromService
        );

        service.create(new FixedAssetAcquisition()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FixedAssetAcquisition', () => {
        const returnedFromService = Object.assign(
          {
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            purchaseDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            purchasePrice: 1,
            fileUploadToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchaseDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FixedAssetAcquisition', () => {
        const returnedFromService = Object.assign(
          {
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            purchaseDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            purchasePrice: 1,
            fileUploadToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchaseDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FixedAssetAcquisition', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
