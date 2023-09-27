package com.mealkitary.reservation.persistence

import com.mealkitary.common.exception.EntityNotFoundException
import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.application.port.input.ReservedProduct
import com.mealkitary.reservation.application.port.output.LoadPaymentPort
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SavePaymentPort
import com.mealkitary.reservation.application.port.output.SaveReservationPort
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.stereotype.Repository
import java.util.UUID

private const val NOT_FOUND_RESERVATION_MESSAGE = "존재하지 않는 예약입니다."
private const val NOT_FOUND_PAYMENT_MESSAGE = "존재하지 않는 결제입니다."

@Repository
class SpringDataJpaReservationPersistenceAdapter(
    private val reservationRepository: ReservationRepository,
    private val paymentRepository: PaymentRepository
) : SaveReservationPort, SavePaymentPort, LoadReservationPort, LoadPaymentPort {

    override fun saveOne(reservation: Reservation): UUID {
        reservationRepository.save(reservation)

        return reservation.id
    }

    override fun saveOne(payment: Payment): UUID {
        paymentRepository.save(payment)

        return payment.id
    }

    override fun loadOneReservationById(reservationId: UUID): Reservation {
        return reservationRepository.findById(reservationId)
            .orElseThrow { throw EntityNotFoundException(NOT_FOUND_RESERVATION_MESSAGE) }
    }

    override fun loadOnePaymentByReservationId(reservationId: UUID): Payment {
        return paymentRepository.findByReservationId(reservationId)
            .orElseThrow { throw EntityNotFoundException(NOT_FOUND_PAYMENT_MESSAGE) }
    }

    override fun queryOneReservationById(reservationId: UUID): ReservationResponse {
        val reservation = reservationRepository.findOneWithShopById(reservationId)
            .orElseThrow { throw EntityNotFoundException(NOT_FOUND_RESERVATION_MESSAGE) }

        return mapToReservationResponse(reservation)
    }

    override fun queryAllReservationByShopId(shopId: Long): List<ReservationResponse> {
        val reservations = reservationRepository.findAllByShopId(shopId)

        return reservations.map { mapToReservationResponse(it) }
    }

    private fun mapToReservationResponse(reservation: Reservation) =
        ReservationResponse(
            reservation.id,
            reservation.shop.title.value,
            reservation.buildDescription(),
            reservation.reserveAt,
            reservation.reservationStatus.name,
            reservation.lineItems.map {
                ReservedProduct(
                    it.itemId.id,
                    it.name,
                    it.price.value,
                    it.count
                )
            }
        )
}
