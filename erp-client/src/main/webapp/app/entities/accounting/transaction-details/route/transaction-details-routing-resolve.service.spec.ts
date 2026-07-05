jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITransactionDetails, TransactionDetails } from '../transaction-details.model';
import { TransactionDetailsService } from '../service/transaction-details.service';

import { TransactionDetailsRoutingResolveService } from './transaction-details-routing-resolve.service';

describe('TransactionDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TransactionDetailsRoutingResolveService;
  let service: TransactionDetailsService;
  let resultTransactionDetails: ITransactionDetails | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TransactionDetailsRoutingResolveService);
    service = TestBed.inject(TransactionDetailsService);
    resultTransactionDetails = undefined;
  });

  describe('resolve', () => {
    it('should return ITransactionDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionDetails).toEqual({ id: 123 });
    });

    it('should return new ITransactionDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTransactionDetails).toEqual(new TransactionDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TransactionDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
