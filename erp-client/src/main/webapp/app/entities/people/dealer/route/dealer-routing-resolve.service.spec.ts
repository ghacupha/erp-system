jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDealer, Dealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';

import { DealerRoutingResolveService } from './dealer-routing-resolve.service';

describe('Dealer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DealerRoutingResolveService;
  let service: DealerService;
  let resultDealer: IDealer | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DealerRoutingResolveService);
    service = TestBed.inject(DealerService);
    resultDealer = undefined;
  });

  describe('resolve', () => {
    it('should return IDealer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDealer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDealer).toEqual({ id: 123 });
    });

    it('should return new IDealer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDealer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDealer).toEqual(new Dealer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Dealer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDealer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDealer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
