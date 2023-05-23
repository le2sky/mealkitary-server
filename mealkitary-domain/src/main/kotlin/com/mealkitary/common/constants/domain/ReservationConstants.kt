package com.mealkitary.common.constants.domain

internal object ReservationConstants {

    object Validation {
        enum class ErrorMessage(val message: String) {
            INVALID_RESERVE_TIME("예약 대상 가게는 해당 시간에 예약을 받지 않습니다."),
            NOTPAID_RESERVATION_CANNOT_ACCEPT("미결제 상태인 예약은 승인할 수 없습니다."),
            NOTPAID_RESERVATION_CANNOT_REJECT("미결제 상태인 예약은 거부할 수 없습니다."),
            ALREADY_REJECTED_RESERVATION_CANNOT_ACCEPT("이미 예약 거부된 건에 대해서 승인할 수 없습니다."),
            ALREADY_RESERVED_RESERVATION_CANNOT_REJECT("이미 예약 확정된 건에 대해서 거부할 수 없습니다."),
            INVALID_RESERVATION_STATUS_FOR_PAYMENT("미결제인 상태에서만 결제 상태를 변경할 수 있습니다."),
            AT_LEAST_ONE_ITEM_REQUIRED("예약 상품은 적어도 한 개 이상이어야 합니다."),
            FUTURE_TIME_REQUIRED("예약 시간은 적어도 미래 시점이어야 합니다.")
        }
    }
}
