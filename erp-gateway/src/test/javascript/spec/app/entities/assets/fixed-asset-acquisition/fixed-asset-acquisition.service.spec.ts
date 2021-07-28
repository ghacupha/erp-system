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
