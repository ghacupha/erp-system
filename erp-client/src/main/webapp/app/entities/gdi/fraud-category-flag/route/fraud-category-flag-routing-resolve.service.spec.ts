jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFraudCategoryFlag, FraudCategoryFlag } from '../fraud-category-flag.model';
import { FraudCategoryFlagService } from '../service/fraud-category-flag.service';

import { FraudCategoryFlagRoutingResolveService } from './fraud-category-flag-routing-resolve.service';

describe('FraudCategoryFlag routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FraudCategoryFlagRoutingResolveService;
  let service: FraudCategoryFlagService;
  let resultFraudCategoryFlag: IFraudCategoryFlag | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FraudCategoryFlagRoutingResolveService);
    service = TestBed.inject(FraudCategoryFlagService);
    resultFraudCategoryFlag = undefined;
  });

  describe('resolve', () => {
    it('should return IFraudCategoryFlag returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudCategoryFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFraudCategoryFlag).toEqual({ id: 123 });
    });

    it('should return new IFraudCategoryFlag if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudCategoryFlag = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFraudCategoryFlag).toEqual(new FraudCategoryFlag());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FraudCategoryFlag })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudCategoryFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFraudCategoryFlag).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
