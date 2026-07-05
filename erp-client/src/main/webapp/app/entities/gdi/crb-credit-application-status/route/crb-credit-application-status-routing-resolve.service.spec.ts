jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbCreditApplicationStatus, CrbCreditApplicationStatus } from '../crb-credit-application-status.model';
import { CrbCreditApplicationStatusService } from '../service/crb-credit-application-status.service';

import { CrbCreditApplicationStatusRoutingResolveService } from './crb-credit-application-status-routing-resolve.service';

describe('CrbCreditApplicationStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbCreditApplicationStatusRoutingResolveService;
  let service: CrbCreditApplicationStatusService;
  let resultCrbCreditApplicationStatus: ICrbCreditApplicationStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbCreditApplicationStatusRoutingResolveService);
    service = TestBed.inject(CrbCreditApplicationStatusService);
    resultCrbCreditApplicationStatus = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbCreditApplicationStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbCreditApplicationStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbCreditApplicationStatus).toEqual({ id: 123 });
    });

    it('should return new ICrbCreditApplicationStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbCreditApplicationStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbCreditApplicationStatus).toEqual(new CrbCreditApplicationStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbCreditApplicationStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbCreditApplicationStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbCreditApplicationStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
