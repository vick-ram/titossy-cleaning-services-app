package com.example.titossycleaningservicesapp.domain.models

enum class Gender {
    MALE, FEMALE, OTHER, NOT_SPECIFIED
}

enum class Roles {
    ADMIN, MANAGER, INVENTORY, FINANCE, SUPERVISOR, CLEANER
}

enum class Availability {
    AVAILABLE, UNAVAILABLE, ON_LEAVE
}

enum class ApprovalStatus {
    PENDING, APPROVED, REJECTED
}

enum class Frequency {
    ONE_TIME, WEEKLY, BIWEEKLY, MONTHLY
}

enum class OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}