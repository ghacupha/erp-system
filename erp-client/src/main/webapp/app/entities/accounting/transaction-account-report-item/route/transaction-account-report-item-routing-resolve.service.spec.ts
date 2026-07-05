jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITransactionAccountReportItem, TransactionAccountReportItem } from '../transaction-account-report-item.model';
import { TransactionAccountReportItemService } from '../service/transaction-account-report-item.service';

import { TransactionAccountReportItemRoutingResolveService } from './transaction-account-report-item-routing-resolve.service';

describe('TransactionAccountReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TransactionAccountReportItemRoutingResolveService;
  let service: TransactionAccountReportItemService;
  let resultTransactionAccountReportItem: ITransactionAccountReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TransactionAccountReportItemRoutingResolveService);
    service = TestBed.inject(TransactionAccountReportItemService);
    resultTransactionAccountReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return ITransactionAccountReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountReportItem).toEqual({ id: 123 });
    });

    it('should return new ITransactionAccountReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTransactionAccountReportItem).toEqual(new TransactionAccountReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TransactionAccountReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
