import { Injectable, EventEmitter } from "@angular/core";
import { UpdateSignals } from "../config/app-constants";
import { AppDataService } from "app/services/app-data.service";
import { Auxiliary } from "./auxiliary";

@Injectable({
    providedIn: 'root'
})
export class LoadingStateService {
    loadingStateEmmiter = new EventEmitter();
    showLoadingTimeoutDelay = null

    constructor(private appDataService: AppDataService) {
    }

    ngOnInit() {
    }

    showLoadingIndication() {
        this.hideLoadingIndication()
        this.showLoadingTimeoutDelay = setTimeout(() => this.loadingStateEmmiter.emit(UpdateSignals.SHOW_LOADING_INDICATION),300)
        // this.loadingStateEmmiter.emit(UpdateSignals.SHOW_LOADING_INDICATION)
    }

    hideLoadingIndication() {
        console.log(this.showLoadingTimeoutDelay)  
        if (this.showLoadingTimeoutDelay != null) {
            clearTimeout(this.showLoadingTimeoutDelay)
        }
        this.loadingStateEmmiter.emit(UpdateSignals.HIDE_LOADING_INDICATION)
    }
}
