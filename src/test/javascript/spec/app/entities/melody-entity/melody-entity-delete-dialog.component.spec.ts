/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { BeethovenTestModule } from '../../../test.module';
import { MelodyEntityDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity-delete-dialog.component';
import { MelodyEntityService } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.service';

describe('Component Tests', () => {

    describe('MelodyEntity Management Delete Component', () => {
        let comp: MelodyEntityDeleteDialogComponent;
        let fixture: ComponentFixture<MelodyEntityDeleteDialogComponent>;
        let service: MelodyEntityService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeethovenTestModule],
                declarations: [MelodyEntityDeleteDialogComponent],
                providers: [
                    MelodyEntityService
                ]
            })
            .overrideTemplate(MelodyEntityDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MelodyEntityDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MelodyEntityService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
