jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CardFraudInformationService } from '../service/card-fraud-information.service';

import { CardFraudInformationDeleteDialogComponent } from './card-fraud-information-delete-dialog.component';

describe('CardFraudInformation Management Delete Component', () => {
  let comp: CardFraudInformationDeleteDialogComponent;
  let fixture: ComponentFixture<CardFraudInformationDeleteDialogComponent>;
  let service: CardFraudInformationService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardFraudInformationDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CardFraudInformationDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardFraudInformationDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CardFraudInformationService);
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
