jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRelatedPartyRelationship, RelatedPartyRelationship } from '../related-party-relationship.model';
import { RelatedPartyRelationshipService } from '../service/related-party-relationship.service';

import { RelatedPartyRelationshipRoutingResolveService } from './related-party-relationship-routing-resolve.service';

describe('RelatedPartyRelationship routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RelatedPartyRelationshipRoutingResolveService;
  let service: RelatedPartyRelationshipService;
  let resultRelatedPartyRelationship: IRelatedPartyRelationship | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RelatedPartyRelationshipRoutingResolveService);
    service = TestBed.inject(RelatedPartyRelationshipService);
    resultRelatedPartyRelationship = undefined;
  });

  describe('resolve', () => {
    it('should return IRelatedPartyRelationship returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRelatedPartyRelationship = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRelatedPartyRelationship).toEqual({ id: 123 });
    });

    it('should return new IRelatedPartyRelationship if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRelatedPartyRelationship = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRelatedPartyRelationship).toEqual(new RelatedPartyRelationship());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RelatedPartyRelationship })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRelatedPartyRelationship = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRelatedPartyRelationship).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
