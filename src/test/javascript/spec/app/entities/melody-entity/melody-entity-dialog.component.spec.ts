/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { BeethovenTestModule } from '../../../test.module';
import { MelodyEntityDialogComponent } from '../../../../../../main/webapp/app/entities/melody-entity/components/melody-entity-dialog.component';
import { MelodyEntityService } from '../../../../../../main/webapp/app/entities/melody-entity/services/melody-entity.service';
import { MelodyEntity } from '../../../../../../main/webapp/app/entities/melody-entity/models/melody-entity.model';
import { ProfileService } from '../../../../../../main/webapp/app/entities/profile';

describe('Component Tests', () => {

    describe('MelodyEntity Management Dialog Component', () => {
        let comp: MelodyEntityDialogComponent;
        let fixture: ComponentFixture<MelodyEntityDialogComponent>;
        let service: MelodyEntityService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeethovenTestModule],
                declarations: [MelodyEntityDialogComponent],
                providers: [
                    ProfileService,
                    MelodyEntityService
                ]
            })
            .overrideTemplate(MelodyEntityDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MelodyEntityDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MelodyEntityService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MelodyEntity(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.melodyEntity = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'melodyEntityListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MelodyEntity();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.melodyEntity = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'melodyEntityListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
