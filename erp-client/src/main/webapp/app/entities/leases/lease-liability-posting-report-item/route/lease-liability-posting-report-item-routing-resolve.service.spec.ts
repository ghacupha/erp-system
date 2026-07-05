jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityPostingReportItem, LeaseLiabilityPostingReportItem } from '../lease-liability-posting-report-item.model';
import { LeaseLiabilityPostingReportItemService } from '../service/lease-liability-posting-report-item.service';

import { LeaseLiabilityPostingReportItemRoutingResolveService } from './lease-liability-posting-report-item-routing-resolve.service';

describe('LeaseLiabilityPostingReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityPostingReportItemRoutingResolveService;
  let service: LeaseLiabilityPostingReportItemService;
  let resultLeaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityPostingReportItemRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityPostingReportItemService);
    resultLeaseLiabilityPostingReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityPostingReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityPostingReportItem).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityPostingReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityPostingReportItem).toEqual(new LeaseLiabilityPostingReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityPostingReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityPostingReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
