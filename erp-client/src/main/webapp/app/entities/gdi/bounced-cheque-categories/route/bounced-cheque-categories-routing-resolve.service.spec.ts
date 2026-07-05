jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBouncedChequeCategories, BouncedChequeCategories } from '../bounced-cheque-categories.model';
import { BouncedChequeCategoriesService } from '../service/bounced-cheque-categories.service';

import { BouncedChequeCategoriesRoutingResolveService } from './bounced-cheque-categories-routing-resolve.service';

describe('BouncedChequeCategories routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BouncedChequeCategoriesRoutingResolveService;
  let service: BouncedChequeCategoriesService;
  let resultBouncedChequeCategories: IBouncedChequeCategories | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BouncedChequeCategoriesRoutingResolveService);
    service = TestBed.inject(BouncedChequeCategoriesService);
    resultBouncedChequeCategories = undefined;
  });

  describe('resolve', () => {
    it('should return IBouncedChequeCategories returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBouncedChequeCategories = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBouncedChequeCategories).toEqual({ id: 123 });
    });

    it('should return new IBouncedChequeCategories if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBouncedChequeCategories = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBouncedChequeCategories).toEqual(new BouncedChequeCategories());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BouncedChequeCategories })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBouncedChequeCategories = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBouncedChequeCategories).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
