jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITALeaseInterestAccrualRule, TALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';
import { TALeaseInterestAccrualRuleService } from '../service/ta-lease-interest-accrual-rule.service';

import { TALeaseInterestAccrualRuleRoutingResolveService } from './ta-lease-interest-accrual-rule-routing-resolve.service';

describe('TALeaseInterestAccrualRule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TALeaseInterestAccrualRuleRoutingResolveService;
  let service: TALeaseInterestAccrualRuleService;
  let resultTALeaseInterestAccrualRule: ITALeaseInterestAccrualRule | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TALeaseInterestAccrualRuleRoutingResolveService);
    service = TestBed.inject(TALeaseInterestAccrualRuleService);
    resultTALeaseInterestAccrualRule = undefined;
  });

  describe('resolve', () => {
    it('should return ITALeaseInterestAccrualRule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTALeaseInterestAccrualRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTALeaseInterestAccrualRule).toEqual({ id: 123 });
    });

    it('should return new ITALeaseInterestAccrualRule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTALeaseInterestAccrualRule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTALeaseInterestAccrualRule).toEqual(new TALeaseInterestAccrualRule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TALeaseInterestAccrualRule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTALeaseInterestAccrualRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTALeaseInterestAccrualRule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
