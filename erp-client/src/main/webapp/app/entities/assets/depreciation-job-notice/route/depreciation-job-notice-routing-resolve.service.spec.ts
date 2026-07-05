jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationJobNotice, DepreciationJobNotice } from '../depreciation-job-notice.model';
import { DepreciationJobNoticeService } from '../service/depreciation-job-notice.service';

import { DepreciationJobNoticeRoutingResolveService } from './depreciation-job-notice-routing-resolve.service';

describe('DepreciationJobNotice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationJobNoticeRoutingResolveService;
  let service: DepreciationJobNoticeService;
  let resultDepreciationJobNotice: IDepreciationJobNotice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationJobNoticeRoutingResolveService);
    service = TestBed.inject(DepreciationJobNoticeService);
    resultDepreciationJobNotice = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationJobNotice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJobNotice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationJobNotice).toEqual({ id: 123 });
    });

    it('should return new IDepreciationJobNotice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJobNotice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationJobNotice).toEqual(new DepreciationJobNotice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationJobNotice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationJobNotice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationJobNotice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
