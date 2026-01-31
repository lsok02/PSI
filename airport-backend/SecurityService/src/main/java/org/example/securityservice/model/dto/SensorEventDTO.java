package org.example.securityservice.model.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class SensorEventDTO {

    @NotNull
    private String sensorId;

    @NotNull
    private String zoneCode;

    @NotNull
    private String alarmType; // SMOKE_DETECTED, DOOR_FORCED, etc.

    private String additionalData;
}