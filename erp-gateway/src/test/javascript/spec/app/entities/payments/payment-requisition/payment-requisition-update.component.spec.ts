import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { PaymentRequisitionUpdateComponent } from 'app/entities/payments/payment-requisition/payment-requisition-update.component';
import { PaymentRequisitionService } from 'app/entities/payments/payment-requisition/payment-requisition.service';
import { PaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';

describe('Component Tests', () => {
  describe('PaymentRequisition Management Update Component', () => {
    let comp: PaymentRequisitionUpdateComponent;
    let fixture: ComponentFixture<PaymentRequisitionUpdateComponent>;
    let service: PaymentRequisitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentRequisitionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PaymentRequisitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentRequisitionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentRequisitionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentRequisition(123);
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
        const entity = new PaymentRequisition();
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
