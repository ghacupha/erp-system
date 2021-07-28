import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetDepreciationUpdateComponent } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation-update.component';
import { FixedAssetDepreciationService } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation.service';
import { FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';

describe('Component Tests', () => {
  describe('FixedAssetDepreciation Management Update Component', () => {
    let comp: FixedAssetDepreciationUpdateComponent;
    let fixture: ComponentFixture<FixedAssetDepreciationUpdateComponent>;
    let service: FixedAssetDepreciationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetDepreciationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FixedAssetDepreciationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetDepreciationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedAssetDepreciationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedAssetDepreciation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedAssetDepreciation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
