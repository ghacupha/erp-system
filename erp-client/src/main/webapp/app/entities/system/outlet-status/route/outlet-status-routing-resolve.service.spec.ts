jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOutletStatus, OutletStatus } from '../outlet-status.model';
import { OutletStatusService } from '../service/outlet-status.service';

import { OutletStatusRoutingResolveService } from './outlet-status-routing-resolve.service';

describe('OutletStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OutletStatusRoutingResolveService;
  let service: OutletStatusService;
  let resultOutletStatus: IOutletStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OutletStatusRoutingResolveService);
    service = TestBed.inject(OutletStatusService);
    resultOutletStatus = undefined;
  });

  describe('resolve', () => {
    it('should return IOutletStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOutletStatus).toEqual({ id: 123 });
    });

    it('should return new IOutletStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOutletStatus).toEqual(new OutletStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OutletStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOutletStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
