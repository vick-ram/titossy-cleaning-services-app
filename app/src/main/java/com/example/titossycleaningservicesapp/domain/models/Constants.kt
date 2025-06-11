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
    PENDING, // inventory manager creates the order
    REVIEWED, // manager reviews the order
    APPROVED, // finance approves the order
    ACKNOWLEDGED, // supplier acknowledges the order
    RECEIVED, // inventory manager receives the order
    COMPLETED, // finance completes the payment
    CANCELLED // order is cancelled
}

enum class BookingStatus {
    PENDING, APPROVED, IN_PROGRESS, COMPLETED, CANCELLED,
}

enum class PaymentStatus {
    PENDING, CONFIRMED, REFUNDED, CANCELLED
}

enum class PaymentMethod {
    CASH, MOBILE, CARD
}