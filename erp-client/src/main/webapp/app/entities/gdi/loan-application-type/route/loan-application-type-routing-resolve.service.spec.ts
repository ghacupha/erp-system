jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILoanApplicationType, LoanApplicationType } from '../loan-application-type.model';
import { LoanApplicationTypeService } from '../service/loan-application-type.service';

import { LoanApplicationTypeRoutingResolveService } from './loan-application-type-routing-resolve.service';

describe('LoanApplicationType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LoanApplicationTypeRoutingResolveService;
  let service: LoanApplicationTypeService;
  let resultLoanApplicationType: ILoanApplicationType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LoanApplicationTypeRoutingResolveService);
    service = TestBed.inject(LoanApplicationTypeService);
    resultLoanApplicationType = undefined;
  });

  describe('resolve', () => {
    it('should return ILoanApplicationType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanApplicationType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanApplicationType).toEqual({ id: 123 });
    });

    it('should return new ILoanApplicationType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanApplicationType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLoanApplicationType).toEqual(new LoanApplicationType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LoanApplicationType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLoanApplicationType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLoanApplicationType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
