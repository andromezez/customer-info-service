package com.apigate.customer_info_service.monitoring;

import lombok.Data;

import java.util.Properties;

/**
 * @author Bayu Utomo
 * @date 5/12/2020 7:19 PM
 */
@Data
public class GitRepositoryState {
    private String tags, branch, commitId, commitIdAbbrev, commitMessageFull, commitMessageShort, commitTime;
    public static final String UNABLE_TO_LOAD = "unable to load";

    public GitRepositoryState(Properties properties) {
        this.tags = String.valueOf(properties.get("git.tags"));
        this.branch = String.valueOf(properties.get("git.branch"));
        this.commitId = String.valueOf(properties.get("git.commit.id"));
        this.commitIdAbbrev = String.valueOf(properties.get("git.commit.id.abbrev"));
        this.commitMessageFull = String.valueOf(properties.get("git.commit.message.full"));
        this.commitMessageShort = String.valueOf(properties.get("git.commit.message.short"));
        this.commitTime = String.valueOf(properties.get("git.commit.time"));
    }

    public GitRepositoryState() {
        this.tags = UNABLE_TO_LOAD;
        this.branch = UNABLE_TO_LOAD;
        this.commitId = UNABLE_TO_LOAD;
        this.commitIdAbbrev = UNABLE_TO_LOAD;
        this.commitMessageFull = UNABLE_TO_LOAD;
        this.commitMessageShort = UNABLE_TO_LOAD;
        this.commitTime = UNABLE_TO_LOAD;
    }
}
