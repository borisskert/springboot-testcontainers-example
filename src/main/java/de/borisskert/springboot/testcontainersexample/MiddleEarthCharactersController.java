package de.borisskert.springboot.testcontainersexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("characters")
public class MiddleEarthCharactersController {

    private final MiddleEarthCharactersRepository repository;

    @Autowired
    public MiddleEarthCharactersController(MiddleEarthCharactersRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MiddleEarthCharacter> findByRace(@RequestParam String race) {
        return repository.findAllByRace(race);
    }

    @PostMapping
    public MiddleEarthCharacter save(@RequestBody MiddleEarthCharacter character) {
        return repository.save(character);
    }
}
