package org.example.securityservice.controller;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {

    private final IncidentTeamRepository teamRepository;

    @GetMapping
    public ResponseEntity<List<IncidentTeam>> getTeams(
            @RequestParam(required = false) TeamStatus status,
            @RequestParam(required = false) IncidentType specialization) {

        List<IncidentTeam> teams;
        if (status != null && specialization != null) {
            teams = teamRepository.findByStatusAndSpecialization(status, specialization);
        } else if (status != null) {
            teams = teamRepository.findByStatus(status);
        } else if (specialization != null) {
            teams = teamRepository.findBySpecialization(specialization);
        } else {
            teams = teamRepository.findAll();
        }

        return ResponseEntity.ok(teams);
    }

    @GetMapping("/available/{type}")
    public ResponseEntity<List<IncidentTeam>> getAvailableTeams(@PathVariable IncidentType type) {
        List<IncidentTeam> teams = teamRepository.findByStatusAndSpecialization(
                TeamStatus.AVAILABLE, type);
        return ResponseEntity.ok(teams);
    }
}