jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardCharges, CardCharges } from '../card-charges.model';
import { CardChargesService } from '../service/card-charges.service';

import { CardChargesRoutingResolveService } from './card-charges-routing-resolve.service';

describe('CardCharges routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardChargesRoutingResolveService;
  let service: CardChargesService;
  let resultCardCharges: ICardCharges | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardChargesRoutingResolveService);
    service = TestBed.inject(CardChargesService);
    resultCardCharges = undefined;
  });

  describe('resolve', () => {
    it('should return ICardCharges returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCharges = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardCharges).toEqual({ id: 123 });
    });

    it('should return new ICardCharges if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCharges = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardCharges).toEqual(new CardCharges());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardCharges })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardCharges = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardCharges).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
