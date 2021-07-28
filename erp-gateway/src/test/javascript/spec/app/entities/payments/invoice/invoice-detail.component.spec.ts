import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { InvoiceDetailComponent } from 'app/entities/payments/invoice/invoice-detail.component';
import { Invoice } from 'app/shared/model/payments/invoice.model';

describe('Component Tests', () => {
  describe('Invoice Management Detail Component', () => {
    let comp: InvoiceDetailComponent;
    let fixture: ComponentFixture<InvoiceDetailComponent>;
    const route = ({ data: of({ invoice: new Invoice(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [InvoiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InvoiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load invoice on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invoice).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
