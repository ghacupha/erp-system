jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILoanPerformanceClassification, LoanPerformanceClassification } from '../loan-performance-classification.model';
import { LoanPerformanceClassificationService } from '../service/loan-performance-classification.service';

import { LoanPerformanceClassificationRoutingResolveService } from './loan-performance-classification-routing-resolve.service';

describe('LoanPerformanceClassification routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LoanPerformanceClassificationRoutingResolveService;
  let service: LoanPerformanceClassificationService;
  let resultLoanPerformanceClassification: ILoanPerformanceClassification | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LoanPerformanceClassificationRoutingResolveService);
    service = TestBed.inject(LoanPerformanceClassificationService);
    resultLoanPerformanceClassification = undefined;
  });

  describe('resolve', () => {
    it('should return ILoanPerformanceClassification returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanPerformanceClassification = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanPerformanceClassification).toEqual({ id: 123 });
    });

    it('should return new ILoanPerformanceClassification if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanPerformanceClassification = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLoanPerformanceClassification).toEqual(new LoanPerformanceClassification());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LoanPerformanceClassification })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanPerformanceClassification = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanPerformanceClassification).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
