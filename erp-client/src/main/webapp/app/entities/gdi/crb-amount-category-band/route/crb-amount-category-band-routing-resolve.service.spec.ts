jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbAmountCategoryBand, CrbAmountCategoryBand } from '../crb-amount-category-band.model';
import { CrbAmountCategoryBandService } from '../service/crb-amount-category-band.service';

import { CrbAmountCategoryBandRoutingResolveService } from './crb-amount-category-band-routing-resolve.service';

describe('CrbAmountCategoryBand routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbAmountCategoryBandRoutingResolveService;
  let service: CrbAmountCategoryBandService;
  let resultCrbAmountCategoryBand: ICrbAmountCategoryBand | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbAmountCategoryBandRoutingResolveService);
    service = TestBed.inject(CrbAmountCategoryBandService);
    resultCrbAmountCategoryBand = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbAmountCategoryBand returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAmountCategoryBand = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAmountCategoryBand).toEqual({ id: 123 });
    });

    it('should return new ICrbAmountCategoryBand if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAmountCategoryBand = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbAmountCategoryBand).toEqual(new CrbAmountCategoryBand());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbAmountCategoryBand })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAmountCategoryBand = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAmountCategoryBand).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
