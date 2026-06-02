jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardFraudInformation, CardFraudInformation } from '../card-fraud-information.model';
import { CardFraudInformationService } from '../service/card-fraud-information.service';

import { CardFraudInformationRoutingResolveService } from './card-fraud-information-routing-resolve.service';

describe('CardFraudInformation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardFraudInformationRoutingResolveService;
  let service: CardFraudInformationService;
  let resultCardFraudInformation: ICardFraudInformation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardFraudInformationRoutingResolveService);
    service = TestBed.inject(CardFraudInformationService);
    resultCardFraudInformation = undefined;
  });

  describe('resolve', () => {
    it('should return ICardFraudInformation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardFraudInformation).toEqual({ id: 123 });
    });

    it('should return new ICardFraudInformation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudInformation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardFraudInformation).toEqual(new CardFraudInformation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardFraudInformation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardFraudInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardFraudInformation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
