jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CardAcquiringTransactionService } from '../service/card-acquiring-transaction.service';

import { CardAcquiringTransactionDeleteDialogComponent } from './card-acquiring-transaction-delete-dialog.component';

describe('CardAcquiringTransaction Management Delete Component', () => {
  let comp: CardAcquiringTransactionDeleteDialogComponent;
  let fixture: ComponentFixture<CardAcquiringTransactionDeleteDialogComponent>;
  let service: CardAcquiringTransactionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardAcquiringTransactionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CardAcquiringTransactionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardAcquiringTransactionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CardAcquiringTransactionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
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
