package com.fosterstory.controller;

import com.fosterstory.bean.Search;
import com.fosterstory.service.FSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chris on 10/3/16.
 */
@Controller
public class FSController {

    @Autowired
    FSService fsService;

    // TODO: 10/4/16 Add animal list to this function
    @RequestMapping (path = "/")
    public String list(Model model,
                       Search search,
                       Pageable pageable,
                       String action) {
        if ((action != null) && (action.equals("clear"))) {
            search = new Search();
        }

        model.addAttribute("types", fsService.listTypes());
        model.addAttribute("breeds", fsService.listBreeds());
        model.addAttribute("search", search);
        model.addAttribute("pageable", pageable);
        model.addAttribute("animals", fsService.listAnimals(pageable));
        return "list";
    }

    // TODO: 10/4/16 about page

    // TODO: 10/4/16 profile page

    // TODO: 10/4/16 story page
}
