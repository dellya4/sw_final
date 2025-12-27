package com.example.demo.enums;

public enum WishStatus {
    DRAFT, // which doesn't publish
    PUBLISHED, // which was publish, it can see all the people
    BOOKED, // which some person take
    DELETED // which was deleted
}