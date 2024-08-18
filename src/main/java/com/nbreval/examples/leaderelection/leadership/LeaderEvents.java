package com.nbreval.examples.leaderelection.leadership;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Component;

/**
 * Component used to listen for OnGranted and OnRevoked events. This events are thrown by fabric8 leader election 
 * library and, using them, is possible to check if current instance is the leader, or not.
 * 
 * When an instance receives a OnGranted event, marks its boolean value as true, and this activates the log in 
 * scheduled task. If receives a OnRevoked event, marks its boolean value as false y the log message is not
 * shown.
 */
@Component
@ConditionalOnProperty(prefix = "spring.cloud.kubernetes.leader", name = "enabled", havingValue = "true")
public class LeaderEvents {
    
    /**
     * Variable to mark instance as leader, or not
     */
    public AtomicBoolean isLeader = new AtomicBoolean(false);

    /**
     * Marks {@link #isLeader} as true to notify the rest of application current instance is the leader
     * @param event Received event information, not used
     */
    @EventListener
    public void grantLeaderEvent(OnGrantedEvent event) {
        isLeader.set(true);
    }

    /**
     * Marks {@link #isLeader} as false to notify the rest of application current instance is not the leader
     * @param event Received event information, not used
     */
    @EventListener
    public void revokeLeaderEvent(OnRevokedEvent event) {
        isLeader.set(false);
    }

    /**
     * Used by the rest of application to check if current instance is the leader
     * @return True is current instance is the leader, else false
     */
    public boolean isLeader() {
        return isLeader.get();
    }
}
