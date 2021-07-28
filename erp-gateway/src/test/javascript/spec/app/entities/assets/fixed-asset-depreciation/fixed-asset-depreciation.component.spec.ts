import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetDepreciationComponent } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation.component';
import { FixedAssetDepreciationService } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation.service';
import { FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';

describe('Component Tests', () => {
  describe('FixedAssetDepreciation Management Component', () => {
    let comp: FixedAssetDepreciationComponent;
    let fixture: ComponentFixture<FixedAssetDepreciationComponent>;
    let service: FixedAssetDepreciationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetDepreciationComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(FixedAssetDepreciationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetDepreciationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedAssetDepreciationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FixedAssetDepreciation(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fixedAssetDepreciations && comp.fixedAssetDepreciations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FixedAssetDepreciation(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fixedAssetDepreciations && comp.fixedAssetDepreciations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
