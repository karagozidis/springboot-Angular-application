import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MatTableDataSource, DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDatepickerInputEvent, MatDialog } from '@angular/material';
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
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DialogListComponent } from 'app/shared/components/dialog-list/dialog-list.component';
import { ActiveSubstationStateService } from 'app/services/activeSubstationState.service';
import { LoadingStateService } from 'app/shared/utils/loading-state.service';
import { ActiveSubstationOfferDTO } from 'app/shared/dto/activeSubstationOffer-dto';

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
  selector: 'app-active-substations-results',
  templateUrl: './active-substations-results.component.html',
  styleUrls: ['./active-substations-results.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

      {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})

export class ActiveSubstationsResultsComponent implements OnInit {


  latestDates: Map<string, Date> = new Map();
  power_reserve = 'Angular 5';
  displayedColumnsEnergyMarket = ['service', 'period_of_activation' ];
  displayedColumnsPowerBalanceProvision = ['service', 'period_of_activation' ];
  displayedColumnsCongestionManagementRelief = ['service', 'period_of_activation'];
  displayedColumnsPricesSLCServicesOne = ['service', 'period_of_activation' ];
  displayedColumnsPricesSLCServicesTwo = ['service', 'period_of_activation' ];
  displayedColumnsProfitsAndCosts = ['quantity','expected','imported']
  dataSource = SERVICES_SCHEMA
  dataSourceEnergyMarket = ENERGY_MARKET_SCHEMA;
  dataSourcePowerBalanceProvision = POWER_BALANCE_PROVISION_SCHEMA;
  dataSourceCongestionManagemetRelief = CONGESTION_MANAGEMENT_RELIEF_SCHEMA;
  dataSourcePricesOne = PRICES_SLC_SERVICES_ONE_SCHEMA;
  dataSourcePricesTwo = PRICES_SLC_SERVICES_TWO_SCHEMA;
  dataSourceProfitsAndCosts = PROFITS_AND_COSTS_SCHEMA

  selectedService = '1'
  selectedResolution = '1'
  selectAll = false;
  periodsArray: boolean[][] = []
  periodArray: Boolean[][];
  cellUpdatedState: Boolean[][];
  energyMarketArray: string[][];
  powerBalanceProvisionArray: string[][]
  congestionManagementReliefArray: string[][];
  pricesServiceFirstArray: string[][];
  pricesServiceSecondArray: string[][];
  outputData:any
  selectedCellInTheRow = []
  noCheckBoxSelected= [];
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
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  graphData = '[{"name":"Energy","x":["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"],"y":["1","2","5","8","5","16","37","8","49","10","21","42","13","14","15","16","17","18","19","20","21","22","23","24"], "mode":"lines+markers", "type": "scatter", "line": {"shape": "hv"}}]'

  public graphEnergyMarket = {
    data: [],
    layout: { hovermode: 'closest',
    autosize: true,
    // height:900 ,
    // width:1500,
  //  title: 'OnCloudAppMetrics Graph',
    }, config: { toImageButtonOptions: {
      format: 'png',
      filename: 'test',
      height: 900,
      width: 1500
    }
  }

  };
  public graphCongestionManagementRelief = {
    data: [],
    layout: { hovermode: 'closest',
    autosize: true,
    // height:900 ,
    // width:1500,
  //  title: 'OnCloudAppMetrics Graph',
    }, config: { toImageButtonOptions: {
      format: 'png',
      filename: 'test',
      height: 900,
      width: 1500
    }
  }

  };
  public graphPowerBalanceProvision = {
    data: [],
    layout: { hovermode: 'closest',
    autosize: true,
    // height:900 ,
    // width:1500,
  //  title: 'OnCloudAppMetrics Graph',
    }, config: { toImageButtonOptions: {
      format: 'png',
      filename: 'test',
      height: 900,
      width: 1500
    }
  }

  };
  public graphPricesSLCServices1 = {
    data: [],
    layout: { hovermode: 'closest',
    autosize: true,
    // height:900 ,
    // width:1500,
  //  title: 'OnCloudAppMetrics Graph',
    }, config: { toImageButtonOptions: {
      format: 'png',
      filename: 'test',
      height: 900,
      width: 1500
    }
  }

  };
  public graphPricesSLCServices2 = {
    data: [],
    layout: { hovermode: 'closest',
    autosize: true,
    // height:900 ,
    // width:1500,
  //  title: 'OnCloudAppMetrics Graph',
    }, config: { toImageButtonOptions: {
      format: 'png',
      filename: 'test',
      height: 900,
      width: 1500
    }
  }

  };



  constructor(private notificationToastService: NotificationToastService, private activeSubstationService: ActiveSubstationService,private loadingStateService: LoadingStateService,
    private dp: DatePipe, public appDataService: AppDataService,private _formBuilder: FormBuilder,private dialog: MatDialog, private activeSubstationStateService: ActiveSubstationStateService) { }

  ngOnInit() {
    this.activeSubstationService.offerGenerated = true;
    this.createGraphData()
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });


    this.initializeCellsArray();
    this.initializeOutputDataArrays()
    console.log(JSON.parse(localStorage.getItem(AppConstants.StorageLabels.SCHEDULED_SERVICES)))
    this.getFromLocalStorage();
    this.getLatestDates();
    this.getLatestScheduledServices();


  }
  /* getOutPutData(){
    this.activeSubstationService.getOutputInfo(this.activeSubstationStateService.historyResult).subscribe(result=>{
      console.log(result)
      if(result!==null && result!==undefined){
        this.refresh();
      }else{
        return
      }
      this.outputData = result
    })
  } */

  refresh(){

  }

  acceptOffer(){
    const dialogRef = this.dialog.open(DialogListComponent, {
      minWidth: '20%',
      minHeight: '30%',
      data:{
        compAction:'acceptOffer'
      }
    })
    dialogRef.afterClosed().subscribe(result => {
      console.log(result)
    if(this.activeSubstationService.acceptOffer === false){
      return;
    }
     this.refresh();
     this.notificationToastService.showNotification(
      '<h4>The substation operator will receive the approval of the offer.',
      'notifications_active',
      'top',
      'center',
      'info');
    });

  }

  rejectOffer(){
    const dialogRef = this.dialog.open(DialogListComponent, {
      minWidth: '20%',
      minHeight: '30%',
      data:{
        compAction:'rejectOffer'
      }
    })
    dialogRef.afterClosed().subscribe(result => {
      if(this.activeSubstationService.rejectOffer === false){
        return;
      }
     this.refresh();
     this.notificationToastService.showNotification(
      '<h4>The substation operator will receive the rejection of the offer.',
      'notifications_active',
      'top',
      'center',
      'info');
    });
  }

  sendOffer(){
    const dialogRef = this.dialog.open(DialogListComponent, {
      minWidth: '20%',
      minHeight: '30%',
      data:{
        compAction:'sendOffer'
      }
    })
    dialogRef.afterClosed().subscribe(result => {
      if(this.activeSubstationService.sendOffer === false){
        return;
      }

     this.sendOfferToSystemOperator()
     this.notificationToastService.showNotification(
      '<h4>The system operator will receive the offer.',
      'notifications_active',
      'top',
      'center',
      'info');
    });
  }

  sendOfferToSystemOperator(){
    let powerBalanceProvisionAPD = []
    let powerBalanceProvisionAPU = []
    let activeSubstationOfferDTO = new ActiveSubstationOfferDTO()
    this.activeSubstationService.getOutputInfo(this.activeSubstationStateService.historyResult).subscribe(result=>{
      console.log(result)
      if(result!==null && result!==undefined){
        console.log('result not null')
        this.outputData = result
      }
      else{
        return;
      }
      console.log(result['metadata']['uuid'])
      for(let i=0;i<24;i++){
        powerBalanceProvisionAPD.push(+this.powerBalanceProvisionArray[0][i])
        powerBalanceProvisionAPU.push(+this.powerBalanceProvisionArray[1][i])
      }
      activeSubstationOfferDTO.powerBalanceProvisionAPD = powerBalanceProvisionAPD
      activeSubstationOfferDTO.powerBalanceProvisionAPU = powerBalanceProvisionAPU
      this.activeSubstationService.postOffer(result['metadata']['uuid'], activeSubstationOfferDTO).subscribe(response =>{
        console.log(response)
      })
      
    })
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
        this.displayValueCmrPrice = new Date()
      }
     if(('generation' in this.latestDates)===true){
      this.displayValueGeneration = this.latestDates['generation'];
     }
     else{
      this.displayValueGeneration = new Date()
    }
     if(('demand' in this.latestDates)===true){
      this.displayValueDemand = this.latestDates['demand'];
     }
     else{
      this.displayValueDemand = new Date()
    }

     if(('pvPower' in this.latestDates)===true){
      this.displayValuePvPower = this.latestDates['pvPower'];
     }
     else{
      this.displayValuePvPower = new Date()
    }
     if(('pvPrice' in this.latestDates)===true){
      this.displayValuePvPrice = this.latestDates['pvPrice'];
     }
     else{
      this.displayValuePvPrice = new Date()
    }
     if(('pbpPrice' in this.latestDates)===true){
      this.displayValuePbpPrice = this.latestDates['pbpPrice'];
     }
     else{
      this.displayValuePbpPrice = new Date()
    }
    if(('settlementPrice' in this.latestDates)===true){
      this.displayValueSettlementPrice = this.latestDates['settlementPrice'];
     }
     else{
      this.displayValueSettlementPrice = new Date()
    }
     if(('tieLinePowerFlow' in this.latestDates)===true){
      this.displayValueTieLinePowerFlow = this.latestDates['tieLinePowerFlow'];
     }
     else{
      this.displayValueTieLinePowerFlow = new Date()
    }
     if(('wholesalePrice' in this.latestDates)===true){
      this.displayValueWholeSalePrice = this.latestDates['wholesalePrice'];
     }
     else{
      this.displayValueWholeSalePrice = new Date()
    }
     if(('windPower' in this.latestDates)===true){
      this.displayValueWindPower = this.latestDates['windPower'];
     }
     else{
      this.displayValueWindPower = new Date()
    }

    });
  }

  getLatestScheduledServices(){
    this.activeSubstationService.getLatestScheduleServicesArray(this.appDataService.userDto.activeSubstationUserGroupId).subscribe(latestServices=>{
      console.log(latestServices)
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
      this.cellUpdatedState = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.SCHEDULED_SERVICES))
      console.log(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY))
    }
    if(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!=='' || localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY)!==null ){
      this.activeSubstationInputOperationConstraints.eint = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.INITIAL_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.eint = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!=='' || localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY)!==null){
      this.activeSubstationInputOperationConstraints.eter = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.FINAL_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.eter = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!=='' || localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY)!==null){
      this.activeSubstationInputOperationConstraints.emin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.emin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!=='' || localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY)!==null){
      this.activeSubstationInputOperationConstraints.emax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_ENERGY))
    }
    else{
      this.activeSubstationInputOperationConstraints.emax = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!=='' || localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE)!==null){
      this.activeSubstationInputOperationConstraints.pchmin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_CHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pchmin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!=='' || localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE)!==null){
      this.activeSubstationInputOperationConstraints.pchmax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_CHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pchmax = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!=='' || localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE)!==null){
      this.activeSubstationInputOperationConstraints.pdismin = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MINIMUM_POWER_DISCHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pdismin = ''
    }
    if(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!=='' || localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE)!==null){
      this.activeSubstationInputOperationConstraints.pdismax = JSON.parse(localStorage.getItem(AppConstants.StorageLabels.MAXIMUM_POWER_DISCHARGE))
    }
    else{
      this.activeSubstationInputOperationConstraints.pdismax = ''
    }
  }

  updateCellCheckedState(row,timeInTheDay){

   /*  if( this.cellUpdatedState[row][timeInTheDay]===true){
      //this.periodArray[row][timeInTheDay] = false
      this.cellUpdatedState[row][timeInTheDay] = false
    }
    else{
      //this.periodArray[row][timeInTheDay] = true;
      this.cellUpdatedState[row][timeInTheDay] = true
    }
    console.log('cellUpdatedState'+this.cellUpdatedState) */
  }


  initializeCellsArray(){

    for(let i=0;i<12;i++){
      this.selectedCellInTheRow.push(i)
      this.noCheckBoxSelected.push(i)
      this.selectedCellInTheRow[i] = false
      this.noCheckBoxSelected[i] = true;
    }
    //this.periodArray = [];
    /* for(let i=0;i<12;i++){
      this.periodArray[i] = [];
      for(let k=0;k<24;k++){
        this.periodArray[i][k] = false
      }
    } */
    this.cellUpdatedState = []
    for(let i=0;i<12;i++){
      this.cellUpdatedState[i] = [];
      for(let k=0;k<24;k++){
        this.cellUpdatedState[i][k] = false
      }
    }
  }
  initializeOutputDataArrays(){

    this.activeSubstationService.getOutputInfo(this.activeSubstationStateService.historyResult).subscribe(result=>{
      console.log(result)
      if(result!==null && result!==undefined){
        console.log('result not null')
        this.outputData = result
      }
      else{
        return;
      }
      console.log(this.outputData)
      let energyMarketAllArrays = []
      let congestionManagementReliefAllArrays = []
      let powerBalanceProvisionAllArrays = []
      let pricesServiceFirstAllArrays = []
      let pricesServiceSecondAllArrays = []



       energyMarketAllArrays.push(this.outputData['schedule']['energyMarketAP'])
       energyMarketAllArrays.push(this.outputData['schedule']['energyMarketRP'])
       energyMarketAllArrays.push(this.outputData['schedule']['energyMarketP'])
       congestionManagementReliefAllArrays.push(this.outputData['schedule']['congestionManagementReliefAP'])
       congestionManagementReliefAllArrays.push(this.outputData['schedule']['congestionManagementReliefRP'])
       congestionManagementReliefAllArrays.push(this.outputData['schedule']['congestionManagementReliefP'])
       powerBalanceProvisionAllArrays.push(this.outputData['schedule']['powerBalanceProvisionAPU'])
       powerBalanceProvisionAllArrays.push(this.outputData['schedule']['powerBalanceProvisionAPD'])
       powerBalanceProvisionAllArrays.push(this.outputData['schedule']['powerBalanceProvisionP'])
       pricesServiceFirstAllArrays.push(this.outputData['schedule']['pricesSLCServices1PSM'])
       pricesServiceFirstAllArrays.push(this.outputData['schedule']['pricesSLCServices1RVM'])
       pricesServiceSecondAllArrays.push(this.outputData['schedule']['pricesSLCServices2PC'])
       pricesServiceSecondAllArrays.push(this.outputData['schedule']['pricesSLCServices2RRE'])


    console.log(pricesServiceFirstAllArrays)
    this.energyMarketArray = []
    this.powerBalanceProvisionArray = []
    this.congestionManagementReliefArray = []
    this.pricesServiceFirstArray = []
    this.pricesServiceSecondArray = []
    for(let i=0;i<3;i++){
      if(i<2){
      this.energyMarketArray[i] = energyMarketAllArrays[i]
      this.powerBalanceProvisionArray[i] = powerBalanceProvisionAllArrays[i]
      this.congestionManagementReliefArray[i] = congestionManagementReliefAllArrays[i]
      this.pricesServiceFirstArray[i]  =  pricesServiceFirstAllArrays[i]
      this.pricesServiceSecondArray[i]  = pricesServiceSecondAllArrays[i]
      }
      else{
        this.energyMarketArray[i] = energyMarketAllArrays[i]
        this.powerBalanceProvisionArray[i] = powerBalanceProvisionAllArrays[i]
        this.congestionManagementReliefArray[i] = congestionManagementReliefAllArrays[i]
      }
      for(let k=0;k<24;k++){
        if(i<2){
          this.energyMarketArray[i][k] = energyMarketAllArrays[i][k]
          this.powerBalanceProvisionArray[i][k] = powerBalanceProvisionAllArrays[i][k]
          this.congestionManagementReliefArray[i][k] = congestionManagementReliefAllArrays[i][k]
          this.pricesServiceFirstArray[i][k] =  pricesServiceFirstAllArrays[i][k]
          this.pricesServiceSecondArray[i][k] = pricesServiceSecondAllArrays[i][k]
        }
        else{
          this.energyMarketArray[i][k] = energyMarketAllArrays[i][k]
          this.powerBalanceProvisionArray[i][k] = powerBalanceProvisionAllArrays[i][k]
          this.congestionManagementReliefArray[i][k] = congestionManagementReliefAllArrays[i][k]
        }
      }
    }

    console.log(this.pricesServiceFirstArray)

    })

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
  }

  createGraphData(){
    this.activeSubstationService.getOutputInfo(this.activeSubstationStateService.historyResult).subscribe(result=>{
      console.log(result)
      if(result!==null && result!==undefined){
        this.outputData = result
      }
      else{
        return;
      }

      console.log(this.outputData)

      let graphArrayEnergyMarket: GraphDataSchema[] = []
      let graphArrayCongestionManagementRelief: GraphDataSchema[] = []
      let graphArrayPowerBalanceProvision: GraphDataSchema[] = []
      let graphArrayPricesSLCServices1: GraphDataSchema[] = []
      let graphArrayPricesSLCServices2: GraphDataSchema[] = []
      let graphDataJSONEnergyMarketAP = new GraphDataSchema()
      let graphDataJSONEnergyMarketRP = new GraphDataSchema()
      let graphDataJSONEnergyMarketP = new GraphDataSchema()
      let graphDataJSONCongestionManagementReliefAP = new GraphDataSchema()
      let graphDataJSONCongestionManagementReliefRP = new GraphDataSchema()
      let graphDataJSONCongestionManagementReliefP = new GraphDataSchema()
      let graphDataJSONPowerBalanceProvisionAPU = new GraphDataSchema()
      let graphDataJSONPowerBalanceProvisionAPD = new GraphDataSchema()
      let graphDataJSONPowerBalanceProvisionP = new GraphDataSchema()
      let graphDataJSONPricesSLCServices1PSM = new GraphDataSchema()
      let graphDataJSONPricesSLCServices1RVM = new GraphDataSchema()
      let graphDataJSONPricesSLCServices2PC = new GraphDataSchema()
      let graphDataJSONPricesSLCServices2RRE = new GraphDataSchema()



      graphDataJSONEnergyMarketAP.x =graphDataJSONEnergyMarketRP.x =graphDataJSONEnergyMarketP.x =graphDataJSONCongestionManagementReliefAP.x =graphDataJSONCongestionManagementReliefRP.x =
      graphDataJSONCongestionManagementReliefP.x = graphDataJSONPowerBalanceProvisionAPU.x =graphDataJSONPowerBalanceProvisionAPD.x =graphDataJSONPowerBalanceProvisionP.x =
      graphDataJSONPricesSLCServices1PSM.x = graphDataJSONPricesSLCServices1RVM.x = graphDataJSONPricesSLCServices2PC.x = graphDataJSONPricesSLCServices2RRE.x= ['1','2','3','4','5','6','7','8','9','10','12','13','14','15','16','17','18','19','20','21','22','23','24']

      graphDataJSONEnergyMarketAP.mode =graphDataJSONEnergyMarketRP.mode =graphDataJSONEnergyMarketP.mode =graphDataJSONCongestionManagementReliefAP.mode =graphDataJSONCongestionManagementReliefRP.mode =
      graphDataJSONCongestionManagementReliefP.mode = graphDataJSONPowerBalanceProvisionAPU.mode =graphDataJSONPowerBalanceProvisionAPD.mode =graphDataJSONPowerBalanceProvisionP.mode =
      graphDataJSONPricesSLCServices1PSM.mode = graphDataJSONPricesSLCServices1RVM.mode = graphDataJSONPricesSLCServices2PC.mode = graphDataJSONPricesSLCServices2RRE.mode= 'lines+markers'

      graphDataJSONEnergyMarketAP.type =graphDataJSONEnergyMarketRP.type =graphDataJSONEnergyMarketP.type =graphDataJSONCongestionManagementReliefAP.type =graphDataJSONCongestionManagementReliefRP.type =
      graphDataJSONCongestionManagementReliefP.type = graphDataJSONPowerBalanceProvisionAPU.type =graphDataJSONPowerBalanceProvisionAPD.type =graphDataJSONPowerBalanceProvisionP.type =
      graphDataJSONPricesSLCServices1PSM.type = graphDataJSONPricesSLCServices1RVM.type = graphDataJSONPricesSLCServices2PC.type = graphDataJSONPricesSLCServices2RRE.type= 'scatter'

      graphDataJSONEnergyMarketAP.line =graphDataJSONEnergyMarketRP.line =graphDataJSONEnergyMarketP.line =graphDataJSONCongestionManagementReliefAP.line =graphDataJSONCongestionManagementReliefRP.line =
      graphDataJSONCongestionManagementReliefP.line = graphDataJSONPowerBalanceProvisionAPU.line =graphDataJSONPowerBalanceProvisionAPD.line =graphDataJSONPowerBalanceProvisionP.line =
      graphDataJSONPricesSLCServices1PSM.line = graphDataJSONPricesSLCServices1RVM.line = graphDataJSONPricesSLCServices2PC.line = graphDataJSONPricesSLCServices2RRE.line= JSON.parse('{"shape": "hv"}')



        graphDataJSONEnergyMarketAP.name = 'energyMarketAP'
        graphDataJSONEnergyMarketRP.name = 'energyMarketRP'
        graphDataJSONEnergyMarketP.name  = 'energyMarketP'
        graphDataJSONCongestionManagementReliefAP.name  = 'congestionManagementReliefAP'
        graphDataJSONCongestionManagementReliefRP.name = 'congestionManagementReliefRP'
        graphDataJSONCongestionManagementReliefP.name = 'congestionManagementReliefP'
        graphDataJSONPowerBalanceProvisionAPU.name = 'powerBalanceProvisionAPU'
        graphDataJSONPowerBalanceProvisionAPD.name = 'powerBalanceProvisionAPD'
        graphDataJSONPowerBalanceProvisionP.name = 'powerBalanceProvisionP'
        graphDataJSONPricesSLCServices1PSM.name = 'pricesSLCServices1PSM'
        graphDataJSONPricesSLCServices1RVM.name = 'pricesSLCServices1RVM'
        graphDataJSONPricesSLCServices2PC.name = 'pricesSLCServices2PC'
        graphDataJSONPricesSLCServices2RRE.name = 'pricesSLCServices2RRE'


        graphDataJSONEnergyMarketAP.y = this.outputData['schedule']['energyMarketAP']
        graphDataJSONEnergyMarketRP.y = this.outputData['schedule']['energyMarketRP']
        graphDataJSONEnergyMarketP.y = this.outputData['schedule']['energyMarketP']
        graphDataJSONCongestionManagementReliefAP.y= this.outputData['schedule']['congestionManagementReliefAP']
        graphDataJSONCongestionManagementReliefRP.y= this.outputData['schedule']['congestionManagementReliefRP']
        graphDataJSONCongestionManagementReliefP.y= this.outputData['schedule']['congestionManagementReliefP']
        graphDataJSONPowerBalanceProvisionAPU.y= this.outputData['schedule']['powerBalanceProvisionAPU']
        graphDataJSONPowerBalanceProvisionAPD.y= this.outputData['schedule']['powerBalanceProvisionAPD']
        graphDataJSONPowerBalanceProvisionP.y= this.outputData['schedule']['powerBalanceProvisionP']
        graphDataJSONPricesSLCServices1PSM.y= this.outputData['schedule']['pricesSLCServices1PSM']
        graphDataJSONPricesSLCServices1RVM.y= this.outputData['schedule']['pricesSLCServices1RVM']
        graphDataJSONPricesSLCServices2PC.y= this.outputData['schedule']['pricesSLCServices2PC']
        graphDataJSONPricesSLCServices2RRE.y = this.outputData['schedule']['pricesSLCServices2RRE']

        graphArrayEnergyMarket.push(graphDataJSONEnergyMarketAP,graphDataJSONEnergyMarketRP,graphDataJSONEnergyMarketP)
        graphArrayCongestionManagementRelief.push(graphDataJSONCongestionManagementReliefAP,graphDataJSONCongestionManagementReliefRP,graphDataJSONCongestionManagementReliefP)
        graphArrayPowerBalanceProvision.push(graphDataJSONPowerBalanceProvisionAPU,graphDataJSONPowerBalanceProvisionAPD,graphDataJSONPowerBalanceProvisionP)
        graphArrayPricesSLCServices1.push(graphDataJSONPricesSLCServices1PSM,graphDataJSONPricesSLCServices1RVM)
        graphArrayPricesSLCServices2.push(graphDataJSONPricesSLCServices2PC,graphDataJSONPricesSLCServices2RRE)

       this.graphEnergyMarket.data = graphArrayEnergyMarket
       this.graphCongestionManagementRelief.data = graphArrayCongestionManagementRelief
       this.graphPowerBalanceProvision.data = graphArrayPowerBalanceProvision
       this.graphPricesSLCServices1.data = graphArrayPricesSLCServices1
       this.graphPricesSLCServices2.data = graphArrayPricesSLCServices2


    })
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

    let dataFilesCmrPriceDate = this.dp.transform(this.displayValueCmrPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesDemandDate = this.dp.transform(this.displayValueDemand, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesGenerationDate = this.dp.transform(this.displayValueGeneration, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesPbpPriceDate = this.dp.transform(this.displayValuePbpPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesPvPowerDate = this.dp.transform(this.displayValuePvPower, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesPvPriceDate = this.dp.transform(this.displayValuePvPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesSettlementPriceDate = this.dp.transform(this.displayValueSettlementPrice, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesTieLinePowerFlowDate = this.dp.transform(this.displayValueTieLinePowerFlow, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesWholeSalePriceDate = this.dp.transform(this.displayValueWholeSalePrice, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    let dataFilesWindPowerDate = this.dp.transform(this.displayValueWindPower, 'yyyy-MM-ddThh:mm:ss') + 'Z'

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
          return;
          }
        }
    }
    this.translatePeriodArrayToHours(this.cellUpdatedState);
  }



  translatePeriodArrayToHours(arrayToTranslate:Boolean[][]){

    for(let k=0;k<12;k++){
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

  }



}

export interface ScheduledServicesSchema {
  service: string;
  power_reserve: string;
  period_of_activation: boolean;
  select_deselect_all: boolean;

}
export interface OutPutSchema {
  service: string;
  period_of_activation: boolean;
  select_deselect_all: boolean

}
export interface ProfitsCostsSchema {
  quantity: string;
  expected: string;
  imported:string;

}

export class PeriodPositionsSchema{
  positionsArray:any[] = [];
  periodArrayIndex:any[][]= []
}

export class GraphDataSchema{
  mode: string;
  type:  string;
  /* line: {"shape": "hv"} */
  line: JSON;
  name: string;
  x: string[] = [];
  y: any[] = []
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
  {service: 'Congestion Relief', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Power Bal. Res. Up', power_reserve: '', select_deselect_all:false, period_of_activation:false},
  {service: 'Power Bal. Res Down', power_reserve: '', select_deselect_all:false, period_of_activation:false},

];
const ENERGY_MARKET_SCHEMA: OutPutSchema[] = [
  {service: 'Active Power',  period_of_activation:false, select_deselect_all: false},
  {service: 'Reactive Power',  period_of_activation:false, select_deselect_all: false},
  {service: 'Price',  period_of_activation:false, select_deselect_all: false},
];
const POWER_BALANCE_PROVISION_SCHEMA: OutPutSchema[] = [
  {service: 'Active Power Up',  period_of_activation:false, select_deselect_all: false},
  {service: 'Active Power Down',  period_of_activation:false, select_deselect_all: false},
  {service: 'Price',  period_of_activation:false, select_deselect_all: false},
];
const CONGESTION_MANAGEMENT_RELIEF_SCHEMA: OutPutSchema[] = [
  {service: 'Active Power',  period_of_activation:false, select_deselect_all: false},
  {service: 'Reactive Power',  period_of_activation:false, select_deselect_all: false},
  {service: 'Price',  period_of_activation:false, select_deselect_all: false},
];
const PRICES_SLC_SERVICES_ONE_SCHEMA: OutPutSchema[] = [
  {service: 'RVM',  period_of_activation:false, select_deselect_all: false},
  {service: 'PSM',  period_of_activation:false, select_deselect_all: false},
];
const PRICES_SLC_SERVICES_TWO_SCHEMA: OutPutSchema[] = [
  {service: 'RRE',  period_of_activation:false, select_deselect_all: false},
  {service: 'PC',  period_of_activation:false, select_deselect_all: false}
];

const PROFITS_AND_COSTS_SCHEMA: ProfitsCostsSchema[] = [
  {quantity: 'Total Profit',  expected: '2', imported:'1'},
  {quantity: 'Disch. Profit',  expected: '3', imported:'2'},
  {quantity: 'Charge Cost',  expected: '4', imported:'4'},
  {quantity: 'PBP Profit',  expected: '5', imported:'7'},
  {quantity: 'CMR Profit',  expected: '6', imported:'1'},
];
