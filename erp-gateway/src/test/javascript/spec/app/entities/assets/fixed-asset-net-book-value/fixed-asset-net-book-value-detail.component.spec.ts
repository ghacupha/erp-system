import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetNetBookValueDetailComponent } from 'app/entities/assets/fixed-asset-net-book-value/fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';

describe('Component Tests', () => {
  describe('FixedAssetNetBookValue Management Detail Component', () => {
    let comp: FixedAssetNetBookValueDetailComponent;
    let fixture: ComponentFixture<FixedAssetNetBookValueDetailComponent>;
    const route = ({ data: of({ fixedAssetNetBookValue: new FixedAssetNetBookValue(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetNetBookValueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FixedAssetNetBookValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetNetBookValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fixedAssetNetBookValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedAssetNetBookValue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
