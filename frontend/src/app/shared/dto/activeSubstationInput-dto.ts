import { ActiveSubstationInputHeaderDTO } from "./activeSubstationInputHeader-dto";
import { ActiveSubstationInputCommandsDTO } from "./activeSubstationInputCommands-dto";
import { ActiveSubstationInputDataFilesDTO } from "./activeSubstationInputDataFiles-dto";
import { ActiveSubstationInputOperationConstraintsDTO } from "./activeSubstationInputOperationConstraints-dto";
import { ActiveSubstationInputFlexibilityServiceScheduleDTO } from "./activeSubstationInputFlexibilityServiceSchedule-dto";
import {  ActiveSubstationInputMetadataDTO } from "./activeSubstationInputMetadata-dto";


export class ActiveSubstationInputDTO {
    constructor(
       public header: ActiveSubstationInputHeaderDTO,
       public commands: ActiveSubstationInputCommandsDTO,
       public dataFiles: ActiveSubstationInputDataFilesDTO,
       public operationConstraints: ActiveSubstationInputOperationConstraintsDTO,
       public onCloudAppbilityServiceSchedule: ActiveSubstationInputFlexibilityServiceScheduleDTO, 
       public metadata: ActiveSubstationInputMetadataDTO
    ) {}    
  }