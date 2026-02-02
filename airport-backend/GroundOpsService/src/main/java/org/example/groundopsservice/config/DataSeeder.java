package org.example.groundopsservice.config;

import lombok.RequiredArgsConstructor;
import org.example.groundopsservice.model.entity.SpecializedEquipment;
import org.example.groundopsservice.model.entity.Vehicle;
import org.example.groundopsservice.model.enumeration.ResourceStatus;
import org.example.groundopsservice.model.enumeration.ResourceType;
import org.example.groundopsservice.repository.TechnicalResourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TechnicalResourceRepository technicalResourceRepository;

    @Override
    public void run(String... args) {
        if (technicalResourceRepository.count() > 0) {
            return;
        }

        Vehicle towTractor = new Vehicle();
        towTractor.setName("Tow Tractor TT-14");
        towTractor.setRegistrationNumber("TT-14");
        towTractor.setVehicleType("Tow Tractor");
        towTractor.setStatus(ResourceStatus.AVAILABLE);
        towTractor.setResourceType(ResourceType.VEHICLE);
        towTractor.setNextMaintenanceDate(LocalDateTime.now().plusDays(14));

        Vehicle fuelTruck = new Vehicle();
        fuelTruck.setName("Fuel Truck FT-07");
        fuelTruck.setRegistrationNumber("FT-07");
        fuelTruck.setVehicleType("Fuel Truck");
        fuelTruck.setStatus(ResourceStatus.IN_USE);
        fuelTruck.setResourceType(ResourceType.VEHICLE);
        fuelTruck.setNextMaintenanceDate(LocalDateTime.now().plusDays(7));

        SpecializedEquipment beltLoader = new SpecializedEquipment();
        beltLoader.setName("Belt Loader BL-3");
        beltLoader.setCategory("Baggage Handling");
        beltLoader.setStatus(ResourceStatus.IN_SERVICE);
        beltLoader.setResourceType(ResourceType.SPECIALIZED_EQUIPMENT);
        beltLoader.setNextMaintenanceDate(LocalDateTime.now().plusDays(3));

        SpecializedEquipment powerUnit = new SpecializedEquipment();
        powerUnit.setName("Ground Power Unit GPU-2");
        powerUnit.setCategory("Power Supply");
        powerUnit.setStatus(ResourceStatus.AVAILABLE);
        powerUnit.setResourceType(ResourceType.SPECIALIZED_EQUIPMENT);
        powerUnit.setNextMaintenanceDate(LocalDateTime.now().plusDays(10));

        Vehicle cateringTruck = new Vehicle();
        cateringTruck.setName("Catering Truck CT-05");
        cateringTruck.setRegistrationNumber("CT-05");
        cateringTruck.setVehicleType("Catering Truck");
        cateringTruck.setStatus(ResourceStatus.AVAILABLE);
        cateringTruck.setResourceType(ResourceType.VEHICLE);
        cateringTruck.setNextMaintenanceDate(LocalDateTime.now().plusDays(5));

        Vehicle pushbackTractor = new Vehicle();
        pushbackTractor.setName("Pushback Tractor PB-09");
        pushbackTractor.setRegistrationNumber("PB-09");
        pushbackTractor.setVehicleType("Pushback Tractor");
        pushbackTractor.setStatus(ResourceStatus.OUT_OF_ORDER);
        pushbackTractor.setResourceType(ResourceType.VEHICLE);
        pushbackTractor.setNextMaintenanceDate(LocalDateTime.now().plusDays(1));

        Vehicle deicingTruck = new Vehicle();
        deicingTruck.setName("Deicing Truck DT-02");
        deicingTruck.setRegistrationNumber("DT-02");
        deicingTruck.setVehicleType("Deicing Truck");
        deicingTruck.setStatus(ResourceStatus.IN_USE);
        deicingTruck.setResourceType(ResourceType.VEHICLE);
        deicingTruck.setNextMaintenanceDate(LocalDateTime.now().plusDays(12));

        SpecializedEquipment conveyor = new SpecializedEquipment();
        conveyor.setName("Baggage Conveyor BC-11");
        conveyor.setCategory("Baggage Handling");
        conveyor.setStatus(ResourceStatus.AVAILABLE);
        conveyor.setResourceType(ResourceType.SPECIALIZED_EQUIPMENT);
        conveyor.setNextMaintenanceDate(LocalDateTime.now().plusDays(9));

        SpecializedEquipment jetBridge = new SpecializedEquipment();
        jetBridge.setName("Jet Bridge JB-4");
        jetBridge.setCategory("Passenger Boarding");
        jetBridge.setStatus(ResourceStatus.IN_USE);
        jetBridge.setResourceType(ResourceType.SPECIALIZED_EQUIPMENT);
        jetBridge.setNextMaintenanceDate(LocalDateTime.now().plusDays(6));

        SpecializedEquipment airStarter = new SpecializedEquipment();
        airStarter.setName("Air Starter AS-7");
        airStarter.setCategory("Aircraft Support");
        airStarter.setStatus(ResourceStatus.AVAILABLE);
        airStarter.setResourceType(ResourceType.SPECIALIZED_EQUIPMENT);
        airStarter.setNextMaintenanceDate(LocalDateTime.now().plusDays(4));

        technicalResourceRepository.save(towTractor);
        technicalResourceRepository.save(fuelTruck);
        technicalResourceRepository.save(beltLoader);
        technicalResourceRepository.save(powerUnit);
        technicalResourceRepository.save(cateringTruck);
        technicalResourceRepository.save(pushbackTractor);
        technicalResourceRepository.save(deicingTruck);
        technicalResourceRepository.save(conveyor);
        technicalResourceRepository.save(jetBridge);
        technicalResourceRepository.save(airStarter);
    }
}
