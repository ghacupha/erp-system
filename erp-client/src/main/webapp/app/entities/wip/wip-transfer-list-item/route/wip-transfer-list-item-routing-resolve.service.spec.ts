jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWIPTransferListItem, WIPTransferListItem } from '../wip-transfer-list-item.model';
import { WIPTransferListItemService } from '../service/wip-transfer-list-item.service';

import { WIPTransferListItemRoutingResolveService } from './wip-transfer-list-item-routing-resolve.service';

describe('WIPTransferListItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WIPTransferListItemRoutingResolveService;
  let service: WIPTransferListItemService;
  let resultWIPTransferListItem: IWIPTransferListItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(WIPTransferListItemRoutingResolveService);
    service = TestBed.inject(WIPTransferListItemService);
    resultWIPTransferListItem = undefined;
  });

  describe('resolve', () => {
    it('should return IWIPTransferListItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPTransferListItem).toEqual({ id: 123 });
    });

    it('should return new IWIPTransferListItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWIPTransferListItem).toEqual(new WIPTransferListItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WIPTransferListItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPTransferListItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
