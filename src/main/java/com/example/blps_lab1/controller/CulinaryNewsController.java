package com.example.blps_lab1.controller;

import com.example.blps_lab1.config.jwt.AuthTokenFilter;
import com.example.blps_lab1.config.jwt.JwtUtils;
import com.example.blps_lab1.dto.request.AddCulinaryNewRequest;
import com.example.blps_lab1.dto.request.AddRecipeRequest;
import com.example.blps_lab1.dto.response.RecipeResponse;
import com.example.blps_lab1.model.basic.Recipe;
import com.example.blps_lab1.model.extended.CulinaryNews;
import com.example.blps_lab1.service.CulinaryNewsService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/culinary-news")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CulinaryNewsController {

    private final AuthTokenFilter authTokenFilter;
    private final JwtUtils jwtUtils;

    private final CulinaryNewsService culinaryNewsService;

    public CulinaryNewsController(AuthTokenFilter authTokenFilter, JwtUtils jwtUtils, CulinaryNewsService culinaryNewsService) {
        this.authTokenFilter = authTokenFilter;
        this.jwtUtils = jwtUtils;
        this.culinaryNewsService = culinaryNewsService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping()
    public ResponseEntity<?> newCulinaryNew(@Valid @RequestBody AddCulinaryNewRequest addCulinaryNewRequest,
                                            HttpServletRequest httpServletRequest) {
        String login = jwtUtils.getLoginFromJwtToken(authTokenFilter.parseJwt(httpServletRequest));

        CulinaryNews culinaryNew = culinaryNewsService.saveCulinaryNew(login, addCulinaryNewRequest);

        return new ResponseEntity<>(culinaryNew, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAllCulinaryNews(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        List<CulinaryNews> allCulinaryNews = culinaryNewsService.getAllCulinaryNews(page, size, sortOrder.toString()).getContent();
        return new ResponseEntity<>(allCulinaryNews, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCulinaryNew(@PathVariable Long id) {
        CulinaryNews culinaryNew = culinaryNewsService.findCulinaryNewById(id);
        return new ResponseEntity<>(culinaryNew, HttpStatus.OK);
    }


}
