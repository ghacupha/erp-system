import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFixedAssetAcquisition, FixedAssetAcquisition } from '../fixed-asset-acquisition.model';

import { FixedAssetAcquisitionService } from './fixed-asset-acquisition.service';

describe('FixedAssetAcquisition Service', () => {
  let service: FixedAssetAcquisitionService;
  let httpMock: HttpTestingController;
  let elemDefault: IFixedAssetAcquisition;
  let expectedResult: IFixedAssetAcquisition | IFixedAssetAcquisition[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FixedAssetAcquisitionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetNumber: 0,
      serviceOutletCode: 'AAAAAAA',
      assetTag: 'AAAAAAA',
      assetDescription: 'AAAAAAA',
      purchaseDate: currentDate,
      assetCategory: 'AAAAAAA',
      purchasePrice: 0,
      fileUploadToken: 'AAAAAAA',
    };
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
          id: 1,
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

    it('should partial update a FixedAssetAcquisition', () => {
      const patchObject = Object.assign(
        {
          assetNumber: 1,
          assetTag: 'BBBBBB',
          assetDescription: 'BBBBBB',
          purchasePrice: 1,
          fileUploadToken: 'BBBBBB',
        },
        new FixedAssetAcquisition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          purchaseDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FixedAssetAcquisition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    describe('addFixedAssetAcquisitionToCollectionIfMissing', () => {
      it('should add a FixedAssetAcquisition to an empty array', () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 123 };
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing([], fixedAssetAcquisition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fixedAssetAcquisition);
      });

      it('should not add a FixedAssetAcquisition to an array that contains it', () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 123 };
        const fixedAssetAcquisitionCollection: IFixedAssetAcquisition[] = [
          {
            ...fixedAssetAcquisition,
          },
          { id: 456 },
        ];
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing(fixedAssetAcquisitionCollection, fixedAssetAcquisition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FixedAssetAcquisition to an array that doesn't contain it", () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 123 };
        const fixedAssetAcquisitionCollection: IFixedAssetAcquisition[] = [{ id: 456 }];
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing(fixedAssetAcquisitionCollection, fixedAssetAcquisition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fixedAssetAcquisition);
      });

      it('should add only unique FixedAssetAcquisition to an array', () => {
        const fixedAssetAcquisitionArray: IFixedAssetAcquisition[] = [{ id: 123 }, { id: 456 }, { id: 74355 }];
        const fixedAssetAcquisitionCollection: IFixedAssetAcquisition[] = [{ id: 123 }];
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing(
          fixedAssetAcquisitionCollection,
          ...fixedAssetAcquisitionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 123 };
        const fixedAssetAcquisition2: IFixedAssetAcquisition = { id: 456 };
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing([], fixedAssetAcquisition, fixedAssetAcquisition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fixedAssetAcquisition);
        expect(expectedResult).toContain(fixedAssetAcquisition2);
      });

      it('should accept null and undefined values', () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 123 };
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing([], null, fixedAssetAcquisition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fixedAssetAcquisition);
      });

      it('should return initial array if no FixedAssetAcquisition is added', () => {
        const fixedAssetAcquisitionCollection: IFixedAssetAcquisition[] = [{ id: 123 }];
        expectedResult = service.addFixedAssetAcquisitionToCollectionIfMissing(fixedAssetAcquisitionCollection, undefined, null);
        expect(expectedResult).toEqual(fixedAssetAcquisitionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
