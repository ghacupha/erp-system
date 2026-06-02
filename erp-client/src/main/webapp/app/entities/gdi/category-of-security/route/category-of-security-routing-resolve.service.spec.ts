jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICategoryOfSecurity, CategoryOfSecurity } from '../category-of-security.model';
import { CategoryOfSecurityService } from '../service/category-of-security.service';

import { CategoryOfSecurityRoutingResolveService } from './category-of-security-routing-resolve.service';

describe('CategoryOfSecurity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoryOfSecurityRoutingResolveService;
  let service: CategoryOfSecurityService;
  let resultCategoryOfSecurity: ICategoryOfSecurity | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CategoryOfSecurityRoutingResolveService);
    service = TestBed.inject(CategoryOfSecurityService);
    resultCategoryOfSecurity = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoryOfSecurity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoryOfSecurity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoryOfSecurity).toEqual({ id: 123 });
    });

    it('should return new ICategoryOfSecurity if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoryOfSecurity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoryOfSecurity).toEqual(new CategoryOfSecurity());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CategoryOfSecurity })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoryOfSecurity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoryOfSecurity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
