jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRemittanceFlag, RemittanceFlag } from '../remittance-flag.model';
import { RemittanceFlagService } from '../service/remittance-flag.service';

import { RemittanceFlagRoutingResolveService } from './remittance-flag-routing-resolve.service';

describe('RemittanceFlag routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RemittanceFlagRoutingResolveService;
  let service: RemittanceFlagService;
  let resultRemittanceFlag: IRemittanceFlag | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RemittanceFlagRoutingResolveService);
    service = TestBed.inject(RemittanceFlagService);
    resultRemittanceFlag = undefined;
  });

  describe('resolve', () => {
    it('should return IRemittanceFlag returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRemittanceFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRemittanceFlag).toEqual({ id: 123 });
    });

    it('should return new IRemittanceFlag if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRemittanceFlag = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRemittanceFlag).toEqual(new RemittanceFlag());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RemittanceFlag })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRemittanceFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRemittanceFlag).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
