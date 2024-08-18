package com.nbreval.examples.leaderelection;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nbreval.examples.leaderelection.leadership.LeaderEvents;

/**
 * Simple component used to enable a task which shows a log message indicating it's the leader. If the application
 * instance is not the leader, nothing is shown.
 * 
 * The component is only enabled if 'spring.cloud.kubernetes.leader.enabled' has 'true' value. This condition 
 * is required to ensure 'LeaderEvents' component is not null, because, in that case, it can throw a NullPointerException.
 */
@Component
@ConditionalOnProperty(prefix = "spring.cloud.kubernetes.leader", name = "enabled", havingValue = "true")
public class ScheduledTask {
    
    private final static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    LeaderEvents leaderEvents;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void onlyLeaderTask() {
        if (leaderEvents.isLeader()) {
            logger.info("I'm currently the leader");
        }
    }

}
