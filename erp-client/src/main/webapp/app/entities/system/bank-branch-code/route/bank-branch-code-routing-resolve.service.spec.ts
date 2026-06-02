jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBankBranchCode, BankBranchCode } from '../bank-branch-code.model';
import { BankBranchCodeService } from '../service/bank-branch-code.service';

import { BankBranchCodeRoutingResolveService } from './bank-branch-code-routing-resolve.service';

describe('BankBranchCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BankBranchCodeRoutingResolveService;
  let service: BankBranchCodeService;
  let resultBankBranchCode: IBankBranchCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BankBranchCodeRoutingResolveService);
    service = TestBed.inject(BankBranchCodeService);
    resultBankBranchCode = undefined;
  });

  describe('resolve', () => {
    it('should return IBankBranchCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankBranchCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankBranchCode).toEqual({ id: 123 });
    });

    it('should return new IBankBranchCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankBranchCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBankBranchCode).toEqual(new BankBranchCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BankBranchCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankBranchCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankBranchCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
