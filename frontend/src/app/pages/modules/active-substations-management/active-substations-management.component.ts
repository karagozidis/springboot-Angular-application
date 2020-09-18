import { Component, OnInit, Input } from '@angular/core';
import { MatTableDataSource, DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDatepickerInputEvent } from '@angular/material';
import { ActiveSubstationInputOperationConstraintsDTO } from 'app/shared/dto/activeSubstationInputOperationConstraints-dto';
import * as moment from 'moment';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import { ActiveSubstationInputFlexibilityServiceScheduleDTO } from 'app/shared/dto/activeSubstationInputFlexibilityServiceSchedule-dto';
import { ActiveSubstationInputHeaderDTO } from 'app/shared/dto/activeSubstationInputHeader-dto';
import { ActiveSubstationInputCommandsDTO } from 'app/shared/dto/activeSubstationInputCommands-dto';
import { ActiveSubstationInputDataFilesDTO } from 'app/shared/dto/activeSubstationInputDataFiles-dto';
import { AppConstants } from 'app/shared/config/app-constants';
import { ActiveSubstationInputDTO } from 'app/shared/dto/activeSubstationInput-dto';
import { ActiveSubstationInputMetadataDTO } from 'app/shared/dto/activeSubstationInputMetadata-dto';
import { ActiveSubstationService } from 'app/services/activeSubstation.service';
import { DatePipe } from '@angular/common';
import { AppDataService } from 'app/services/app-data.service';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-active-substations-management',
  templateUrl: './active-substations-management.component.html',
  styleUrls: ['./active-substations-management.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

      {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})

export class ActiveSubstationsManagementComponent implements OnInit {


  latestDates: Map<string, Date> = new Map();
  power_reserve = 'Angular 5';
  displayedColumns = ['service', 'power_reserve', 'period_of_activation','select_deselect_all' ];
  dataSource = SERVICES_SCHEMA
  selectedService = '1'
  selectedResolution = '1'
  selectAll = false;
  periodsArray: boolean[][] = []
  rows = 10
  columns = 24
  periodArray: Boolean[][];
  cellUpdatedState: Boolean[][];
  booleanValuesFromSysOperator: Boolean[][];
  selectedCellInTheRow = []
  activeSubstationInput = new ActiveSubstationInputDTO(new ActiveSubstationInputHeaderDTO(),new ActiveSubstationInputCommandsDTO(),new ActiveSubstationInputDataFilesDTO(),new ActiveSubstationInputOperationConstraintsDTO(),
  new ActiveSubstationInputFlexibilityServiceScheduleDTO(),new ActiveSubstationInputMetadataDTO())
  activeSubstationInputHeader = new ActiveSubstationInputHeaderDTO();
  activeSubstationInputCommands = new ActiveSubstationInputCommandsDTO();
  activeSubstationInputDataFiles = new ActiveSubstationInputDataFilesDTO();
  activeSubstationInputOperationConstraints = new ActiveSubstationInputOperationConstraintsDTO();
  activeSubstationInputFlexibilityServices = new ActiveSubstationInputFlexibilityServiceScheduleDTO()
  activeSubstationInputMetadata = new ActiveSubstationInputMetadataDTO()
  services = [];
  today: Date = new Date();
  currentYear: number = this.today.getFullYear();
 currentMonth: number = new Date().getMonth();
 currentDay: number = this.today.getDate();
 tomorrow:number = new Date().getDate()+1
 displayValueCmrPrice
 displayValueGeneration
 displayValueDemand
 displayValuePvPower
 displayValuePbpPrice
 displayValuePvPrice
 displayValueSettlementPrice
 displayValueTieLinePowerFlow
 displayValueWholeSalePrice
 displayValueWindPower
 /* displayValueCmrPrice = new Date()
 displayValueGeneration = new Date()
 displayValueDemand = new Date()
 displayValuePvPower = new Date()
 displayValuePbpPrice = new Date()
 displayValuePvPrice = new Date()
 displayValueTieLinePowerFlow = new Date()
 displayValueWholeSalePrice = new Date()
 displayValueWindPower = new Date() */

  powerBalance:boolean = false
  /* public minDate: Object = new Date(this.currentYear, this.currentMonth, this.currentDay);
  public maxDate: Object =  new Date(this.currentYear, this.currentMonth, this.tomorrow); */
  minDate = new Date(this.currentYear, this.currentMonth, this.currentDay)
  maxDate = new Date(this.currentYear, this.currentMonth, this.tomorrow)
  displayValue = new Date();
  systemOperatorModuleIncluded = false
  substationOperatorModuleIncluded = false
  importForecastedDataModuleIncluded = false
  abortExecution = false;
  cmrPriceLabel = 'CMR Price'
  generationLabel = 'Generation'
  demandLabel = 'Demand'
  pvPowerLabel = 'Pv Power'
  pvPriceLabel = 'Pv Price'
  pbpPriceLabel = 'Pbp Price';
  settlementPriceLabel = 'Settlement Price'
  tieLinePowerFlowLabel = 'TieLinePowerFlow'
  wholeSalePriceLabel = 'WholeSale Price'
  windPowerLabel = 'Wind Power'



  constructor(private notificationToastService: NotificationToastService, private activeSubstationService: ActiveSubstationService,
    private dp: DatePipe, public appDataService: AppDataService) { }

  ngOnInit() {


    this.initializeCellsArray();
    console.log(JSON.parse(localStorage.getItem(AppConstants.StorageLabels.SCHEDULED_SERVICES)))
    this.getFromLocalStorage();
    this.getLatestDates();
    this.getLatestScheduledServices();

  }
  getLatestDates(){
    this.activeSubstationService.getLatestDates().subscribe(result=>{
      this.latestDates = result;
      console.log(this.latestDates)

      if(('cmrPrice' in this.latestDates)===true){
        this.displayValueCmrPrice = this.latestDates['cmrPrice'];
        console.log(this.displayValueCmrPrice)
      }
      else{
        this.displayValueCmrPrice = ''
        this.cmrPriceLabel = 'No Available Data'
      }
     if(('generation' in this.latestDates)===true){
      this.displayValueGeneration = this.latestDates['generation'];
     }
     else{
      this.displayValueGeneration = ''
      this.generationLabel = 'No Available Data'
    }
     if(('demand' in this.latestDates)===true){
      this.displayValueDemand = this.latestDates['demand'];
     }
     else{
      this.displayValueDemand = ''
      this.demandLabel = 'No Available Data'
    }

     if(('pvPower' in this.latestDates)===true){
      this.displayValuePvPower = this.latestDates['pvPower'];
     }
     else{
      this.displayValuePvPower = ''
      this.pvPowerLabel = 'No Available Data'
    }
     if(('pvPrice' in this.latestDates)===true){
      this.displayValuePvPrice = this.latestDates['pvPrice'];
     }
     else{
      this.displayValuePvPrice = ''
      this.pvPriceLabel = 'No Available Data'
    }
     if(('pbpPrice' in this.latestDates)===true){
      this.displayValuePbpPrice = this.latestDates['pbpPrice'];
     }
     else{
      this.displayValuePbpPrice =''
      this.pbpPriceLabel = 'No Available Data'
    }
    if(('settlementPrice' in this.latestDates)===true){
      this.displayValueSettlementPrice = this.latestDates['settlementPrice'];
     }
     else{
      this.displayValueSettlementPrice = ''
      this.settlementPriceLabel = 'No Available Data'
    }
     if(('tieLinePowerFlow' in this.latestDates)===true){
      this.displayValueTieLinePowerFlow = this.latestDates['tieLinePowerFlow'];
     }
     else{
      this.displayValueTieLinePowerFlow = ''
      this.tieLinePowerFlowLabel = 'No Available Data'
    }
     if(('wholesalePrice' in this.latestDates)===true){
      this.displayValueWholeSalePrice = this.latestDates['wholesalePrice'];
     }
     else{
      this.displayValueWholeSalePrice = ''
      this.wholeSalePriceLabel = 'No Available Data'
    }
     if(('windPower' in this.latestDates)===true){
      this.displayValueWindPower = this.latestDates['windPower'];
     }
     else{
      this.displayValueWindPower = ''
      this.windPowerLabel = 'No Available Data'
    }

    });
  }

  getLatestScheduledServices(){
    this.activeSubstationService.getLatestScheduleServicesArray(this.appDataService.userDto.activeSubstationUserGroupId).subscribe(latestServices=>{
      console.log(latestServices)
      console.log(this.booleanValuesFromSysOperator)
      this.cellUpdatedState = this.mapTimePeriodsToBooleans(latestServices)
      console.log(this.cellUpdatedState)

      for(let k=0;k<10;k++){
        for(let p=0;p<24;p++){
          if(this.cellUpdatedState[k].includes(false)){
            continue;
          }
          else{
            this.selectedCellInTheRow[k]=true;
          }
        }
      }
    })
  }

  updateAllCellsInRowCheckedState(row){
        if(this.selectedCellInTheRow[row] === true){
          for(let k=0;k<24;k++){
            this.cellUpdatedState[row][k] = true
            //this.periodArray[row][k]=true
          }
        }
        else{
          for(let k=0;k<24;k++){
             this.cellUpdatedState[row][k] = false;
            //this.periodArray[row][k]=false
          }
        }
        console.log('cellUpdatedState'+this.cellUpdatedState)

  }
  getFromLocalStorage(){
    if(JSON.parse(localStorage.getItem(AppConstants.StorageLabels.SCHEDULED_SERVICES))!=null && this.appDataService.userDto.modulesCrm.includes('substation-operator')){
      console.log('substation with values in local storage')
      console.log(JSON.parse(localStorage.getItem(AppConstants.StorageLabels.SCHEDULED_SERVICES)))
      console.log(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY))
    }
    if(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!=='' && localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!==null && localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.eint = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.eint = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!=='' && localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!==null && localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.eter = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.eter = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!=='' && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!==null && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.emin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.emin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!=='' && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!==null && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.emax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.emax = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!=='' && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!==null && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!=='undefined'){
      this.activeSubstationInputOperationConstraints.pchmin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pchmin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!=='' && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!==null && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!=='undefined'){
      this.activeSubstationInputOperationConstraints.pchmax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pchmax = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!=='' && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!==null && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!=='undefined'){
      this.activeSubstationInputOperationConstraints.pdismin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pdismin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!=='' && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!==null && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!==undefined && localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!=='undefined'){
      this.activeSubstationInputOperationConstraints.pdismax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pdismax = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY)!=='' && localStorage.getItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY)!==null && localStorage.getItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.etach = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY))
    }
    else{
      this.activeSubstationInputOperationConstraints.etach = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY)!=='' && localStorage.getItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY)!==null && localStorage.getItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY)!==undefined && localStorage.getItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY)!=='undefined'){
      this.activeSubstationInputOperationConstraints.etadis = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY))
    }
    else{
      this.activeSubstationInputOperationConstraints.etadis = ''
    }
  }




  initializeCellsArray(){

    for(let i=0;i<10;i++){
      this.selectedCellInTheRow.push(i)
      this.selectedCellInTheRow[i] = false
    }
    //this.periodArray = [];
    /* for(let i=0;i<12;i++){
      this.periodArray[i] = [];
      for(let k=0;k<24;k++){
        this.periodArray[i][k] = false
      }
    } */
    this.cellUpdatedState = []
    this.booleanValuesFromSysOperator = [];
    for(let i=0;i<10;i++){
      this.cellUpdatedState[i] = [];
      this.booleanValuesFromSysOperator[i] = [];
      for(let k=0;k<24;k++){
        this.cellUpdatedState[i][k] = false
        this.booleanValuesFromSysOperator[i][k] = false
      }
    }
    /* for(let i=0;i<2;i++){
      this.booleanValuesFromSysOperator[i] = [];
      for(let k=0;k<24;k++){
        this.booleanValuesFromSysOperator[i][k] = false
      }
    } */
  }
  catchDateEvent(type: string, event: MatDatepickerInputEvent<Date>, dataFileVar){
    if(dataFileVar ==='displayValueCmrPrice'){
      this.displayValueCmrPrice = new Date(event.value)
    }
    if(dataFileVar ==='displayValueGeneration'){
      this.displayValueGeneration = new Date(event.value)
    }

    if(dataFileVar ==='displayValueDemand'){
      this.displayValueDemand = new Date(event.value)
    }
    if(dataFileVar ==='displayValuePvPower'){
      this.displayValuePvPower = new Date(event.value)
    }
    if(dataFileVar ==='displayValuePbpPrice'){
      this.displayValuePbpPrice = new Date(event.value)
    }
    if(dataFileVar ==='displayValuePvPrice'){
      this.displayValuePvPrice = new Date(event.value)
    }
    if(dataFileVar ==='displayValueTieLinePowerFlow'){
      this.displayValueTieLinePowerFlow = new Date(event.value)
    }
    if(dataFileVar ==='displayValueWholeSalePrice'){
      this.displayValueWholeSalePrice = new Date(event.value)
    }
    if(dataFileVar ==='displayValueWindPower'){
      this.displayValueWindPower = new Date(event.value)
    }

  }

  mapTimePeriodsToBooleans(timePeriods:any[]){
    console.log(timePeriods)
    let currentIndex = 0;
    let convertedBooleanArray:Boolean[][];
    let temporaryBooleanValue:Boolean[][]
    convertedBooleanArray = [];
    temporaryBooleanValue = [];
    for(let i=0;i<10;i++){
      convertedBooleanArray[i] = [];
      temporaryBooleanValue[i] = [];
      for(let k=0;k<24;k++){
        //convertedBooleanArray[i][k] = false
        if(i===0){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['inertiaEmulation'])
        }
        else if(i===1){
           convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['frequencyRegulation'])
        }
        else if(i===2){
          console.log(timePeriods['modalFrequencies'])

          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['modalFrequencies'])
          console.log(convertedBooleanArray[i])
        }
         else if(i===3){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['voltageRegulation'])
        }
        else if(i===4){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['resourceVariability'])
        }
        else if(i===5){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['powerShifting'])
        }
        else if(i===6){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['rampRate'])
        }
        else if(i===7){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['powerCurtailment'])
        }
        else if(i===8){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['powerBalance'])
        }
        else if(i===9){
          convertedBooleanArray[i] = this.mapServiceArrayToBooleans(timePeriods['congestionRelief'])
        }
      }
    }
    console.log(convertedBooleanArray)
    return convertedBooleanArray;


  }
  mapServiceArrayToBooleans(serviceArray:any[]){

      let updatedServiceArray = []
      let serviceArrayWithBooleans:Number[] = [];
      let results:Boolean[] = [];

      updatedServiceArray = serviceArray.map(x => x-1)
      for(let i=0;i<24;i++){
        serviceArrayWithBooleans[i] = i;

      }
      results =  serviceArrayWithBooleans.map(n =>updatedServiceArray.includes(n))
      return results
  }
  runExecution(){

    console.log(this.cellUpdatedState)
    this.setInLocalStorage()
    console.log(localStorage)

    this.initializeBasicInformation();
    this.initializeScheduleArray();
    if(this.abortExecution === true){
      return
    }
    else{
    this.activeSubstationInput.header =this.activeSubstationInputHeader;
    this.activeSubstationInput.commands = this.activeSubstationInputCommands
    this.activeSubstationInput.dataFiles = this.activeSubstationInputDataFiles;
    this.activeSubstationInput.operationConstraints = this.activeSubstationInputOperationConstraints;
    this.activeSubstationInputFlexibilityServices = this.activeSubstationInputFlexibilityServices;
    this.activeSubstationInput.metadata = this.activeSubstationInputMetadata;

    this.activeSubstationInput = new ActiveSubstationInputDTO(this.activeSubstationInput.header,this.activeSubstationInput.commands,this.activeSubstationInput.dataFiles,
      this.activeSubstationInput.operationConstraints,this.activeSubstationInputFlexibilityServices,this.activeSubstationInput.metadata)

    console.log('Final DTO: '+JSON.stringify(this.activeSubstationInput))
    console.log('Sys Operator Final DTO: '+(JSON.stringify(this.activeSubstationInputFlexibilityServices)))
     if(this.appDataService.userDto.modulesCrm.includes('system-operator')===true){
       this.activeSubstationService.postLatestScheduleServicesArray(this.activeSubstationInputFlexibilityServices, this.appDataService.userDto.activeSubstationUserGroupId).subscribe(response =>{
          console.log(response)
       });
     }
     else{
        this.activeSubstationService.postInputFile(this.activeSubstationInput).subscribe(
        response => {
      });
     }


      this.notificationToastService.showNotification(
        '<h4>The Input File process started!</h4>You will be notified to check the results when execution will be finished.',
        'notifications_active',
        'top',
        'center',
        'info');

    this.clearAllDTOs()
  }

  }
  setInLocalStorage(){
    localStorage.setItem(AppConstants.StorageLabels.SCHEDULED_SERVICES, JSON.stringify(this.cellUpdatedState))
    localStorage.setItem(AppConstants.StorageLabels.INITIAL_ENERGY, JSON.stringify(this.activeSubstationInputOperationConstraints.eint))
    localStorage.setItem(AppConstants.StorageLabels.FINAL_ENERGY, JSON.stringify(this.activeSubstationInputOperationConstraints.eter))
    localStorage.setItem(AppConstants.StorageLabels.MINIMUM_ENERGY, JSON.stringify(this.activeSubstationInputOperationConstraints.emin))
    localStorage.setItem(AppConstants.StorageLabels.MAXIMUM_ENERGY, JSON.stringify(this.activeSubstationInputOperationConstraints.emax))
    localStorage.setItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE, JSON.stringify(this.activeSubstationInputOperationConstraints.pchmin))
    localStorage.setItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE, JSON.stringify(this.activeSubstationInputOperationConstraints.pchmax))
    localStorage.setItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE, JSON.stringify(this.activeSubstationInputOperationConstraints.pdismin))
    localStorage.setItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE, JSON.stringify(this.activeSubstationInputOperationConstraints.pdismax))
    localStorage.setItem(AppConstants.StorageLabels.CHARGE_EFFICIENCY, JSON.stringify(this.activeSubstationInputOperationConstraints.etach))
    localStorage.setItem(AppConstants.StorageLabels.DISCHARGE_EFFICIENCY, JSON.stringify(this.activeSubstationInputOperationConstraints.etadis))
  }

  activateCommandBasedOnService(serviceNumber){

    this.activeSubstationInputCommands.drm = false
    this.activeSubstationInputCommands.bms = false
    this.activeSubstationInputCommands.ems = false
    this.activeSubstationInputCommands.gso = false
    this.activeSubstationInputCommands.pms = false
    switch(serviceNumber){
      case '1':{
        this.activeSubstationInputCommands.drm = true;
        break;
      }
      case '2':{
        this.activeSubstationInputCommands.bms = true;
        break;
      }
      case '3':{
        console.log('here')
        this.activeSubstationInputCommands.ems = true;
        break;
      }
      case '4':{
        this.activeSubstationInputCommands.gso = true;
        break;
      }
      case '5':{
        this.activeSubstationInputCommands.pms = true;
        break;
      }
      default:{
        this.activeSubstationInputCommands.drm = false
        this.activeSubstationInputCommands.bms = false
        this.activeSubstationInputCommands.ems = false
        this.activeSubstationInputCommands.gso = false
        this.activeSubstationInputCommands.pms = false
        break;
      }
    }
  }

  clearAllDTOs(){
    this.activeSubstationInput = new ActiveSubstationInputDTO(new ActiveSubstationInputHeaderDTO(),new ActiveSubstationInputCommandsDTO(),new ActiveSubstationInputDataFilesDTO(),new ActiveSubstationInputOperationConstraintsDTO(),
    new ActiveSubstationInputFlexibilityServiceScheduleDTO(),new ActiveSubstationInputMetadataDTO())
    this.activeSubstationInputHeader = new ActiveSubstationInputHeaderDTO();
    this.activeSubstationInputCommands = new ActiveSubstationInputCommandsDTO();
    this.activeSubstationInputDataFiles = new ActiveSubstationInputDataFilesDTO();
    //this.activeSubstationInputOperationConstraints = new ActiveSubstationInputOperationConstraintsDTO();
    this.activeSubstationInputFlexibilityServices = new ActiveSubstationInputFlexibilityServiceScheduleDTO()
    this.activeSubstationInputMetadata = new ActiveSubstationInputMetadataDTO()
  }

  initializeBasicInformation(){
    this.activeSubstationInputHeader.service = this.selectedService;
    this.activateCommandBasedOnService(this.selectedService);
    this.activeSubstationInputHeader.clientId = null;
    this.activeSubstationInputHeader.clientInterface = AppConstants.ActiveSubstation.Headers.CLIENT_INTERFACE;
    this.activeSubstationInputHeader.date = this.dp.transform (new Date(), 'yyyy-MM-ddThh:mm:ss') + 'Z'
    this.activeSubstationInputHeader.realeseTime = this.activeSubstationInputHeader.date;

    this.activeSubstationInputCommands.clientEnquiry = AppConstants.ActiveSubstation.Commands.CLIENT_ENQUIRY;
    this.activeSubstationInputCommands.serverAcknowledge = AppConstants.ActiveSubstation.Commands.SERVER_ACKNOWLEDGE

    let dataFilesCmrPriceDate = this.displayValueCmrPrice!==''? this.dp.transform(this.displayValueCmrPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z' : null
    let dataFilesDemandDate = this.displayValueDemand!==''? this.dp.transform(this.displayValueDemand, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesGenerationDate = this.displayValueGeneration!==''?this.dp.transform(this.displayValueGeneration, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesPbpPriceDate = this.displayValuePbpPrice!==''?this.dp.transform(this.displayValuePbpPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesPvPowerDate = this.displayValuePvPower!==''?this.dp.transform(this.displayValuePvPower, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesPvPriceDate = this.displayValuePvPrice!==''?this.dp.transform(this.displayValuePvPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesSettlementPriceDate = this.displayValueSettlementPrice!==''?this.dp.transform(this.displayValueSettlementPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesTieLinePowerFlowDate = this.displayValueTieLinePowerFlow!==''?this.dp.transform(this.displayValueTieLinePowerFlow, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesWholeSalePriceDate = this.displayValueWholeSalePrice!==''?this.dp.transform(this.displayValueWholeSalePrice, 'yyyy-MM-ddThh:mm:ss') + 'Z':null
    let dataFilesWindPowerDate = this.displayValueWindPower!==''?this.dp.transform(this.displayValueWindPower, 'yyyy-MM-ddThh:mm:ss') + 'Z':null

    this.activeSubstationInputDataFiles.cmrPrice= dataFilesCmrPriceDate
    this.activeSubstationInputDataFiles.demand= dataFilesDemandDate
    this.activeSubstationInputDataFiles.generation = dataFilesGenerationDate
    this.activeSubstationInputDataFiles.pbpPrice =dataFilesPbpPriceDate
    this.activeSubstationInputDataFiles.pvPower=dataFilesPvPowerDate
    this.activeSubstationInputDataFiles.pvPrice=dataFilesPvPriceDate
    this.activeSubstationInputDataFiles.settlementPrice = dataFilesSettlementPriceDate
    this.activeSubstationInputDataFiles.tieLinePowerFlow=dataFilesTieLinePowerFlowDate
    this.activeSubstationInputDataFiles.wholesalePrice=dataFilesWholeSalePriceDate
    this.activeSubstationInputDataFiles.windPower=dataFilesWindPowerDate


    this.activeSubstationInputMetadata.uuid = ""
    this.activeSubstationInputMetadata.usergroup = this.appDataService.userDto.activeSubstationUserGroupId.toString();
    this.activeSubstationInputMetadata.status = null;





  }

  initializeScheduleArray(){
    this.abortExecution = false
    let totalPercentage = 0;
    this.services = this.dataSource.map((row: ScheduledServicesSchema) => {
      return { power_reserve: row.power_reserve}
    });
    this.mapServicesWithPercentages(this.services)

    for(let i=0;i<this.services.length;i++){
        if(this.services[i]['power_reserve']!==undefined){
          totalPercentage+=+(this.services[i]['power_reserve'])
          console.log(totalPercentage)
          if(totalPercentage>100){
            this.notificationToastService.showNotification('The total percentage of power reserve should be less than or equal to 100%.',
            'error',
            'top',
            'center',
            'danger');
            this.abortExecution = true;
          return;
          }
        }
    }
    this.translatePeriodArrayToHours(this.cellUpdatedState);
  }



  translatePeriodArrayToHours(arrayToTranslate:Boolean[][]){

    for(let k=0;k<10;k++){
      for(let i=0;i<24;i++){

          if(arrayToTranslate[k][i]===true && k===0){
            this.activeSubstationInputFlexibilityServices.inertiaEmulation.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===1){
            this.activeSubstationInputFlexibilityServices.frequencyRegulation.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===2){
            this.activeSubstationInputFlexibilityServices.modalFrequencies.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===3){
            this.activeSubstationInputFlexibilityServices.voltageRegulation.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===4){
            this.activeSubstationInputFlexibilityServices.resourceVariability.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===5){
            this.activeSubstationInputFlexibilityServices.powerShifting.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===6){
            this.activeSubstationInputFlexibilityServices.rampRate.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===7){
            this.activeSubstationInputFlexibilityServices.powerCurtailment.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===8){
            this.activeSubstationInputFlexibilityServices.powerBalance.push(i+1)
          }
          else if(arrayToTranslate[k][i]===true && k===9){
            this.activeSubstationInputFlexibilityServices.congestionRelief.push(i+1)
          }
          this.activeSubstationInputFlexibilityServices.powerBalanceReserve = this.powerBalance;

      }
    }

  }

  mapServicesWithPercentages(services){
    console.log(services)
    services[0]['power_reserve']!=='' || services[0]['power_reserve']!==undefined ?this.activeSubstationInputFlexibilityServices.inertiaEmulationPercentage = services[0]['power_reserve'] : this.activeSubstationInputFlexibilityServices.inertiaEmulationPercentage
    services[1]['power_reserve']!=='' || services[1]['power_reserve']!==undefined ? this.activeSubstationInputFlexibilityServices.frequencyRegulationPercentage= services[1]['power_reserve'] : this.activeSubstationInputFlexibilityServices.frequencyRegulationPercentage
    services[2]['power_reserve']!=='' || services[2]['power_reserve']!==undefined ?this.activeSubstationInputFlexibilityServices.modalFrequenciesPercentage=services[2]['power_reserve'] : this.activeSubstationInputFlexibilityServices.modalFrequenciesPercentage
    services[3]['power_reserve']!=='' || services[3]['power_reserve']!==undefined ?this.activeSubstationInputFlexibilityServices.voltageRegulationPercentage=services[3]['power_reserve'] : this.activeSubstationInputFlexibilityServices.voltageRegulationPercentage
    services[4]['power_reserve']!== ''|| services[4]['power_reserve']!==undefined ?this.activeSubstationInputFlexibilityServices.resourceVariabilityPercentage=services[4]['power_reserve'] : this.activeSubstationInputFlexibilityServices.resourceVariabilityPercentage
    services[5]['power_reserve']!== ''|| services[5]['power_reserve']!==undefined ?this.activeSubstationInputFlexibilityServices.powerShiftingPercentage=services[5]['power_reserve'] : this.activeSubstationInputFlexibilityServices.powerShiftingPercentage
    services[6]['power_reserve']!=='' || services[6]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.rampRatePercentage=services[6]['power_reserve'] : this.activeSubstationInputFlexibilityServices.rampRatePercentage
    services[7]['power_reserve']!== ''|| services[7]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.powerCurtailmentPercentage=services[7]['power_reserve'] : this.activeSubstationInputFlexibilityServices.powerCurtailmentPercentage
    services[8]['power_reserve']!=='' || services[8]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.powerBalancePercentage=services[8]['power_reserve'] : this.activeSubstationInputFlexibilityServices.powerBalancePercentage
    services[9]['power_reserve']!=='' || services[9]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.congestionReliefPercentage=services[9]['power_reserve'] : this.activeSubstationInputFlexibilityServices.congestionReliefPercentage
   /*  services[10]['power_reserve']!==''|| services[10]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.powerBalanceReserveUpPercentage=services[10]['power_reserve'] : this.activeSubstationInputFlexibilityServices.powerBalanceReserveUpPercentage
    services[11]['power_reserve']!==''|| services[11]['power_reserve']!==undefined?this.activeSubstationInputFlexibilityServices.powerBalanceReserveDownPercentage=services[11]['power_reserve'] : this.activeSubstationInputFlexibilityServices.powerBalanceReserveDownPercentage */
  }



}

export interface ScheduledServicesSchema {
  service: string;
  power_reserve: string;
  period_of_activation: boolean;
  select_deselect_all: boolean;

}
export class PeriodPositionsSchema{
  positionsArray:any[] = [];
  periodArrayIndex:any[][]= []
}

const SERVICES_SCHEMA: ScheduledServicesSchema[] = [
  {service: 'Inertia Emulation', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Frequency Regulation', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Modal Frequencies', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Voltage Regulation', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Resource Variability', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Power Shifting', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Ramp Rate', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Power Curtailment', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Power Balance', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Congestion Relief', power_reserve: '', select_deselect_all:false, period_of_activation:false}

];
