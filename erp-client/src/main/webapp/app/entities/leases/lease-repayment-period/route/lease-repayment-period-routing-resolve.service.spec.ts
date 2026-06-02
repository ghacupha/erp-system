jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseRepaymentPeriod, LeaseRepaymentPeriod } from '../lease-repayment-period.model';
import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';

import { LeaseRepaymentPeriodRoutingResolveService } from './lease-repayment-period-routing-resolve.service';

describe('LeaseRepaymentPeriod routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseRepaymentPeriodRoutingResolveService;
  let service: LeaseRepaymentPeriodService;
  let resultLeaseRepaymentPeriod: ILeaseRepaymentPeriod | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseRepaymentPeriodRoutingResolveService);
    service = TestBed.inject(LeaseRepaymentPeriodService);
    resultLeaseRepaymentPeriod = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseRepaymentPeriod returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseRepaymentPeriod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseRepaymentPeriod).toEqual({ id: 123 });
    });

    it('should return new ILeaseRepaymentPeriod if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseRepaymentPeriod = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseRepaymentPeriod).toEqual(new LeaseRepaymentPeriod());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseRepaymentPeriod })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseRepaymentPeriod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseRepaymentPeriod).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
