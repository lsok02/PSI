package org.example.securityservice.model.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.securityservice.model.enumeration.SensorType;

@Data
public class SensorEventDTO {

    @NotNull
    private String sensorId;

    @NotNull
    private String zoneCode;

    @NotNull
    private SensorType alarmType; // SMOKE_DETECTED, DOOR_FORCED, etc.

    private String additionalData;
}