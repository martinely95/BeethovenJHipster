import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Profile } from './profile.model';
import { ProfileService } from './profile.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-profile',
    templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit, OnDestroy {
profiles: Profile[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private profileService: ProfileService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.profileService.query().subscribe(
            (res: ResponseWrapper) => {
                this.profiles = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProfiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Profile) {
        return item.id;
    }
    registerChangeInProfiles() {
        this.eventSubscriber = this.eventManager.subscribe('profileListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
