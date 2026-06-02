jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyCode, CountyCode } from '../county-code.model';
import { CountyCodeService } from '../service/county-code.service';

import { CountyCodeRoutingResolveService } from './county-code-routing-resolve.service';

describe('CountyCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyCodeRoutingResolveService;
  let service: CountyCodeService;
  let resultCountyCode: ICountyCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyCodeRoutingResolveService);
    service = TestBed.inject(CountyCodeService);
    resultCountyCode = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyCode).toEqual({ id: 123 });
    });

    it('should return new ICountyCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyCode).toEqual(new CountyCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
