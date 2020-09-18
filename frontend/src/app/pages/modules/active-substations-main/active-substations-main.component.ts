import { Component, OnInit } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-active-substations-main',
  templateUrl: './active-substations-main.component.html',
  styleUrls: ['./active-substations-main.component.scss']
})
export class ActiveSubstationsMainComponent implements OnInit {
  tabIndex = 0 ;
  systemOperatorModuleIncluded = false
  substationOperatorModuleIncluded = false
  importForecastedDataModuleIncluded = true
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup: FormGroup;
  forthFormGroup: FormGroup;
  firstClass = false;
  secondClass = false
  thirdClass = false;
  forthClass = false

  changeTab(event){
    this.tabIndex = event.index;
  }
  constructor(public appDataService: AppDataService,private _formBuilder: FormBuilder) { }

  ngOnInit() {
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });
    this.thirdFormGroup = this._formBuilder.group({
      thirdCtrl: ['', Validators.required]
    });
    this.forthFormGroup = this._formBuilder.group({
      forthCtrl: ['', Validators.required]
    });

    this.firstClass = true;

  }

}
