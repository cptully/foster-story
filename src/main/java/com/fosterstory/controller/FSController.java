package com.fosterstory.controller;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.*;
import com.fosterstory.exceptions.UserNotFoundException;
import com.fosterstory.service.*;
import com.fosterstory.utility.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by chris on 10/3/16.
 */
@Controller
public class FSController {

    private final Integer ADMIN = 1;
    private final Integer USER = 2;

    @Autowired
    FSService fsService;

    @Autowired
    UserService userService;

    @Autowired
    TumblrService tumblrService;

    @Autowired
    AnimalService animalService;

    @Autowired
    PostService postService;

    @Autowired
    PhotoPostService photoPostService;

    @Autowired
    ImageService imageService;

    @RequestMapping(path = "/")
    public String list(Model model,
                       Search search,
                       @PageableDefault(size = 12) Pageable pageable,
                       String action,
                       HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user;
        
        try
        {
            user = userService.findById(userId);
        } catch (UserNotFoundException e)
        {
            return "listError";
        }

        if ((action != null) && (action.equals("clear"))) {
            search = new Search();
        }


        Page<Animal> animals = fsService.listAnimals(search, pageable);
        List<Integer> imageIds = new ArrayList<>();
        model.addAttribute("animals", animals);
        for (Animal animal : animals) {
            if (animal.getImages().size() > 0) {
                imageIds.add(animal.getImages().get(animal.getImages().size() - 1).getId());
            } else {
                imageIds.add(0);
            }
        }
        model.addAttribute("imageIds", imageIds);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        model.addAttribute("search", search);
        model.addAttribute("pageable", pageable);
        model.addAttribute("user", user);

        tumblrService.getPostsFromTumblr("");
        Page<PhotoPost> photoPosts = photoPostService.listPosts(pageable);
        model.addAttribute("photoPosts", photoPosts);

        return "list";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(Model model,
                           HttpSession session) {
//        if (bindingResult.hasErrors()){
//            //model.addAttribute("user", user);
//            //return "redirect:/login";
//        }
//        user = userService.findById((Integer) session.getAttribute("userId"));
        User user;
        if (session.getAttribute("userId") != null) {
            try
            {
                user = userService.findById((Integer) session.getAttribute("userId"));
            } catch (UserNotFoundException e)
            {
                return "registerError";
            }
        } else {
            user = new User();
        }
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(defaultValue = "") String confirmPassowrd,
                           Model model,
                           HttpSession session) {
        if (!bindingResult.hasErrors()) {
            try {
                if (user.getPassword().equals(user.getConfirmPassword())) {
                    fsService.saveUser(user);
                    session.setAttribute("userId", user.getId());
                    return "redirect:/";
                } else {
                    FieldError fieldError = new FieldError("user", "confirmPassword", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Passwords do not match");
                    bindingResult.addError(fieldError);
                }
            } catch (PasswordStorage.CannotPerformOperationException e) {
                FieldError fieldError = new FieldError("user", "password", user.getPassword(), false, new String[]{"Invalid.user.password"}, (String[]) null, "Invalid password");
                bindingResult.addError(fieldError);
                e.printStackTrace();
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("bindingResult", bindingResult);

        return "register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Login login, String returnPath, Model model, HttpSession session) {

        model.addAttribute("login", login);
        model.addAttribute("returnPath", returnPath);

        // get the user (or null if not logged in)
        model.addAttribute("user", fsService.getUserOrNull((Integer) session.getAttribute("userId")));

        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, @Valid Login login, HttpSession session) {

        User user = fsService.authenticateUser(login);

        // get the user (or null if not logged in)
        model.addAttribute("user", user);

        if (user != null) {
            session.setAttribute("userId", user.getId());
            if (login.getReturnPath() != null) {
                return "redirect:" + login.returnPath;
            } else {
                return "redirect:/";
            }
        } else {
            return "login";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    @RequestMapping(path = "/about")
    public String about(Model model, HttpSession session) {
        User user;
        try
        {
            user = userService.findById((Integer)session.getAttribute("userId"));
        } catch (UserNotFoundException e)
        {
            return "error";
        }
        model.addAttribute("user", user);
        return "about";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.POST)
    public String profile(Model model,
                          String action,
                          @Valid User userData,
                          BindingResult bindingResult,
                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
//            model.addAttribute("returnPath", "/profile");
            return "redirect:/login?returnPath=/profile";
        }

        User user = userData;
        if (!bindingResult.hasErrors()) {
            user = fsService.getUser(userId);
            if ((action != null) && (action.equals("save"))) {
                // update the user data from the form with the logged in user id & password
                // and keep the password out of the UI
                if (userData.getPassword() == null) {
                    userData.setPassword(user.getPassword());
                }

                if (user.getId() != null) {
                    if (null != userService.findById(user.getId()) .getImage()) {
                        user.setImage(userService.findById(user.getId()).getImage());
                    }
                }

                if (action.equals("save")) {
                    // save the user and pray
                    try {
                        user = fsService.saveUser(userData);
                    } catch (PasswordStorage.CannotPerformOperationException e) {
                        //error
                        FieldError fieldError = new FieldError("user", "password", user.getPassword(), false,
                                new String[]{"Invalid.user.password"},
                                (String[]) null, "Username / password combination incorrect");
                        bindingResult.addError(fieldError);

                        e.printStackTrace();
                        return "redirect:/login";
                    }
                }
            }
        }

        if (bindingResult.hasErrors()) {

        }

        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String profile(Model model,
                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login?returnPath=/profile";
        }

        User user = fsService.getUser(userId);
        model.addAttribute("user", user);
        Integer imageId;
        if (user.getImage() == null) {
            imageId = 0;
        } else {
            imageId = user.getImage().getId();
        }
        model.addAttribute("imageId", imageId);
        return "profile";
    }

    @RequestMapping(path = "/story", method = RequestMethod.GET)
    public String story(Model model,
                        @RequestParam(defaultValue = "0") Integer animalId,
                        Animal animal,
                        HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/story";
        }

        Integer imageId;

        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        model.addAttribute("user", user);
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        if (animalId != 0) {
            Optional<Animal> optionalAnimal = animalService.findById(animalId);
            animal = optionalAnimal.orElseGet(Animal::new);
        } else if (animals.size() > 0) {
            animal = animals.get(animals.size() - 1);
        }

        if (null != animal) {
            model.addAttribute("animal", animal);
            if (animal.getImages().size() > 0) {
                imageId = animal.getImages().get(animal.getImages().size() - 1).getId();
            } else {
                imageId = null;
            }
        }
        else
        {
            imageId = null;
        }
        model.addAttribute("imageId", imageId);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        return "story";
    }

    @RequestMapping(path = "/story", method = RequestMethod.POST)
    public String story(Model model,
                        @Valid Animal animal,
                        BindingResult bindingResult,
                        String action,
                        HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/story";
        }

        if (bindingResult.hasErrors()) {
            return "story";
        }

        Integer imageId;

        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        model.addAttribute("user", user);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        if (animal.getImages().size() > 0) {
            imageId = animal.getImages().get(animal.getImages().size() - 1).getId();
        } else {
            imageId = null;
        }
        model.addAttribute("imageId", imageId);
        if ((action != null) && (action.equals("save"))) {
            try {
                if (animal != null) {
                    animal.setUser(user);
                    if (animal.getBreed() == null) {
                        Breed breed = fsService.getBreedById(animal.getBreed().getId());
                        animal.setBreed(breed);
                    }

                    if (animal.getId() != null) {
                        if (animalService.findById(animal.getId()).isPresent() && animalService.findById(animal.getId()).get().getImages() != null) {
                            animal.setImages(animalService.findById(animal.getId()).get().getImages());
                        }
                    }

                    if (!user.getAnimals().contains(animal)) {
                        user.getAnimals().add(animal);
                        fsService.saveUser(user);
                    } else {
                        fsService.saveAnimal(animal);
                    }
                }
                model.addAttribute("animal", animal);
                return "redirect:/story";
            } catch (PasswordStorage.CannotPerformOperationException e) {
                // set errors
                return "story";
            }
        } else if ((action != null) && (action.equals("clear"))) {
            animal = new Animal();
        } else{
            if (animals.size() >= 1) {
                animal = animals.get(animals.size() - 1);
            } else {
                animal = new Animal();
            }
        }

        model.addAttribute("animal", animal);
        return "story";
    }

    @GetMapping("/story/feature")
    @ResponseBody
    public ResponseEntity feature(Integer imageId) throws URISyntaxException {

        if ((imageId != null) && (imageService.findOne(imageId).getContentType() != null)){
            Image image = imageService.findOne(imageId);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
                    .body(image.getImage());
        } else {
            return ResponseEntity
                    .status(301)
                    .location(new URI("//placehold.it/100"))
                    .build();
        }
    }

    // TODO: 10/18/16 make this work....
    @RequestMapping(path = "/story/images")
    public String images(Model model) {
        List<Image> images = imageService.findAll();
        model.addAttribute("images", images);

        return "images";
    }

    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity serveFile(Integer imageId) throws URISyntaxException {

        if ((imageId != null) && (imageService.findOne(imageId) != null) && (imageService.findOne(imageId).getContentType() != null)){
            Image image = imageService.findOne(imageId);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
                    .body(image.getImage());
        } else {
            return ResponseEntity
                    .status(301)
                    .location(new URI("//placehold.it/100"))
                    .build();
        }
    }


    @RequestMapping(path = "/user/image", method = RequestMethod.POST)
    public String userImage(Model model,
                            Integer userId,
                            @RequestParam(name = "file") MultipartFile file,
                            HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/profile";
        }

        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        model.addAttribute("user", user);

        if (!file.isEmpty()) {
            try {
                Image image = new Image();
                if (file.getBytes().length > 1000000000) {
                    // TODO: 10/18/16 invalid file size error
                    //spew error
                }
                image.setImage(file.getBytes());
                image.setContentType(file.getContentType());
                user.setImage(image);
                fsService.saveUser(user);
            } catch (IOException | PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("imageId", user.getImage().getId());
        return "redirect:/profile?userId=" + userId;
    }


    @RequestMapping(path = "/story/image", method = RequestMethod.POST)
    public String storyImage(Model model,
                             Integer animalId,
                             @RequestParam(name = "file") MultipartFile file,
                             HttpSession session) {
        if ((session.getAttribute("userId") == null) || (fsService.getUserOrNull((Integer) session.getAttribute("userId")) == null)) {
            return "redirect:/login?returnPath=/story";
        }

        User user = fsService.getUser((Integer) session.getAttribute("userId"));
        List<Animal> animals = user.getAnimals();
        model.addAttribute("animals", animals);
        model.addAttribute("count", animals.size());
        Animal animal = animalService.findById(animalId).orElse(new Animal());
        model.addAttribute("animal", animal);
        model.addAttribute("user", user);
        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());

        if (!file.isEmpty()) {
            try {
                Image image = new Image();
                if (file.getBytes().length > 1000000000) {
                    // TODO: 10/18/16 invalid file size error
                    //spew error
                }
                image.setImage(file.getBytes());
                image.setContentType(file.getContentType());
                animal.getImages().add(image);
                fsService.saveAnimal(animal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        model.addAttribute("animal", animal);
        return "redirect:/story?animalId=" + animalId;
    }

    @RequestMapping(path = "/viewStory")
    public String viewStory(Model model,
                            @RequestParam Integer animalId,
                            HttpSession session) {
        if (animalId == null) {
            return "redirect:/list";
        }

        Animal animal = fsService.getAnimal(animalId);
//        tumblrService.getPostsFromTumblr("");

//        Page<PhotoPost> posts = photoPostService.listPosts(pageable);
//        model.addAttribute("posts", posts);
        Integer imageId;
        if (animal.getImages().size() > 0) {
            imageId = animal.getImages().get(animal.getImages().size() - 1).getId();
        } else {
            imageId = 0;
        }
        model.addAttribute("imageId", imageId);
        model.addAttribute("animal", animal);
        User user = userService.findById((Integer)session.getAttribute("userId"));
        model.addAttribute("user", user);
        return "viewStory";
    }

}