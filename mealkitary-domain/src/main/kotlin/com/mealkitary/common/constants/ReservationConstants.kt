package com.mealkitary.common.constants

internal object ReservationConstants {

    object Validation {
        enum class ErrorMessage(val message: String) {
            INVALID_RESERVE_TIME("예약 대상 가게는 해당 시간에 예약을 받지 않습니다."),
            NOTPAID_RESERVATION_CANNOT_ACCEPT("미결제 상태인 예약은 승인할 수 없습니다."),
            NOTPAID_RESERVATION_CANNOT_REJECT("미결제 상태인 예약은 거부할 수 없습니다."),
            ALREADY_ACCEPTED_RESERVATION("이미 승인된 예약입니다."),
            ALREADY_REJECTED_RESERVATION("이미 거절된 예약입니다."),
            ALREADY_REJECTED_RESERVATION_CANNOT_ACCEPT("이미 예약 거부된 건에 대해서 승인할 수 없습니다."),
            ALREADY_RESERVED_RESERVATION_CANNOT_REJECT("이미 예약 확정된 건에 대해서 거부할 수 없습니다."),
            INVALID_RESERVATION_STATUS_FOR_PAYMENT("미결제인 상태에서만 이용 가능한 기능입니다."),
            AT_LEAST_ONE_ITEM_REQUIRED("예약 상품은 적어도 한 개 이상이어야 합니다."),
            AT_LEAST_ONE_ITEM_COUNT_REQUIRED("개별 항목의 수량은 적어도 한 개 이상이어야 합니다."),
            FUTURE_TIME_REQUIRED("예약 시간은 적어도 미래 시점이어야 합니다."),
            ALREADY_PROCESSED_RESERVATION("이미 처리하고 있는 예약입니다."),
            INVALID_RESERVATION_STATUS_FOR_OPERATION("정상적으로 생성된 예약에 대해서만 수행 가능합니다.")
        }
    }
}
