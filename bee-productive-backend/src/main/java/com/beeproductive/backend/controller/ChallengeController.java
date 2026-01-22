package com.beeproductive.backend.controller;

import com.beeproductive.backend.service.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private ChallengeService challengeService;

}
