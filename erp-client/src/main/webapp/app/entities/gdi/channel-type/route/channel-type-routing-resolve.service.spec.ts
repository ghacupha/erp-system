jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChannelType, ChannelType } from '../channel-type.model';
import { ChannelTypeService } from '../service/channel-type.service';

import { ChannelTypeRoutingResolveService } from './channel-type-routing-resolve.service';

describe('ChannelType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ChannelTypeRoutingResolveService;
  let service: ChannelTypeService;
  let resultChannelType: IChannelType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ChannelTypeRoutingResolveService);
    service = TestBed.inject(ChannelTypeService);
    resultChannelType = undefined;
  });

  describe('resolve', () => {
    it('should return IChannelType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChannelType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultChannelType).toEqual({ id: 123 });
    });

    it('should return new IChannelType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChannelType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultChannelType).toEqual(new ChannelType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ChannelType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChannelType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultChannelType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
