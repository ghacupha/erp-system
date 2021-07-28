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
import { FixedAssetDepreciationService } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation.service';
import { IFixedAssetDepreciation, FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';
import { DepreciationRegime } from 'app/shared/model/enumerations/depreciation-regime.model';

describe('Service Tests', () => {
  describe('FixedAssetDepreciation Service', () => {
    let injector: TestBed;
    let service: FixedAssetDepreciationService;
    let httpMock: HttpTestingController;
    let elemDefault: IFixedAssetDepreciation;
    let expectedResult: IFixedAssetDepreciation | IFixedAssetDepreciation[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FixedAssetDepreciationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new FixedAssetDepreciation(
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        0,
        DepreciationRegime.STRAIGHT_LINE_BASIS,
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            depreciationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FixedAssetDepreciation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            depreciationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depreciationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new FixedAssetDepreciation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FixedAssetDepreciation', () => {
        const returnedFromService = Object.assign(
          {
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            depreciationDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            depreciationAmount: 1,
            depreciationRegime: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depreciationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FixedAssetDepreciation', () => {
        const returnedFromService = Object.assign(
          {
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            depreciationDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            depreciationAmount: 1,
            depreciationRegime: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            depreciationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FixedAssetDepreciation', () => {
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
