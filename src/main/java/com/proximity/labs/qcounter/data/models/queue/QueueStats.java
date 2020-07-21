package com.proximity.labs.qcounter.data.models.queue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "queue_stats")
public class QueueStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private QueueState state = QueueState.RUNNING;

    @Column(name = "current_in_queue", nullable = false)
    private Integer currentInQueue = 0;

    @Column(name = "current_queue", nullable = false)
    private Integer currentQueue = 0;

    @OneToOne(optional = false, mappedBy = "queueStats")
    private Queue queue;

    public QueueStats(Queue queue) {
        this.queue = queue;
    }

    public QueueStats() {}

    public Long getId() {
        return id;
    }

    public QueueState getState() {
        return state;
    }

    public void setState(QueueState state) {
        this.state = state;
    }

    public Integer getCurrentInQueue() {
        return currentInQueue;
    }

    public void setCurrentInQueue(Integer currentInQueue) {
        this.currentInQueue = currentInQueue;
    }

    public Integer getCurrentQueue() {
        return currentQueue;
    }

    public void setCurrentQueue(Integer currentQueue) {
        this.currentQueue = currentQueue;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    
}