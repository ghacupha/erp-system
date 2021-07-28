import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetAcquisitionUpdateComponent } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition-update.component';
import { FixedAssetAcquisitionService } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition.service';
import { FixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';

describe('Component Tests', () => {
  describe('FixedAssetAcquisition Management Update Component', () => {
    let comp: FixedAssetAcquisitionUpdateComponent;
    let fixture: ComponentFixture<FixedAssetAcquisitionUpdateComponent>;
    let service: FixedAssetAcquisitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetAcquisitionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FixedAssetAcquisitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetAcquisitionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedAssetAcquisitionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedAssetAcquisition(123);
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
        const entity = new FixedAssetAcquisition();
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
