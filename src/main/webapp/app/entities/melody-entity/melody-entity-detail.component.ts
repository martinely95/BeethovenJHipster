import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MelodyEntity } from './melody-entity.model';
import { MelodyEntityService } from './melody-entity.service';

@Component({
    selector: 'jhi-melody-entity-detail',
    templateUrl: './melody-entity-detail.component.html'
})
export class MelodyEntityDetailComponent implements OnInit, OnDestroy {

    melodyEntity: MelodyEntity;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private melodyEntityService: MelodyEntityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMelodyEntities();
    }

    load(id) {
        this.melodyEntityService.find(id).subscribe((melodyEntity) => {
            this.melodyEntity = melodyEntity;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMelodyEntities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'melodyEntityListModification',
            (response) => this.load(this.melodyEntity.id)
        );
    }
}
