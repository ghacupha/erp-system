jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardFraudIncidentCategory, CardFraudIncidentCategory } from '../card-fraud-incident-category.model';
import { CardFraudIncidentCategoryService } from '../service/card-fraud-incident-category.service';

import { CardFraudIncidentCategoryRoutingResolveService } from './card-fraud-incident-category-routing-resolve.service';

describe('CardFraudIncidentCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardFraudIncidentCategoryRoutingResolveService;
  let service: CardFraudIncidentCategoryService;
  let resultCardFraudIncidentCategory: ICardFraudIncidentCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardFraudIncidentCategoryRoutingResolveService);
    service = TestBed.inject(CardFraudIncidentCategoryService);
    resultCardFraudIncidentCategory = undefined;
  });

  describe('resolve', () => {
    it('should return ICardFraudIncidentCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudIncidentCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardFraudIncidentCategory).toEqual({ id: 123 });
    });

    it('should return new ICardFraudIncidentCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudIncidentCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardFraudIncidentCategory).toEqual(new CardFraudIncidentCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardFraudIncidentCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudIncidentCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardFraudIncidentCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
