jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardCategoryType, CardCategoryType } from '../card-category-type.model';
import { CardCategoryTypeService } from '../service/card-category-type.service';

import { CardCategoryTypeRoutingResolveService } from './card-category-type-routing-resolve.service';

describe('CardCategoryType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardCategoryTypeRoutingResolveService;
  let service: CardCategoryTypeService;
  let resultCardCategoryType: ICardCategoryType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardCategoryTypeRoutingResolveService);
    service = TestBed.inject(CardCategoryTypeService);
    resultCardCategoryType = undefined;
  });

  describe('resolve', () => {
    it('should return ICardCategoryType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCategoryType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardCategoryType).toEqual({ id: 123 });
    });

    it('should return new ICardCategoryType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCategoryType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardCategoryType).toEqual(new CardCategoryType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardCategoryType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCategoryType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardCategoryType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
