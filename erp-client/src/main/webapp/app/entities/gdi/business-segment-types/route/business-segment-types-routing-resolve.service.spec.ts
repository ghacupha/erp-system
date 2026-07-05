jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBusinessSegmentTypes, BusinessSegmentTypes } from '../business-segment-types.model';
import { BusinessSegmentTypesService } from '../service/business-segment-types.service';

import { BusinessSegmentTypesRoutingResolveService } from './business-segment-types-routing-resolve.service';

describe('BusinessSegmentTypes routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BusinessSegmentTypesRoutingResolveService;
  let service: BusinessSegmentTypesService;
  let resultBusinessSegmentTypes: IBusinessSegmentTypes | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BusinessSegmentTypesRoutingResolveService);
    service = TestBed.inject(BusinessSegmentTypesService);
    resultBusinessSegmentTypes = undefined;
  });

  describe('resolve', () => {
    it('should return IBusinessSegmentTypes returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusinessSegmentTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusinessSegmentTypes).toEqual({ id: 123 });
    });

    it('should return new IBusinessSegmentTypes if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusinessSegmentTypes = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBusinessSegmentTypes).toEqual(new BusinessSegmentTypes());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BusinessSegmentTypes })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusinessSegmentTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusinessSegmentTypes).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
