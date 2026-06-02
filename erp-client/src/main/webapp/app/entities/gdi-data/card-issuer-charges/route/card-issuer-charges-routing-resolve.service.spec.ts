jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardIssuerCharges, CardIssuerCharges } from '../card-issuer-charges.model';
import { CardIssuerChargesService } from '../service/card-issuer-charges.service';

import { CardIssuerChargesRoutingResolveService } from './card-issuer-charges-routing-resolve.service';

describe('CardIssuerCharges routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardIssuerChargesRoutingResolveService;
  let service: CardIssuerChargesService;
  let resultCardIssuerCharges: ICardIssuerCharges | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardIssuerChargesRoutingResolveService);
    service = TestBed.inject(CardIssuerChargesService);
    resultCardIssuerCharges = undefined;
  });

  describe('resolve', () => {
    it('should return ICardIssuerCharges returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardIssuerCharges = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardIssuerCharges).toEqual({ id: 123 });
    });

    it('should return new ICardIssuerCharges if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardIssuerCharges = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardIssuerCharges).toEqual(new CardIssuerCharges());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardIssuerCharges })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardIssuerCharges = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardIssuerCharges).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
