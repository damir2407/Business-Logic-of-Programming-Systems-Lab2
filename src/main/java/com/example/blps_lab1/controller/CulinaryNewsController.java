package com.example.blps_lab1.controller;

import com.example.blps_lab1.security.AuthTokenFilter;
import com.example.blps_lab1.security.JwtUtils;
import com.example.blps_lab1.dto.request.AddCulinaryNewRequest;
import com.example.blps_lab1.model.extended.CulinaryNews;
import com.example.blps_lab1.service.CulinaryNewsService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/culinary-news")
public class CulinaryNewsController {

    private final AuthTokenFilter authTokenFilter;
    private final JwtUtils jwtUtils;

    private final CulinaryNewsService culinaryNewsService;

    public CulinaryNewsController(AuthTokenFilter authTokenFilter, JwtUtils jwtUtils, CulinaryNewsService culinaryNewsService) {
        this.authTokenFilter = authTokenFilter;
        this.jwtUtils = jwtUtils;
        this.culinaryNewsService = culinaryNewsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public CulinaryNews newCulinaryNew(@Valid @RequestBody AddCulinaryNewRequest addCulinaryNewRequest,
                                       HttpServletRequest httpServletRequest) {
        String login = jwtUtils.getLoginFromJwtToken(authTokenFilter.parseJwt(httpServletRequest));
        return culinaryNewsService.saveCulinaryNew(login, addCulinaryNewRequest);
    }

    @GetMapping()
    public List<CulinaryNews> getAllCulinaryNews(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return culinaryNewsService.getAllCulinaryNews(page, size, sortOrder.toString()).getContent();
    }

    @GetMapping("{id}")
    public CulinaryNews getCulinaryNew(@PathVariable Long id) {
        return culinaryNewsService.findCulinaryNewById(id);
    }


}
