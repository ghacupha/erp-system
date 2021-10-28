import {MockStore, provideMockStore} from "@ngrx/store/testing";

jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { InvoiceService } from '../service/invoice.service';

import { InvoiceDeleteDialogComponent } from './invoice-delete-dialog.component';
import {initialState} from "../../../../store/global-store.definition";
import {LoggerTestingModule} from "ngx-logger/testing";

describe('Component Tests', () => {
  describe('Invoice Management Delete Component', () => {
    let comp: InvoiceDeleteDialogComponent;
    let fixture: ComponentFixture<InvoiceDeleteDialogComponent>;
    let service: InvoiceService;
    let mockActiveModal: NgbActiveModal;
    let store: MockStore;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, LoggerTestingModule],
        declarations: [InvoiceDeleteDialogComponent],
        providers: [NgbActiveModal, provideMockStore({initialState})],
      })
        .overrideTemplate(InvoiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InvoiceService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      store = TestBed.inject(MockStore);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
