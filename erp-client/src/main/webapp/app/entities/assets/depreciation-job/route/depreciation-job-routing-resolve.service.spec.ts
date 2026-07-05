jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationJob, DepreciationJob } from '../depreciation-job.model';
import { DepreciationJobService } from '../service/depreciation-job.service';

import { DepreciationJobRoutingResolveService } from './depreciation-job-routing-resolve.service';

describe('DepreciationJob routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationJobRoutingResolveService;
  let service: DepreciationJobService;
  let resultDepreciationJob: IDepreciationJob | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationJobRoutingResolveService);
    service = TestBed.inject(DepreciationJobService);
    resultDepreciationJob = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationJob returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJob = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationJob).toEqual({ id: 123 });
    });

    it('should return new IDepreciationJob if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJob = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationJob).toEqual(new DepreciationJob());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationJob })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJob = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationJob).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
