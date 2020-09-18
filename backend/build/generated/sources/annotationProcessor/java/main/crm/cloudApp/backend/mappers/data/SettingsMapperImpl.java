package crm.cloudApp.backend.mappers.data;

import crm.cloudApp.backend.dto.data.SettingsDTO;
import crm.cloudApp.backend.models.data.Settings;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-01T12:12:14+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.6 (JetBrains s.r.o)"
)
@Component
public class SettingsMapperImpl extends SettingsMapper {

    @Override
    public Settings map(SettingsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Settings settings = new Settings();

        if ( dto.getCreatedOn() != null ) {
            settings.setCreatedOn( dto.getCreatedOn() );
        }
        if ( dto.getId() != null ) {
            settings.setId( dto.getId() );
        }
        if ( dto.getTag() != null ) {
            settings.setTag( dto.getTag() );
        }
        if ( dto.getName() != null ) {
            settings.setName( dto.getName() );
        }
        if ( dto.getValue() != null ) {
            settings.setValue( dto.getValue() );
        }

        return settings;
    }

    @Override
    public SettingsDTO map(Settings entity) {
        if ( entity == null ) {
            return null;
        }

        SettingsDTO settingsDTO = new SettingsDTO();

        if ( entity.getId() != null ) {
            settingsDTO.setId( entity.getId() );
        }
        if ( entity.getCreatedOn() != null ) {
            settingsDTO.setCreatedOn( entity.getCreatedOn() );
        }
        if ( entity.getCreatedBy() != null ) {
            settingsDTO.setCreatedBy( entity.getCreatedBy() );
        }
        if ( entity.getTag() != null ) {
            settingsDTO.setTag( entity.getTag() );
        }
        if ( entity.getName() != null ) {
            settingsDTO.setName( entity.getName() );
        }
        if ( entity.getValue() != null ) {
            settingsDTO.setValue( entity.getValue() );
        }

        return settingsDTO;
    }

    @Override
    public void map(SettingsDTO dto, Settings entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getCreatedOn() != null ) {
            entity.setCreatedOn( dto.getCreatedOn() );
        }
        else {
            entity.setCreatedOn( null );
        }
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        else {
            entity.setId( null );
        }
        if ( dto.getTag() != null ) {
            entity.setTag( dto.getTag() );
        }
        else {
            entity.setTag( null );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        else {
            entity.setName( null );
        }
        if ( dto.getValue() != null ) {
            entity.setValue( dto.getValue() );
        }
        else {
            entity.setValue( null );
        }
    }
}
