import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetDepreciationDetailComponent } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation-detail.component';
import { FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';

describe('Component Tests', () => {
  describe('FixedAssetDepreciation Management Detail Component', () => {
    let comp: FixedAssetDepreciationDetailComponent;
    let fixture: ComponentFixture<FixedAssetDepreciationDetailComponent>;
    const route = ({ data: of({ fixedAssetDepreciation: new FixedAssetDepreciation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetDepreciationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FixedAssetDepreciationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetDepreciationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fixedAssetDepreciation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedAssetDepreciation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
