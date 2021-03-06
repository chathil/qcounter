package com.proximity.labs.qcounter.data.models.queue;

import java.nio.IntBuffer;
import java.util.*;

import javax.persistence.*;

import com.proximity.labs.qcounter.data.models.audit.DateAudit;
import com.proximity.labs.qcounter.data.models.user.User;


@Entity
@Table(name = "queues")
public class Queue extends DateAudit {

    
    public Queue() {
    }

    /**
     * Do not send this id to client, only use this id in back-end
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queue_seq")
    @SequenceGenerator(name = "queue_seq", allocationSize = 1, initialValue = 11)
    private Long id;

    /**
     * This is the id that will be send to client.
     */
    @Column(nullable = false, unique = true)
    private String clientGeneratedId;

    @Column(nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String desc;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "increment_by", nullable = false)
    private Integer incrementBy;

    @Column(name = "valid_until", nullable = false)
    private Date validUntil;

    @Column(nullable = false)
    private String contact;

    @Column(name = "is_closed_queue", nullable = false)
    private Boolean isClosedQueue;

    @Column(nullable = false)
    private String location = "Indonesia";

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "queue_stats_id", unique = true, referencedColumnName = "id")
    private QueueStats queueStats;

    @OneToMany(mappedBy = "queue", fetch = FetchType.EAGER, cascade =
    CascadeType.ALL)
    private Set<InQueue> inQueues = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    public Queue(User owner, String clientGeneratedId, String name, String desc, Integer maxCapacity, Integer incrementBy, Date validUntil, String contact,
            Boolean isClosedQueue, String location) {
        this.name = name;
        this.desc = desc;
        this.maxCapacity = maxCapacity;
        this.incrementBy = incrementBy;
        this.validUntil = validUntil;
        this.contact = contact;
        this.isClosedQueue = isClosedQueue;
        this.location = location;
        this.clientGeneratedId = clientGeneratedId;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<InQueue> getInQueues() {
        return inQueues;
    }

    public void setInQueues(Set<InQueue> inQueues) {
        this.inQueues = inQueues;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getIncrementBy() {
        return incrementBy;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public String getContact() {
        return contact;
    }

    public Boolean getIsClosedQueue() {
        return isClosedQueue;
    }

    public String getLocation() {
        return location;
    }

    public QueueStats getQueueStats() {
        return queueStats;
    }

    public void setQueueStats(QueueStats queueStats) {
        this.queueStats = queueStats;
    }

    public String getClientGeneratedId() {
        return clientGeneratedId;
    }

    public void setClientGeneratedId(String clientGeneratedId) {
        this.clientGeneratedId = clientGeneratedId;
    }

    public User getOwner() {
        return owner;
    }    
}