jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouInitialDirectCost, RouInitialDirectCost } from '../rou-initial-direct-cost.model';
import { RouInitialDirectCostService } from '../service/rou-initial-direct-cost.service';

import { RouInitialDirectCostRoutingResolveService } from './rou-initial-direct-cost-routing-resolve.service';

describe('RouInitialDirectCost routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouInitialDirectCostRoutingResolveService;
  let service: RouInitialDirectCostService;
  let resultRouInitialDirectCost: IRouInitialDirectCost | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouInitialDirectCostRoutingResolveService);
    service = TestBed.inject(RouInitialDirectCostService);
    resultRouInitialDirectCost = undefined;
  });

  describe('resolve', () => {
    it('should return IRouInitialDirectCost returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouInitialDirectCost = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouInitialDirectCost).toEqual({ id: 123 });
    });

    it('should return new IRouInitialDirectCost if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouInitialDirectCost = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouInitialDirectCost).toEqual(new RouInitialDirectCost());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouInitialDirectCost })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouInitialDirectCost = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouInitialDirectCost).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
