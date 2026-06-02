jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISettlement, Settlement } from '../settlement.model';
import { SettlementService } from '../service/settlement.service';

import { SettlementRoutingResolveService } from './settlement-routing-resolve.service';

describe('Settlement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SettlementRoutingResolveService;
  let service: SettlementService;
  let resultSettlement: ISettlement | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SettlementRoutingResolveService);
    service = TestBed.inject(SettlementService);
    resultSettlement = undefined;
  });

  describe('resolve', () => {
    it('should return ISettlement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSettlement).toEqual({ id: 123 });
    });

    it('should return new ISettlement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSettlement).toEqual(new Settlement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Settlement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSettlement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
