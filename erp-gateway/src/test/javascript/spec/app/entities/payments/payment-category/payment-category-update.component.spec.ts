import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { PaymentCategoryUpdateComponent } from 'app/entities/payments/payment-category/payment-category-update.component';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/payment-category.service';
import { PaymentCategory } from 'app/shared/model/payments/payment-category.model';

describe('Component Tests', () => {
  describe('PaymentCategory Management Update Component', () => {
    let comp: PaymentCategoryUpdateComponent;
    let fixture: ComponentFixture<PaymentCategoryUpdateComponent>;
    let service: PaymentCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentCategoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PaymentCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentCategory(123);
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
        const entity = new PaymentCategory();
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
