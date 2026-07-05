jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISourceRemittancePurposeType, SourceRemittancePurposeType } from '../source-remittance-purpose-type.model';
import { SourceRemittancePurposeTypeService } from '../service/source-remittance-purpose-type.service';

import { SourceRemittancePurposeTypeRoutingResolveService } from './source-remittance-purpose-type-routing-resolve.service';

describe('SourceRemittancePurposeType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SourceRemittancePurposeTypeRoutingResolveService;
  let service: SourceRemittancePurposeTypeService;
  let resultSourceRemittancePurposeType: ISourceRemittancePurposeType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SourceRemittancePurposeTypeRoutingResolveService);
    service = TestBed.inject(SourceRemittancePurposeTypeService);
    resultSourceRemittancePurposeType = undefined;
  });

  describe('resolve', () => {
    it('should return ISourceRemittancePurposeType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSourceRemittancePurposeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSourceRemittancePurposeType).toEqual({ id: 123 });
    });

    it('should return new ISourceRemittancePurposeType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSourceRemittancePurposeType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSourceRemittancePurposeType).toEqual(new SourceRemittancePurposeType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SourceRemittancePurposeType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSourceRemittancePurposeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSourceRemittancePurposeType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
