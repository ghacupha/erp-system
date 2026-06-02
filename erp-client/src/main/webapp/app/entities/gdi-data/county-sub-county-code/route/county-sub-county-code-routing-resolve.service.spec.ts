jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountySubCountyCode, CountySubCountyCode } from '../county-sub-county-code.model';
import { CountySubCountyCodeService } from '../service/county-sub-county-code.service';

import { CountySubCountyCodeRoutingResolveService } from './county-sub-county-code-routing-resolve.service';

describe('CountySubCountyCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountySubCountyCodeRoutingResolveService;
  let service: CountySubCountyCodeService;
  let resultCountySubCountyCode: ICountySubCountyCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountySubCountyCodeRoutingResolveService);
    service = TestBed.inject(CountySubCountyCodeService);
    resultCountySubCountyCode = undefined;
  });

  describe('resolve', () => {
    it('should return ICountySubCountyCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountySubCountyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountySubCountyCode).toEqual({ id: 123 });
    });

    it('should return new ICountySubCountyCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountySubCountyCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountySubCountyCode).toEqual(new CountySubCountyCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountySubCountyCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountySubCountyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountySubCountyCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
