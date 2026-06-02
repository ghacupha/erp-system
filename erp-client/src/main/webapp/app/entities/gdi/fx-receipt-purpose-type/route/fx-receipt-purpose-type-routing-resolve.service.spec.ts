jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFxReceiptPurposeType, FxReceiptPurposeType } from '../fx-receipt-purpose-type.model';
import { FxReceiptPurposeTypeService } from '../service/fx-receipt-purpose-type.service';

import { FxReceiptPurposeTypeRoutingResolveService } from './fx-receipt-purpose-type-routing-resolve.service';

describe('FxReceiptPurposeType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FxReceiptPurposeTypeRoutingResolveService;
  let service: FxReceiptPurposeTypeService;
  let resultFxReceiptPurposeType: IFxReceiptPurposeType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FxReceiptPurposeTypeRoutingResolveService);
    service = TestBed.inject(FxReceiptPurposeTypeService);
    resultFxReceiptPurposeType = undefined;
  });

  describe('resolve', () => {
    it('should return IFxReceiptPurposeType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxReceiptPurposeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxReceiptPurposeType).toEqual({ id: 123 });
    });

    it('should return new IFxReceiptPurposeType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxReceiptPurposeType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFxReceiptPurposeType).toEqual(new FxReceiptPurposeType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FxReceiptPurposeType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxReceiptPurposeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxReceiptPurposeType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
