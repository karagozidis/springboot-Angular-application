  import { Component, OnInit, ViewChild, ChangeDetectorRef, Inject, TemplateRef } from '@angular/core';
  import { UserGroupDTO } from 'app/shared/dto/user-group-dto';
  import { HistoryService } from 'app/services/history.service';
  import { MatPaginator, MatSort, MAT_DIALOG_DATA, MatDialog, MatTableDataSource } from '@angular/material';
  import { UserGroupService } from 'app/services/user-group.service';
  import { Router } from '@angular/router';
  import {COLUMN_NAMES, HYPPO_IO_OUTPUT_DATA, SAMPLE_DATA,
    SAMPLE_DATA_SEPT} from 'app/pages/modules/ge-hyppo-io/ge-hyppo-io-mock-data/hyppo-io-output-data';
  import { ResultService } from 'app/services/results.service';


  @Component({
    selector: 'ge-hyppo-io-results',
    templateUrl: './ge-hyppo-io-results.component.html',
    styleUrls: ['./ge-hyppo-io-results.component.scss']
  })
  export class GeHyppoIoResultsComponent implements OnInit {
    sample_data = {}
    empData: any = null;
    dataSource;
    @ViewChild(MatSort, null) sort: MatSort;
    @ViewChild(MatPaginator, null) paginator: MatPaginator;
    searchKey: string;
    displayedColumns = COLUMN_NAMES
    namesArray = [];

    contentArray: any = null;
    bodyText = 'Metadata:\nSiteName_mod : Name of the project ' +
               '\nData_source : Name of the input file' +
               '\nCountry : Country of the project' +
               '\nEquipment : GT frame' +
               '\nCustomer : Customer Name' +
               '\nController : Name of the Operating Company' +
               '\nSiteName : Name of the Site' +
               '\nSerial_Number : Serial Number of the asset' +
               '\nContract : Contract Information' +
               '\n\nFinancial\nStarting_date\nBattery_price[$] : Initial price of the battery' +
               '\nScen1_Saved_gas[kg] : Saved gas due to better efficiency in scenario 1' +
               '\nScen2_more_pr_prft[$] : Profits gained selling more capacity reserve with scenario 2' +
               '\nScen1_New_setpoint_prft[$] : Profits gained selling more electricity with scenario 2' +
               '\nSFFR_prft[$] : Profits gained selling the super fast frequency response service' +
               '\n\nBattery\nStarting_date\nInput' +
               '\nApparent Power[MVA] : Apperent power needed for the battery, can be different from power when black start is required' +
               '\nPower_to_pr[MW] : Share of the power battery used to increase the capacity reserve' +
               '\nPower_to_load[MW] : Share of the power battery used to increase the load during primary response' +
               '\nEnergy_max[MWh] : Size of the battery' +
               '\nPower[MW] : The power the battery is able to deliver' +
               '\nPower_super_fast_resp[MW] : The maximum power to deliver for the super fast frequency response' +
               '\n\noutput\nMean_added_hr[MW/(kg/s)] : The mean added power over fuel ratio thanks to higher GT load' +
               '\nMean_added_power[MW] : The mean added power thanks to the battery' +
               '\n\nstats\nMAX_batt_Power[MW] : The max amount of battery power reached during primary response' +
               '\nBattery_losses[MWh] : The energy losses due to battery efficiency ' +
               '\nGT_resp_time[%] : The percentage of the time the GT is used to provide primary response' +
               '\nMAX_SOC[%] : The max State Of Charge reached during primary response by the battery' +
               '\nmin_SOC[%] : The min State Of Charge reached during primary response by the battery' +
               '\nBat_resp_time[%] : The percentage of the time the Battery is used to provide primary response' +
               // tslint:disable-next-line:max-line-length
               '\nSFFR_total_discharge[MWh] : The value of constant power to be purchased from the grid to end the period with the same battery energy level' +
               // tslint:disable-next-line:max-line-length
               '\nSFFR_deadband_recharge_needed[MW] : The value of power to be purchased from the grid during the deadband period to end the period with the same battery energy level' +
               '\n\nCalc_param\nstarting_date' +
               // tslint:disable-next-line:max-line-length
               '\nbaseload[MW] : Estimated baseload - calculated as the max of the 5 minutes rolling average of the power, when the power plant is not responding' +
               // tslint:disable-next-line:max-line-length
               '\nprimary_reserve[MW] : Estimated power plant capacity response - calculated as the baseload minus the max of the 5 minutes rolling average of the power, when the power plant is responding'+
               // tslint:disable-next-line:max-line-length
               '\n\nTime_stats\nOverall: The sum of all the time intervals analyzed\nStarting_date\nStart : The starting time of this time interval' +
               '\nFinish : The ending time of this time interval' +
               '\nDelta_time : Finish minus start time for this time interval';


    constructor(private userGroupService: UserGroupService,
      private historyService: HistoryService,
      private resultService: ResultService,
      private router: Router, private dialog: MatDialog,
      @Inject(MAT_DIALOG_DATA) public data: any,  private changeDetectorRefs: ChangeDetectorRef) {}

    ngOnInit() {
        this.resultService.getResults(this.historyService.selectedData.uuid).subscribe(data => {
        let jsonString = '{'
        for(let i = 0; i < data.data.length; i++) {
          jsonString += '"' + data.names[i] + '":' + data.data[i]
          if(i < data.data.length - 1) {
              jsonString += ','
          }
        }
        jsonString += '}'
        this.contentArray = JSON.parse(jsonString)
    });
    }


    openGraphTab() {
      const a = (<HTMLElement>document.querySelectorAll('.mat-tab-label')[3] ).click();

    }
  }
