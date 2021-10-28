import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceDetailComponent } from './invoice-detail.component';
import {provideMockStore} from "@ngrx/store/testing";
import {initialState} from "../../../../store/global-store.definition";

describe('Component Tests', () => {
  describe('Invoice Management Detail Component', () => {
    let comp: InvoiceDetailComponent;
    let fixture: ComponentFixture<InvoiceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InvoiceDetailComponent],
        providers: [
          provideMockStore({initialState}),
          {
            provide: ActivatedRoute,
            useValue: { data: of({ invoice: { id: 123 } }) },
          },
        ],
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
        expect(comp.invoice).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
