    import { Component, OnInit, AfterViewInit, ViewChild, ViewEncapsulation, ElementRef } from '@angular/core';
    import { RequestOptions } from '@angular/http';
    import { FileSelectDirective, FileUploader, FileUploadModule} from 'ng2-file-upload';
    import { GeHyppoService } from 'app/services/ge-hyppo.service';
    import { GeHyppoParametersDTO } from 'app/shared/dto/ge-hyppo-parameters-dto';
    import { MappingDTO } from 'app/shared/dto/mappings-dto';
    import { ConfigurationParametersDTO } from 'app/shared/dto/configuration-parameters-dto';
    import { UserGroupService } from 'app/services/user-group.service';
    import { FormControl, FormGroup, FormArray, FormBuilder } from '@angular/forms';
    import { Overlay, BlockScrollStrategy, CloseScrollStrategy } from '@angular/cdk/overlay';
    import { MAT_AUTOCOMPLETE_SCROLL_STRATEGY, MAT_SELECT_SCROLL_STRATEGY,
      MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent } from '@angular/material';
    import { Observable } from 'rxjs';
    import { startWith, map } from 'rxjs/operators';
    import { ENTER, COMMA } from '@angular/cdk/keycodes';
    import { InternalMessageService } from 'app/shared/utils/internal-message.service'
    import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
    import { AppConstants } from 'app/shared/config/app-constants';
    import { AppDataService } from 'app/services/app-data.service';
    import { Auxiliary } from 'app/shared/utils/auxiliary';

    // export const environment = {
    //   production: false,
    //   API_BASE_URL: 'http://localhost:8120/crm-local/cxf/api',
    // };
    const uri = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/upload-files';

    export function scrollFactory(overlay: Overlay): () => CloseScrollStrategy {
      return () => overlay.scrollStrategies.close();
    }
    @Component({
      selector: 'ge-hyppo-io-new',
      templateUrl: 'ge-hyppo-io-new.component.html',
      styleUrls: ['./ge-hyppo-io-new.component.scss'],
      providers: [
        { provide: MAT_AUTOCOMPLETE_SCROLL_STRATEGY, useFactory: scrollFactory, deps: [Overlay] }
      ]
    // encapsulation: ViewEncapsulation.None,
    })
    export class GEHyppoIONewComponent implements OnInit {

      selectedUserGroupName = 'me';
      usergroups: any = [];
      selectedUserGroup = 0;

     // messageServiceSubscription;
      uploader: FileUploader = new FileUploader({url: uri});
      uploader_: FileUploader = new FileUploader({url: uri});
      attachmentList: any = [];
      finalFileListToSend: any = [];
      attachmentList_: any = [];
      finalFileListToSend_: any = [];
      csvContent: string;
      public fileUploadFlag: boolean = false;  // You can use this flag to show your progress bar
      public fileUploadFlagForPrice: boolean = false;
      uploadDescription1 = 'dataUpload'
      uploadDescription2 = 'energyDataUpload'
      userGroupToPutInDTO = '';
      configurationParameters = new ConfigurationParametersDTO('10', true, false, true,
      true, '', '', true, '5', true, '55', '8.5', true,
      '', '', '20', '47.5', '0.9', true, false, 'ext',
      // tslint:disable-next-line:max-line-length
      true, '25', '9999', false, '200000', '300000', '0', true, '33', '', false, '0.015', '0.5', '10');
      mappings = new MappingDTO('', '', '', '', '', '' , '', '', '', '', '', '', '', '', '', '', '', '', '');
      geHyppoParams = new GeHyppoParametersDTO('', '', this.configurationParameters,
      this.mappings, [], '', '')
      alertMessage = 'This field is required'
      filteredOptions = [];
      myControl: FormControl = new FormControl();
      form: FormGroup;
      variantsArray: FormArray;
      groupedParametersToPost = [];
      graphParametersArray: string[][] = [];
      visible = true;
      selectable = true;
      removable = true;
      separatorKeysCodes: number[] = [ENTER, COMMA];
      parameterCtrl = new FormControl();
      filteredParameters: Observable<string[]>;
      parameters = [] ;
      allParameters: string[] = ['Plant_Power[MW]','ST_Power[MW]','GT1_Power[MW]',
      'Ambient_Temp[degC]','GT1_Primary_Response[MW]', 'GT2_Primary_Response[MW]', 'GT3_Primary_Response[MW]',
      'Plant_Primary_Response[MW]','GT1_Fuel_Consumption[kg/s]','GT2_Fuel_Consumption[kg/s]','GT3_Fuel_Consumption[kg/s]',
      'Plant_Fuel_Consumption[kg/s]',
      'Plant_Power_Corrected[MW]','PFRLDCMD_PLANT_ISO,Price_Electricity[$/MWh]',
      'Response[boolean]','Power/Fuel_ratio[MJ_el/kg_fuel]','GT_All_Power[MW]',
      'ST/GT_Power_ratio[-]','Scen1_Power_Added_GT[MW]','Scen1_Power_Added_Plant[MW]',
      'Scen1_fuel_consumption_incr[kg/s]','Scen1_Power_Fuel_Ratio_incr[MJ_el/kg_fuel]',
      'Scen1_Prim_Resp_GT[MW]','Scen1_Prim_Resp_Batt[MW]','Battery_Energy[MWh]',
      'Battery_Losses_Cumulated[MWh]','Battery_SOC[%]','Scen1_Revenues[$]',
      'Scen1_Profits[$]','Scen1_Saved_Fuel[kg]','Scen2_Profits[$]','Frequency[Hz]',
      'Freq_filtered[Hz]','SFFR_rvn[$]','SFFR_power_bought[MW]','SFFR_energy_cost[$]','SFFR_profit[$]'];
      listOflistParameters = [];
      paramsStringToSend = '';


      // tslint:disable-next-line:max-line-length
      description1 = 'When time gap in data is lasts for more then this value in days, the analysis will be performed individually for each consecutive time interval';
      description2 = 'If True, do not performa analysis when the power plant is not connected to the grid';
      description3 = 'If True, do not performa analysis when the power plant is not connected to the grid';
      // tslint:disable-next-line:max-line-length
      description4 = 'If True, assign NaN value to signals which are out of the ranges defined in the analiti_init. Used to avoid non-physical data anomaly.';
      // tslint:disable-next-line:max-line-length
      description6 = 'If True, create a charts to visualize tag and calculated quantityes. The list of tags to visualize is defined in the main.py file';
      // tslint:disable-next-line:max-line-length
      description7 = 'If True the baseload and the primary reserve will be estimated with a statistical study of the data. If False, please specify baseload and primary reserve values';
      description8 = 'Baseload value, when the power plant is not responding. Ignored if BAR_detect_parameters is True';
      description9 = 'Primary reserve value for the entire power plant. Ignored if BAR_detect_parameters is True';
      description10 = 'Defines the time interval between the rows in the input csv. It should be consistent with data';
      // tslint:disable-next-line:max-line-length
      description11 = 'If True check in the data/pricing folder for a csv file to get the electricity sale prices, along the evaluated period, with the same time interval of the data. If False, uses BAR_alternative_fixed_price';
      description12 = 'Used when BAR_use_price_data_$/MWh is False, as the price of electric energy along the period';
      description13 = 'Price of the capacity primary reserve';
      // tslint:disable-next-line:max-line-length
      description14 = 'If True, designs the battery power to replace the ST (1/3 P_tot) and to last for 30 minutes. If False takes the imput values from BAR_battery_power_MW and BAR_battery_energy_MWH.';
      description15 = 'Used when BAR_auto_size_bat is False, as the power capaticy of the battery ';
      description16 = 'Used when BAR_auto_size_bat is False, as the energy capaticy of the battery';
      description17 = 'The price of gas to be used in the financial analysis';
      description18 = 'Not working, was supposed to estimate the efficinency';
      description19 = 'The efficiency of the battery, and related equipment, both charging and uncharging';
      description20 = 'If True, limits the max of the PFRLDCMD with the calculated value, to tackle bad data which can lead to false stats';
      description21 = 'Look for metadata information about the power plant in the MulReport.csv file in the configuration folder';
      // tslint:disable-next-line:max-line-length
      description22 = 'Choose the nomenclature of the input, osm if coming from GE server, ext if named after specification input specifications';
      description23 = 'Rename output data with spaking names and add the UoM';
      // tslint:disable-next-line:max-line-length
      description24 = 'Determine the share of the battery to be used for capacity reserve. The rest of the battery will be used to increase  the load during primary response operations. If 0, all battery power (if <  1/3 of PR) is used to increase the baseload. If 100, all the battery power is used to increase the capacity reserve of the power plant.';
      // tslint:disable-next-line:max-line-length
      description25 = 'Limit to the additional primary reserve capacity to be sold to the grid. By Default is 9999, so it will not be applied';
      description26 = 'When 1, does a rought design of the battery for being able to sustain 3 black starts operation in a row';
      description27 = 'Price of the battery - Energy';
      description28 = 'Price of the battery equipment - Power';
      description29 = 'Price of the converter to be';
      description30 = 'When 1, stores an .csv containing all the calculated data';
      description31 = 'Limit the gas turbine power augmentation to a percentage of the primary reserve';
      // tslint:disable-next-line:max-line-length
      description32 = 'Defines the outputs shown in the charts. Different charts are separated by semicolum `;` and different outputs are separated by a space `` ';
      // tslint:disable-next-line:max-line-length
      description33 = 'When 1, the "Frequency" entry in the data will be used to simulate a super fast response utilization of the battery. When this mode is active, only results related to SFFR should be considered';
      description34 = 'The deadband in the grid frequency in which the super fast response will not be active';
      description35 = 'The frequency deviation at which the maximum battery power output occurs';
      description36 = 'The size of the power output to be used for super fast response';
      description37 = 'ST Generator Power - 0 if missing';
      // tslint:disable-next-line:max-line-length
      description38 = 'Grid Frequency';
      description39 = 'Ambient temperature - set to 15 degC if missing';
      description40 = 'GT Generator Power';
      description41 = 'Fuel mass flow';
      description42 = '1 when the generator is connected to the grid, 0 otherwise. -all 1 if missing';

      description43 = 'Primary response given by the GT (according to droop and primary reserve)';



      @ViewChild('parameterInput', {static: false}) parameterInput: ElementRef<HTMLInputElement>;
      @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
      @ViewChild('chipList', {static: false}) chipList;

      constructor(private geHyppoService: GeHyppoService,
         private userGroupService: UserGroupService,
         private formBuilder: FormBuilder,
         private appDataService: AppDataService,
         private notificationToastService: NotificationToastService,
         private msgService: InternalMessageService
         ) {

        this.filteredParameters = this.parameterCtrl.valueChanges.pipe(
          startWith(null),
          map((parameter: string | null) => parameter ? this._filter(parameter) : this.allParameters.slice()));
              this.uploader.onProgressItem = (progress: any) => {
              this.uploader_.onProgressItem = (progress_: any) => {
        }
      };
        this.uploader.onCompleteItem = (item: any, response: any , status: any, headers: any) => {
          this.fileUploadFlag = false;
          this.attachmentList.push((item.file.name))
        }

        this.uploader_.onCompleteItem = (item_: any, response_: any , status_: any, headers_: any) => {
          this.fileUploadFlagForPrice = false;
            this.attachmentList_.push((item_.file.name));
          }
      }


      ngOnInit() {
 
      const that = this;
      this.userGroupService.getAllUserGroups().subscribe(data => {
        this.usergroups = data;
        for(let i = 0 ; i< this.usergroups.length;i++){
          if(this.usergroups[i]['name'] === 'me') {
            this.selectedUserGroup = this.usergroups[i]['id']
          }

        }
    });

      this.form = this.formBuilder.group({
        items: this.formBuilder.array([
          this.formBuilder.group({
           listContents: ''
          })
        ]),
      });
      this.graphParametersArray.push([]);


      }

      focusParameterInput(){
        this.parameterInput.nativeElement.blur();
        this.parameterInput.nativeElement.focus();
      }
      getSelectedUserGroup(useGroupSelected) {
        this.userGroupToPutInDTO = useGroupSelected.option.value;
      }

      public loadFiles() {
        this.fileUploadFlag = true;
        this.uploader.uploadAll();
      }

    public loadEnergyCSVFiles() {
      this.fileUploadFlagForPrice = true;
      this.uploader_.uploadAll();
    }

    superFrCondition() {
      if(this.configurationParameters.super_fast_fr === false) {
        this.configurationParameters.grid_deadband_hz = ''
        this.configurationParameters.grid_freq_abs_range = '';
        this.configurationParameters.sffr_power_size_MW = '';
        return true
      } else {
        this.configurationParameters.grid_deadband_hz = '0.015';
        this.configurationParameters.grid_freq_abs_range = '0.5';
        this.configurationParameters.sffr_power_size_MW = '10'
        return false;
      }
    }
    autosizeBatCondition() {
      if(this.configurationParameters.auto_size_bat === true) {
        this.configurationParameters.battery_power_MW = '';
        this.configurationParameters.battery_energy_MWH = '';
        return true
      } else {
        return false
      }

    }

    removeFile(item, description) {
      if (description === 'dataUpload') {
      const removedItemIndex = this.attachmentList.indexOf(item.file.name)
      this.uploader.removeFromQueue(item);
      this.attachmentList.splice(removedItemIndex, 1)
    } else {
      const removedItemIndex = this.attachmentList_.indexOf(item.file.name)
      this.uploader_.removeFromQueue(item);
      this.attachmentList_.splice(removedItemIndex, 1)
    }

    }

    runAlogrithm() {
      this.geHyppoParams.usergroup = this.selectedUserGroup.toString();
      this.geHyppoParams.dataFileNames = this.attachmentList;
      this.geHyppoParams.priceFileName = this.attachmentList_[0];
      this.postGEHyppoDTO();
    }


    webSocketsFeedback() {
      alert('Web sockets feedback just received');
    }

    checkValidationStatus(){
      let status = true;
      if(this.geHyppoParams.name ==='' || this.geHyppoParams.name === null || this.geHyppoParams.name ===undefined){
        this.notificationToastService.showNotification(
          'You have not declared a name for the execution.',
          'error',
          'top',
          'center',
          'danger');
         status = false;
      }
      // tslint:disable-next-line:max-line-length
      else if(this.geHyppoParams.configurationParameters.data_time_interval_sec === ''|| this.geHyppoParams.configurationParameters.data_time_interval_sec === null
    || this.geHyppoParams.configurationParameters.data_time_interval_sec === undefined){
      this.notificationToastService.showNotification(
        'You have not declared any input for "Data Time Interval Sec (seconds)" field .',
        'error',
        'top',
        'center',
        'danger');
        status = false;
    }

      // tslint:disable-next-line:max-line-length
      else if ( this.geHyppoParams.configurationParameters.batt_percent_to_pr === '' || this.geHyppoParams.configurationParameters.batt_percent_to_pr === null
      || this.geHyppoParams.configurationParameters.batt_percent_to_pr === undefined){
        this.notificationToastService.showNotification(
          'You have not declared any input for "Battery % To Pr (%)" field.',
          'error',
          'top',
          'center',
          'danger');
          status = false;
      } else {
      for (let k = 0; k < this.graphParametersArray.length; k++) {
        if (this.graphParametersArray[k].length === 0) {
        // this.chipList.errorState = true;
          const errorList = k + 1
          this.notificationToastService.showNotification(
          'You have  declared no graph parameters in List of Graphs ' + errorList,
          'error',
          'top',
          'center',
          'danger');

          status = false;
        }
      }
    }
      return status
    }


    onSubmit() {
     if ( this.checkValidationStatus() === false) {
       return;
     }

      for (let k = 0; k < this.graphParametersArray.length; k++) {

        if (this.graphParametersArray.length === 1) {
          this.listOflistParameters.push(this.graphParametersArray[k])
          this.listOflistParameters.push(' ')
        } else {
          for (let p = 0; p < this.graphParametersArray[k].length; p++) {

            this.listOflistParameters.push(' ')
            this.listOflistParameters.push(this.graphParametersArray[k][p])
          }
          if (k < this.graphParametersArray.length - 1 ) {
          this.listOflistParameters.push(';')
          }
        }
      }

      console.log(this.listOflistParameters)

      if (this.listOflistParameters.length <= 2){
        this.paramsStringToSend = this.listOflistParameters[0].toString().replace(/,/g , ' ')

      } else {
      this.paramsStringToSend = this.listOflistParameters.toString().replace(/,/g , '').replace(/^\s+/g, '');
    }

    
      this.geHyppoParams.configurationParameters.chart_output_listoflist = this.paramsStringToSend;
      console.log(this.geHyppoParams.configurationParameters.chart_output_listoflist)

      this.geHyppoParams.dataFileNames = this.attachmentList
      this.geHyppoParams.priceFileName = this.attachmentList_[0]
      this.geHyppoParams.usergroup = this.selectedUserGroup.toString();
      console.log(this.geHyppoParams)
      this.postGEHyppoDTO();
      this.listOflistParameters = [];

      this.notificationToastService.showNotification(
        '<h4>HyppoIo execution started!</h4>You will be notified to check the results when execution will be finished.',
        'notifications_active',
        'top',
        'center',
        'warning');
    }

    postGEHyppoDTO() {

    this.geHyppoService.postGeHyppoParameters(this.geHyppoParams).subscribe(
      response => {
      });
    }
    get formArr() {
      return this.form.get('items') as FormArray;
    }

    createItem() {
      return this.formBuilder.group({

       listContents : ''
      })
    }

    removeItem(index: number) {
      this.variantsArray.removeAt(index);
      this.graphParametersArray.splice(index, 1);
    }

    addNext(): void {
      this.variantsArray = this.form.get('items') as FormArray
      this.variantsArray.push(this.formBuilder.group({
       listContents: ''
      }));

      this.graphParametersArray.push([]);
      //(this.form.controls['items'] as FormArray).push(this.createItem())
    }
    add(event: MatChipInputEvent, eventIndex: number): void {
      // Add parameter only when MatAutocomplete is not open
      // To make sure this does not conflict with OptionSelected Event
      if (!this.matAutocomplete.isOpen) {
        const input = event.input;
        const value = event.value;

        // Add our parameter
        if ((value || '').trim()) {
          this.graphParametersArray[eventIndex].push(value.trim());
        }

        // Reset the input value
        if (input) {
          input.value = '';
        }
      }

    }

    removeGraphParameter(parameter: string, indice: number): void {
      const graphItemIndex = this.graphParametersArray[indice].indexOf(parameter);
    if (graphItemIndex >= 0) {
      this.graphParametersArray[indice].splice(graphItemIndex, 1);
    }
    }

    selected(event: MatAutocompleteSelectedEvent, eventIndex: number): void {
      this.groupedParametersToPost.push(event.option.value)
      this.graphParametersArray[eventIndex].push(event.option.value);



      this.parameterInput.nativeElement.value = '';
      this.parameterCtrl.setValue(null);
    }

    private _filter(value: string): string[] {
      const filterValue = value.toLowerCase();

      return this.allParameters.filter(parameter => parameter.toLowerCase().indexOf(filterValue) === 0);
    }



    }
