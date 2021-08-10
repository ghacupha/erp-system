import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { DepreciationRegime } from 'app/entities/enumerations/depreciation-regime.model';
import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';

import { FixedAssetNetBookValueService } from './fixed-asset-net-book-value.service';

describe('Service Tests', () => {
  describe('FixedAssetNetBookValue Service', () => {
    let service: FixedAssetNetBookValueService;
    let httpMock: HttpTestingController;
    let elemDefault: IFixedAssetNetBookValue;
    let expectedResult: IFixedAssetNetBookValue | IFixedAssetNetBookValue[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FixedAssetNetBookValueService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        assetNumber: 0,
        serviceOutletCode: 'AAAAAAA',
        assetTag: 'AAAAAAA',
        assetDescription: 'AAAAAAA',
        netBookValueDate: currentDate,
        assetCategory: 'AAAAAAA',
        netBookValue: 0,
        depreciationRegime: DepreciationRegime.STRAIGHT_LINE_BASIS,
        fileUploadToken: 'AAAAAAA',
        compilationToken: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            netBookValueDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FixedAssetNetBookValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            netBookValueDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            netBookValueDate: currentDate,
          },
          returnedFromService
        );

        service.create(new FixedAssetNetBookValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FixedAssetNetBookValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            netBookValueDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            netBookValue: 1,
            depreciationRegime: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            netBookValueDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FixedAssetNetBookValue', () => {
        const patchObject = Object.assign(
          {
            assetTag: 'BBBBBB',
            netBookValue: 1,
            depreciationRegime: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          new FixedAssetNetBookValue()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            netBookValueDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FixedAssetNetBookValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            assetNumber: 1,
            serviceOutletCode: 'BBBBBB',
            assetTag: 'BBBBBB',
            assetDescription: 'BBBBBB',
            netBookValueDate: currentDate.format(DATE_FORMAT),
            assetCategory: 'BBBBBB',
            netBookValue: 1,
            depreciationRegime: 'BBBBBB',
            fileUploadToken: 'BBBBBB',
            compilationToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            netBookValueDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FixedAssetNetBookValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFixedAssetNetBookValueToCollectionIfMissing', () => {
        it('should add a FixedAssetNetBookValue to an empty array', () => {
          const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 123 };
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing([], fixedAssetNetBookValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fixedAssetNetBookValue);
        });

        it('should not add a FixedAssetNetBookValue to an array that contains it', () => {
          const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 123 };
          const fixedAssetNetBookValueCollection: IFixedAssetNetBookValue[] = [
            {
              ...fixedAssetNetBookValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing(fixedAssetNetBookValueCollection, fixedAssetNetBookValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FixedAssetNetBookValue to an array that doesn't contain it", () => {
          const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 123 };
          const fixedAssetNetBookValueCollection: IFixedAssetNetBookValue[] = [{ id: 456 }];
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing(fixedAssetNetBookValueCollection, fixedAssetNetBookValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fixedAssetNetBookValue);
        });

        it('should add only unique FixedAssetNetBookValue to an array', () => {
          const fixedAssetNetBookValueArray: IFixedAssetNetBookValue[] = [{ id: 123 }, { id: 456 }, { id: 26881 }];
          const fixedAssetNetBookValueCollection: IFixedAssetNetBookValue[] = [{ id: 123 }];
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing(
            fixedAssetNetBookValueCollection,
            ...fixedAssetNetBookValueArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 123 };
          const fixedAssetNetBookValue2: IFixedAssetNetBookValue = { id: 456 };
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing([], fixedAssetNetBookValue, fixedAssetNetBookValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fixedAssetNetBookValue);
          expect(expectedResult).toContain(fixedAssetNetBookValue2);
        });

        it('should accept null and undefined values', () => {
          const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 123 };
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing([], null, fixedAssetNetBookValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fixedAssetNetBookValue);
        });

        it('should return initial array if no FixedAssetNetBookValue is added', () => {
          const fixedAssetNetBookValueCollection: IFixedAssetNetBookValue[] = [{ id: 123 }];
          expectedResult = service.addFixedAssetNetBookValueToCollectionIfMissing(fixedAssetNetBookValueCollection, undefined, null);
          expect(expectedResult).toEqual(fixedAssetNetBookValueCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
