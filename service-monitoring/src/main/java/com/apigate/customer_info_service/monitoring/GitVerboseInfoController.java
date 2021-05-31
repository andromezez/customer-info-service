package com.apigate.customer_info_service.monitoring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Bayu Utomo
 * @date 5/12/2020 6:56 PM
 */
@RestController
@RequestMapping("/monitor/info/git")
public class GitVerboseInfoController {
    private GitRepositoryState gitRepositoryState;
    @GetMapping
    public ResponseEntity<GitRepositoryState> get(){

        if (gitRepositoryState == null)
        {
            Properties properties = new Properties();
            try {
                properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
                gitRepositoryState = new GitRepositoryState(properties);
            } catch (IOException e) {
                gitRepositoryState = new GitRepositoryState();
            }
        }
        if(gitRepositoryState.getBranch().equals(GitRepositoryState.UNABLE_TO_LOAD)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gitRepositoryState);
        }
        return ResponseEntity.ok(gitRepositoryState);
    }

}
