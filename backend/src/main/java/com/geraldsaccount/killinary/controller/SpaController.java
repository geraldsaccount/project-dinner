package com.geraldsaccount.killinary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    /**
     * Forwards any request that is not for an API endpoint or a static file to the
     * frontend's entry point.
     * This is necessary for a single-page application with client-side routing.
     * The regular expression in @RequestMapping ensures that paths with extensions
     * (like .js, .css) are not caught.
     *
     * @return The path to the frontend's entry point, which will then handle the
     *         routing.
     */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forward() {
        return "forward:/";
    }
}
