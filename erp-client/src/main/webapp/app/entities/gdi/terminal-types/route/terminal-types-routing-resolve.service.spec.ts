jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITerminalTypes, TerminalTypes } from '../terminal-types.model';
import { TerminalTypesService } from '../service/terminal-types.service';

import { TerminalTypesRoutingResolveService } from './terminal-types-routing-resolve.service';

describe('TerminalTypes routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TerminalTypesRoutingResolveService;
  let service: TerminalTypesService;
  let resultTerminalTypes: ITerminalTypes | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TerminalTypesRoutingResolveService);
    service = TestBed.inject(TerminalTypesService);
    resultTerminalTypes = undefined;
  });

  describe('resolve', () => {
    it('should return ITerminalTypes returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTerminalTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTerminalTypes).toEqual({ id: 123 });
    });

    it('should return new ITerminalTypes if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTerminalTypes = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTerminalTypes).toEqual(new TerminalTypes());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TerminalTypes })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTerminalTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTerminalTypes).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
