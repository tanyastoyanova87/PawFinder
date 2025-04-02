package app.config;

import app.pet.model.AgeStatus;
import app.pet.model.Gender;
import app.pet.model.HairLength;
import app.pet.model.Specie;
import app.pet.service.PetService;
import app.user.model.Country;
import app.user.service.UserService;
import app.web.dto.AddPetRequest;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final PetService petService;

    @Autowired
    public DataLoader(PetService petRepository, UserService userService) {
        this.petService = petRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {

        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("Test")
                .username("admin")
                .email("admin@abv.bg")
                .password("admin123A")
                .confirmPassword("admin123A")
                .country(Country.BULGARIA)
                .build();

        userService.register(registerRequest);
        userService.giveRoleToAdmin(registerRequest);

        if (!petService.getAllPets().isEmpty()) {
            return;
        }

        AddPetRequest dog1 = AddPetRequest.builder()
                .name("Sharo")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://canine.org/wp-content/uploads/2022/05/puppycalendar-300x300.jpg")
                .description("Sharo is a loyal and affectionate adult dog who once had a loving owner, but unfortunately, his family had to relocate and couldn’t take him along. Sharo has been well-trained, enjoys long walks, and loves playing fetch. He has a strong protective instinct and would make a great companion for an individual or a family with an active lifestyle. His best features are his intelligence and loyalty. Sharo needs a home with a yard or a family that enjoys outdoor activities. Could you be the one to give Sharo a forever home?")
                .build();

        AddPetRequest dog2 = AddPetRequest.builder()
                .name("Iory")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.YOUNG)
                .hairLength(HairLength.LONG)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://www.rover.com/blog/wp-content/uploads/2019/09/YorkshireTerrier-min-1-300x300.jpg")
                .description("Iory is a playful young dog with long, silky hair and a heart full of love. He was found abandoned near a shelter, confused and scared. Despite his rough start, he has blossomed into a confident and friendly companion. Iory adores cuddles, enjoys learning new tricks, and thrives on attention. His best feature is his energetic and loving personality. Iory would do best in a home where he gets plenty of playtime and affection. Are you ready to welcome this joyful pup into your life?")
                .build();

        AddPetRequest dog3 = AddPetRequest.builder()
                .name("Rocky")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.BABY)
                .hairLength(HairLength.MEDIUM)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://cdn.greenfieldpuppies.com/wp-content/uploads/2019/06/alaskan-klee-kai-puppy-300x300.jpg")
                .description("Rocky is a baby pup with a medium-length coat and an adventurous spirit. He was part of an unwanted litter and ended up at the shelter, but that hasn’t dampened his love for life. He enjoys exploring new places, playing with toys, and snuggling after a long day of fun. His best feature is his boundless curiosity. Rocky needs a patient and caring family to guide him through puppyhood and help him grow into a wonderful companion. Could Rocky be your perfect match?")
                .build();

        AddPetRequest dog4 = AddPetRequest.builder()
                .name("Max")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.MEDIUM)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://petcenternj.com/wp-content/uploads/2021/11/Border-Collie-300x300.png")
                .description("Max is a strong and intelligent adult dog with a striking medium coat. He was rescued from a difficult situation, but despite his past, he remains a loyal and loving companion. Max is great at learning commands and enjoys challenges. His best features are his intelligence and devotion. He needs a family that understands his need for mental stimulation and physical activity. If you’re looking for a devoted friend, Max is the one for you!")
                .build();

        AddPetRequest dog5 = AddPetRequest.builder()
                .name("Toby")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://www.rover.com/blog/wp-content/uploads/2019/09/GermanShepherdDog-min-1-300x300.jpg")
                .description("Toby is an adult dog with short hair and a heart full of love. His previous owners had to give him up due to financial hardships, but he still believes in second chances. Toby is friendly, calm, and enjoys spending time with people of all ages. His best feature is his gentle nature. He needs a loving home where he can feel secure and cherished. Could Toby be your newest family member?")
                .build();

        AddPetRequest dog6 = AddPetRequest.builder()
                .name("Lucy")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.BABY)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.mutkut.com/wp-content/uploads/2015/12/Loving-You-White-Dog-Display-Picture-300x300.jpg")
                .description("Lucy is an adorable baby pup with short, fluffy fur and an irresistible charm. She was found abandoned near a park, cold and frightened, but she never lost her trusting heart. Lucy is playful, loves cuddles, and has a way of making everyone smile. Her best feature is her joyful spirit. She needs a family that will nurture her as she grows into a happy and confident dog. Can you give Lucy the safe and loving home she deserves?")
                .build();

        AddPetRequest dog7 = AddPetRequest.builder()
                .name("Chanel")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.YOUNG)
                .hairLength(HairLength.LONG)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.bostonterriersociety.com/wp-content/uploads/2023/02/3-300x300.jpg")
                .description("Chanel is a young dog with long, gorgeous hair and a personality to match. She was surrendered by a family who couldn’t care for her anymore, but she still craves love and companionship. Chanel enjoys pampering, belly rubs, and playing with other dogs. Her best feature is her elegance and affectionate nature. She would thrive in a home where she is spoiled with attention. Are you ready to add a touch of Chanel’s charm to your life?")
                .build();

        AddPetRequest dog8 = AddPetRequest.builder()
                .name("Zoe")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.YOUNG)
                .hairLength(HairLength.MEDIUM)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.akc.org/wp-content/uploads/2017/10/Cavalier-running-300x300.jpg")
                .description("Zoe is a lively and affectionate young dog with a medium coat and an endless supply of kisses. She was rescued from an overcrowded shelter, where she struggled to get the love she deserved. Zoe is outgoing, loves making new friends, and enjoys daily adventures. Her best feature is her social personality. She needs a home where she can receive plenty of love and engagement. If you’re looking for a playful companion, Zoe is the perfect fit!")
                .build();

        AddPetRequest dog9 = AddPetRequest.builder()
                .name("Xena")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://kaykosdogshades.com/cdn/shop/products/LargeYellow_300x.jpg?v=1646256595")
                .description("Xena is a strong and loving adult dog with a short coat and a heart full of courage. She was found as a stray, but her spirit remained unbroken. Xena is loyal, protective, and enjoys the company of people who appreciate her bravery. Her best feature is her unwavering devotion. She needs a home where she feels safe and valued. Could you be the one to show Xena the love she deserves?")
                .build();

        AddPetRequest dog10 = AddPetRequest.builder()
                .name("Millie")
                .specie(Specie.DOG)
                .ageStatus(AgeStatus.YOUNG)
                .hairLength(HairLength.MEDIUM)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.mutkut.com/wp-content/uploads/2015/12/Vineyard-Vines-By-Mascot-Whale-Collar-on-Dog-300x300.jpg")
                .description("Millie is a young dog with a beautiful medium-length coat and a heart as big as her personality. She was rescued from neglect but has only love to give. Millie is affectionate, intelligent, and eager to please. Her best feature is her warm and trusting nature. She needs a patient and understanding home where she can continue to grow into the amazing dog she is meant to be. Will you be the one to give Millie a second chance?")
                .build();

        AddPetRequest cat1 = AddPetRequest.builder()
                .name("Luna")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.YOUNG)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://i.pinimg.com/736x/0e/f5/49/0ef5497a58c2ba5900ffa8a4656ed0a8.jpg")
                .description("Luna is a young cat with a short, sleek coat and mesmerizing eyes. She was rescued from the streets as a kitten and has since learned to love human affection. Luna is playful, enjoys sunbathing by the window, and loves chasing feather toys. Her best feature is her independent yet loving nature. She needs a home where she can feel safe and loved. Could Luna be your new feline companion?")
                .build();

        AddPetRequest cat2 = AddPetRequest.builder()
                .name("Rosie")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.allthebestpetcare.com/wp-content/uploads/2021/10/cbd-oil-cat-anxiety-300x300.jpg")
                .description("Rosie is a stunning adult cat with a short coat and a gentle soul. She was surrendered by an elderly owner who could no longer care for her. Rosie enjoys quiet moments, warm laps, and gentle strokes. Her best feature is her sweet and affectionate personality. She needs a home where she can relax and be pampered. Are you ready to give Rosie a peaceful forever home?")
                .build();

        AddPetRequest cat3 = AddPetRequest.builder()
                .name("Daisy")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.BABY)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://rukminim1.flixcart.com/image/300/300/l3khsi80/poster/t/d/b/small-cute-cat-poster-multicolor-photo-paper-print-poster-cute-original-imagezffcmvryfk8.jpeg")
                .description("Daisy is a baby kitten with a short coat and an adventurous heart. She was found abandoned in a box, but her spirit remains unshaken. Daisy loves to explore, climb, and pounce on toys. Her best feature is her playful and curious personality. She needs a home where she can receive love and playtime. Could you be the one to give Daisy a fresh start?")
                .build();

        AddPetRequest cat4 = AddPetRequest.builder()
                .name("Tom")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://catfriendly.com/wp-content/uploads/2016/11/Cat-Lying-Down-Red-coloring-Istock-300x300.jpg")
                .description("Tom is an adult cat with a striking red coat and a charming personality. He was rescued from the streets, where he had to fend for himself. Despite his past, he is affectionate and enjoys human company. His best feature is his resilience and loving nature. He needs a home where he can finally relax and feel safe. Could Tom be your perfect feline friend?")
                .build();

        AddPetRequest cat5 = AddPetRequest.builder()
                .name("Poppy")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://m.media-amazon.com/images/S/aplus-media-library-service-media/302017fd-ba48-4e8c-af9d-325ff288cbfa.__CR0,0,300,300_PT0_SX300_V1___.jpg")
                .description("Poppy is an adult cat with a sleek short coat and a playful attitude. He was left behind when his previous owners moved away, but he still believes in love. Poppy enjoys interactive toys and chasing after moving objects. His best feature is his fun-loving and energetic nature. He needs a home where he can feel wanted and entertained. Will you be the one to give Poppy a happy ending?")
                .build();

        AddPetRequest cat6 = AddPetRequest.builder()
                .name("Molly")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.BABY)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://www.kittenkornerrescue.com/wp-content/uploads/2019/01/Adopt-Square-1-300x300.jpg")
                .description("Molly is a tiny baby kitten with short, fluffy fur and a heart full of love. She was found alone, crying for warmth and comfort. Molly is affectionate, loves to cuddle, and enjoys gentle pets. Her best feature is her sweet and trusting nature. She needs a home where she can grow up feeling safe and loved. Could Molly be the kitten you’ve been searching for?")
                .build();

        AddPetRequest cat7 = AddPetRequest.builder()
                .name("Milo")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://pawr.org/wp-content/uploads/2024/11/ava2-300x300.jpg")
                .description("Milo is a confident adult cat with short fur and a wise gaze. He was rescued from a hoarding situation and has since flourished into a friendly and sociable cat. Milo enjoys companionship, both human and feline, and loves lounging in cozy spots. His best feature is his charming personality. He needs a home where he can continue to feel secure and cherished. Are you the one for Milo?")
                .build();

        AddPetRequest cat8 = AddPetRequest.builder()
                .name("Charlie")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://pawr.org/wp-content/uploads/2025/02/ama2-300x300.jpg")
                .description("Charlie is a stunning adult cat with short fur and a strong sense of curiosity. He was abandoned at a shelter, but he remains hopeful for a second chance. Charlie enjoys interactive toys, exploring new spaces, and cuddling on his terms. His best feature is his playful and intelligent nature. He needs a home that appreciates his curious personality. Could Charlie be your next feline friend?")
                .build();

        AddPetRequest cat9 = AddPetRequest.builder()
                .name("Leo")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.LONG)
                .gender(Gender.MALE)
                .vaccinated(true)
                .picture("https://cdn.shopify.com/s/files/1/3000/3146/files/igor-cat-kitten-21_large.jpg?v=1542611648")
                .description("Leo is an adult cat with a long, luxurious coat and a regal presence. He was rescued from neglect but has retained his gentle and loving demeanor. Leo enjoys being brushed, lounging in sunny spots, and receiving affection. His best feature is his elegance and calm nature. He needs a home where he can feel truly appreciated. Could you be the one to give Leo the royal treatment?")
                .build();

        AddPetRequest cat10 = AddPetRequest.builder()
                .name("Lily")
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.BABY)
                .hairLength(HairLength.LONG)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .picture("https://puppiezo.com/wp-content/uploads/2023/06/IMG_2104-300x300.jpeg")
                .description("Lily is a fluffy baby kitten with long, soft fur and an angelic face. She was abandoned at a young age but has shown incredible resilience. Lily loves to pounce, chase, and curl up in a warm lap. Her best feature is her playful and affectionate nature. She needs a home where she can feel loved and protected. Will you be the one to give Lily the forever home she deserves?")
                .build();

        petService.addPet(dog1);
        petService.addPet(dog2);
        petService.addPet(dog3);
        petService.addPet(dog4);
        petService.addPet(dog5);
        petService.addPet(dog6);
        petService.addPet(dog7);
        petService.addPet(dog8);
        petService.addPet(dog9);
        petService.addPet(dog10);
        petService.addPet(cat1);
        petService.addPet(cat2);
        petService.addPet(cat3);
        petService.addPet(cat4);
        petService.addPet(cat5);
        petService.addPet(cat6);
        petService.addPet(cat7);
        petService.addPet(cat8);
        petService.addPet(cat9);
        petService.addPet(cat10);
    }
}
