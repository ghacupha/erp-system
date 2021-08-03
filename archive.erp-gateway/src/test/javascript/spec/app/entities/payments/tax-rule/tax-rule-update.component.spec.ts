import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { TaxRuleUpdateComponent } from 'app/entities/payments/tax-rule/tax-rule-update.component';
import { TaxRuleService } from 'app/entities/payments/tax-rule/tax-rule.service';
import { TaxRule } from 'app/shared/model/payments/tax-rule.model';

describe('Component Tests', () => {
  describe('TaxRule Management Update Component', () => {
    let comp: TaxRuleUpdateComponent;
    let fixture: ComponentFixture<TaxRuleUpdateComponent>;
    let service: TaxRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TaxRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaxRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaxRule(123);
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
        const entity = new TaxRule();
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
