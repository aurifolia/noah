package org.example.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/28
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/001")
    @PreFilter("filterObject.length() > 0")
    @PostFilter("filterObject.length() > 0")
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject == 'test001'")
    public List<String> test001(@RequestBody List<String> args) {
        return Arrays.asList("test001");
    }

    @PostMapping("/002")
    public String test002() {
        return "test002";
    }

    @GetMapping("/003")
    public String test003() {
        return "test003";
    }
}
