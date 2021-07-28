import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { TaxRuleDetailComponent } from 'app/entities/payments/tax-rule/tax-rule-detail.component';
import { TaxRule } from 'app/shared/model/payments/tax-rule.model';

describe('Component Tests', () => {
  describe('TaxRule Management Detail Component', () => {
    let comp: TaxRuleDetailComponent;
    let fixture: ComponentFixture<TaxRuleDetailComponent>;
    const route = ({ data: of({ taxRule: new TaxRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TaxRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaxRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taxRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taxRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
