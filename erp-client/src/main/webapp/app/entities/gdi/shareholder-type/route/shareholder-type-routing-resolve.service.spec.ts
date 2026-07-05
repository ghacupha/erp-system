jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShareholderType, ShareholderType } from '../shareholder-type.model';
import { ShareholderTypeService } from '../service/shareholder-type.service';

import { ShareholderTypeRoutingResolveService } from './shareholder-type-routing-resolve.service';

describe('ShareholderType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ShareholderTypeRoutingResolveService;
  let service: ShareholderTypeService;
  let resultShareholderType: IShareholderType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ShareholderTypeRoutingResolveService);
    service = TestBed.inject(ShareholderTypeService);
    resultShareholderType = undefined;
  });

  describe('resolve', () => {
    it('should return IShareholderType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShareholderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShareholderType).toEqual({ id: 123 });
    });

    it('should return new IShareholderType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShareholderType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultShareholderType).toEqual(new ShareholderType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ShareholderType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShareholderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultShareholderType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
