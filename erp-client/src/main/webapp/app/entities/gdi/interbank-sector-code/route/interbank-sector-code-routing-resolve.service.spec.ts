jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInterbankSectorCode, InterbankSectorCode } from '../interbank-sector-code.model';
import { InterbankSectorCodeService } from '../service/interbank-sector-code.service';

import { InterbankSectorCodeRoutingResolveService } from './interbank-sector-code-routing-resolve.service';

describe('InterbankSectorCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InterbankSectorCodeRoutingResolveService;
  let service: InterbankSectorCodeService;
  let resultInterbankSectorCode: IInterbankSectorCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InterbankSectorCodeRoutingResolveService);
    service = TestBed.inject(InterbankSectorCodeService);
    resultInterbankSectorCode = undefined;
  });

  describe('resolve', () => {
    it('should return IInterbankSectorCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInterbankSectorCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInterbankSectorCode).toEqual({ id: 123 });
    });

    it('should return new IInterbankSectorCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInterbankSectorCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInterbankSectorCode).toEqual(new InterbankSectorCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InterbankSectorCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInterbankSectorCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInterbankSectorCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
