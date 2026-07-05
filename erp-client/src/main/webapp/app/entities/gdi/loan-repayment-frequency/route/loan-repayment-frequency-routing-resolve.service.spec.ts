jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILoanRepaymentFrequency, LoanRepaymentFrequency } from '../loan-repayment-frequency.model';
import { LoanRepaymentFrequencyService } from '../service/loan-repayment-frequency.service';

import { LoanRepaymentFrequencyRoutingResolveService } from './loan-repayment-frequency-routing-resolve.service';

describe('LoanRepaymentFrequency routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LoanRepaymentFrequencyRoutingResolveService;
  let service: LoanRepaymentFrequencyService;
  let resultLoanRepaymentFrequency: ILoanRepaymentFrequency | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LoanRepaymentFrequencyRoutingResolveService);
    service = TestBed.inject(LoanRepaymentFrequencyService);
    resultLoanRepaymentFrequency = undefined;
  });

  describe('resolve', () => {
    it('should return ILoanRepaymentFrequency returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanRepaymentFrequency = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanRepaymentFrequency).toEqual({ id: 123 });
    });

    it('should return new ILoanRepaymentFrequency if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanRepaymentFrequency = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLoanRepaymentFrequency).toEqual(new LoanRepaymentFrequency());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LoanRepaymentFrequency })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanRepaymentFrequency = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanRepaymentFrequency).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
