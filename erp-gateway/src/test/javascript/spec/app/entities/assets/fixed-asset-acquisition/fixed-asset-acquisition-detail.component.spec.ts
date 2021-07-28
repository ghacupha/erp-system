import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetAcquisitionDetailComponent } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition-detail.component';
import { FixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';

describe('Component Tests', () => {
  describe('FixedAssetAcquisition Management Detail Component', () => {
    let comp: FixedAssetAcquisitionDetailComponent;
    let fixture: ComponentFixture<FixedAssetAcquisitionDetailComponent>;
    const route = ({ data: of({ fixedAssetAcquisition: new FixedAssetAcquisition(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetAcquisitionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FixedAssetAcquisitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetAcquisitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fixedAssetAcquisition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedAssetAcquisition).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
