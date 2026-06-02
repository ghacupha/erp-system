jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITAAmortizationRule, TAAmortizationRule } from '../ta-amortization-rule.model';
import { TAAmortizationRuleService } from '../service/ta-amortization-rule.service';

import { TAAmortizationRuleRoutingResolveService } from './ta-amortization-rule-routing-resolve.service';

describe('TAAmortizationRule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TAAmortizationRuleRoutingResolveService;
  let service: TAAmortizationRuleService;
  let resultTAAmortizationRule: ITAAmortizationRule | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TAAmortizationRuleRoutingResolveService);
    service = TestBed.inject(TAAmortizationRuleService);
    resultTAAmortizationRule = undefined;
  });

  describe('resolve', () => {
    it('should return ITAAmortizationRule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTAAmortizationRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTAAmortizationRule).toEqual({ id: 123 });
    });

    it('should return new ITAAmortizationRule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTAAmortizationRule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTAAmortizationRule).toEqual(new TAAmortizationRule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TAAmortizationRule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTAAmortizationRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTAAmortizationRule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
