  import { Component, OnInit, ViewChild, ElementRef, TemplateRef } from '@angular/core';
  import { PlotlyService } from 'angular-plotly.js';
  import { MatTextareaAutosize, MatAutocomplete, MatAutocompleteSelectedEvent, MatChipInputEvent } from '@angular/material';
import { GraphService } from 'app/services/graph.service';
import { HistoryService } from 'app/services/history.service';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { FormControl, FormBuilder, FormArray, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { ModalService } from 'app/services/modal.service';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

  @Component({
    selector: 'ge-hyppo-io-graph',
    templateUrl: './ge-hyppo-io-graph.component.html',
    styleUrls: ['./ge-hyppo-io-graph.component.scss']
  })
  export class GeHyppoIoGraphComponent implements OnInit {
    dataTraces: any;

    csvSelected: string;
    parametersListToPass = '';
    encodedParametersList = '';
    encodedCSVFileName = '';
    groupedParametersToPost = [];
    form: FormGroup;
    variantsArray: FormArray;
    graphParametersArray: string[][] = [];
    visible = true;
    selectable = true;
    removable = true;
    separatorKeysCodes: number[] = [ENTER, COMMA];
    parameterCtrl = new FormControl();
    filteredParameters: Observable<string[]>;
    parameters = [] ;
    showUpdateGraphLoadingButton = false
    bodyText = 'Power(DWATT) - DWATT_PLANT normalized with the ambient Temperature' + '\n' +
    'Plant_Power[MW] - Sum of all generators power ' +
    '\n\GTx_Primary_Response[MW] - Primary response=(100-THNSYS)*Design load/Droop' +
    '\nST_Power[MW] - Power at the ST generator\nGTx_Power[MW]-Power at the GT generator\nAmbient_Temp[degC] - Ambient air temperature' +
    // tslint:disable-next-line:max-line-length
    '\nPlant_Primary_Response[MW]-Sum of all GTPrimary frequency corrected MW Bias. In case of super fast primary response the value will be calculated from the frequency' +
    // tslint:disable-next-line:max-line-length
    '\nGTx_Fuel_Consumption[kg/s] - GT Gas Fuel Mass Flow Rate Median\nPlant_Fuel_Consumption[kg/s] - Sum of all GT Gas Fuel Mass Flow Rate Median' +
    '\nPFRLDCMD_PLANT_ISO - PFRLDCMD_PLANT normalized with the ambient Temperature]' +
    '\nResponse[boolean] - Assert if the power plant is doing primary response' +
    '\nPrice_Electricity[$/MWh] - Price of electricity' +
    '\nPower/Fuel_ratio[MJ_el/kg_fuel] - Ratio between the power and the fuel mass flow rate ' +
    '\nGT_All_Power[MW] - Sum of all GT generators power' +
    '\nST/GT_Power_ratio[-] - Power ratio between ST and GT, calculated with data when possible, otherwise estimated'+
    // tslint:disable-next-line:max-line-length
    '\nScen1_Power_Added_GT[MW] - During active primary response, the GT power output is increased by this amount since the battery is guaranteeing the same amount of capacity reserve'+
    // tslint:disable-next-line:max-line-length
    '\nScen1_Power_Added_Plant[MW] - The GT Power added thanks to the battery increases also the ST output. This represenst the total, GT+ST' +
    '\nScen1_fuel_consumption_incr[kg/s] - When during active primary response, the GT power output is increased, more fuel is used' +
    '\nScen1_Power_Fuel_Ratio_incr[MJ_el/kg_fuel] - Working on a bigger load, the whole power plant is able to perform better' +
    '\nScen1_Prim_Resp_GT[MW] - Primary response given by the turbine, when also the battery is providing capacity reserve' +
    '\nScen1_Prim_Resp_Batt[MW] - Primary response given by the battery, after the turbine has reached its maximum load' +
    '\nBattery_Energy[MWh] - Energy contained in the battery' +
    '\nBattery_Losses_Cumulated[MWh] - Cumulated of the battery energy losses due to overall efficiency defined in the input' +
    '\nBattery_SOC[%] - Battery current state of charge' +
    '\nScen1_Revenues[$] - Estimated revenues produced increasing the GT load thanks to the battery' +
    '\nScen1_Profits[$] - Revenues estimated by scenario 1 minus gas expenses' +
    // tslint:disable-next-line:max-line-length
    '\nScen1_Saved_Fuel[kg]- kg of spared gas calculated as compared to what I would have burned to do the same amount of MWh with the efficiency without the battery' +
    '\nScen2_Profits[$] - Profits estimated by scenario 2'

    allParameters: string[] = ['Power(DWATT)','GT1_Power[MW]','Plant_Power[MW]','GT1_Primary_Response[MW]',
    'Plant_Primary_Response[MW]', 'GT1_Fuel_Consumption[kg/s]','Plant_Fuel_Consumption[kg/s]',
    'Ambient_Temp[degC]', 'Grid_Connected(L52ONLINE)','GT2_Power[MW]','GT2_Primary_Response[MW]',
    'GT2_Fuel_Consumption[kg/s]', 'GT3_Power[MW]', 'GT3_Primary_Response[MW]', 'GT3_Fuel_Consumption[kg/s]',
    'Price_Electricity[$/MWh]', 'Response[boolean]', 'ETA_GT1', 'ETA_GT2',
    'ETA_GT3','Power/Fuel_ratio[MJ_el/kg_fuel]',
    'GT_All_Power[MW]','ETA_GT_ALL','ST/GT_Power_ratio[-]','ETA_PLANT',
    'Scen1_Power_Added_GT[MW]','Scen1_Power_Added_Plant[MW]',
    'Scen1_fuel_consumption_incr[kg/s]','Scen1_Power_Fuel_Ratio_incr[MJ_el/kg_fuel]',
    'Scen1_Prim_Resp_GT[MW]','Scen1_Prim_Resp_Batt[MW]','Battery_Energy[MWh]',
    'Battery_Losses_Cumulated[MWh]','Battery_SOC[%]','Scen1_Revenues[$]',
    'Scen1_Profits[$]','Scen1_Saved_Fuel[kg]','Scen2_Profits[$]'];
    listOflistParameters = [];
    modalRef: BsModalRef;
    csvFileList = [];
    public graph = {
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


    @ViewChild('parameterInput', {static: false}) parameterInput: ElementRef<HTMLInputElement>;
    @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
    constructor(public plotlyService: PlotlyService, public historyService: HistoryService,
      private appDataService: AppDataService,
       public graphService: GraphService, private formBuilder: FormBuilder) {
        this.filteredParameters = this.parameterCtrl.valueChanges.pipe(
          startWith(null),
          map((parameter: string | null) => parameter ? this._filter(parameter) : this.allParameters.slice()));
    }

  ngOnInit() {

    this.form = this.formBuilder.group({
      items: this.formBuilder.array([
        this.formBuilder.group({
         listContents: ''
        })
      ]),
    });
    this.graphParametersArray.push([]);

    this.graphService.getSpecificRunCSVs(this.historyService.selectedData.uuid).subscribe(csvsResponse =>{
        this.csvFileList = csvsResponse ;
    });

    if(this.historyService.selectedData.uuid !== undefined) {
    }

  }
  focusParameterInput(){
    this.parameterInput.nativeElement.blur();
    this.parameterInput.nativeElement.focus();
  }
  onSubmit(){
      let graphLoadingButtonTimer = setTimeout(() => {this.showUpdateGraphLoadingButton = true})

      for (let k = 0; k < this.graphParametersArray.length; k++) {
          this.listOflistParameters.push(this.graphParametersArray[k])
      }
      if (this.listOflistParameters.length === 0) {
          this.parametersListToPass = ','
      } else {
          this.parametersListToPass = this.listOflistParameters.toString();
      }

      this.encodedCSVFileName = encodeURIComponent(this.csvSelected)
      this.encodedParametersList = encodeURIComponent(this.parametersListToPass)
      this.graph.data = [];
      this.graphService.getGraph(this.historyService.selectedData.uuid,  this.encodedCSVFileName,
          this.encodedParametersList).subscribe(response => {
              for (let i = 0; i < response.length; i++) {
                  this.graph.data.push(response[i])
              }
              clearTimeout(graphLoadingButtonTimer)
              this.showUpdateGraphLoadingButton = false
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

  }
  add(event: MatChipInputEvent, eventIndex: number): void {
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
