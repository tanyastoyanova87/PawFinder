package app.web;

import app.pet.model.Pet;
import app.pet.service.PetService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class RestPetController {

    private final PetService petService;
    private final UserService userService;

    public RestPetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("pets-for-adoption/{id}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likePet(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
        Pet pet = petService.getById(id);
        User user = userService.getById(authenticationMetaData.getId());
        userService.likePet(pet, user);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", user.getFavouritePets().contains(pet));

        return ResponseEntity.ok(response);
    }

}
