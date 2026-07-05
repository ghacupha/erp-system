jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouDepreciationRequest, RouDepreciationRequest } from '../rou-depreciation-request.model';
import { RouDepreciationRequestService } from '../service/rou-depreciation-request.service';

import { RouDepreciationRequestRoutingResolveService } from './rou-depreciation-request-routing-resolve.service';

describe('RouDepreciationRequest routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouDepreciationRequestRoutingResolveService;
  let service: RouDepreciationRequestService;
  let resultRouDepreciationRequest: IRouDepreciationRequest | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouDepreciationRequestRoutingResolveService);
    service = TestBed.inject(RouDepreciationRequestService);
    resultRouDepreciationRequest = undefined;
  });

  describe('resolve', () => {
    it('should return IRouDepreciationRequest returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationRequest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationRequest).toEqual({ id: 123 });
    });

    it('should return new IRouDepreciationRequest if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationRequest = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouDepreciationRequest).toEqual(new RouDepreciationRequest());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouDepreciationRequest })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationRequest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationRequest).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
