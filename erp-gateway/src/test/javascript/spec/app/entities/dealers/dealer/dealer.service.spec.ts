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
import { DealerService } from 'app/entities/dealers/dealer/dealer.service';
import { IDealer, Dealer } from 'app/shared/model/dealers/dealer.model';

describe('Service Tests', () => {
  describe('Dealer Service', () => {
    let injector: TestBed;
    let service: DealerService;
    let httpMock: HttpTestingController;
    let elemDefault: IDealer;
    let expectedResult: IDealer | IDealer[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DealerService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Dealer(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Dealer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dealer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dealer', () => {
        const returnedFromService = Object.assign(
          {
            dealerName: 'BBBBBB',
            taxNumber: 'BBBBBB',
            postalAddress: 'BBBBBB',
            physicalAddress: 'BBBBBB',
            accountName: 'BBBBBB',
            accountNumber: 'BBBBBB',
            bankersName: 'BBBBBB',
            bankersBranch: 'BBBBBB',
            bankersSwiftCode: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dealer', () => {
        const returnedFromService = Object.assign(
          {
            dealerName: 'BBBBBB',
            taxNumber: 'BBBBBB',
            postalAddress: 'BBBBBB',
            physicalAddress: 'BBBBBB',
            accountName: 'BBBBBB',
            accountNumber: 'BBBBBB',
            bankersName: 'BBBBBB',
            bankersBranch: 'BBBBBB',
            bankersSwiftCode: 'BBBBBB',
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

      it('should delete a Dealer', () => {
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
