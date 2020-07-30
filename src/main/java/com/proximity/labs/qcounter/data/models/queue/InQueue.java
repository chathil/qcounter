package com.proximity.labs.qcounter.data.models.queue;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.proximity.labs.qcounter.data.models.user.User;

@Entity
@Table(name = "in_queues")
public class InQueue {

    public InQueue() {}

    public InQueue(Queue queue, User user, String name, String contact, int queueNum) {
        this.queue = queue;
        this.user = user;
        this.contact = contact;
        this.queueNum = queueNum;
        this.name = name;
    } 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "queue_id", nullable = false)
    private Queue queue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    private Date joinedAt = Date.from(Instant.now());

    @Column(name = "exited_at", nullable = true)
    private Date exitedAt;

    @Column(name = "blocked_at", nullable = true)
    private Date blockedAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(name = "queue_num", nullable = false)
    private Integer queueNum;

    @Column(name = "called_at", nullable = true)
    private Date calledAt;

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Date getExitedAt() {
        return exitedAt;
    }

    public void setExitedAt(Date exitedAt) {
        this.exitedAt = exitedAt;
    }

    public Date getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Date blockedAt) {
        this.blockedAt = blockedAt;
    }

    public Date getCalledAt() {
        return calledAt;
    }

    public void setCalledAt(Date calledAt) {
        this.calledAt = calledAt;
    }

    public Long getId() {
        return id;
    }

    public Queue getQueue() {
        return queue;
    }

    public User getUser() {
        return user;
    }

    public String getContact() {
        return contact;
    }

    public Integer getQueueNum() {
        return queueNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}