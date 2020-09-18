export class ActiveSubstationInputFlexibilityServiceScheduleDTO{
  //FIX ME
    public  inertiaEmulationPercentage:string = '';
    public  inertiaEmulation:any[] = [];
    public  frequencyRegulationPercentage:string= '';
    public  frequencyRegulation:any[] = [];
    public  modalFrequenciesPercentage:string= '';
    public  modalFrequencies:any[] = [];
    public  voltageRegulationPercentage:string= '';
    public  voltageRegulation:any[] = [];
    public  resourceVariabilityPercentage:string= '';
    public  resourceVariability:any[] = [];
    public  powerShiftingPercentage:string= ''; 
    public  powerShifting:any[] = [];
    public  rampRatePercentage:string= '';
    public  rampRate:any[] = [];
    public  powerCurtailmentPercentage:string= '';
    public  powerCurtailment:any[] = [];
    public  powerBalancePercentage:string= '';
    public  powerBalance:any[] = [];
    public  congestionReliefPercentage:string= '';
    public  congestionRelief:any[] = [];
    public  powerBalanceReserve: boolean
}