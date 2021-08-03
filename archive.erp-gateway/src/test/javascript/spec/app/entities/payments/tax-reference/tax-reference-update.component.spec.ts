import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { TaxReferenceUpdateComponent } from 'app/entities/payments/tax-reference/tax-reference-update.component';
import { TaxReferenceService } from 'app/entities/payments/tax-reference/tax-reference.service';
import { TaxReference } from 'app/shared/model/payments/tax-reference.model';

describe('Component Tests', () => {
  describe('TaxReference Management Update Component', () => {
    let comp: TaxReferenceUpdateComponent;
    let fixture: ComponentFixture<TaxReferenceUpdateComponent>;
    let service: TaxReferenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxReferenceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TaxReferenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxReferenceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaxReferenceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaxReference(123);
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
        const entity = new TaxReference();
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
