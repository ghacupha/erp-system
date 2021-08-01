jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMessageToken, MessageToken } from '../message-token.model';
import { MessageTokenService } from '../service/message-token.service';

import { MessageTokenRoutingResolveService } from './message-token-routing-resolve.service';

describe('Service Tests', () => {
  describe('MessageToken routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MessageTokenRoutingResolveService;
    let service: MessageTokenService;
    let resultMessageToken: IMessageToken | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MessageTokenRoutingResolveService);
      service = TestBed.inject(MessageTokenService);
      resultMessageToken = undefined;
    });

    describe('resolve', () => {
      it('should return IMessageToken returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMessageToken = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMessageToken).toEqual({ id: 123 });
      });

      it('should return new IMessageToken if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMessageToken = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMessageToken).toEqual(new MessageToken());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MessageToken })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMessageToken = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMessageToken).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
