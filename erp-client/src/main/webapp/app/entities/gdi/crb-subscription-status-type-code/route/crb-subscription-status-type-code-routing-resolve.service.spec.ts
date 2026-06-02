jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbSubscriptionStatusTypeCode, CrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';
import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';

import { CrbSubscriptionStatusTypeCodeRoutingResolveService } from './crb-subscription-status-type-code-routing-resolve.service';

describe('CrbSubscriptionStatusTypeCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbSubscriptionStatusTypeCodeRoutingResolveService;
  let service: CrbSubscriptionStatusTypeCodeService;
  let resultCrbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbSubscriptionStatusTypeCodeRoutingResolveService);
    service = TestBed.inject(CrbSubscriptionStatusTypeCodeService);
    resultCrbSubscriptionStatusTypeCode = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbSubscriptionStatusTypeCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSubscriptionStatusTypeCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbSubscriptionStatusTypeCode).toEqual({ id: 123 });
    });

    it('should return new ICrbSubscriptionStatusTypeCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSubscriptionStatusTypeCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbSubscriptionStatusTypeCode).toEqual(new CrbSubscriptionStatusTypeCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbSubscriptionStatusTypeCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSubscriptionStatusTypeCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbSubscriptionStatusTypeCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
