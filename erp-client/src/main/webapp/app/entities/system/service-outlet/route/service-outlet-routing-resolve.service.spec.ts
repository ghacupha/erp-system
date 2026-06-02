jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IServiceOutlet, ServiceOutlet } from '../service-outlet.model';
import { ServiceOutletService } from '../service/service-outlet.service';

import { ServiceOutletRoutingResolveService } from './service-outlet-routing-resolve.service';

describe('ServiceOutlet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ServiceOutletRoutingResolveService;
  let service: ServiceOutletService;
  let resultServiceOutlet: IServiceOutlet | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ServiceOutletRoutingResolveService);
    service = TestBed.inject(ServiceOutletService);
    resultServiceOutlet = undefined;
  });

  describe('resolve', () => {
    it('should return IServiceOutlet returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultServiceOutlet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultServiceOutlet).toEqual({ id: 123 });
    });

    it('should return new IServiceOutlet if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultServiceOutlet = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultServiceOutlet).toEqual(new ServiceOutlet());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ServiceOutlet })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultServiceOutlet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultServiceOutlet).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
